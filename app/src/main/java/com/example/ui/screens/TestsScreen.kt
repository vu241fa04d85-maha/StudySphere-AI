package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BadgePill
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun TestsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val allTests by viewModel.allTests.collectAsStateWithLifecycle()
    val activeTest by viewModel.activeTest.collectAsStateWithLifecycle()
    val testQuestions by viewModel.testQuestions.collectAsStateWithLifecycle()
    val currentTestIndex by viewModel.currentTestIndex.collectAsStateWithLifecycle()
    val selectedTestAnswers by viewModel.selectedTestAnswers.collectAsStateWithLifecycle()
    val isFinished by viewModel.isTestFinished.collectAsStateWithLifecycle()
    val testScore by viewModel.testScore.collectAsStateWithLifecycle()
    val remainingSeconds by viewModel.testRemainingSeconds.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBgCanvas)
    ) {
        if (activeTest != null && testQuestions.isNotEmpty()) {
            if (isFinished) {
                // Official Test Result View
                val test = activeTest!!
                val passed = testScore >= test.passingScore
                val minutes = remainingSeconds / 60
                val seconds = remainingSeconds % 60

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        border = BorderStroke(1.dp, if (passed) EmeraldSuccess else RoseError),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Icon(
                                imageVector = if (passed) Icons.Default.Verified else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (passed) EmeraldLight else RoseError,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = if (passed) "Examination Passed!" else "Did Not Meet Passing Score",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = test.title,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Official Score", color = TextMuted, fontSize = 11.sp)
                                    Text("$testScore / ${test.totalMarks}", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 20.sp)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Passing Mark", color = TextMuted, fontSize = 11.sp)
                                    Text("${test.passingScore}", fontWeight = FontWeight.Bold, color = CyanLight, fontSize = 20.sp)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Status", color = TextMuted, fontSize = 11.sp)
                                    Text(if (passed) "CERTIFIED" else "FAILED", fontWeight = FontWeight.Bold, color = if (passed) EmeraldLight else RoseError, fontSize = 18.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { viewModel.exitTest() },
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Back to Examination Portal", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Live Timed Examination Runner
                val question = testQuestions[currentTestIndex]
                val currentChoice = selectedTestAnswers[currentTestIndex]
                val minutes = remainingSeconds / 60
                val seconds = remainingSeconds % 60
                val isTimeCritical = remainingSeconds < 300

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Top Status & Countdown Bar
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        border = BorderStroke(1.dp, if (isTimeCritical) RoseError else DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(if (isTimeCritical) RoseError else EmeraldSuccess, RoundedCornerShape(4.dp)))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Proctored Exam", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 12.sp)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = if (isTimeCritical) RoseError else CyanAccent, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format("%02d:%02d", minutes, seconds),
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTimeCritical) RoseError else CyanLight,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Question ${currentTestIndex + 1} of ${testQuestions.size} (${question.marks} Marks)",
                        fontWeight = FontWeight.Bold,
                        color = CyanLight,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = question.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    question.options.forEachIndexed { optIdx, optText ->
                        val isSelected = currentChoice == optIdx
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) IndigoDark else DarkSurfaceElevated
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) IndigoLight else DarkBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { viewModel.answerTestQuestion(currentTestIndex, optIdx) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(if (isSelected) IndigoLight else DarkBorder, RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${('A'.code + optIdx).toChar()}",
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = optText,
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.prevTestQuestion() },
                            enabled = currentTestIndex > 0,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Previous")
                        }

                        if (currentTestIndex == testQuestions.size - 1) {
                            Button(
                                onClick = { viewModel.submitActiveTest() },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Final Submit", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = { viewModel.nextTestQuestion() },
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Next Question")
                            }
                        }
                    }
                }
            }
        } else {
            // Official Proctored Tests List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = "Official Proctored Examinations",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Admin-created timed academic certifications and mid-terms",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                items(allTests) { test ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.startOfficialTest(test) }
                            .testTag("test_card_${test.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                BadgePill(text = test.subject, color = IndigoLight)
                                BadgePill(text = "Proctored by ${test.createdByAdminCode}", color = CyanAccent)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = test.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = test.description,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text("⏱️ ${test.durationMinutes} mins • Total: ${test.totalMarks} Marks", fontSize = 11.sp, color = TextMuted)
                                    Text("Passing Threshold: ${test.passingScore} Marks", fontSize = 11.sp, color = EmeraldLight)
                                }

                                Button(
                                    onClick = { viewModel.startOfficialTest(test) },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text("Start Exam", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
