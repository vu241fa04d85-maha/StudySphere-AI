package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Hackathon
import com.example.ui.components.BadgePill
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun HackathonsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val allHackathons by viewModel.allHackathons.collectAsStateWithLifecycle()

    var showSubmitDialog by remember { mutableStateOf<Hackathon?>(null) }
    var projectName by remember { mutableStateOf("") }
    var projectUrl by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBgCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Campus & Global Hackathons",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Collaborate, submit AI projects & climb the student leaderboard",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        items(allHackathons) { hack ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hackathon_card_${hack.id}")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BadgePill(text = "Active Competition", color = EmeraldSuccess)
                        Text(
                            text = hack.prizePool,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = hack.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Theme: ${hack.theme}",
                        fontWeight = FontWeight.SemiBold,
                        color = CyanLight,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = hack.description,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Rules: ${hack.rules}", color = TextMuted, fontSize = 11.sp)
                            Text("Eligibility: ${hack.eligibility}", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "👥 ${hack.participantCount} Participants",
                            fontSize = 12.sp,
                            color = TextMuted
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    showSubmitDialog = hack
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Submit Project", fontSize = 11.sp)
                            }

                            Button(
                                onClick = { viewModel.registerForHackathon(hack.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Register", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Submit Project Modal
    if (showSubmitDialog != null) {
        val hack = showSubmitDialog!!
        AlertDialog(
            onDismissRequest = { showSubmitDialog = null },
            title = { Text("Submit Hackathon Project", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Submitting to: ${hack.title}", color = CyanLight, fontSize = 12.sp)

                    OutlinedTextField(
                        value = projectName,
                        onValueChange = { projectName = it },
                        label = { Text("Project / Team Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = projectUrl,
                        onValueChange = { projectUrl = it },
                        label = { Text("GitHub Repository / Demo Link") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val name = if (projectName.isNotBlank()) projectName else "AI-SA Innovation Project"
                        val url = if (projectUrl.isNotBlank()) projectUrl else "https://github.com/student/ai-sa-demo"
                        viewModel.submitHackathonProject(hack.id, name, url)
                        showSubmitDialog = null
                        projectName = ""
                        projectUrl = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                ) {
                    Text("Submit Project")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = null }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }
}
