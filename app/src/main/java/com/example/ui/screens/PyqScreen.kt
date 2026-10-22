package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PyqDatabase
import com.example.data.model.Question
import com.example.data.model.QuestionPaper
import com.example.data.model.SubjectType
import com.example.data.model.UserProfile

@Composable
fun PyqScreen(
  profile: UserProfile,
  questions: List<Question>,
  bookmarkedIds: Set<String>,
  selectedSubject: SubjectType?,
  selectedYear: Int?,
  onSelectSubject: (SubjectType?) -> Unit,
  onSelectYear: (Int?) -> Unit,
  onToggleBookmark: (String) -> Unit,
  onToggleMalayalam: (Boolean) -> Unit,
  onStartOmrPaper: (QuestionPaper) -> Unit,
  onDownloadPaperPdf: (QuestionPaper) -> Unit,
) {
  val context = LocalContext.current
  val isMl = profile.isMalayalam

  var selectedTab by remember { mutableIntStateOf(0) }
  val userSelectedOptions = remember { mutableStateMapOf<String, Int>() }
  val expandedExplanations = remember { mutableStateMapOf<String, Boolean>() }

  val papers = remember(profile.examPreference) {
    PyqDatabase.questionPapers.filter { it.examPreference == profile.examPreference }
      .ifEmpty { PyqDatabase.questionPapers }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("pyq_screen")
  ) {
    // Header Bar with Language Switch
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = if (isMl) "മുൻകാല ചോദ്യങ്ങൾ (PYQ)" else "Previous Year Questions",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "${profile.examPreference.displayName} • ${questions.size} Questions Available",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Surface(
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .clickable { onToggleMalayalam(!isMl) }
          .border(
            1.dp,
            if (isMl) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            RoundedCornerShape(20.dp)
          )
          .testTag("pyq_malayalam_toggle"),
        color = if (isMl) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Translate,
            contentDescription = null,
            tint = if (isMl) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isMl) "മലയാളം" else "English",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isMl) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // Top Tabs: 1. Subject Question Bank, 2. Official Papers & PDF Download
    val tabs = listOf(
      if (isMl) "ചോദ്യങ്ങൾ (MCQ Bank)" else "Questions (MCQ Bank)",
      if (isMl) "ഔദ്യോഗിക പേപ്പറുകൾ & PDF" else "Official Papers & PDF"
    )

    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      edgePadding = 20.dp,
      containerColor = MaterialTheme.colorScheme.background,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = MaterialTheme.colorScheme.primary
        )
      }
    ) {
      tabs.forEachIndexed { index, tabTitle ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = tabTitle,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
              color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        )
      }
    }

    if (selectedTab == 0) {
      // Filters Bar (Subject & Year)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface)
          .padding(vertical = 8.dp)
      ) {
        // Subject filters
        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item {
            FilterChip(
              selected = selectedSubject == null,
              onClick = { onSelectSubject(null) },
              label = { Text("All Subjects") },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
          items(SubjectType.entries) { subj ->
            FilterChip(
              selected = selectedSubject == subj,
              onClick = { onSelectSubject(if (selectedSubject == subj) null else subj) },
              label = { Text(if (isMl) subj.titleMl else subj.titleEn) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        // Year filters
        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item {
            FilterChip(
              selected = selectedYear == null,
              onClick = { onSelectYear(null) },
              label = { Text("All Years") },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
              )
            )
          }
          listOf(2023, 2022, 2021).forEach { year ->
            item {
              FilterChip(
                selected = selectedYear == year,
                onClick = { onSelectYear(if (selectedYear == year) null else year) },
                label = { Text("$year") },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.secondary,
                  selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                )
              )
            }
          }
        }
      }

      // Questions List
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        if (questions.isEmpty()) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "No questions match the selected filters.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        itemsIndexed(questions) { index, question ->
          val isBookmarked = bookmarkedIds.contains(question.id)
          val selectedOption = userSelectedOptions[question.id]
          val isExplanationVisible = expandedExplanations[question.id] == true

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              // Top tag row
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(MaterialTheme.colorScheme.primaryContainer)
                      .padding(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "Q${index + 1} • ${question.year}",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (isMl) question.subject.titleMl else question.subject.titleEn,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                IconButton(
                  onClick = { onToggleBookmark(question.id) },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Text(
                text = question.paperTitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Question Text
              Text(
                text = if (isMl && question.questionMl.isNotBlank()) question.questionMl else question.questionEn,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
              )

              Spacer(modifier = Modifier.height(14.dp))

              // Options
              val options = if (isMl && question.optionsMl.isNotEmpty()) question.optionsMl else question.optionsEn
              val labels = listOf("A", "B", "C", "D")

              options.forEachIndexed { optIdx, optText ->
                val isSelected = selectedOption == optIdx
                val isCorrect = optIdx == question.correctIndex
                val isEvaluated = selectedOption != null

                val borderColor = when {
                  isEvaluated && isCorrect -> Color(0xFF10B981)
                  isEvaluated && isSelected -> Color(0xFFEF4444)
                  else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                }

                val bgColor = when {
                  isEvaluated && isCorrect -> Color(0xFF10B981).copy(alpha = 0.12f)
                  isEvaluated && isSelected -> Color(0xFFEF4444).copy(alpha = 0.12f)
                  else -> MaterialTheme.colorScheme.surface
                }

                Surface(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                      if (selectedOption == null) {
                        userSelectedOptions[question.id] = optIdx
                        expandedExplanations[question.id] = true
                      }
                    }
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp)),
                  color = bgColor
                ) {
                  Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Box(
                      modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                          if (isEvaluated && isCorrect) Color(0xFF10B981)
                          else if (isEvaluated && isSelected) Color(0xFFEF4444)
                          else MaterialTheme.colorScheme.surfaceVariant
                        ),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = labels[optIdx],
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEvaluated && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = optText,
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.onSurface,
                      fontSize = 13.sp
                    )
                  }
                }
              }

              // Explanation section toggle
              if (selectedOption != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                      expandedExplanations[question.id] = !(expandedExplanations[question.id] ?: false)
                    },
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (isMl) "വിശദീകരണം കാണുക" else "View Detailed Explanation",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                  Text(
                    text = if (isExplanationVisible) "▲" else "▼",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 10.sp
                  )
                }

                AnimatedVisibility(visible = isExplanationVisible) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = 8.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                      .padding(12.dp)
                  ) {
                    Text(
                      text = if (isMl && question.explanationMl.isNotBlank()) question.explanationMl else question.explanationEn,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      lineHeight = 18.sp
                    )
                  }
                }
              }
            }
          }
        }
      }
    } else {
      // Tab 2: Official Papers & PDF Download
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            )
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                Icons.Default.Download,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = if (isMl) "നേരിട്ട് PDF ഡൗൺലോഡ് ചെയ്യാം" else "Instant Printable PDF Downloads",
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                  text = if (isMl) "ചോദ്യങ്ങളും ഉത്തരസൂചികയും അടങ്ങിയ ഔദ്യോഗിക ഫോർമാറ്റിലുള്ള PDF" else "Official formatted question papers with detailed answer key grid.",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
              }
            }
          }
        }

        items(papers) { paper ->
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
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "${paper.year} PAPER",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                  )
                }

                Text(
                  text = "${paper.durationMinutes} Mins • ${paper.totalMarks.toInt()} Marks",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = if (isMl) paper.titleMl else paper.titleEn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = "Target Level: ${paper.educationLevel.label}  •  Questions: ${paper.questions.size}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Spacer(modifier = Modifier.height(16.dp))

              // Action buttons row
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                // Download PDF button
                OutlinedButton(
                  onClick = { onDownloadPaperPdf(paper) },
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(text = "PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Practice in OMR mode button
                Button(
                  onClick = { onStartOmrPaper(paper) },
                  modifier = Modifier.weight(1.4f),
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                  Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (isMl) "OMR ടെസ്റ്റ്" else "OMR Test",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
