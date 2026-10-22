package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.DailyGoal
import com.example.data.model.EducationLevel
import com.example.data.model.ExamMilestone
import com.example.data.model.ExamPreference
import com.example.data.model.OMRResult
import com.example.data.model.StreakDayStatus
import com.example.data.model.StudentEnergyLevel
import com.example.data.model.SubjectType
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UserPreferencesRepository(context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("exam_mindcare_prefs", Context.MODE_PRIVATE)

  private val _userProfile = MutableStateFlow(loadProfile())
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  private val _recentResults = MutableStateFlow(loadRecentResults())
  val recentResults: StateFlow<List<OMRResult>> = _recentResults.asStateFlow()

  private val _bookmarkedIds = MutableStateFlow(loadBookmarks())
  val bookmarkedIds: StateFlow<Set<String>> = _bookmarkedIds.asStateFlow()

  private val _dailyGoals = MutableStateFlow(loadDailyGoals())
  val dailyGoals: StateFlow<List<DailyGoal>> = _dailyGoals.asStateFlow()

  // New users start with blank / 0 study minutes
  private val _todayStudyMinutes = MutableStateFlow(prefs.getInt(KEY_TODAY_MINUTES, 0))
  val todayStudyMinutes: StateFlow<Int> = _todayStudyMinutes.asStateFlow()

  private val _dailyTargetHours = MutableStateFlow(prefs.getFloat(KEY_DAILY_TARGET_HOURS, 3.5f))
  val dailyTargetHours: StateFlow<Float> = _dailyTargetHours.asStateFlow()

  // New users start with blank / 0 longest streak
  private val _longestStreakDays = MutableStateFlow(prefs.getInt(KEY_LONGEST_STREAK, 0))
  val longestStreakDays: StateFlow<Int> = _longestStreakDays.asStateFlow()

  private val _energyLevel = MutableStateFlow(
    StudentEnergyLevel.fromName(prefs.getString(KEY_ENERGY_LEVEL, StudentEnergyLevel.HIGH_FOCUS.name))
  )
  val energyLevel: StateFlow<StudentEnergyLevel> = _energyLevel.asStateFlow()

  private val _examMilestone = MutableStateFlow(loadExamMilestone())
  val examMilestone: StateFlow<ExamMilestone> = _examMilestone.asStateFlow()

  private val _subjectStudyMinutes = MutableStateFlow(loadSubjectStudyMinutes())
  val subjectStudyMinutes: StateFlow<Map<SubjectType, Int>> = _subjectStudyMinutes.asStateFlow()

  private fun loadProfile(): UserProfile {
    val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    val name = prefs.getString(KEY_NAME, "") ?: ""
    val phone = prefs.getString(KEY_PHONE, "") ?: ""
    val educationStr = prefs.getString(KEY_EDUCATION, EducationLevel.DEGREE.name)
    val examStr = prefs.getString(KEY_EXAM, ExamPreference.KERALA_PSC.name)
    val isMalayalam = prefs.getBoolean(KEY_IS_MALAYALAM, false)
    // New user starts blank at 0 streak, 0 zen mins, 0 tests completed
    val streakDays = prefs.getInt(KEY_STREAK, 0)
    val zenMinutes = prefs.getInt(KEY_ZEN_MINUTES, 0)
    val testsCount = prefs.getInt(KEY_TESTS_COUNT, 0)

    return UserProfile(
      name = name,
      phoneNumber = phone,
      educationLevel = EducationLevel.fromName(educationStr),
      examPreference = ExamPreference.fromName(examStr),
      isMalayalam = isMalayalam,
      streakDays = streakDays,
      zenMinutesTotal = zenMinutes,
      testsCompletedCount = testsCount,
      isLoggedIn = isLoggedIn
    )
  }

  fun saveProfile(profile: UserProfile) {
    prefs.edit()
      .putBoolean(KEY_IS_LOGGED_IN, profile.isLoggedIn)
      .putString(KEY_NAME, profile.name)
      .putString(KEY_PHONE, profile.phoneNumber)
      .putString(KEY_EDUCATION, profile.educationLevel.name)
      .putString(KEY_EXAM, profile.examPreference.name)
      .putBoolean(KEY_IS_MALAYALAM, profile.isMalayalam)
      .putInt(KEY_STREAK, profile.streakDays)
      .putInt(KEY_ZEN_MINUTES, profile.zenMinutesTotal)
      .putInt(KEY_TESTS_COUNT, profile.testsCompletedCount)
      .apply()
    val previousExam = _userProfile.value.examPreference
    _userProfile.value = profile

    // Only if exam preference changes or goals are uninitialized, refresh default goals & milestone
    if (previousExam != profile.examPreference || _dailyGoals.value.isEmpty()) {
      refreshGoalsForExam(profile.examPreference)
    }
  }

  fun resetAllProgressForNewUser() {
    _todayStudyMinutes.value = 0
    _longestStreakDays.value = 0
    _recentResults.value = emptyList()
    val zeroMap = SubjectType.entries.associateWith { 0 }
    _subjectStudyMinutes.value = zeroMap
    saveSubjectStudyMinutes(zeroMap)

    prefs.edit()
      .putInt(KEY_TODAY_MINUTES, 0)
      .putInt(KEY_LONGEST_STREAK, 0)
      .putInt(KEY_STREAK, 0)
      .putInt(KEY_ZEN_MINUTES, 0)
      .putInt(KEY_TESTS_COUNT, 0)
      .remove(KEY_RESULTS_JSON)
      .remove(KEY_GOALS_JSON)
      .apply()

    refreshGoalsForExam(_userProfile.value.examPreference)
  }

  fun updateExamPreference(preference: ExamPreference) {
    val current = _userProfile.value
    val updated = current.copy(examPreference = preference)
    saveProfile(updated)
  }

  fun updateEducationLevel(level: EducationLevel) {
    val current = _userProfile.value
    val updated = current.copy(educationLevel = level)
    saveProfile(updated)
  }

  fun toggleMalayalam(isMalayalam: Boolean) {
    val current = _userProfile.value
    val updated = current.copy(isMalayalam = isMalayalam)
    saveProfile(updated)
  }

  fun addZenMinutes(minutes: Int) {
    val current = _userProfile.value
    val newStreak = maxOf(current.streakDays, 1)
    val updated = current.copy(
      zenMinutesTotal = current.zenMinutesTotal + minutes,
      streakDays = newStreak
    )
    if (newStreak > _longestStreakDays.value) {
      _longestStreakDays.value = newStreak
      prefs.edit().putInt(KEY_LONGEST_STREAK, newStreak).apply()
    }
    saveProfile(updated)
    markGoalCategoryCompleted("Mindfulness")
  }

  fun recordTestResult(result: OMRResult) {
    val currentList = _recentResults.value.toMutableList()
    currentList.add(0, result)
    _recentResults.value = currentList
    saveResultsToPrefs(currentList)

    val currentProfile = _userProfile.value
    val newStreak = maxOf(currentProfile.streakDays + 1, 1)
    val updated = currentProfile.copy(
      testsCompletedCount = currentProfile.testsCompletedCount + 1,
      streakDays = newStreak
    )
    if (newStreak > _longestStreakDays.value) {
      _longestStreakDays.value = newStreak
      prefs.edit().putInt(KEY_LONGEST_STREAK, newStreak).apply()
    }
    saveProfile(updated)

    // Also credit 30 mins to today's study minutes
    addStudyMinutes(30)
    markGoalCategoryCompleted("Mock Test")
  }

  fun toggleBookmark(questionId: String) {
    val current = _bookmarkedIds.value.toMutableSet()
    if (current.contains(questionId)) {
      current.remove(questionId)
    } else {
      current.add(questionId)
    }
    _bookmarkedIds.value = current
    prefs.edit().putStringSet(KEY_BOOKMARKS, current).apply()
  }

  // --- GOALS & STREAK MANAGEMENT ---

  fun toggleGoalCompleted(goalId: String) {
    val current = _dailyGoals.value.map { goal ->
      if (goal.id == goalId) goal.copy(isCompleted = !goal.isCompleted) else goal
    }
    _dailyGoals.value = current
    saveDailyGoalsToPrefs(current)

    // Check if at least 1 goal completed to light up Day 1 streak
    val completedCount = current.count { it.isCompleted }
    if (completedCount >= 1) {
      val profile = _userProfile.value
      val newStreak = maxOf(profile.streakDays, 1)
      if (newStreak != profile.streakDays) {
        val updated = profile.copy(streakDays = newStreak)
        if (newStreak > _longestStreakDays.value) {
          _longestStreakDays.value = newStreak
          prefs.edit().putInt(KEY_LONGEST_STREAK, newStreak).apply()
        }
        saveProfile(updated)
      }
    }
  }

  fun addCustomGoal(title: String, category: String, quantity: String) {
    val newGoal = DailyGoal(
      id = "custom_${System.currentTimeMillis()}",
      titleEn = title,
      titleMl = title,
      category = category.ifBlank { "Custom" },
      isCompleted = false,
      targetQuantity = quantity,
      isCustom = true
    )
    val current = _dailyGoals.value.toMutableList()
    current.add(newGoal)
    _dailyGoals.value = current
    saveDailyGoalsToPrefs(current)
  }

  fun removeCustomGoal(goalId: String) {
    val current = _dailyGoals.value.filterNot { it.id == goalId }
    _dailyGoals.value = current
    saveDailyGoalsToPrefs(current)
  }

  private fun markGoalCategoryCompleted(category: String) {
    val current = _dailyGoals.value.map { goal ->
      if (goal.category.equals(category, ignoreCase = true) && !goal.isCompleted) {
        goal.copy(isCompleted = true)
      } else goal
    }
    _dailyGoals.value = current
    saveDailyGoalsToPrefs(current)
  }

  fun addStudyMinutes(minutes: Int) {
    val newTotal = _todayStudyMinutes.value + minutes
    _todayStudyMinutes.value = newTotal
    prefs.edit().putInt(KEY_TODAY_MINUTES, newTotal).apply()
  }

  fun logStudySession(subject: SubjectType, minutes: Int) {
    addStudyMinutes(minutes)
    val currentMap = _subjectStudyMinutes.value.toMutableMap()
    val prev = currentMap[subject] ?: 0
    currentMap[subject] = prev + minutes
    _subjectStudyMinutes.value = currentMap
    saveSubjectStudyMinutes(currentMap)

    // If student studies at least 15 minutes, light up Day 1 streak
    val profile = _userProfile.value
    val newStreak = maxOf(profile.streakDays, 1)
    if (newStreak != profile.streakDays) {
      if (newStreak > _longestStreakDays.value) {
        _longestStreakDays.value = newStreak
        prefs.edit().putInt(KEY_LONGEST_STREAK, newStreak).apply()
      }
      saveProfile(profile.copy(streakDays = newStreak))
    }
  }

  fun setDailyTargetHours(hours: Float) {
    _dailyTargetHours.value = hours
    prefs.edit().putFloat(KEY_DAILY_TARGET_HOURS, hours).apply()
  }

  fun setEnergyLevel(level: StudentEnergyLevel) {
    _energyLevel.value = level
    prefs.edit().putString(KEY_ENERGY_LEVEL, level.name).apply()
  }

  fun updateExamMilestoneDays(days: Int) {
    val current = _examMilestone.value
    val updated = current.copy(daysRemaining = days)
    _examMilestone.value = updated
    prefs.edit().putInt(KEY_EXAM_DAYS_LEFT, days).apply()
  }

  // Weekly status for visual calendar heatmap dynamically computed from actual system calendar
  fun getWeeklyStatus(): List<StreakDayStatus> {
    val todayCal = Calendar.getInstance()
    val todayYear = todayCal.get(Calendar.YEAR)
    val todayDayOfYear = todayCal.get(Calendar.DAY_OF_YEAR)

    // Calendar.DAY_OF_WEEK: Sunday=1, Monday=2, ..., Saturday=7
    val currentDayOfWeek = todayCal.get(Calendar.DAY_OF_WEEK)
    // Offset from Monday of the current week (Monday is first day)
    val daysFromMonday = if (currentDayOfWeek == Calendar.SUNDAY) 6 else (currentDayOfWeek - Calendar.MONDAY)

    val startOfWeekCal = Calendar.getInstance().apply {
      timeInMillis = todayCal.timeInMillis
      add(Calendar.DAY_OF_YEAR, -daysFromMonday)
    }

    val dayFormat = SimpleDateFormat("EEE", Locale.US)
    val fullDateFormat = SimpleDateFormat("MMM d", Locale.US)

    val userStreak = _userProfile.value.streakDays
    val todayMinutes = _todayStudyMinutes.value

    return (0..6).map { dayOffset ->
      val cal = Calendar.getInstance().apply {
        timeInMillis = startOfWeekCal.timeInMillis
        add(Calendar.DAY_OF_YEAR, dayOffset)
      }

      val isToday = (cal.get(Calendar.YEAR) == todayYear && cal.get(Calendar.DAY_OF_YEAR) == todayDayOfYear)
      val isPast = cal.before(todayCal) && !isToday

      val isCompleted = when {
        isToday -> todayMinutes >= 30
        isPast -> {
          val daysAgo = daysFromMonday - dayOffset
          daysAgo in 1 until userStreak
        }
        else -> false
      }

      val studyMins = when {
        isToday -> todayMinutes
        isCompleted -> 45
        else -> 0
      }

      StreakDayStatus(
        dayLabel = dayFormat.format(cal.time),
        dateNumber = cal.get(Calendar.DAY_OF_MONTH),
        isCompleted = isCompleted,
        studyMinutes = studyMins,
        isToday = isToday,
        formattedDate = fullDateFormat.format(cal.time)
      )
    }
  }

  fun getActualCurrentDateFormatted(isMalayalam: Boolean = false): String {
    val date = Date()
    return if (isMalayalam) {
      SimpleDateFormat("yyyy MMMM d, EEEE", Locale.getDefault()).format(date)
    } else {
      SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(date)
    }
  }

  fun getActualCurrentTimeFormatted(): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
  }

  private fun loadDailyGoals(): List<DailyGoal> {
    val rawJson = prefs.getString(KEY_GOALS_JSON, null)
    if (rawJson != null) {
      try {
        val array = JSONArray(rawJson)
        val list = mutableListOf<DailyGoal>()
        for (i in 0 until array.length()) {
          val obj = array.getJSONObject(i)
          list.add(
            DailyGoal(
              id = obj.getString("id"),
              titleEn = obj.getString("titleEn"),
              titleMl = obj.getString("titleMl"),
              category = obj.getString("category"),
              isCompleted = obj.getBoolean("isCompleted"),
              targetQuantity = obj.optString("targetQuantity", ""),
              isCustom = obj.optBoolean("isCustom", false)
            )
          )
        }
        if (list.isNotEmpty()) return list
      } catch (_: Exception) {}
    }
    return getDefaultGoalsForExam(_userProfile.value.examPreference)
  }

  private fun saveDailyGoalsToPrefs(list: List<DailyGoal>) {
    try {
      val array = JSONArray()
      list.forEach { g ->
        val obj = JSONObject()
        obj.put("id", g.id)
        obj.put("titleEn", g.titleEn)
        obj.put("titleMl", g.titleMl)
        obj.put("category", g.category)
        obj.put("isCompleted", g.isCompleted)
        obj.put("targetQuantity", g.targetQuantity)
        obj.put("isCustom", g.isCustom)
        array.put(obj)
      }
      prefs.edit().putString(KEY_GOALS_JSON, array.toString()).apply()
    } catch (_: Exception) {}
  }

  private fun refreshGoalsForExam(exam: ExamPreference) {
    val defaultGoals = getDefaultGoalsForExam(exam)
    _dailyGoals.value = defaultGoals
    saveDailyGoalsToPrefs(defaultGoals)

    val milestone = getDefaultMilestoneForExam(exam)
    _examMilestone.value = milestone
  }

  // All initial default goals start uncompleted (false) for a fresh clean slate
  private fun getDefaultGoalsForExam(exam: ExamPreference): List<DailyGoal> {
    return when (exam) {
      ExamPreference.KERALA_PSC -> listOf(
        DailyGoal(
          id = "goal_1",
          titleEn = "Solve 15 Kerala Renaissance & History PYQs",
          titleMl = "15 കേരള നവോത്ഥാന PYQ ചോദ്യങ്ങൾ പരിശീലിക്കുക",
          category = "PYQ Practice",
          isCompleted = false,
          targetQuantity = "15 Questions"
        ),
        DailyGoal(
          id = "goal_2",
          titleEn = "Attempt 1 Full OMR Mock Test (LDC/Degree Level)",
          titleMl = "1 സമ്പൂർണ്ണ OMR മോഡൽ പരീക്ഷ പൂർത്തിയാക്കുക",
          category = "Mock Test",
          isCompleted = false,
          targetQuantity = "1 Paper"
        ),
        DailyGoal(
          id = "goal_3",
          titleEn = "Revise Indian Constitution Fundamental Rights & Writs",
          titleMl = "ഭരണഘടന: മൗലികാവകാശങ്ങളും റിട്ടുകളും റിവിഷൻ ചെയ്യുക",
          category = "Revision",
          isCompleted = false,
          targetQuantity = "45 Mins"
        ),
        DailyGoal(
          id = "goal_4",
          titleEn = "Complete 5-Min 4-7-8 Breathing & Mind Care Break",
          titleMl = "5 മിനിറ്റ് 4-7-8 ശ്വസന വ്യായാമം പൂർത്തിയാക്കുക",
          category = "Mindfulness",
          isCompleted = false,
          targetQuantity = "5 Mins"
        ),
        DailyGoal(
          id = "goal_5",
          titleEn = "Read Current Affairs & Kerala GK Updates",
          titleMl = "സമകാലിക വിവരങ്ങളും കേരള വിവരങ്ങളും വായിക്കുക",
          category = "Current Affairs",
          isCompleted = false,
          targetQuantity = "30 Mins"
        )
      )
      ExamPreference.UPSC -> listOf(
        DailyGoal(
          id = "goal_1",
          titleEn = "Analyze 10 UPSC Prelims GS Paper I Multi-Statement Questions",
          titleMl = "യു.പി.എസ്.സി 10 മുൻവർഷ ചോദ്യങ്ങൾ പരിശോധിക്കുക",
          category = "PYQ Practice",
          isCompleted = false,
          targetQuantity = "10 Questions"
        ),
        DailyGoal(
          id = "goal_2",
          titleEn = "Complete 1 Sectional CSAT Aptitude Drill",
          titleMl = "സിസാറ്റ് റീസണിംഗ് പരിശീലനം നടത്തുക",
          category = "Mock Test",
          isCompleted = false,
          targetQuantity = "40 Mins"
        ),
        DailyGoal(
          id = "goal_3",
          titleEn = "Revise Polity Money Bill & Judicial Doctrines",
          titleMl = "പോളിറ്റി മണി ബിൽ, സുപ്രീം കോടതി വിധികൾ റിവിഷൻ",
          category = "Revision",
          isCompleted = false,
          targetQuantity = "60 Mins"
        ),
        DailyGoal(
          id = "goal_4",
          titleEn = "5-Min Box Breathing Focus Session Before Study Sprint",
          titleMl = "പഠനത്തിന് മുൻപ് 5 മിനിറ്റ് ബോക്സ് ബ്രീത്തിംഗ്",
          category = "Mindfulness",
          isCompleted = false,
          targetQuantity = "5 Mins"
        )
      )
      ExamPreference.SSC_BANKING, ExamPreference.RRB_TECH -> listOf(
        DailyGoal(
          id = "goal_1",
          titleEn = "Solve 20 Quantitative Aptitude & Speed Maths Sets",
          titleMl = "20 മാത്തമാറ്റിക്സ് കണക്കുകൾ വേഗത്തിൽ പരിഹരിക്കുക",
          category = "PYQ Practice",
          isCompleted = false,
          targetQuantity = "20 Problems"
        ),
        DailyGoal(
          id = "goal_2",
          titleEn = "Attempt 1 Full Sectional Speed Test",
          titleMl = "1 സ്പീഡ് മോക്ക് ടെസ്റ്റ് പൂർത്തിയാക്കുക",
          category = "Mock Test",
          isCompleted = false,
          targetQuantity = "1 Test"
        ),
        DailyGoal(
          id = "goal_3",
          titleEn = "General Science & Technical Principles Revision",
          titleMl = "പൊതുശാസ്ത്രം & ടെക്നിക്കൽ റിവിഷൻ",
          category = "Revision",
          isCompleted = false,
          targetQuantity = "45 Mins"
        ),
        DailyGoal(
          id = "goal_4",
          titleEn = "3-Minute Eye Rest & Mind Relaxation Routine",
          titleMl = "3 മിനിറ്റ് കണ്ണ് റിലാക്സേഷൻ വ്യായാമം",
          category = "Mindfulness",
          isCompleted = false,
          targetQuantity = "3 Mins"
        )
      )
    }
  }

  private fun loadExamMilestone(): ExamMilestone {
    val days = prefs.getInt(KEY_EXAM_DAYS_LEFT, 48)
    val exam = _userProfile.value.examPreference
    return getDefaultMilestoneForExam(exam).copy(daysRemaining = days)
  }

  private fun getDefaultMilestoneForExam(exam: ExamPreference): ExamMilestone {
    return when (exam) {
      ExamPreference.KERALA_PSC -> ExamMilestone(
        titleEn = "Kerala PSC LDC / Degree Prelims",
        titleMl = "കേരള പി.എസ്.സി പ്രിലിംസ് പരീക്ഷ",
        targetDateEpochMs = System.currentTimeMillis() + (48L * 24 * 3600 * 1000),
        daysRemaining = 48
      )
      ExamPreference.UPSC -> ExamMilestone(
        titleEn = "UPSC Civil Services Prelims (CSE)",
        titleMl = "യു.പി.എസ്.സി സിവിൽ സർവീസസ് പ്രിലിംസ്",
        targetDateEpochMs = System.currentTimeMillis() + (112L * 24 * 3600 * 1000),
        daysRemaining = 112
      )
      ExamPreference.SSC_BANKING -> ExamMilestone(
        titleEn = "SSC CGL / IBPS PO Tier I",
        titleMl = "എസ്.എസ്.സി സി.ജി.എൽ ടയർ 1",
        targetDateEpochMs = System.currentTimeMillis() + (64L * 24 * 3600 * 1000),
        daysRemaining = 64
      )
      ExamPreference.RRB_TECH -> ExamMilestone(
        titleEn = "Railway ALP & Technical CBT 1",
        titleMl = "റെയിൽവേ എ.എൽ.പി സി.ബി.ടി 1",
        targetDateEpochMs = System.currentTimeMillis() + (75L * 24 * 3600 * 1000),
        daysRemaining = 75
      )
    }
  }

  // All subjects start clean with 0 minutes for new users
  private fun loadSubjectStudyMinutes(): Map<SubjectType, Int> {
    val map = mutableMapOf<SubjectType, Int>()
    SubjectType.entries.forEach { sub ->
      map[sub] = prefs.getInt("sub_${sub.name.lowercase()}", 0)
    }
    return map
  }

  private fun saveSubjectStudyMinutes(map: Map<SubjectType, Int>) {
    val editor = prefs.edit()
    map.forEach { (sub, mins) ->
      editor.putInt("sub_${sub.name.lowercase()}", mins)
    }
    editor.apply()
  }

  private fun loadBookmarks(): Set<String> {
    return prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
  }

  private fun loadRecentResults(): List<OMRResult> {
    val rawJson = prefs.getString(KEY_RESULTS_JSON, null) ?: return emptyList()
    return try {
      val array = JSONArray(rawJson)
      val list = mutableListOf<OMRResult>()
      for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        val userAnswersMap = mutableMapOf<Int, Int>()
        if (obj.has("userAnswers")) {
          val answersObj = obj.getJSONObject("userAnswers")
          answersObj.keys().forEach { key ->
            userAnswersMap[key.toInt()] = answersObj.getInt(key)
          }
        }
        list.add(
          OMRResult(
            paperId = obj.optString("paperId"),
            paperTitle = obj.optString("paperTitle"),
            examPreference = ExamPreference.fromName(obj.optString("exam")),
            totalQuestions = obj.optInt("totalQuestions"),
            attempted = obj.optInt("attempted"),
            correct = obj.optInt("correct"),
            wrong = obj.optInt("wrong"),
            omitted = obj.optInt("omitted"),
            score = obj.optDouble("score"),
            totalMarks = obj.optDouble("totalMarks"),
            accuracyPercent = obj.optDouble("accuracyPercent"),
            timeTakenSeconds = obj.optLong("timeTakenSeconds"),
            timestamp = obj.optLong("timestamp"),
            userAnswers = userAnswersMap
          )
        )
      }
      list
    } catch (_: Exception) {
      emptyList()
    }
  }

  private fun saveResultsToPrefs(list: List<OMRResult>) {
    try {
      val array = JSONArray()
      list.take(20).forEach { res ->
        val obj = JSONObject()
        obj.put("paperId", res.paperId)
        obj.put("paperTitle", res.paperTitle)
        obj.put("exam", res.examPreference.name)
        obj.put("totalQuestions", res.totalQuestions)
        obj.put("attempted", res.attempted)
        obj.put("correct", res.correct)
        obj.put("wrong", res.wrong)
        obj.put("omitted", res.omitted)
        obj.put("score", res.score)
        obj.put("totalMarks", res.totalMarks)
        obj.put("accuracyPercent", res.accuracyPercent)
        obj.put("timeTakenSeconds", res.timeTakenSeconds)
        obj.put("timestamp", res.timestamp)
        val answersObj = JSONObject()
        res.userAnswers.forEach { (k, v) ->
          answersObj.put(k.toString(), v)
        }
        obj.put("userAnswers", answersObj)
        array.put(obj)
      }
      prefs.edit().putString(KEY_RESULTS_JSON, array.toString()).apply()
    } catch (_: Exception) {}
  }

  companion object {
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_NAME = "user_name"
    private const val KEY_PHONE = "user_phone"
    private const val KEY_EDUCATION = "user_education"
    private const val KEY_EXAM = "user_exam_preference"
    private const val KEY_IS_MALAYALAM = "is_malayalam"
    private const val KEY_STREAK = "streak_days"
    private const val KEY_LONGEST_STREAK = "longest_streak_days"
    private const val KEY_ZEN_MINUTES = "zen_minutes"
    private const val KEY_TESTS_COUNT = "tests_count"
    private const val KEY_BOOKMARKS = "bookmarks"
    private const val KEY_RESULTS_JSON = "results_json"
    private const val KEY_GOALS_JSON = "daily_goals_json"
    private const val KEY_TODAY_MINUTES = "today_study_minutes"
    private const val KEY_DAILY_TARGET_HOURS = "daily_target_hours"
    private const val KEY_ENERGY_LEVEL = "student_energy_level"
    private const val KEY_EXAM_DAYS_LEFT = "exam_days_left"
  }
}
