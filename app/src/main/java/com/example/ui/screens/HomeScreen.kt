package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PyqDatabase
import com.example.data.model.ExamPreference
import com.example.data.model.Question
import com.example.data.model.QuestionPaper
import com.example.data.model.SubjectType
import com.example.data.model.UserProfile

@Composable
fun HomeScreen(
  profile: UserProfile,
  onNavigateToOmr: (paper: QuestionPaper?) -> Unit,
  onNavigateToMeditation: () -> Unit,
  onNavigateToPyq: (subject: SubjectType?) -> Unit,
  onNavigateToTracker: () -> Unit,
  onNavigateToProfile: () -> Unit,
  onToggleMalayalam: (Boolean) -> Unit,
  onDownloadPaper: (QuestionPaper) -> Unit,
) {
  val context = LocalContext.current
  val isMl = profile.isMalayalam

  // Tailored question of the day from the selected exam
  val dailyQuestion = remember(profile.examPreference) {
    PyqDatabase.allQuestions.firstOrNull { it.examPreference == profile.examPreference }
      ?: PyqDatabase.allQuestions.first()
  }

  var selectedOptionDaily by remember { mutableStateOf<Int?>(null) }
  var showDailyExplanation by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("home_screen"),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // Top Greeting & Language Switch Bar
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (isMl) "സ്വാഗതം, ${profile.name.ifBlank { "ഉദ്യോഗാർത്ഥി" }}" else "Welcome back, ${profile.name.ifBlank { "Aspirant" }}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "${profile.examPreference.badge} • ${profile.educationLevel.label}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Language Switch Chip
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable { onToggleMalayalam(!isMl) }
              .border(
                1.dp,
                if (isMl) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                RoundedCornerShape(20.dp)
              )
              .testTag("home_malayalam_toggle"),
            color = if (isMl) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = "Language",
                tint = if (isMl) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isMl) "മലയാളം" else "English",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (isMl) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          IconButton(
            onClick = onNavigateToProfile,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .testTag("home_profile_button")
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = "Profile",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // Hero Illustration Card with Dynamic Syllabus Variant
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.study_hero),
            contentDescription = "Study Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Gradient overlay
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Transparent,
                    Color(0xCC09121E),
                    Color(0xF009121E)
                  ),
                  startY = 40f
                )
              )
          )

          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(16.dp)
          ) {
            Text(
              text = if (isMl) profile.examPreference.titleMl else profile.examPreference.displayName,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = profile.examPreference.syllabusOverview,
              style = MaterialTheme.typography.bodySmall,
              color = Color.White.copy(alpha = 0.9f),
              maxLines = 2,
              fontSize = 11.sp
            )
          }
        }
      }
    }

    // Prep & Zen Streak Metrics
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Streak Card
        Card(
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToTracker() }
            .testTag("home_streak_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = null,
              tint = Color(0xFFF97316),
              modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = if (profile.streakDays == 0) "0 Days 🌱" else "${profile.streakDays} Days 🔥",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = if (isMl) "ലക്ഷ്യങ്ങളും സ്ട്രീക്കും" else "Goals & Streak",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = Color(0xFFF97316),
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }

        // Zen Minutes Card
        Card(
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToMeditation() },
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
          )
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.SelfImprovement,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "${profile.zenMinutesTotal} Mins",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = if (isMl) "മനഃശാന്തി സമയം" else "Mind Wellness",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
            }
          }
        }
      }
    }

    // Quick Action Cards
    item {
      Spacer(modifier = Modifier.height(20.dp))
      Text(
        text = if (isMl) "പ്രധാന പ്രവർത്തനങ്ങൾ" else "Quick Actions",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp),
        color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(10.dp))

      LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // 1. OMR Simulation Exam
        item {
          Card(
            modifier = Modifier
              .width(220.dp)
              .clickable {
                val targetPaper = PyqDatabase.questionPapers.firstOrNull { it.examPreference == profile.examPreference }
                  ?: PyqDatabase.questionPapers.first()
                onNavigateToOmr(targetPaper)
              }
              .testTag("action_omr_test"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  Icons.Default.Assignment,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = if (isMl) "OMR പരീക്ഷാ സിമുലേറ്റർ" else "OMR Exam Simulator",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isMl) "യഥാർത്ഥ ബബ്ളിംഗ്, ടൈമർ & -0.33 നെഗറ്റീവ് മാർക്കിംഗ്" else "Real bubbling, timer & -0.33 negative marking",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                maxLines = 2
              )
            }
          }
        }

        // 2. Free Mind Care & 4-7-8 Breathing
        item {
          Card(
            modifier = Modifier
              .width(220.dp)
              .clickable { onNavigateToMeditation() }
              .testTag("action_meditation"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  Icons.Default.SelfImprovement,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSecondary
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = if (isMl) "സൗജന്യ ധ്യാനം & റിലാക്സേഷൻ" else "Free Mind Relaxation",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
              Text(
                text = if (isMl) "പരീക്ഷാ ഉത്കണ്ഠ അകറ്റാൻ 4-7-8 ശ്വസന വ്യായാമം" else "4-7-8 breathing & 20-20-20 eye strain reset",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                fontSize = 11.sp,
                maxLines = 2
              )
            }
          }
        }

        // 3. Download Official PYQ PDF
        item {
          Card(
            modifier = Modifier
              .width(220.dp)
              .clickable {
                val paper = PyqDatabase.questionPapers.firstOrNull { it.examPreference == profile.examPreference }
                  ?: PyqDatabase.questionPapers.first()
                onDownloadPaper(paper)
              }
              .testTag("action_download_pdf"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  Icons.Default.Download,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.tertiary
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = if (isMl) "ചോദ്യപേപ്പർ PDF ഡൗൺലോഡ്" else "Download PYQ as PDF",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isMl) "പ്രിന്റ് ചെയ്യാവുന്ന ചോദ്യപേപ്പറും ഉത്തരസൂചികയും" else "Printable question papers with answer keys",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                maxLines = 2
              )
            }
          }
        }
      }
    }

    // High Yield PYQ Question of the Day
    item {
      Spacer(modifier = Modifier.height(24.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            Icons.Default.Lightbulb,
            contentDescription = null,
            tint = Color(0xFFF59E0B),
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isMl) "ഇന്നത്തെ PYQ ചോദ്യം" else "Question of the Day",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
        Text(
          text = dailyQuestion.paperTitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.primary,
          fontSize = 11.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Question text
          Text(
            text = if (isMl && dailyQuestion.questionMl.isNotBlank()) dailyQuestion.questionMl else dailyQuestion.questionEn,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(14.dp))

          val options = if (isMl && dailyQuestion.optionsMl.isNotEmpty()) dailyQuestion.optionsMl else dailyQuestion.optionsEn
          val optionLabels = listOf("A", "B", "C", "D")

          options.forEachIndexed { optIndex, optText ->
            val isSelected = selectedOptionDaily == optIndex
            val isCorrect = optIndex == dailyQuestion.correctIndex
            val showCorrectness = selectedOptionDaily != null

            val borderColor = when {
              showCorrectness && isCorrect -> Color(0xFF10B981)
              showCorrectness && isSelected -> Color(0xFFEF4444)
              isSelected -> MaterialTheme.colorScheme.primary
              else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            }

            val bgColor = when {
              showCorrectness && isCorrect -> Color(0xFF10B981).copy(alpha = 0.12f)
              showCorrectness && isSelected -> Color(0xFFEF4444).copy(alpha = 0.12f)
              isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
              else -> MaterialTheme.colorScheme.surface
            }

            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                  if (selectedOptionDaily == null) {
                    selectedOptionDaily = optIndex
                    showDailyExplanation = true
                  }
                }
                .border(1.dp, borderColor, RoundedCornerShape(10.dp)),
              color = bgColor
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                      if (showCorrectness && isCorrect) Color(0xFF10B981)
                      else if (showCorrectness && isSelected) Color(0xFFEF4444)
                      else MaterialTheme.colorScheme.surfaceVariant
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = optionLabels[optIndex],
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (showCorrectness && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = optText,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          AnimatedVisibility(visible = showDailyExplanation) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .padding(12.dp)
            ) {
              Text(
                text = if (isMl) "വിശദീകരണം (Explanation):" else "Explanation:",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (isMl && dailyQuestion.explanationMl.isNotBlank()) dailyQuestion.explanationMl else dailyQuestion.explanationEn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    // Subject Breakdown Tailored to Selected Exam
    item {
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        text = if (isMl) "വിഷയാടിസ്ഥാനത്തിലുള്ള ചോദ്യങ്ങൾ" else "Subject-wise PYQ Sets",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp),
        color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(10.dp))
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

    items(relevantSubjects) { subject ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 5.dp)
          .clickable { onNavigateToPyq(subject) }
          .testTag("subject_card_${subject.name}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = subject.iconEmoji,
              fontSize = 22.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (isMl) subject.titleMl else subject.titleEn,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isMl) subject.titleEn else subject.titleMl,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Icon(
            imageVector = Icons.Default.NavigateNext,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}
