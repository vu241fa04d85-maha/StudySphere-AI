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
import androidx.compose.material.icons.automirrored.filled.*
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
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun AdminScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val activeAdmin by viewModel.activeAdmin.collectAsStateWithLifecycle()
    val allAdmins by viewModel.allAdmins.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val allTests by viewModel.allTests.collectAsStateWithLifecycle()
    val allHackathons by viewModel.allHackathons.collectAsStateWithLifecycle()
    val allQuizzes by viewModel.allQuizzes.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("Overview") } // "Overview", "Admins (50)", "Create Test", "Create Hackathon"

    // Create Test form state
    var testTitle by remember { mutableStateOf("") }
    var testSubj by remember { mutableStateOf("Computer Science") }
    var testDesc by remember { mutableStateOf("") }
    var testDuration by remember { mutableStateOf("45") }
    var testMarks by remember { mutableStateOf("50") }
    var testPass by remember { mutableStateOf("35") }

    // Create Hackathon form state
    var hackTitle by remember { mutableStateOf("") }
    var hackTheme by remember { mutableStateOf("") }
    var hackDesc by remember { mutableStateOf("") }
    var hackRules by remember { mutableStateOf("Teams of 1-4. Projects must be open source.") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBgCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // Protected Admin Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = IndigoLight)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Administration Console",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Text(
                                text = "Authenticated as ${activeAdmin?.fullName ?: "Staff"} (${activeAdmin?.adminCode ?: "ADMIN001"})",
                                fontSize = 12.sp,
                                color = CyanLight
                            )
                        }

                        Button(
                            onClick = { viewModel.switchToStudentRole() },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Exit Admin", color = RoseError, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Tab Row
        item {
            val tabs = listOf("Overview", "Admins (50)", "Create Test", "Create Hackathon")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                tabs.forEach { tab ->
                    val isSelected = activeTab == tab
                    FilterChip(
                        selected = isSelected,
                        onClick = { activeTab = tab },
                        label = { Text(tab, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IndigoPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = DarkSurfaceElevated,
                            labelColor = TextSecondary
                        ),
                        border = BorderStroke(1.dp, if (isSelected) IndigoLight else DarkBorder)
                    )
                }
            }
        }

        when (activeTab) {
            "Overview" -> {
                // Platform Metrics
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatCard(
                            title = "Total Students",
                            value = "${allUsers.size}",
                            subtitle = "Registered accounts",
                            icon = Icons.Default.People,
                            accentColor = CyanAccent,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Admin Accounts",
                            value = "${allAdmins.size}",
                            subtitle = "ADMIN001 - ADMIN050",
                            icon = Icons.Default.Security,
                            accentColor = IndigoLight,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatCard(
                            title = "Proctored Tests",
                            value = "${allTests.size}",
                            subtitle = "Active evaluations",
                            icon = Icons.AutoMirrored.Filled.Assignment,
                            accentColor = EmeraldSuccess,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Hackathons",
                            value = "${allHackathons.size}",
                            subtitle = "Campus challenges",
                            icon = Icons.Default.EmojiEvents,
                            accentColor = AmberWarning,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Active Tests Management
                item {
                    Text("Official Tests in Circulation", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                }

                items(allTests) { test ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(test.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                                Text("${test.subject} • ${test.durationMinutes}m • Created by ${test.createdByAdminCode}", color = TextSecondary, fontSize = 11.sp)
                            }
                            IconButton(onClick = { viewModel.deleteOfficialTest(test.id) }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = RoseError)
                            }
                        }
                    }
                }
            }

            "Admins (50)" -> {
                item {
                    Text(
                        text = "50 Pre-Seeded Admin Credentials (ADMIN001 - ADMIN050)",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Toggle account active/suspended state and oversee campus proctors",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                items(allAdmins) { admin ->
                    val isActive = admin.status == "active"
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, if (isActive) DarkBorder else RoseError.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = admin.adminCode,
                                        fontWeight = FontWeight.Bold,
                                        color = IndigoLight,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 13.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    BadgePill(text = admin.role, color = if (admin.role == "super_admin") AmberWarning else CyanAccent)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${admin.fullName} • ${admin.email}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            Button(
                                onClick = { viewModel.toggleAdminStatus(admin) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isActive) DarkSurfaceElevated else EmeraldSuccess
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isActive) "Suspend" else "Activate",
                                    color = if (isActive) RoseError else Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            "Create Test" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Text("Create & Publish Official Proctored Test", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)

                            OutlinedTextField(
                                value = testTitle,
                                onValueChange = { testTitle = it },
                                label = { Text("Test Title (e.g. Distributed Systems Midterm)") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = testSubj,
                                onValueChange = { testSubj = it },
                                label = { Text("Subject / Discipline") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = testDesc,
                                onValueChange = { testDesc = it },
                                label = { Text("Instructions & Scope") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = testDuration,
                                    onValueChange = { testDuration = it },
                                    label = { Text("Duration (mins)") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = testMarks,
                                    onValueChange = { testMarks = it },
                                    label = { Text("Total Marks") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = testPass,
                                    onValueChange = { testPass = it },
                                    label = { Text("Passing Mark") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Button(
                                onClick = {
                                    val title = if (testTitle.isNotBlank()) testTitle else "CS400 Official Certification"
                                    val desc = if (testDesc.isNotBlank()) testDesc else "Proctored academic evaluation"
                                    val dur = testDuration.toIntOrNull() ?: 45
                                    val marks = testMarks.toIntOrNull() ?: 50
                                    val pass = testPass.toIntOrNull() ?: 35
                                    viewModel.createOfficialTest(title, testSubj, desc, dur, marks, pass)
                                    testTitle = ""
                                    testDesc = ""
                                    activeTab = "Overview"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Publish Test to Students", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            "Create Hackathon" -> {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Text("Launch New Campus Hackathon", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)

                            OutlinedTextField(
                                value = hackTitle,
                                onValueChange = { hackTitle = it },
                                label = { Text("Hackathon Title") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = hackTheme,
                                onValueChange = { hackTheme = it },
                                label = { Text("Theme (e.g. AI for Health & Education)") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = hackDesc,
                                onValueChange = { hackDesc = it },
                                label = { Text("Description & Objectives") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = hackRules,
                                onValueChange = { hackRules = it },
                                label = { Text("Rules & Requirements") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = {
                                    val title = if (hackTitle.isNotBlank()) hackTitle else "Campus Tech Hackathon"
                                    val theme = if (hackTheme.isNotBlank()) hackTheme else "Innovation for Tomorrow"
                                    val desc = if (hackDesc.isNotBlank()) hackDesc else "Build high-impact software solutions"
                                    viewModel.createHackathon(title, theme, desc, hackRules)
                                    hackTitle = ""
                                    hackTheme = ""
                                    hackDesc = ""
                                    activeTab = "Overview"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Launch Hackathon", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
