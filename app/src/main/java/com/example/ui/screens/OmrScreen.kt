package com.example.ui.screens

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PyqDatabase
import com.example.data.model.OMRResult
import com.example.data.model.QuestionPaper
import com.example.data.model.UserProfile
import com.example.ui.ActiveOMRState
import java.util.Locale

@Composable
fun OmrScreen(
  profile: UserProfile,
  omrState: ActiveOMRState?,
  onStartPaper: (QuestionPaper) -> Unit,
  onSelectOption: (questionIndex: Int, optionIndex: Int) -> Unit,
  onSetCurrentQuestion: (Int) -> Unit,
  onSubmitTest: () -> Unit,
  onResetTest: () -> Unit,
  onDownloadScorecard: (OMRResult) -> Unit,
  onDownloadPaperPdf: (QuestionPaper) -> Unit,
) {
  val context = LocalContext.current
  val isMl = profile.isMalayalam
  var showConfirmSubmitDialog by remember { mutableStateOf(false) }

  if (omrState == null) {
    // --- 1. TEST SELECTION & INSTRUCTIONS SCREEN ---
    val availablePapers = remember(profile.examPreference) {
      PyqDatabase.questionPapers.filter { it.examPreference == profile.examPreference }
        .ifEmpty { PyqDatabase.questionPapers }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(top = 20.dp, bottom = 96.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Column {
          Text(
            text = if (isMl) "OMR പരീക്ഷാ സിമുലേറ്റർ" else "OMR Exam Simulator",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = if (isMl) "യഥാർത്ഥ പരീക്ഷാ ഹാളിലെ അനുഭവം: ടൈമർ, ബബ്ളിംഗ് ഷീറ്റ്, നെഗറ്റീവ് മാർക്കിംഗ്"
            else "Experience authentic PSC/UPSC exam conditions with real bubble sheet & negative marking",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Rules Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
          )
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (isMl) "പരീക്ഷാ നിബന്ധനകൾ (Exam Rules):" else "Exam Rules & Instructions:",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "• Correct Answer: +1.0 Mark\n• Incorrect Answer: -0.33 Negative Mark (PSC) / -0.66 (UPSC)\n• Unattempted: 0 Marks\n• Realistic ink bubble sheet with haptic feedback\n• Instant verified performance scorecard & PDF export",
              fontSize = 12.sp,
              lineHeight = 18.sp,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }
      }

      item {
        Text(
          text = if (isMl) "ലഭ്യമായ മാതൃകാ ചോദ്യപേപ്പറുകൾ" else "Select Question Paper",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }

      items(availablePapers) { paper ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(MaterialTheme.colorScheme.primary)
                  .padding(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Text(
                  text = paper.examPreference.badge,
                  color = MaterialTheme.colorScheme.onPrimary,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Text(
                text = "${paper.durationMinutes} Mins • ${paper.totalMarks.toInt()} Marks",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = if (isMl) paper.titleMl else paper.titleEn,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = "${paper.questions.size} Questions • Negative Marking: -${paper.negativeMarkingPerWrong}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = { onDownloadPaperPdf(paper) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("PDF", fontSize = 12.sp)
              }

              Button(
                onClick = { onStartPaper(paper) },
                modifier = Modifier
                  .weight(1.5f)
                  .testTag("start_omr_${paper.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
              ) {
                Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isMl) "തുടങ്ങുക" else "Start OMR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }
          }
        }
      }
    }
  } else if (!omrState.isCompleted) {
    // --- 2. ACTIVE OMR EXAM SIMULATOR ---
    val paper = omrState.paper
    val currentQ = paper.questions.getOrElse(omrState.currentQuestionIndex) { paper.questions.first() }
    val remainingMins = omrState.remainingSeconds / 60
    val remainingSecs = omrState.remainingSeconds % 60
    val timeFormatted = String.format(Locale.US, "%02d:%02d", remainingMins, remainingSecs)
    val isLowTime = omrState.remainingSeconds < 300

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .testTag("active_omr_test")
    ) {
      // Top Status Bar: Title, Live Timer, Attempted Count & Submit button
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Timer
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (isLowTime) Color(0xFFEF4444).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer)
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Timer,
              contentDescription = "Timer",
              tint = if (isLowTime) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = timeFormatted,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = if (isLowTime) Color(0xFFEF4444) else MaterialTheme.colorScheme.onPrimaryContainer
            )
          }

          Text(
            text = "Attempted: ${omrState.userAnswers.size}/${paper.questions.size}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Button(
            onClick = { showConfirmSubmitDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.testTag("submit_omr_button")
          ) {
            Text(if (isMl) "സമർപ്പിക്കുക" else "Submit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp)
      ) {
        // Current Question Card
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Question ${omrState.currentQuestionIndex + 1} of ${paper.questions.size}",
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.titleMedium,
                  color = MaterialTheme.colorScheme.primary
                )

                Text(
                  text = if (isMl) currentQ.subject.titleMl else currentQ.subject.titleEn,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = if (isMl && currentQ.questionMl.isNotBlank()) currentQ.questionMl else currentQ.questionEn,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
              )

              Spacer(modifier = Modifier.height(14.dp))

              // Question Options
              val options = if (isMl && currentQ.optionsMl.isNotEmpty()) currentQ.optionsMl else currentQ.optionsEn
              val labels = listOf("A", "B", "C", "D")
              val chosenOption = omrState.userAnswers[omrState.currentQuestionIndex]

              options.forEachIndexed { optIndex, optText ->
                val isSelected = chosenOption == optIndex
                Surface(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelectOption(omrState.currentQuestionIndex, optIndex) }
                    .border(
                      1.5.dp,
                      if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                      RoundedCornerShape(10.dp)
                    ),
                  color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                ) {
                  Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Box(
                      modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = labels[optIndex],
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
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

              Spacer(modifier = Modifier.height(12.dp))

              // Navigation Buttons (Prev / Next)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                OutlinedButton(
                  onClick = {
                    if (omrState.currentQuestionIndex > 0) {
                      onSetCurrentQuestion(omrState.currentQuestionIndex - 1)
                    }
                  },
                  enabled = omrState.currentQuestionIndex > 0,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Icon(Icons.Default.NavigateBefore, contentDescription = null)
                  Text("Prev")
                }

                OutlinedButton(
                  onClick = {
                    if (omrState.currentQuestionIndex < paper.questions.size - 1) {
                      onSetCurrentQuestion(omrState.currentQuestionIndex + 1)
                    }
                  },
                  enabled = omrState.currentQuestionIndex < paper.questions.size - 1,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Next")
                  Icon(Icons.Default.NavigateNext, contentDescription = null)
                }
              }
            }
          }
        }

        // Section header for OMR Sheet
        item {
          Spacer(modifier = Modifier.height(18.dp))
          Text(
            text = if (isMl) "ഔദ്യോഗിക OMR ഷീറ്റ് (ടാപ്പ് ചെയ്ത് ബബ്ൾ ചെയ്യുക)" else "Official OMR Sheet (Tap to Bubble)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = if (isMl) "നീല/കറുപ്പ് മഷി ബബ്ളിംഗ് സിമുലേഷൻ • വൈബ്രേഷൻ ഫീഡ്ബാക്ക്" else "Realistic pen ink bubbling with haptic vibration feedback",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(8.dp))
        }

        // Complete OMR Grid Row Items
        itemsIndexed(paper.questions) { qIndex, _ ->
          val selectedOpt = omrState.userAnswers[qIndex]
          val isCurrentActive = omrState.currentQuestionIndex == qIndex

          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(8.dp))
              .clickable { onSetCurrentQuestion(qIndex) }
              .border(
                if (isCurrentActive) 1.5.dp else 1.dp,
                if (isCurrentActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
              ),
            color = if (isCurrentActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Q${qIndex + 1}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isCurrentActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(36.dp)
              )

              // 4 OMR Bubbles (A, B, C, D)
              Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                listOf("A", "B", "C", "D").forEachIndexed { optIndex, label ->
                  val isBubbled = selectedOpt == optIndex

                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(CircleShape)
                      .background(
                        if (isBubbled) Color(0xFF1E293B) // Dark pen ink filled
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                      )
                      .border(
                        1.5.dp,
                        if (isBubbled) Color(0xFF0F172A) else MaterialTheme.colorScheme.outline,
                        CircleShape
                      )
                      .clickable { onSelectOption(qIndex, optIndex) }
                      .testTag("omr_bubble_${qIndex}_$optIndex"),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = label,
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                      color = if (isBubbled) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    if (showConfirmSubmitDialog) {
      AlertDialog(
        onDismissRequest = { showConfirmSubmitDialog = false },
        title = { Text(if (isMl) "പരീക്ഷ പൂർത്തിയാക്കണോ?" else "Submit OMR Exam?") },
        text = {
          Text(
            text = "You have attempted ${omrState.userAnswers.size} of ${paper.questions.size} questions.\n\nNegative marking (-${paper.negativeMarkingPerWrong}) will be applied to incorrect answers. Do you want to submit now?"
          )
        },
        confirmButton = {
          Button(
            onClick = {
              showConfirmSubmitDialog = false
              onSubmitTest()
            }
          ) {
            Text("Yes, Submit")
          }
        },
        dismissButton = {
          TextButton(onClick = { showConfirmSubmitDialog = false }) {
            Text("Keep Reviewing")
          }
        }
      )
    }
  } else {
    // --- 3. TEST COMPLETED: SCORECARD & PERFORMANCE ANALYSIS ---
    val res = omrState.result ?: return
    val paper = omrState.paper

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(top = 20.dp, bottom = 96.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(38.dp)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = if (isMl) "പരീക്ഷാ ഫലം (Assessment Result)" else "Assessment Scorecard",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = paper.titleEn,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
        }
      }

      // Grand Score Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = if (isMl) "ലഭിച്ച ആകെ മാർക്ക് (Net Score)" else "TOTAL NET SCORE",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "${String.format(Locale.US, "%.2f", res.score)} / ${paper.totalMarks.toInt()}",
              style = MaterialTheme.typography.headlineLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Accuracy: ${String.format(Locale.US, "%.1f", res.accuracyPercent)}%  •  Time: ${res.timeTakenSeconds / 60}m ${res.timeTakenSeconds % 60}s",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Metrics Grid (Attempted, Correct, Wrong, Omitted)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              MetricChip(
                label = "Attempted",
                value = "${res.attempted}",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
              )
              MetricChip(
                label = "Correct (+1)",
                value = "${res.correct}",
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
              )
              MetricChip(
                label = "Wrong (-0.33)",
                value = "${res.wrong}",
                color = Color(0xFFEF4444),
                modifier = Modifier.weight(1f)
              )
              MetricChip(
                label = "Omitted (0)",
                value = "${res.omitted}",
                color = Color(0xFF64748B),
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // Download Scorecard PDF Button
      item {
        Button(
          onClick = { onDownloadScorecard(res) },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("download_scorecard_pdf"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Icon(Icons.Default.Download, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isMl) "റിപ്പോർട്ട് കാർഡ് PDF ഡൗൺലോഡ്" else "Download Scorecard PDF",
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Detailed Question Review
      item {
        Text(
          text = if (isMl) "ചോദ്യോത്തര അവലോകനം (Question Review)" else "Question-by-Question Review",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }

      itemsIndexed(paper.questions) { index, q ->
        val userAns = res.userAnswers[index]
        val isCorrect = userAns == q.correctIndex
        val isOmitted = userAns == null
        val labels = listOf("A", "B", "C", "D")

        Card(
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
              Text(
                text = "Q${index + 1}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(
                    if (isCorrect) Color(0xFF10B981).copy(alpha = 0.15f)
                    else if (isOmitted) Color(0xFF64748B).copy(alpha = 0.15f)
                    else Color(0xFFEF4444).copy(alpha = 0.15f)
                  )
                  .padding(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Text(
                  text = if (isCorrect) "+1.0 Correct" else if (isOmitted) "0 Omitted" else "-0.33 Wrong",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isCorrect) Color(0xFF10B981) else if (isOmitted) Color(0xFF64748B) else Color(0xFFEF4444)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = if (isMl && q.questionMl.isNotBlank()) q.questionMl else q.questionEn,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Your Answer: ${if (userAns != null) labels[userAns] else "Not Attempted"}  |  Correct Answer: ${labels[q.correctIndex]}",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = if (isMl && q.explanationMl.isNotBlank()) q.explanationMl else q.explanationEn,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      item {
        OutlinedButton(
          onClick = onResetTest,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Refresh, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text(if (isMl) "മറ്റൊരു പരീക്ഷ തിരഞ്ഞെടുക്കുക" else "Take Another Exam")
        }
      }
    }
  }
}

@Composable
private fun MetricChip(
  label: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(10.dp),
    color = color.copy(alpha = 0.1f)
  ) {
    Column(
      modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = value,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = color
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = label,
        fontSize = 10.sp,
        color = color.copy(alpha = 0.9f),
        textAlign = TextAlign.Center
      )
    }
  }
}
