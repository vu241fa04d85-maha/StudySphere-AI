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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BadgePill
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun QuizScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val allQuizzes by viewModel.allQuizzes.collectAsStateWithLifecycle()
    val activeQuiz by viewModel.activeQuiz.collectAsStateWithLifecycle()
    val quizQuestions by viewModel.quizQuestions.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentQuizIndex.collectAsStateWithLifecycle()
    val selectedAnswers by viewModel.selectedQuizAnswers.collectAsStateWithLifecycle()
    val isFinished by viewModel.isQuizFinished.collectAsStateWithLifecycle()
    val score by viewModel.quizScore.collectAsStateWithLifecycle()

    var showGenerateDialog by remember { mutableStateOf(false) }
    var generateTopic by remember { mutableStateOf("") }
    var generateDifficulty by remember { mutableStateOf("Medium") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBgCanvas)
    ) {
        if (activeQuiz != null && quizQuestions.isNotEmpty()) {
            if (isFinished) {
                // Quiz Results Card
                val totalMarks = quizQuestions.sumOf { it.marks }
                val pct = if (totalMarks > 0) (score * 100) / totalMarks else 0

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                            border = BorderStroke(1.dp, if (pct >= 60) EmeraldSuccess else RoseError),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (pct >= 60) Icons.Default.EmojiEvents else Icons.Default.SentimentDissatisfied,
                                    contentDescription = null,
                                    tint = if (pct >= 60) EmeraldLight else RoseError,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (pct >= 60) "Quiz Completed Successfully!" else "Review & Try Again!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${activeQuiz?.title}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Score", color = TextMuted, fontSize = 11.sp)
                                        Text("$score / $totalMarks", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Percentage", color = TextMuted, fontSize = 11.sp)
                                        Text("$pct%", fontWeight = FontWeight.Bold, color = if (pct >= 60) EmeraldLight else RoseError, fontSize = 18.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Time Taken", color = TextMuted, fontSize = 11.sp)
                                        Text("2m 04s", fontWeight = FontWeight.Bold, color = CyanLight, fontSize = 18.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = { viewModel.exitQuiz() },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Back to Quizzes", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Detailed Answer Explanations
                    item {
                        Text("Question Review & Explanations", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                    }

                    items(quizQuestions) { q ->
                        val userAns = selectedAnswers[quizQuestions.indexOf(q)]
                        val isCorrect = userAns == q.correctAnswer

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = BorderStroke(1.dp, if (isCorrect) EmeraldSuccess.copy(alpha = 0.4f) else RoseError.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(q.question, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Your Answer: ${userAns ?: "None"} (${if (isCorrect) "Correct" else "Incorrect"})", color = if (isCorrect) EmeraldLight else RoseError, fontSize = 12.sp)
                                Text("Correct Answer: ${q.correctAnswer}", color = EmeraldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Explanation: ${q.explanation}", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            } else {
                // Active Question Runner
                val question = quizQuestions[currentIndex]
                val currentAnswer = selectedAnswers[currentIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header with Progress & Exit
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { viewModel.exitQuiz() }) {
                            Icon(Icons.Default.Close, contentDescription = "Exit", tint = TextMuted)
                        }
                        Text(
                            text = "Question ${currentIndex + 1} of ${quizQuestions.size}",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        BadgePill(text = activeQuiz?.difficulty ?: "Medium", color = AmberWarning)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / quizQuestions.size },
                        color = CyanAccent,
                        trackColor = DarkSurfaceElevated,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = question.question,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val options = listOf(
                        Pair("A", question.optionA),
                        Pair("B", question.optionB),
                        Pair("C", question.optionC),
                        Pair("D", question.optionD)
                    )

                    options.forEach { (key, optText) ->
                        val isSelected = currentAnswer == key
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
                                .clickable { viewModel.answerQuizQuestion(currentIndex, key) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            if (isSelected) IndigoLight else DarkBorder,
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key,
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
                            onClick = { viewModel.prevQuizQuestion() },
                            enabled = currentIndex > 0,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Previous")
                        }

                        Button(
                            onClick = { viewModel.nextQuizQuestion() },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (currentIndex == quizQuestions.size - 1) "Submit Quiz" else "Next")
                        }
                    }
                }
            }
        } else {
            // Quiz Catalog List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Practice Quizzes",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Test your grasp with AI-generated drills",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        Button(
                            onClick = { showGenerateDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("generate_quiz_modal_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Quiz", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                items(allQuizzes) { quiz ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.startQuiz(quiz) }
                            .testTag("quiz_card_${quiz.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                BadgePill(text = quiz.subject, color = IndigoLight)
                                BadgePill(
                                    text = quiz.difficulty,
                                    color = when (quiz.difficulty) {
                                        "Hard" -> RoseError
                                        "Medium" -> AmberWarning
                                        else -> EmeraldSuccess
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = quiz.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = quiz.description,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "📝 ${quiz.questionCount} Questions",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                Button(
                                    onClick = { viewModel.startQuiz(quiz) },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text("Start Quiz", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Generate Quiz Modal
    if (showGenerateDialog) {
        AlertDialog(
            onDismissRequest = { showGenerateDialog = false },
            title = { Text("AI Quiz Generator", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Generate custom practice drills instantly from any subject or topic.", color = TextSecondary, fontSize = 12.sp)
                    OutlinedTextField(
                        value = generateTopic,
                        onValueChange = { generateTopic = it },
                        label = { Text("Topic / Keyword (e.g. Memory Management)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Easy", "Medium", "Hard").forEach { diff ->
                            FilterChip(
                                selected = generateDifficulty == diff,
                                onClick = { generateDifficulty = diff },
                                label = { Text(diff, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val topic = if (generateTopic.isNotBlank()) generateTopic else "Computer Systems"
                        val newQuiz = com.example.data.model.Quiz(
                            id = java.util.UUID.randomUUID().toString(),
                            title = "$topic Practice Drill",
                            description = "AI-generated quiz focusing on core principles and exam scenarios.",
                            subject = topic,
                            difficulty = generateDifficulty,
                            questionCount = 5
                        )
                        viewModel.startQuiz(newQuiz)
                        showGenerateDialog = false
                        generateTopic = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Generate & Start")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGenerateDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }
}
