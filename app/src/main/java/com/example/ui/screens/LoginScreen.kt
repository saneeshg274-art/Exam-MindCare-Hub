package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.EducationLevel
import com.example.data.model.ExamPreference
import com.example.data.model.UserProfile

@Composable
fun LoginScreen(
  initialProfile: UserProfile,
  onLoginSuccess: (name: String, phone: String, education: EducationLevel, exam: ExamPreference, malayalam: Boolean) -> Unit,
) {
  var name by remember { mutableStateOf(initialProfile.name) }
  var phone by remember { mutableStateOf(initialProfile.phoneNumber) }
  var selectedEducation by remember { mutableStateOf(initialProfile.educationLevel) }
  var selectedExam by remember { mutableStateOf(initialProfile.examPreference) }
  var isMalayalam by remember { mutableStateOf(initialProfile.isMalayalam || initialProfile.examPreference == ExamPreference.KERALA_PSC) }

  var educationDropdownExpanded by remember { mutableStateOf(false) }
  var examDropdownExpanded by remember { mutableStateOf(false) }

  var nameError by remember { mutableStateOf(false) }
  var phoneError by remember { mutableStateOf(false) }

  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(24.dp))

      // Top Hero Badge & Logo
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary
              )
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.SelfImprovement,
          contentDescription = "Exam&MindCare Hub",
          tint = Color.White,
          modifier = Modifier.size(46.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Exam&MindCare Hub",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )

      Text(
        text = "Higher Exam Prep & Mind Wellness Platform",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Dynamic Preparation Target Card based on selected Exam
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = selectedExam.badge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = if (isMalayalam) selectedExam.titleMl else selectedExam.displayName,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Target Posts: ${selectedExam.targetTagline}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Tier Matching: ${selectedEducation.targetTiers}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Input Form
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text(
            text = "Create Aspirant Profile",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Content and practice tests adapt to your preference",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(18.dp))

          // 1. Full Name
          OutlinedTextField(
            value = name,
            onValueChange = {
              name = it
              if (it.isNotBlank()) nameError = false
            },
            label = { Text("Full Name / പേര്") },
            placeholder = { Text("Enter your full name") },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = "Name")
            },
            isError = nameError,
            supportingText = if (nameError) {
              { Text("Please enter your name") }
            } else null,
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("name_input"),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // 2. Phone Number
          OutlinedTextField(
            value = phone,
            onValueChange = { input ->
              val filtered = input.filter { it.isDigit() }.take(10)
              phone = filtered
              if (filtered.length == 10) phoneError = false
            },
            label = { Text("Phone Number / ഫോൺ നമ്പർ") },
            placeholder = { Text("10-digit mobile number") },
            leadingIcon = {
              Icon(Icons.Default.Phone, contentDescription = "Phone")
            },
            prefix = { Text("+91 ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = phoneError,
            supportingText = if (phoneError) {
              { Text("Please enter a valid 10-digit phone number") }
            } else null,
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("phone_input"),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 3. Education Dropdown (10th, +2, Degree, ITI, Diploma)
          Text(
            text = "Educational Qualification",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))

          Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = "${selectedEducation.label} (${selectedEducation.labelMl})",
              onValueChange = {},
              readOnly = true,
              leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
              trailingIcon = {
                Icon(
                  Icons.Default.ArrowDropDown,
                  contentDescription = "Select Education",
                  modifier = Modifier.clickable { educationDropdownExpanded = true }
                )
              },
              modifier = Modifier
                .fillMaxWidth()
                .clickable { educationDropdownExpanded = true }
                .testTag("education_dropdown"),
              shape = RoundedCornerShape(12.dp)
            )

            DropdownMenu(
              expanded = educationDropdownExpanded,
              onDismissRequest = { educationDropdownExpanded = false },
              modifier = Modifier.fillMaxWidth(0.85f)
            ) {
              EducationLevel.entries.forEach { level ->
                DropdownMenuItem(
                  text = {
                    Column {
                      Text(
                        text = "${level.label}  •  ${level.labelMl}",
                        fontWeight = if (selectedEducation == level) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedEducation == level) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                      )
                      Text(
                        text = level.targetTiers,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                      )
                    }
                  },
                  onClick = {
                    selectedEducation = level
                    educationDropdownExpanded = false
                  },
                  leadingIcon = if (selectedEducation == level) {
                    { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                  } else null
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 4. Target Exam Dropdown (PSC, UPSC, SSC, RRB)
          Text(
            text = "What You Prefer to Study",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))

          Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = "${selectedExam.displayName} (${selectedExam.titleMl})",
              onValueChange = {},
              readOnly = true,
              leadingIcon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
              trailingIcon = {
                Icon(
                  Icons.Default.ArrowDropDown,
                  contentDescription = "Select Exam",
                  modifier = Modifier.clickable { examDropdownExpanded = true }
                )
              },
              modifier = Modifier
                .fillMaxWidth()
                .clickable { examDropdownExpanded = true }
                .testTag("exam_dropdown"),
              shape = RoundedCornerShape(12.dp)
            )

            DropdownMenu(
              expanded = examDropdownExpanded,
              onDismissRequest = { examDropdownExpanded = false },
              modifier = Modifier.fillMaxWidth(0.85f)
            ) {
              ExamPreference.entries.forEach { exam ->
                DropdownMenuItem(
                  text = {
                    Column {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                          text = "${exam.displayName} (${exam.titleMl})",
                          fontWeight = if (selectedExam == exam) FontWeight.Bold else FontWeight.Normal,
                          color = if (selectedExam == exam) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                      }
                      Text(
                        text = exam.targetTagline,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                      )
                    }
                  },
                  onClick = {
                    selectedExam = exam
                    // Default to Malayalam true if Kerala PSC selected
                    if (exam == ExamPreference.KERALA_PSC) {
                      isMalayalam = true
                    }
                    examDropdownExpanded = false
                  },
                  leadingIcon = if (selectedExam == exam) {
                    { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                  } else null
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 5. Malayalam Language Toggle Switch (Highlighted for Kerala PSC)
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .border(
                1.dp,
                if (isMalayalam) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
              )
              .background(
                if (isMalayalam) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                else MaterialTheme.colorScheme.surface
              )
              .padding(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Icon(
                  imageVector = Icons.Default.Translate,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "മലയാളം പതിപ്പ് (Malayalam Version)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "PSC ചോദ്യങ്ങളും വിശദീകരണങ്ങളും മലയാളത്തിൽ കാണിക്കുക",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                  )
                }
              }

              Switch(
                checked = isMalayalam,
                onCheckedChange = { isMalayalam = it },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = MaterialTheme.colorScheme.primary,
                  checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.testTag("malayalam_toggle")
              )
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          // Submit Button
          Button(
            onClick = {
              var valid = true
              if (name.isBlank()) {
                nameError = true
                valid = false
              }
              if (phone.length < 10) {
                phoneError = true
                valid = false
              }

              if (valid) {
                onLoginSuccess(name, phone, selectedEducation, selectedExam, isMalayalam)
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("submit_login_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary
            )
          ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Start Preparation & Zen",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Free mindfulness sessions • Real PYQ Papers • Authentic OMR Sheets • Instant PDF Export",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        fontSize = 11.sp
      )

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
