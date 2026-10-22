package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EducationLevel
import com.example.data.model.ExamPreference
import com.example.data.model.OMRResult
import com.example.data.model.UserProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
  profile: UserProfile,
  recentResults: List<OMRResult>,
  onUpdateExamPreference: (ExamPreference) -> Unit,
  onUpdateEducation: (EducationLevel) -> Unit,
  onToggleMalayalam: (Boolean) -> Unit,
  onDownloadScorecard: (OMRResult) -> Unit,
  onEditProfileClick: () -> Unit,
) {
  val isMl = profile.isMalayalam

  var examDropdownExpanded by remember { mutableStateOf(false) }
  var educationDropdownExpanded by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 20.dp)
      .testTag("profile_screen"),
    contentPadding = PaddingValues(top = 20.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Profile Header Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(70.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AccountCircle,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(52.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = profile.name.ifBlank { "Aspirant" },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Phone,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "+91 ${profile.phoneNumber.ifBlank { "Not provided" }}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = profile.examPreference.badge,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }
            Box(
              modifier = Modifier
                .weight(1.5f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = profile.educationLevel.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    // Performance Metrics Row (Streak, Zen Mins, Tests Done)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        PerformanceCard(
          icon = Icons.Default.LocalFireDepartment,
          tint = Color(0xFFF97316),
          title = "${profile.streakDays} Days",
          subtitle = "Study Streak",
          modifier = Modifier.weight(1f)
        )
        PerformanceCard(
          icon = Icons.Default.SelfImprovement,
          tint = MaterialTheme.colorScheme.primary,
          title = "${profile.zenMinutesTotal} Mins",
          subtitle = "Zen Minutes",
          modifier = Modifier.weight(1f)
        )
        PerformanceCard(
          icon = Icons.Default.EmojiEvents,
          tint = Color(0xFFEAB308),
          title = "${profile.testsCompletedCount} Done",
          subtitle = "Tests Completed",
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Dynamic Variant Switcher Section
    item {
      Text(
        text = if (isMl) "പഠന മുൻഗണന മാറ്റുക (Content Variant)" else "Customize Preparation Preference",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Switching preference instantly transforms your question papers, syllabus, and language options.",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // 1. Switch Target Exam
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Target Higher Exam",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))

          Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { examDropdownExpanded = true }
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
              color = MaterialTheme.colorScheme.surface
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "${profile.examPreference.displayName} (${profile.examPreference.titleMl})",
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = profile.examPreference.targetTagline,
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
              }
            }

            DropdownMenu(
              expanded = examDropdownExpanded,
              onDismissRequest = { examDropdownExpanded = false }
            ) {
              ExamPreference.entries.forEach { exam ->
                DropdownMenuItem(
                  text = {
                    Column {
                      Text(
                        text = "${exam.displayName} (${exam.titleMl})",
                        fontWeight = if (profile.examPreference == exam) FontWeight.Bold else FontWeight.Normal,
                        color = if (profile.examPreference == exam) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                      )
                      Text(text = exam.targetTagline, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                  },
                  onClick = {
                    onUpdateExamPreference(exam)
                    examDropdownExpanded = false
                  },
                  leadingIcon = if (profile.examPreference == exam) {
                    { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                  } else null
                )
              }
            }
          }
        }
      }
    }

    // 2. Switch Education Qualification Level
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Educational Qualification",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))

          Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { educationDropdownExpanded = true }
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
              color = MaterialTheme.colorScheme.surface
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.School, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "${profile.educationLevel.label} (${profile.educationLevel.labelMl})",
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = profile.educationLevel.targetTiers,
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
              }
            }

            DropdownMenu(
              expanded = educationDropdownExpanded,
              onDismissRequest = { educationDropdownExpanded = false }
            ) {
              EducationLevel.entries.forEach { level ->
                DropdownMenuItem(
                  text = {
                    Column {
                      Text(
                        text = "${level.label}  •  ${level.labelMl}",
                        fontWeight = if (profile.educationLevel == level) FontWeight.Bold else FontWeight.Normal,
                        color = if (profile.educationLevel == level) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                      )
                      Text(text = level.targetTiers, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                  },
                  onClick = {
                    onUpdateEducation(level)
                    educationDropdownExpanded = false
                  },
                  leadingIcon = if (profile.educationLevel == level) {
                    { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                  } else null
                )
              }
            }
          }
        }
      }
    }

    // 3. Malayalam Language Toggle Switch
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Translate, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "മലയാളം പതിപ്പ് (Malayalam Version)",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Show Kerala PSC questions and explanations in Malayalam",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Switch(
            checked = isMl,
            onCheckedChange = onToggleMalayalam,
            colors = SwitchDefaults.colors(
              checkedThumbColor = MaterialTheme.colorScheme.primary,
              checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
          )
        }
      }
    }

    // Recent OMR Test Results History
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = if (isMl) "പരീക്ഷാ ചരിത്രം & സ്കോർകാർഡുകൾ" else "Recent Test History & Scorecards",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    if (recentResults.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Text(
            text = "No tests completed yet. Start an OMR simulation test to earn scorecards and export them as PDF!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
          )
        }
      }
    } else {
      items(recentResults) { res ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = res.paperTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(res.timestamp))
              Text(
                text = "$dateStr • Score: ${String.format(Locale.US, "%.2f", res.score)}/${res.totalMarks.toInt()} (${String.format(Locale.US, "%.0f", res.accuracyPercent)}% Acc)",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }

            IconButton(
              onClick = { onDownloadScorecard(res) },
              modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
              Icon(
                Icons.Default.Download,
                contentDescription = "Download Scorecard PDF",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      OutlinedButton(
        onClick = onEditProfileClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (isMl) "പ്രൊഫൈൽ വിവരങ്ങൾ തിരുത്തുക" else "Edit Personal Info / Log Out")
      }
    }
  }
}

@Composable
private fun PerformanceCard(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  tint: Color,
  title: String,
  subtitle: String,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
