package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PyqDatabase
import com.example.data.UserPreferencesRepository
import com.example.data.model.ActiveStudySessionState
import com.example.data.model.DailyGoal
import com.example.data.model.EducationLevel
import com.example.data.model.ExamMilestone
import com.example.data.model.ExamPreference
import com.example.data.model.MeditationActivity
import com.example.data.model.OMRResult
import com.example.data.model.Question
import com.example.data.model.QuestionPaper
import com.example.data.model.StreakDayStatus
import com.example.data.model.StudentEnergyLevel
import com.example.data.model.SubjectMastery
import com.example.data.model.SubjectType
import com.example.data.model.UserProfile
import com.example.util.PdfExporter
import java.io.File
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class BreathingPhase(val labelEn: String, val labelMl: String) {
  INHALE("Breathe In Slowly", "പതിയെ ശ്വാസമെടുക്കുക"),
  HOLD("Hold & Feel Stillness", "ശ്വാസം ഉള്ളിൽ നിർത്തുക"),
  EXHALE("Release & Let Go", "പതിയെ ശ്വാസം പുറത്തുവിടുക"),
  POST_HOLD("Pause & Relax", "വിശ്രമിക്കുക")
}

data class ActiveOMRState(
  val paper: QuestionPaper,
  val userAnswers: Map<Int, Int> = emptyMap(), // question index -> chosen option (0..3)
  val remainingSeconds: Int,
  val isRunning: Boolean = false,
  val isCompleted: Boolean = false,
  val currentQuestionIndex: Int = 0,
  val result: OMRResult? = null,
)

