package com.example.data.model

enum class ExamPreference(
  val displayName: String,
  val titleMl: String,
  val badge: String,
  val targetTagline: String,
  val syllabusOverview: String,
) {
  KERALA_PSC(
    displayName = "Kerala PSC",
    titleMl = "കേരള പി.എസ്.സി",
    badge = "KPSC",
    targetTagline = "LDC, VEO, Secretariat Asst, University Asst, Sub-Inspector & KAS",
    syllabusOverview = "Kerala Renaissance, Geography & History of Kerala, Indian Constitution, General Science, Mental Ability, Malayalam & English.",
  ),
  UPSC(
    displayName = "UPSC Civil Services",
    titleMl = "യു.പി.എസ്.സി",
    badge = "UPSC / IAS",
    targetTagline = "Civil Services Examination (IAS, IPS, IFS, IRS) & Central Services",
    syllabusOverview = "General Studies Paper I (Polity, History, Geography, Economy, Ecology) & CSAT Paper II (Reasoning, Comprehension).",
  ),
  SSC_BANKING(
    displayName = "SSC & Banking",
    titleMl = "എസ്.എസ്.സി & ബാങ്കിംഗ്",
    badge = "SSC / IBPS",
    targetTagline = "CGL, CHSL, MTS, IBPS PO, Clerk & SBI Exams",
    syllabusOverview = "Quantitative Aptitude, Logical Reasoning, General Awareness, Banking/Financial Awareness & English Comprehension.",
  ),
  RRB_TECH(
    displayName = "RRB & Technical",
    titleMl = "റെയിൽവേ & ടെക്നിക്കൽ",
    badge = "RRB / Tech",
    targetTagline = "Railway NTPC, Group D, ALP, JE & Technical Diploma/ITI Posts",
    syllabusOverview = "Basic Science & Engineering, General Awareness, Mathematics, Reasoning & Trade Technical Fundamentals.",
  );

  companion object {
    fun fromName(name: String?): ExamPreference =
      entries.find { it.name.equals(name, ignoreCase = true) } ?: KERALA_PSC
  }
}

enum class EducationLevel(
  val label: String,
  val labelMl: String,
  val targetTiers: String,
) {
  TENTH(
    label = "10th / SSLC",
    labelMl = "10-ാം തരം / SSLC",
    targetTiers = "10th Level Prelims / Mains: LDC, LGS, Field Worker, Office Attendant, Village Field Asst",
  ),
  PLUS_TWO(
    label = "+2 / Higher Secondary",
    labelMl = "+2 / ഹയർ സെക്കൻഡറി",
    targetTiers = "12th Level Prelims: Civil Police Officer (CPO), Fireman, Beat Forest Officer, CHSL",
  ),
  DEGREE(
    label = "Degree / Graduation",
    labelMl = "ബിരുദം (Degree)",
    targetTiers = "Degree Level Prelims: Secretariat Asst, University Asst, Sub-Inspector, KAS, UPSC CSE, CGL",
  ),
  ITI(
    label = "ITI (Technical)",
    labelMl = "ഐ.ടി.ഐ (ITI)",
    targetTiers = "Technical Grade Posts: Tradesman, Railway Assistant Loco Pilot (ALP), Draughtsman",
  ),
  DIPLOMA(
    label = "Polytechnic Diploma",
    labelMl = "പോളിടെക്നിക് ഡിപ്ലോമ",
    targetTiers = "Engineering & Technical: Overseer Gr. I/II/III, Sub-Engineer, Junior Engineer (JE)",
  );

  companion object {
    fun fromName(name: String?): EducationLevel =
      entries.find { it.name.equals(name, ignoreCase = true) } ?: DEGREE
  }
}

enum class SubjectType(
  val titleEn: String,
  val titleMl: String,
  val iconEmoji: String,
) {
  RENAISSANCE("Kerala Renaissance & History", "കേരള നവോത്ഥാനവും ചരിത്രവും", "🏛️"),
  CONSTITUTION("Indian Constitution & Polity", "ഇന്ത്യൻ ഭരണഘടനയും രാഷ്ട്രീയവും", "⚖️"),
  GEOGRAPHY("Geography & Environment", "ഭൂമിശാസ്ത്രവും പരിസ്ഥിതിയും", "🌍"),
  SCIENCE("General Science & Tech", "പൊതുശാസ്ത്രവും സാങ്കേതികവിദ്യയും", "🔬"),
  MATHS_REASONING("Maths & Mental Ability", "ലഘുഗണിതവും മാനസികശേഷിയും", "📐"),
  CURRENT_AFFAIRS("Current Affairs & GK", "സമകാലിക വിവരങ്ങളും പൊതുവിജ്ഞാനവും", "📰"),
  MALAYALAM("Malayalam Language", "മലയാളം വ്യാകരണവും സാഹിത്യവും", "✍️"),
  ENGLISH("General English", "ജനറൽ ഇംഗ്ലീഷ്", "📖"),
  ECONOMY("Indian Economy & Finance", "ഇന്ത്യൻ സമ്പദ്‌വ്യവസ്ഥ", "📈"),
  CSAT("CSAT Aptitude & Logic", "സിസാറ്റ് യുക്തിചിന്ത", "🧩")
}

