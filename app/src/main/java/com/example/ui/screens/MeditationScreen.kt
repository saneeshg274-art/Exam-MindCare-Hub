package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PyqDatabase
import com.example.data.model.MeditationActivity
import com.example.data.model.MeditationExerciseType
import com.example.data.model.UserProfile
import com.example.ui.ActiveMeditationState
import com.example.ui.BreathingPhase
import java.util.Locale

@Composable
fun MeditationScreen(
  profile: UserProfile,
  meditationState: ActiveMeditationState?,
  onStartActivity: (MeditationActivity) -> Unit,
  onPauseResume: () -> Unit,
  onStopMeditation: () -> Unit,
) {
  val isMl = profile.isMalayalam

  if (meditationState != null) {
    // --- ACTIVE GUIDED BREATHING / MEDITATION SESSION OVERLAY ---
    val activity = meditationState.activity
    val currentPhase = meditationState.currentPhase

    val targetScale = when (currentPhase) {
      BreathingPhase.INHALE -> 1.45f
      BreathingPhase.HOLD -> 1.45f
      BreathingPhase.EXHALE -> 0.95f
      BreathingPhase.POST_HOLD -> 0.95f
    }

    val animatedScale by animateFloatAsState(
      targetValue = targetScale,
      animationSpec = tween(
        durationMillis = (meditationState.phaseRemainingSeconds * 1000).coerceAtLeast(1000),
        easing = FastOutSlowInEasing
      ),
      label = "breathingScale"
    )

    val phaseLabel = when (currentPhase) {
      BreathingPhase.INHALE -> if (isMl) "ശ്വാസമെടുക്കുക (Inhale)" else "Inhale Slowly"
      BreathingPhase.HOLD -> if (isMl) "ശ്വാസം നിർത്തുക (Hold)" else "Hold Stillness"
      BreathingPhase.EXHALE -> if (isMl) "ശ്വാസം പുറത്തുവിടുക (Exhale)" else "Release Slowly"
      BreathingPhase.POST_HOLD -> if (isMl) "വിശ്രമിക്കുക (Pause)" else "Pause & Relax"
    }

    val totalMins = meditationState.totalRemainingSeconds / 60
    val totalSecs = meditationState.totalRemainingSeconds % 60
    val formattedTotalTime = String.format(Locale.US, "%02d:%02d", totalMins, totalSecs)

    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            listOf(
              Color(0xFFEDF8F7),
              Color(0xFFE2F4F0),
              Color(0xFFF4FAF9)
            )
          )
        )
        .padding(24.dp)
        .testTag("active_meditation_session")
    ) {
      // Top row: Activity Title & Close
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.TopCenter),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (isMl) activity.titleMl else activity.titleEn,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003735)
          )
          Text(
            text = "Remaining: $formattedTotalTime  •  Cycle ${meditationState.currentCycleCount}",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF386663)
          )
        }

        IconButton(
          onClick = onStopMeditation,
          modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF006A67).copy(alpha = 0.1f))
        ) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF006A67))
        }
      }

      // Center: Breathing Animated Orb
      Column(
        modifier = Modifier
          .align(Alignment.Center)
          .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(240.dp),
          contentAlignment = Alignment.Center
        ) {
          // Outer halo
          Box(
            modifier = Modifier
              .size(220.dp)
              .scale(animatedScale)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  listOf(
                    Color(0xFF2EAA7B).copy(alpha = 0.35f),
                    Color(0xFF006A67).copy(alpha = 0.12f),
                    Color.Transparent
                  )
                )
              )
          )

          // Inner solid orb
          Box(
            modifier = Modifier
              .size(130.dp)
              .scale(animatedScale * 0.85f)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(
                  listOf(
                    Color(0xFF006A67),
                    Color(0xFF004D4A)
                  )
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "${meditationState.phaseRemainingSeconds}s",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.White
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
          text = phaseLabel,
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF005653),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Exam Mind Affirmation
        Text(
          text = if (isMl) activity.affirmationMl else activity.affirmationEn,
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF213533),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.padding(horizontal = 20.dp)
        )
      }

      // Bottom: Pause/Resume & Finish controls
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .padding(bottom = 80.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = onPauseResume,
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006A67)),
          modifier = Modifier.size(56.dp)
        ) {
          Icon(
            imageVector = if (meditationState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "Toggle",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
          )
        }
      }
    }
  } else {
    // --- MEDITATION HUB & EXERCISES LIST ---
    val activities = PyqDatabase.meditationActivities

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .testTag("meditation_screen"),
      contentPadding = PaddingValues(bottom = 96.dp)
    ) {
      // Top Peaceful Banner with Illustration
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.meditate_hero),
            contentDescription = "Meditation Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

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
                  startY = 50f
                )
              )
          )

          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(20.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF2EAA7B))
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "100% FREE MINDFULNESS",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (isMl) "വിദ്യാർത്ഥി മനഃശാന്തി കേന്ദ്രം" else "Student Mind Wellness Zone",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = if (isMl) "പരീക്ഷാ ഉത്കണ്ഠ അകറ്റാനും ഏകാഗ്രത വർദ്ധിപ്പിക്കാനുമുള്ള ശാസ്ത്രീയ വ്യായാമങ്ങൾ"
              else "Free breathing & relaxation exercises engineered to eliminate exam stress and fatigue.",
              style = MaterialTheme.typography.bodySmall,
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 11.sp
            )
          }
        }
      }

      // Zen Streak Card
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
          )
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.SelfImprovement,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = "${profile.zenMinutesTotal} Mindful Minutes Practiced",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = "Practicing just 3-5 mins between study sessions boosts retention by 40% and keeps your nervous system relaxed.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                fontSize = 11.sp
              )
            }
          }
        }
      }

      // Exercise List Header
      item {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
          text = if (isMl) "ലഭ്യമായ റിലാക്സേഷൻ വ്യായാമങ്ങൾ" else "Mind Care Exercises",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(horizontal = 20.dp),
          color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))
      }

      // 4 Specialized Activities
      items(activities) { act ->
        val icon = when (act.type) {
          MeditationExerciseType.BREATH_478 -> Icons.Default.Spa
          MeditationExerciseType.BOX_BREATHING -> Icons.Default.Eco
          MeditationExerciseType.EYE_REST_202020 -> Icons.Default.Visibility
          MeditationExerciseType.EXAM_MINDFULNESS -> Icons.Default.SelfImprovement
          MeditationExerciseType.BRAIN_RESET -> Icons.Default.Hearing
        }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onStartActivity(act) }
            .testTag("meditation_item_${act.id}"),
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
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = if (isMl) act.titleMl else act.titleEn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "${act.durationMinutes} Minutes Session",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                  )
                }
              }

              Button(
                onClick = { onStartActivity(act) },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
              ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Start", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = if (isMl) act.subtitleMl else act.subtitleEn,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "✨ Benefit: ",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = if (isMl) act.benefitsMl else act.benefitsEn,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // Student Study Wellness Tips Card
      item {
        Spacer(modifier = Modifier.height(18.dp))
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = if (isMl) "വിദ്യാർത്ഥികൾക്കുള്ള ടിപ്പുകൾ (Study Tips):" else "Aspirant Mind Habits:",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "1. Follow 50-10 Pomodoro: Study for 50 mins, do 5 mins Box Breathing.\n2. Practice 20-20-20 eye rest every 20 minutes to prevent migraine and eye strain.\n3. Before entering mock tests, do 3 minutes of 4-7-8 breathing to activate parasympathetic calm.",
              style = MaterialTheme.typography.bodySmall,
              fontSize = 12.sp,
              lineHeight = 18.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
            )
          }
        }
      }
    }
  }
}
