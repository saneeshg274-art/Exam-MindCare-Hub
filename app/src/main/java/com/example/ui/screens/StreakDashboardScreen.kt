package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActiveStudySessionState
import com.example.data.model.DailyGoal
import com.example.data.model.ExamMilestone
import com.example.data.model.StreakDayStatus
import com.example.data.model.StudentEnergyLevel
import com.example.data.model.SubjectMastery
import com.example.data.model.SubjectType
import com.example.data.model.UserProfile
import java.util.Locale

@Composable
fun StreakDashboardScreen(
  profile: UserProfile,
  weeklyActivity: List<StreakDayStatus>,
  dailyGoals: List<DailyGoal>,
  todayStudyMinutes: Int,
  dailyTargetHours: Float,
  longestStreakDays: Int,
  energyLevel: StudentEnergyLevel,
  examMilestone: ExamMilestone,
  subjectMasteryList: List<SubjectMastery>,
  studyTimerState: ActiveStudySessionState,
  onToggleGoal: (String) -> Unit,
  onAddCustomGoal: (title: String, category: String, quantity: String) -> Unit,
  onRemoveCustomGoal: (String) -> Unit,
  onSetDailyTargetHours: (Float) -> Unit,
  onSetEnergyLevel: (StudentEnergyLevel) -> Unit,
  onUpdateMilestoneDays: (Int) -> Unit,
  onStartStudySession: (SubjectType, Int) -> Unit,
  onPauseResumeStudySession: () -> Unit,
  onFinishStudySession: () -> Unit,
  onCancelStudySession: () -> Unit,
  onNavigateToSubjectPractice: (SubjectType) -> Unit,
  onNavigateToMeditation: () -> Unit,
) {
  val isMl = profile.isMalayalam
  var showAddGoalDialog by remember { mutableStateOf(false) }
  var showEditMilestoneDialog by remember { mutableStateOf(false) }
  var showStartSessionDialog by remember { mutableStateOf(false) }

  val targetMinutes = (dailyTargetHours * 60).toInt().coerceAtLeast(60)
  val studyProgress = (todayStudyMinutes.toFloat() / targetMinutes.toFloat()).coerceIn(0f, 1f)
  val animatedStudyProgress by animateFloatAsState(targetValue = studyProgress, label = "studyProgress")

  val completedGoalsCount = dailyGoals.count { it.isCompleted }
  val goalsProgress = if (dailyGoals.isNotEmpty()) completedGoalsCount.toFloat() / dailyGoals.size.toFloat() else 0f

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 20.dp)
      .testTag("streak_dashboard_screen"),
    contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Title & Motivation
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (isMl) "പഠന സ്ട്രീക്കും പുരോഗതിയും" else "Study Streak & Goals",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = if (isMl) "സ്ഥിരത വിജയത്തിലേക്കുള്ള കുറുക്കുവഴിയാണ്" else "Daily consistency turns aspirants into rank holders",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Exam Milestone Pill
        Surface(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { showEditMilestoneDialog = true }
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              Icons.Default.HourglassBottom,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${examMilestone.daysRemaining}d Left",
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }
      }
    }

    // 1. Giant Glowing Streak Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(
                    Brush.radialGradient(
                      listOf(
                        Color(0xFFFFA000),
                        Color(0xFFEA580C)
                      )
                    )
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.LocalFireDepartment,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(34.dp)
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column {
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(
                    text = "${profile.streakDays}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEA580C)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (isMl) "ദിവസത്തെ സ്ട്രീക്ക്" else if (profile.streakDays == 0) "Day Streak 🌱" else "Day Streak 🔥",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                  )
                }
                Text(
                  text = if (profile.streakDays == 0 && longestStreakDays == 0) {
                    if (isMl) "ഇന്ന് പഠനം ആരംഭിക്കൂ • ഡേ 1 തുടക്കം" else "Start Day 1 today • Ready to build!"
                  } else {
                    "Best Streak: $longestStreakDays Days  •  Active Aspirant"
                  },
                  style = MaterialTheme.typography.bodySmall,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // 7-Day Rolling Heatmap
          Text(
            text = if (isMl) "ഈ ആഴ്ചയിലെ സ്ഥിരത (Weekly Activity):" else "Weekly Consistency Heatmap:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            weeklyActivity.forEach { day ->
              val flameColor = when {
                day.isCompleted -> Color(0xFFEA580C)
                day.isToday -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
              }

              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(
                    if (day.isToday) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    else Color.Transparent
                  )
                  .padding(vertical = 6.dp, horizontal = 4.dp)
              ) {
                Text(
                  text = day.dayLabel,
                  fontSize = 11.sp,
                  fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
                  color = if (day.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                      if (day.isCompleted) Color(0xFFEA580C).copy(alpha = 0.15f)
                      else if (day.isToday) MaterialTheme.colorScheme.surfaceVariant
                      else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                    .border(
                      1.5.dp,
                      if (day.isCompleted) Color(0xFFEA580C) else if (day.isToday) MaterialTheme.colorScheme.primary else Color.Transparent,
                      CircleShape
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  if (day.isCompleted) {
                    Icon(
                      imageVector = Icons.Default.LocalFireDepartment,
                      contentDescription = null,
                      tint = Color(0xFFEA580C),
                      modifier = Modifier.size(18.dp)
                    )
                  } else {
                    Text(
                      text = "${day.dateNumber}",
                      fontSize = 11.sp,
                      color = if (day.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = if (day.studyMinutes > 0) "${day.studyMinutes}m" else "-",
                  fontSize = 9.sp,
                  color = if (day.isCompleted) Color(0xFFEA580C) else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // 2. Daily Study Target Progress & Live Stopwatch
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = if (isMl) "ഇന്നത്തെ പഠന ലക്ഷ്യം" else "Daily Study Goal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              val hoursLogged = todayStudyMinutes / 60
              val minsLogged = todayStudyMinutes % 60
              Text(
                text = "$hoursLogged h $minsLogged m / ${String.format(Locale.US, "%.1f", dailyTargetHours)} hrs (${(studyProgress * 100).toInt()}% Met)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
              )
            }

            // Target selector dropdown chip
            var targetDropdownExpanded by remember { mutableStateOf(false) }
            Box {
              Surface(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { targetDropdownExpanded = true }
                  .background(MaterialTheme.colorScheme.surfaceVariant)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "${String.format(Locale.US, "%.1f", dailyTargetHours)}h Target ▼",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              DropdownMenu(
                expanded = targetDropdownExpanded,
                onDismissRequest = { targetDropdownExpanded = false }
              ) {
                listOf(2.0f, 3.0f, 3.5f, 4.5f, 6.0f, 8.0f).forEach { hours ->
                  DropdownMenuItem(
                    text = { Text("$hours Hours / Day") },
                    onClick = {
                      onSetDailyTargetHours(hours)
                      targetDropdownExpanded = false
                    }
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          LinearProgressIndicator(
            progress = { animatedStudyProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Live Study Timer Box / Button
          if (studyTimerState.isRunning) {
            val elapsedSec = studyTimerState.elapsedSeconds
            val timerMins = elapsedSec / 60
            val timerSecs = elapsedSec % 60
            val timerFormatted = String.format(Locale.US, "%02d:%02d", timerMins, timerSecs)

            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .padding(14.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "Focused Study: ${if (isMl) studyTimerState.selectedSubject.titleMl else studyTimerState.selectedSubject.titleEn}",
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                      color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                  }
                  Text(
                    text = timerFormatted,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  IconButton(
                    onClick = onPauseResumeStudySession,
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(MaterialTheme.colorScheme.surface)
                  ) {
                    Icon(
                      imageVector = if (studyTimerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                      contentDescription = "Pause",
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(20.dp)
                    )
                  }

                  Button(
                    onClick = onFinishStudySession,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                  ) {
                    Text(if (isMl) "പൂർത്തിയാക്കി" else "Log Session", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          } else {
            Button(
              onClick = { showStartSessionDialog = true },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isMl) "പഠന സെഷൻ ആരംഭിക്കുക (Start Focus Session)" else "Start Focused Study Session",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          }
        }
      }
    }

    // 3. Aspirant Energy & Mindset Check-in
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = if (isMl) "ഇന്നത്തെ മാനസികാവസ്ഥ (Focus & Energy Check-in):" else "Daily Energy & Focus Check-in:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            StudentEnergyLevel.entries.forEach { level ->
              val isSelected = energyLevel == level
              Surface(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .clickable { onSetEnergyLevel(level) }
                  .border(
                    1.dp,
                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    RoundedCornerShape(10.dp)
                  )
                  .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                  )
                  .padding(vertical = 8.dp),
                color = Color.Transparent
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(text = level.emoji, fontSize = 18.sp)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = if (isMl) level.labelMl else level.labelEn,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }
    }

    // 4. Daily Goals Checklist
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (isMl) "ഇന്നത്തെ ലക്ഷ്യങ്ങൾ" else "Daily Goal Targets",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.width(8.dp))
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer
          ) {
            Text(
              text = "$completedGoalsCount/${dailyGoals.size}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
          }
        }

        IconButton(
          onClick = { showAddGoalDialog = true },
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
          Icon(
            Icons.Default.Add,
            contentDescription = "Add Goal",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }

    items(dailyGoals) { goal ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onToggleGoal(goal.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (goal.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (goal.isCompleted) 0.dp else 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Checkbox(
              checked = goal.isCompleted,
              onCheckedChange = { onToggleGoal(goal.id) },
              colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                checkmarkColor = Color.White
              ),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (isMl) goal.titleMl else goal.titleEn,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (goal.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                color = if (goal.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else TextDecoration.None
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = goal.category,
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.Medium
                )
                if (goal.targetQuantity.isNotBlank()) {
                  Text(
                    text = " • ${goal.targetQuantity}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }

          if (goal.isCustom) {
            IconButton(
              onClick = { onRemoveCustomGoal(goal.id) },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(
                Icons.Default.Close,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }

    // 5. Subject Mastery & Syllabus Coverage
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = if (isMl) "വിഷയാടിസ്ഥാനത്തിലുള്ള പുരോഗതി (Mastery)" else "Syllabus Mastery & Progress",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Track completion rates and logged study hours across your tailored exam subjects",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    items(subjectMasteryList) { mastery ->
      val progressAnim by animateFloatAsState(
        targetValue = mastery.masteryPercentage / 100f,
        label = "masteryAnim"
      )

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToSubjectPractice(mastery.subject) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = mastery.subject.iconEmoji, fontSize = 18.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = if (isMl) mastery.subject.titleMl else mastery.subject.titleEn,
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "${mastery.studyMinutesLogged / 60}h ${mastery.studyMinutesLogged % 60}m studied • ${mastery.correctCount}/${mastery.totalQuestions} Questions Mastered",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Text(
              text = "${mastery.masteryPercentage.toInt()}%",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          LinearProgressIndicator(
            progress = { progressAnim },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )
        }
      }
    }
  }

  // Dialog: Add Custom Goal
  if (showAddGoalDialog) {
    var goalTitle by remember { mutableStateOf("") }
    var goalCategory by remember { mutableStateOf("Revision") }
    var goalQuantity by remember { mutableStateOf("30 Mins") }

    AlertDialog(
      onDismissRequest = { showAddGoalDialog = false },
      title = { Text(if (isMl) "പുതിയ പഠന ലക്ഷ്യം ചേർക്കുക" else "Add Custom Study Target") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = goalTitle,
            onValueChange = { goalTitle = it },
            label = { Text("Goal Title / ലക്ഷ്യം") },
            placeholder = { Text("e.g. Read Current Affairs capsule") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = goalCategory,
            onValueChange = { goalCategory = it },
            label = { Text("Category (e.g. Revision, PYQ, Maths)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = goalQuantity,
            onValueChange = { goalQuantity = it },
            label = { Text("Target Quantity (e.g. 25 Qs, 45 Mins)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (goalTitle.isNotBlank()) {
              onAddCustomGoal(goalTitle.trim(), goalCategory.trim(), goalQuantity.trim())
              showAddGoalDialog = false
            }
          }
        ) {
          Text("Add Goal")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddGoalDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // Dialog: Edit Milestone Days
  if (showEditMilestoneDialog) {
    var daysInput by remember { mutableStateOf("${examMilestone.daysRemaining}") }

    AlertDialog(
      onDismissRequest = { showEditMilestoneDialog = false },
      title = { Text(if (isMl) "പരീക്ഷാ തിയതി ക്രമീകരിക്കുക" else "Adjust Exam Countdown Days") },
      text = {
        Column {
          Text(text = "Target Exam: ${if (isMl) examMilestone.titleMl else examMilestone.titleEn}", fontSize = 12.sp)
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = daysInput,
            onValueChange = { daysInput = it.filter { ch -> ch.isDigit() } },
            label = { Text("Days Remaining / അവശേഷിക്കുന്ന ദിവസങ്ങൾ") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val d = daysInput.toIntOrNull() ?: examMilestone.daysRemaining
            onUpdateMilestoneDays(d)
            showEditMilestoneDialog = false
          }
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditMilestoneDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // Dialog: Start Study Session
  if (showStartSessionDialog) {
    var chosenSubject by remember { mutableStateOf(SubjectType.CONSTITUTION) }
    var chosenDuration by remember { mutableStateOf(45) }

    AlertDialog(
      onDismissRequest = { showStartSessionDialog = false },
      title = { Text(if (isMl) "പഠന സെഷൻ ക്രമീകരിക്കുക" else "Configure Study Session") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(text = "Select Focus Subject:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

          var subjectDropdownExpanded by remember { mutableStateOf(false) }
          Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
              onClick = { subjectDropdownExpanded = true },
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("${chosenSubject.iconEmoji} ${if (isMl) chosenSubject.titleMl else chosenSubject.titleEn}")
            }
            DropdownMenu(
              expanded = subjectDropdownExpanded,
              onDismissRequest = { subjectDropdownExpanded = false }
            ) {
              SubjectType.entries.forEach { subj ->
                DropdownMenuItem(
                  text = { Text("${subj.iconEmoji} ${if (isMl) subj.titleMl else subj.titleEn}") },
                  onClick = {
                    chosenSubject = subj
                    subjectDropdownExpanded = false
                  }
                )
              }
            }
          }

          Text(text = "Select Duration:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf(25, 45, 60, 90).forEach { mins ->
              val isSel = chosenDuration == mins
              FilterChip(
                selected = isSel,
                onClick = { chosenDuration = mins },
                label = { Text("${mins}m") },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primary,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onStartStudySession(chosenSubject, chosenDuration)
            showStartSessionDialog = false
          }
        ) {
          Text("Start Timer")
        }
      },
      dismissButton = {
        TextButton(onClick = { showStartSessionDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
