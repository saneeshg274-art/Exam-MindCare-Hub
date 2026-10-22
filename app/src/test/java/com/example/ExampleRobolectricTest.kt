package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PyqDatabase
import com.example.data.UserPreferencesRepository
import com.example.data.model.EducationLevel
import com.example.data.model.ExamPreference
import com.example.data.model.SubjectType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Exam&MindCare Hub", appName)
  }

  @Test
  fun `verify pyq database contents and Malayalam translations`() {
    val questions = PyqDatabase.allQuestions
    assertTrue("Questions list should not be empty", questions.isNotEmpty())

    val kpscQuestions = questions.filter { it.examPreference == ExamPreference.KERALA_PSC }
    assertTrue("KPSC questions should be available", kpscQuestions.isNotEmpty())

    val upscQuestions = questions.filter { it.examPreference == ExamPreference.UPSC }
    assertTrue("UPSC questions should be available", upscQuestions.isNotEmpty())

    kpscQuestions.forEach { q ->
      assertTrue("Question ID ${q.id} must have Malayalam text", q.questionMl.isNotBlank())
      assertEquals("Question ID ${q.id} must have 4 options", 4, q.optionsEn.size)
      assertTrue("Correct index must be within 0..3", q.correctIndex in 0..3)
    }
  }

  @Test
  fun `verify question papers and meditation activities`() {
    val papers = PyqDatabase.questionPapers
    assertTrue("Should have official question papers", papers.isNotEmpty())

    val meditationList = PyqDatabase.meditationActivities
    assertEquals("Should have 4 mind wellness activities", 4, meditationList.size)
  }

  @Test
  fun `verify streak tracker and daily study goals`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = UserPreferencesRepository(context)

    // Verify daily goals exist
    val goals = repository.dailyGoals.value
    assertTrue("Daily goals should not be empty", goals.isNotEmpty())

    // Toggle a goal
    val firstGoal = goals.first()
    val initialCompleted = firstGoal.isCompleted
    repository.toggleGoalCompleted(firstGoal.id)
    val updatedGoal = repository.dailyGoals.value.first { it.id == firstGoal.id }
    assertEquals("Goal completion should invert", !initialCompleted, updatedGoal.isCompleted)

    // Add custom goal
    val initialGoalCount = repository.dailyGoals.value.size
    repository.addCustomGoal("Read Constitution Art 21", "Revision", "30 mins")
    assertEquals(initialGoalCount + 1, repository.dailyGoals.value.size)

    // Log study minutes
    val initialMins = repository.todayStudyMinutes.value
    repository.logStudySession(SubjectType.CONSTITUTION, 25)
    assertEquals(initialMins + 25, repository.todayStudyMinutes.value)

    // Verify 7-day weekly activity status
    val weekly = repository.getWeeklyStatus()
    assertEquals(7, weekly.size)
  }

  @Test
  fun `verify new user starts with blank streak and records`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = UserPreferencesRepository(context)
    repository.resetAllProgressForNewUser()

    val profile = repository.userProfile.value
    assertEquals("New user streak must be 0", 0, profile.streakDays)
    assertEquals("New user zen minutes must be 0", 0, profile.zenMinutesTotal)
    assertEquals("New user tests completed count must be 0", 0, profile.testsCompletedCount)
    assertEquals("Today study minutes must be 0", 0, repository.todayStudyMinutes.value)
    assertEquals("Longest streak days must be 0", 0, repository.longestStreakDays.value)
    assertTrue("Recent test results must be empty", repository.recentResults.value.isEmpty())

    // All initial goals should be uncompleted
    val goals = repository.dailyGoals.value
    assertTrue("Initial daily goals must not be empty", goals.isNotEmpty())
    assertTrue("All initial goals must be uncompleted", goals.none { it.isCompleted })

    // Weekly heatmap days must all start uncompleted
    val weekly = repository.getWeeklyStatus()
    assertEquals(7, weekly.size)
    assertTrue("Weekly days must start uncompleted", weekly.none { it.isCompleted })
  }
}