data class Question(
  val id: String,
  val examPreference: ExamPreference,
  val subject: SubjectType,
  val educationLevel: EducationLevel,
  val year: Int,
  val paperTitle: String,
  val questionEn: String,
  val questionMl: String,
  val optionsEn: List<String>,
  val optionsMl: List<String>,
  val correctIndex: Int, // 0 = A, 1 = B, 2 = C, 3 = D
  val explanationEn: String,
  val explanationMl: String,
)

data class QuestionPaper(
  val id: String,
  val titleEn: String,
  val titleMl: String,
  val examPreference: ExamPreference,
  val educationLevel: EducationLevel,
  val year: Int,
  val durationMinutes: Int,
  val totalMarks: Double,
  val negativeMarkingPerWrong: Double = 0.33,
  val questions: List<Question>,
)

data class UserProfile(
  val name: String = "",
  val phoneNumber: String = "",
  val educationLevel: EducationLevel = EducationLevel.DEGREE,
  val examPreference: ExamPreference = ExamPreference.KERALA_PSC,
  val isMalayalam: Boolean = false,
  val streakDays: Int = 1,
  val zenMinutesTotal: Int = 0,
  val testsCompletedCount: Int = 0,
  val isLoggedIn: Boolean = false,
)

data class OMRResult(
  val paperId: String,
  val paperTitle: String,
  val examPreference: ExamPreference,
  val totalQuestions: Int,
  val attempted: Int,
  val correct: Int,
  val wrong: Int,
  val omitted: Int,
  val score: Double,
  val totalMarks: Double,
  val accuracyPercent: Double,
  val timeTakenSeconds: Long,
  val timestamp: Long,
  val userAnswers: Map<Int, Int>, // question index -> chosen option (0..3)
)

enum class MeditationExerciseType {
  BREATH_478,
  BOX_BREATHING,
  EYE_REST_202020,
  EXAM_MINDFULNESS,
  BRAIN_RESET
}

data class MeditationActivity(
  val id: String,
  val type: MeditationExerciseType,
  val titleEn: String,
  val titleMl: String,
  val subtitleEn: String,
  val subtitleMl: String,
  val durationMinutes: Int,
  val benefitsEn: String,
  val benefitsMl: String,
  val cycleInhale: Int = 4,
  val cycleHold: Int = 4,
  val cycleExhale: Int = 4,
  val cycleHoldAfter: Int = 0,
  val affirmationEn: String = "",
  val affirmationMl: String = "",
)

data class DailyGoal(
  val id: String,
  val titleEn: String,
  val titleMl: String,
  val category: String,
  val isCompleted: Boolean = false,
  val targetQuantity: String = "",
  val isCustom: Boolean = false,
)

data class StreakDayStatus(
  val dayLabel: String,
  val dateNumber: Int,
  val isCompleted: Boolean,
  val studyMinutes: Int,
  val isToday: Boolean,
  val formattedDate: String = "",
)

data class SubjectMastery(
  val subject: SubjectType,
  val totalQuestions: Int,
  val correctCount: Int,
  val attemptedCount: Int,
  val masteryPercentage: Float,
  val studyMinutesLogged: Int,
)

data class ExamMilestone(
  val titleEn: String,
  val titleMl: String,
  val targetDateEpochMs: Long,
  val daysRemaining: Int,
)

enum class StudentEnergyLevel(val emoji: String, val labelEn: String, val labelMl: String) {
  HIGH_FOCUS("⚡", "High Focus", "ഉയർന്ന ഏകാഗ്രത"),
  CALM_STEADY("🧘", "Calm & Steady", "ശാന്തമായ മനസ്സ്"),
  FATIGUED("☕", "Fatigued / Need Break", "ക്ഷീണം / വിശ്രമം വേണം");

  companion object {
    fun fromName(name: String?): StudentEnergyLevel =
      entries.find { it.name.equals(name, ignoreCase = true) } ?: CALM_STEADY
  }
}

data class ActiveStudySessionState(
  val isRunning: Boolean = false,
  val elapsedSeconds: Long = 0,
  val targetMinutes: Int = 45,
  val selectedSubject: SubjectType = SubjectType.CONSTITUTION,
)
