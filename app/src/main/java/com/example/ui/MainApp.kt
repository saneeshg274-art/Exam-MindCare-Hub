package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MeditationScreen
import com.example.ui.screens.OmrScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.PyqScreen
import com.example.ui.screens.StreakDashboardScreen

enum class AppTab(val labelEn: String, val labelMl: String) {
  HOME("Home", "ഹോം"),
  TRACKER("Streak", "സ്ട്രീക്ക്"),
  PYQ("PYQs", "ചോദ്യങ്ങൾ"),
  OMR("OMR", "OMR"),
  MEDITATION("Mind Care", "മനഃശാന്തി"),
  PROFILE("Profile", "പ്രൊഫൈൽ")
}

@Composable
fun MainApp(viewModel: MainViewModel) {
  val context = LocalContext.current
  val profile by viewModel.userProfile.collectAsState()
  val recentResults by viewModel.recentResults.collectAsState()
  val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
  val filteredQuestions by viewModel.filteredQuestions.collectAsState()
  val selectedSubject by viewModel.selectedSubject.collectAsState()
  val selectedYear by viewModel.selectedYear.collectAsState()
  val omrState by viewModel.omrState.collectAsState()
  val meditationState by viewModel.meditationState.collectAsState()

  // Streak & Dashboard state
  val weeklyActivity by viewModel.weeklyActivity.collectAsState()
  val dailyGoals by viewModel.dailyGoals.collectAsState()
  val todayStudyMinutes by viewModel.todayStudyMinutes.collectAsState()
  val dailyTargetHours by viewModel.dailyTargetHours.collectAsState()
  val longestStreakDays by viewModel.longestStreakDays.collectAsState()
  val energyLevel by viewModel.energyLevel.collectAsState()
  val examMilestone by viewModel.examMilestone.collectAsState()
  val subjectMasteryList by viewModel.subjectMasteryList.collectAsState()
  val studyTimerState by viewModel.studyTimerState.collectAsState()

  var selectedTab by remember { mutableStateOf(AppTab.HOME) }
  var isEditingProfileManually by remember { mutableStateOf(false) }

  // If user is not logged in or chose to re-edit profile
  if (!profile.isLoggedIn || isEditingProfileManually) {
    LoginScreen(
      initialProfile = profile,
      onLoginSuccess = { name, phone, education, exam, malayalam ->
        viewModel.login(name, phone, education, exam, malayalam)
        isEditingProfileManually = false
      }
    )
    return
  }

  // Handle system back button gracefully
  BackHandler(enabled = selectedTab != AppTab.HOME || omrState != null || meditationState != null) {
    when {
      meditationState != null -> viewModel.stopMeditation()
      omrState != null -> viewModel.resetOMRTest()
      selectedTab != AppTab.HOME -> selectedTab = AppTab.HOME
    }
  }

  // The 5 primary tabs to show in the navigation bar
  val primaryTabs = listOf(
    AppTab.HOME,
    AppTab.TRACKER,
    AppTab.PYQ,
    AppTab.OMR,
    AppTab.MEDITATION
  )

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    bottomBar = {
      // Hide bottom bar during active full-screen meditation or during an active OMR exam to avoid distraction
      val hideBottomBar = (meditationState != null) || (omrState != null && !omrState!!.isCompleted)
      if (!hideBottomBar) {
        NavigationBar(
          modifier = Modifier
            .navigationBarsPadding()
            .testTag("bottom_nav_bar"),
          containerColor = MaterialTheme.colorScheme.surface,
          tonalElevation = 6.dp
        ) {
          primaryTabs.forEach { tab ->
            val isSelected = selectedTab == tab
            val iconSelected = when (tab) {
              AppTab.HOME -> Icons.Default.Home
              AppTab.TRACKER -> Icons.Default.LocalFireDepartment
              AppTab.PYQ -> Icons.Default.MenuBook
              AppTab.OMR -> Icons.Default.Assignment
              AppTab.MEDITATION -> Icons.Default.SelfImprovement
              AppTab.PROFILE -> Icons.Default.Person
            }
            val iconUnselected = when (tab) {
              AppTab.HOME -> Icons.Outlined.Home
              AppTab.TRACKER -> Icons.Outlined.LocalFireDepartment
              AppTab.PYQ -> Icons.Outlined.MenuBook
              AppTab.OMR -> Icons.Outlined.Assignment
              AppTab.MEDITATION -> Icons.Outlined.SelfImprovement
              AppTab.PROFILE -> Icons.Outlined.Person
            }

            NavigationBarItem(
              selected = isSelected,
              onClick = { selectedTab = tab },
              icon = {
                Icon(
                  imageVector = if (isSelected) iconSelected else iconUnselected,
                  contentDescription = tab.labelEn
                )
              },
              label = {
                Text(
                  text = if (profile.isMalayalam) tab.labelMl else tab.labelEn,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
              ),
              modifier = Modifier.testTag("nav_tab_${tab.name}")
            )
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        AppTab.HOME -> {
          HomeScreen(
            profile = profile,
            onNavigateToOmr = { paper ->
              if (paper != null) {
                viewModel.startOMRTest(paper)
              }
              selectedTab = AppTab.OMR
            },
            onNavigateToMeditation = {
              selectedTab = AppTab.MEDITATION
            },
            onNavigateToPyq = { subj ->
              viewModel.setSubjectFilter(subj)
              selectedTab = AppTab.PYQ
            },
            onNavigateToTracker = {
              selectedTab = AppTab.TRACKER
            },
            onNavigateToProfile = {
              selectedTab = AppTab.PROFILE
            },
            onToggleMalayalam = { viewModel.toggleMalayalam(it) },
            onDownloadPaper = { paper ->
              val file = viewModel.downloadPaperPdf(context, paper)
              if (file != null) {
                Toast.makeText(context, "Question Paper downloaded as PDF!", Toast.LENGTH_SHORT).show()
              }
            }
          )
        }
        AppTab.TRACKER -> {
          StreakDashboardScreen(
            profile = profile,
            weeklyActivity = weeklyActivity,
            dailyGoals = dailyGoals,
            todayStudyMinutes = todayStudyMinutes,
            dailyTargetHours = dailyTargetHours,
            longestStreakDays = longestStreakDays,
            energyLevel = energyLevel,
            examMilestone = examMilestone,
            subjectMasteryList = subjectMasteryList,
            studyTimerState = studyTimerState,
            onToggleGoal = { viewModel.toggleGoalCompleted(it) },
            onAddCustomGoal = { title, cat, qty -> viewModel.addCustomGoal(title, cat, qty) },
            onRemoveCustomGoal = { viewModel.removeCustomGoal(it) },
            onSetDailyTargetHours = { viewModel.setDailyTargetHours(it) },
            onSetEnergyLevel = { viewModel.setEnergyLevel(it) },
            onUpdateMilestoneDays = { viewModel.updateMilestoneDays(it) },
            onStartStudySession = { subj, mins -> viewModel.startStudySession(subj, mins) },
            onPauseResumeStudySession = { viewModel.pauseResumeStudySession() },
            onFinishStudySession = {
              viewModel.finishStudySession()
              Toast.makeText(context, "Study session logged! Great focus!", Toast.LENGTH_SHORT).show()
            },
            onCancelStudySession = { viewModel.cancelStudySession() },
            onNavigateToSubjectPractice = { subj ->
              viewModel.setSubjectFilter(subj)
              selectedTab = AppTab.PYQ
            },
            onNavigateToMeditation = {
              selectedTab = AppTab.MEDITATION
            }
          )
        }
        AppTab.PYQ -> {
          PyqScreen(
            profile = profile,
            questions = filteredQuestions,
            bookmarkedIds = bookmarkedIds,
            selectedSubject = selectedSubject,
            selectedYear = selectedYear,
            onSelectSubject = { viewModel.setSubjectFilter(it) },
            onSelectYear = { viewModel.setYearFilter(it) },
            onToggleBookmark = { viewModel.toggleBookmark(it) },
            onToggleMalayalam = { viewModel.toggleMalayalam(it) },
            onStartOmrPaper = { paper ->
              viewModel.startOMRTest(paper)
              selectedTab = AppTab.OMR
            },
            onDownloadPaperPdf = { paper ->
              val file = viewModel.downloadPaperPdf(context, paper)
              if (file != null) {
                Toast.makeText(context, "Question Paper PDF generated!", Toast.LENGTH_SHORT).show()
              }
            }
          )
        }
        AppTab.OMR -> {
          OmrScreen(
            profile = profile,
            omrState = omrState,
            onStartPaper = { paper -> viewModel.startOMRTest(paper) },
            onSelectOption = { qIdx, optIdx -> viewModel.selectOMROption(qIdx, optIdx) },
            onSetCurrentQuestion = { qIdx -> viewModel.setOMRCurrentQuestion(qIdx) },
            onSubmitTest = { viewModel.submitOMRTest() },
            onResetTest = { viewModel.resetOMRTest() },
            onDownloadScorecard = { res ->
              val file = viewModel.downloadScorecardPdf(context, res)
              if (file != null) {
                Toast.makeText(context, "Official Scorecard PDF downloaded!", Toast.LENGTH_SHORT).show()
              }
            },
            onDownloadPaperPdf = { paper ->
              val file = viewModel.downloadPaperPdf(context, paper)
              if (file != null) {
                Toast.makeText(context, "Question Paper PDF generated!", Toast.LENGTH_SHORT).show()
              }
            }
          )
        }
        AppTab.MEDITATION -> {
          MeditationScreen(
            profile = profile,
            meditationState = meditationState,
            onStartActivity = { act -> viewModel.startMeditation(act) },
            onPauseResume = { viewModel.pauseResumeMeditation() },
            onStopMeditation = { viewModel.stopMeditation() }
          )
        }
        AppTab.PROFILE -> {
          ProfileScreen(
            profile = profile,
            recentResults = recentResults,
            onUpdateExamPreference = { viewModel.updateExamPreference(it) },
            onUpdateEducation = { viewModel.updateEducationLevel(it) },
            onToggleMalayalam = { viewModel.toggleMalayalam(it) },
            onDownloadScorecard = { res ->
              viewModel.downloadScorecardPdf(context, res)
            },
            onEditProfileClick = {
              isEditingProfileManually = true
            }
          )
        }
      }
    }
  }
}