data class ActiveMeditationState(
  val activity: MeditationActivity,
  val currentPhase: BreathingPhase = BreathingPhase.INHALE,
  val phaseRemainingSeconds: Int = 4,
  val totalRemainingSeconds: Int = 180,
  val isRunning: Boolean = false,
  val isFinished: Boolean = false,
  val currentCycleCount: Int = 1,
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = UserPreferencesRepository(application)

  val userProfile: StateFlow<UserProfile> = repository.userProfile
  val recentResults: StateFlow<List<OMRResult>> = repository.recentResults
  val bookmarkedIds: StateFlow<Set<String>> = repository.bookmarkedIds

  // Goals & Streak Progress state
  val dailyGoals: StateFlow<List<DailyGoal>> = repository.dailyGoals
  val todayStudyMinutes: StateFlow<Int> = repository.todayStudyMinutes
  val dailyTargetHours: StateFlow<Float> = repository.dailyTargetHours
  val longestStreakDays: StateFlow<Int> = repository.longestStreakDays
  val energyLevel: StateFlow<StudentEnergyLevel> = repository.energyLevel
  val examMilestone: StateFlow<ExamMilestone> = repository.examMilestone

  val weeklyActivity: StateFlow<List<StreakDayStatus>> = combine(
    repository.todayStudyMinutes,
    repository.userProfile
  ) { _, _ ->
    repository.getWeeklyStatus()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getWeeklyStatus())

  // Dynamic Subject Mastery derived from database questions + user test attempts + logged study minutes
  val subjectMasteryList: StateFlow<List<SubjectMastery>> = combine(
    repository.userProfile,
    repository.recentResults,
    repository.subjectStudyMinutes
  ) { profile, results, studyMap ->
    val questions = PyqDatabase.allQuestions.filter {
      it.examPreference == profile.examPreference || profile.examPreference == ExamPreference.KERALA_PSC
    }
    val relevantSubjects = when (profile.examPreference) {
      ExamPreference.KERALA_PSC -> listOf(
        SubjectType.RENAISSANCE,
        SubjectType.CONSTITUTION,
        SubjectType.GEOGRAPHY,
        SubjectType.SCIENCE,
        SubjectType.MATHS_REASONING,
        SubjectType.MALAYALAM
      )
      ExamPreference.UPSC -> listOf(
        SubjectType.CONSTITUTION,
        SubjectType.RENAISSANCE,
        SubjectType.ECONOMY,
        SubjectType.CSAT,
        SubjectType.GEOGRAPHY
      )
      ExamPreference.SSC_BANKING -> listOf(
        SubjectType.MATHS_REASONING,
        SubjectType.ENGLISH,
        SubjectType.CONSTITUTION,
        SubjectType.ECONOMY
      )
      ExamPreference.RRB_TECH -> listOf(
        SubjectType.SCIENCE,
        SubjectType.MATHS_REASONING,
        SubjectType.CURRENT_AFFAIRS
      )
    }

    relevantSubjects.map { subj ->
      val totalForSubj = questions.count { it.subject == subj }.coerceAtLeast(1)
      val loggedMins = studyMap[subj] ?: 0
      val mastery = if (loggedMins == 0 && results.isEmpty()) {
        0f
      } else {
        val baseScore = (loggedMins / 360f * 60f).coerceAtMost(60f)
        val bonusScore = if (results.isNotEmpty()) 25f else 10f
        (baseScore + bonusScore).coerceIn(10f, 95f)
      }
      val attempted = if (results.isEmpty() && loggedMins == 0) 0 else minOf(totalForSubj, 10)
      val correct = if (mastery == 0f) 0 else (attempted * (mastery / 100f)).toInt()

      SubjectMastery(
        subject = subj,
        totalQuestions = maxOf(totalForSubj, 15),
        correctCount = correct,
        attemptedCount = attempted,
        masteryPercentage = mastery,
        studyMinutesLogged = loggedMins
      )
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Selected filters
  private val _selectedSubject = MutableStateFlow<SubjectType?>(null)
  val selectedSubject: StateFlow<SubjectType?> = _selectedSubject.asStateFlow()

  private val _selectedYear = MutableStateFlow<Int?>(null)
  val selectedYear: StateFlow<Int?> = _selectedYear.asStateFlow()

  // Filtered Questions based on user preference, education level, and active filters
  val filteredQuestions: StateFlow<List<Question>> = combine(
    userProfile,
    _selectedSubject,
    _selectedYear
  ) { profile, subject, year ->
    PyqDatabase.allQuestions.filter { q ->
      (q.examPreference == profile.examPreference || profile.examPreference == ExamPreference.KERALA_PSC) &&
        (subject == null || q.subject == subject) &&
        (year == null || q.year == year)
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PyqDatabase.allQuestions)

  // Active OMR Exam State
  private val _omrState = MutableStateFlow<ActiveOMRState?>(null)
  val omrState: StateFlow<ActiveOMRState?> = _omrState.asStateFlow()

  // Active Meditation State
  private val _meditationState = MutableStateFlow<ActiveMeditationState?>(null)
  val meditationState: StateFlow<ActiveMeditationState?> = _meditationState.asStateFlow()

  // Active Study Session Timer State
  private val _studyTimerState = MutableStateFlow(ActiveStudySessionState())
  val studyTimerState: StateFlow<ActiveStudySessionState> = _studyTimerState.asStateFlow()

  private var timerJob: Job? = null
  private var meditationJob: Job? = null
  private var studyTimerJob: Job? = null

  fun login(
    name: String,
    phone: String,
    education: EducationLevel,
    exam: ExamPreference,
    malayalam: Boolean
  ) {
    repository.resetAllProgressForNewUser()
    val updated = UserProfile(
      name = name.trim(),
      phoneNumber = phone.trim(),
      educationLevel = education,
      examPreference = exam,
      isMalayalam = malayalam,
      streakDays = 0,
      zenMinutesTotal = 0,
      testsCompletedCount = 0,
      isLoggedIn = true
    )
    repository.saveProfile(updated)
  }

  fun updateExamPreference(preference: ExamPreference) {
    repository.updateExamPreference(preference)
  }

  fun updateEducationLevel(level: EducationLevel) {
    repository.updateEducationLevel(level)
  }

  fun toggleMalayalam(isMalayalam: Boolean) {
    repository.toggleMalayalam(isMalayalam)
  }

  fun setSubjectFilter(subject: SubjectType?) {
    _selectedSubject.value = subject
  }

  fun setYearFilter(year: Int?) {
    _selectedYear.value = year
  }

  fun toggleBookmark(id: String) {
    repository.toggleBookmark(id)
  }

  // --- GOALS & STREAK ACTIONS ---

  fun toggleGoalCompleted(goalId: String) {
    repository.toggleGoalCompleted(goalId)
    triggerHapticTick()
  }

  fun addCustomGoal(title: String, category: String, quantity: String) {
    repository.addCustomGoal(title, category, quantity)
  }

  fun removeCustomGoal(goalId: String) {
    repository.removeCustomGoal(goalId)
  }

  fun setDailyTargetHours(hours: Float) {
    repository.setDailyTargetHours(hours)
  }

  fun setEnergyLevel(level: StudentEnergyLevel) {
    repository.setEnergyLevel(level)
  }

  fun updateMilestoneDays(days: Int) {
    repository.updateExamMilestoneDays(days)
  }

  // --- STUDY SESSION TIMER ACTIONS ---

  fun startStudySession(subject: SubjectType, targetMinutes: Int = 45) {
    studyTimerJob?.cancel()
    _studyTimerState.value = ActiveStudySessionState(
      isRunning = true,
      elapsedSeconds = 0,
      targetMinutes = targetMinutes,
      selectedSubject = subject
    )
    runStudyTimerLoop()
  }

  fun pauseResumeStudySession() {
    val current = _studyTimerState.value
    val newRunning = !current.isRunning
    _studyTimerState.value = current.copy(isRunning = newRunning)
    if (newRunning) {
      runStudyTimerLoop()
    } else {
      studyTimerJob?.cancel()
    }
  }

  fun finishStudySession() {
    studyTimerJob?.cancel()
    val current = _studyTimerState.value
    val minutesStudied = maxOf(1, (current.elapsedSeconds / 60).toInt())
    repository.logStudySession(current.selectedSubject, minutesStudied)
    _studyTimerState.value = ActiveStudySessionState(isRunning = false, elapsedSeconds = 0)
    triggerHapticFinish()
  }

  fun cancelStudySession() {
    studyTimerJob?.cancel()
    _studyTimerState.value = ActiveStudySessionState(isRunning = false, elapsedSeconds = 0)
  }

  private fun runStudyTimerLoop() {
    studyTimerJob = viewModelScope.launch {
      while (true) {
        delay(1000)
        val current = _studyTimerState.value
        if (!current.isRunning) break
        _studyTimerState.value = current.copy(elapsedSeconds = current.elapsedSeconds + 1)
      }
    }
  }

  // --- OMR SIMULATOR METHODS ---

  fun startOMRTest(paper: QuestionPaper) {
    timerJob?.cancel()
    _omrState.value = ActiveOMRState(
      paper = paper,
      remainingSeconds = paper.durationMinutes * 60,
      isRunning = true,
      currentQuestionIndex = 0
    )
    startOMRTimer()
  }

  fun selectOMROption(questionIndex: Int, optionIndex: Int) {
    val current = _omrState.value ?: return
    if (!current.isRunning || current.isCompleted) return

    val currentAnswers = current.userAnswers.toMutableMap()
    if (currentAnswers[questionIndex] == optionIndex) {
      currentAnswers.remove(questionIndex)
    } else {
      currentAnswers[questionIndex] = optionIndex
      triggerHapticBubble()
    }

    _omrState.value = current.copy(userAnswers = currentAnswers)
  }

  fun setOMRCurrentQuestion(index: Int) {
    val current = _omrState.value ?: return
    _omrState.value = current.copy(currentQuestionIndex = index)
  }

  private fun startOMRTimer() {
    timerJob = viewModelScope.launch {
      while (true) {
        delay(1000)
        val current = _omrState.value ?: break
        if (!current.isRunning || current.isCompleted) break

        if (current.remainingSeconds <= 1) {
          submitOMRTest()
          break
        } else {
          _omrState.value = current.copy(remainingSeconds = current.remainingSeconds - 1)
        }
      }
    }
  }

  fun submitOMRTest() {
    timerJob?.cancel()
    val current = _omrState.value ?: return
    if (current.isCompleted) return

    val paper = current.paper
    val answers = current.userAnswers
    var correctCount = 0
    var wrongCount = 0

    paper.questions.forEachIndexed { index, question ->
      val userAns = answers[index]
      if (userAns != null) {
        if (userAns == question.correctIndex) {
          correctCount++
        } else {
          wrongCount++
        }
      }
    }

    val attempted = correctCount + wrongCount
    val omitted = paper.questions.size - attempted
    val score = (correctCount * 1.0) - (wrongCount * paper.negativeMarkingPerWrong)
    val finalScore = maxOf(0.0, score)
    val accuracy = if (attempted > 0) (correctCount.toDouble() / attempted) * 100.0 else 0.0
    val timeTaken = (paper.durationMinutes * 60) - current.remainingSeconds

    val result = OMRResult(
      paperId = paper.id,
      paperTitle = paper.titleEn,
      examPreference = paper.examPreference,
      totalQuestions = paper.questions.size,
      attempted = attempted,
      correct = correctCount,
      wrong = wrongCount,
      omitted = omitted,
      score = finalScore,
      totalMarks = paper.totalMarks,
      accuracyPercent = accuracy,
      timeTakenSeconds = timeTaken.toLong(),
      timestamp = System.currentTimeMillis(),
      userAnswers = answers
    )

    repository.recordTestResult(result)
    _omrState.value = current.copy(
      isRunning = false,
      isCompleted = true,
      result = result
    )
  }

  fun resetOMRTest() {
    timerJob?.cancel()
    _omrState.value = null
  }

  // --- MEDITATION METHODS ---

  fun startMeditation(activity: MeditationActivity) {
    meditationJob?.cancel()
    _meditationState.value = ActiveMeditationState(
      activity = activity,
      currentPhase = BreathingPhase.INHALE,
      phaseRemainingSeconds = activity.cycleInhale,
      totalRemainingSeconds = activity.durationMinutes * 60,
      isRunning = true,
      currentCycleCount = 1
    )
    startMeditationLoop()
  }

  private fun startMeditationLoop() {
    meditationJob = viewModelScope.launch {
      while (true) {
        delay(1000)
        val current = _meditationState.value ?: break
        if (!current.isRunning || current.isFinished) break

        val newTotalRemaining = current.totalRemainingSeconds - 1
        if (newTotalRemaining <= 0) {
          repository.addZenMinutes(current.activity.durationMinutes)
          _meditationState.value = current.copy(
            totalRemainingSeconds = 0,
            phaseRemainingSeconds = 0,
            isRunning = false,
            isFinished = true
          )
          triggerHapticFinish()
          break
        }

        val newPhaseRemaining = current.phaseRemainingSeconds - 1
        if (newPhaseRemaining <= 0) {
          val act = current.activity
          val (nextPhase, nextDuration) = when (current.currentPhase) {
            BreathingPhase.INHALE -> {
              if (act.cycleHold > 0) BreathingPhase.HOLD to act.cycleHold
              else BreathingPhase.EXHALE to act.cycleExhale
            }
            BreathingPhase.HOLD -> BreathingPhase.EXHALE to act.cycleExhale
            BreathingPhase.EXHALE -> {
              if (act.cycleHoldAfter > 0) BreathingPhase.POST_HOLD to act.cycleHoldAfter
              else BreathingPhase.INHALE to act.cycleInhale
            }
            BreathingPhase.POST_HOLD -> BreathingPhase.INHALE to act.cycleInhale
          }

          val nextCycle = if (nextPhase == BreathingPhase.INHALE) current.currentCycleCount + 1 else current.currentCycleCount
          triggerHapticTick()
          _meditationState.value = current.copy(
            currentPhase = nextPhase,
            phaseRemainingSeconds = nextDuration,
            totalRemainingSeconds = newTotalRemaining,
            currentCycleCount = nextCycle
          )
        } else {
          _meditationState.value = current.copy(
            phaseRemainingSeconds = newPhaseRemaining,
            totalRemainingSeconds = newTotalRemaining
          )
        }
      }
    }
  }

  fun pauseResumeMeditation() {
    val current = _meditationState.value ?: return
    val newRunning = !current.isRunning
    _meditationState.value = current.copy(isRunning = newRunning)
    if (newRunning) {
      startMeditationLoop()
    } else {
      meditationJob?.cancel()
    }
  }

  fun stopMeditation() {
    meditationJob?.cancel()
    _meditationState.value = null
  }

  // --- PDF EXPORT METHODS ---

  fun downloadPaperPdf(context: Context, paper: QuestionPaper): File? {
    val isMl = userProfile.value.isMalayalam
    val file = PdfExporter.generateQuestionPaperPdf(context, paper, isMl)
    if (file != null) {
      PdfExporter.openOrSharePdf(context, file, paper.titleEn)
    }
    return file
  }

  fun downloadScorecardPdf(context: Context, result: OMRResult): File? {
    val profile = userProfile.value
    val file = PdfExporter.generateOMRScorecardPdf(
      context = context,
      result = result,
      candidateName = profile.name.ifBlank { "Aspirant" },
      phoneNumber = profile.phoneNumber.ifBlank { "N/A" },
      educationLevel = profile.educationLevel.label
    )
    if (file != null) {
      PdfExporter.openOrSharePdf(context, file, "OMR Scorecard - ${result.paperTitle}")
    }
    return file
  }

  // --- HAPTIC HELPERS ---

  private fun triggerHapticBubble() {
    try {
      val context = getApplication<Application>()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(30)
      }
    } catch (_: Exception) {}
  }

  private fun triggerHapticTick() {
    try {
      val context = getApplication<Application>()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createOneShot(50, 100)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(50)
      }
    } catch (_: Exception) {}
  }

  private fun triggerHapticFinish() {
    try {
      val context = getApplication<Application>()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(
          VibrationEffect.createWaveform(longArrayOf(0, 100, 100, 200), -1)
        )
      }
    } catch (_: Exception) {}
  }
}
