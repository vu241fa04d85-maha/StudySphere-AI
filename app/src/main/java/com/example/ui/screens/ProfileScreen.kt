package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BadgePill
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudyViewModel
import com.example.ui.viewmodel.UserRole

@Composable
fun ProfileScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val activeAdmin by viewModel.activeAdmin.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    var showAdminLoginDialog by remember { mutableStateOf(false) }
    var adminCodeInput by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }
    var showLegalDialog by remember { mutableStateOf<String?>(null) } // "privacy" or "terms"
    var showExportDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBgCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // Profile Avatar & Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_main_card")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(IndigoPrimary, CyanAccent))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (userRole == UserRole.ADMIN) "AD" else (currentUser?.fullName?.take(2)?.uppercase() ?: "PK"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (userRole == UserRole.ADMIN) (activeAdmin?.fullName ?: "Administrator") else (currentUser?.fullName ?: "Prasad K."),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = if (userRole == UserRole.ADMIN) (activeAdmin?.email ?: "admin@portal.edu") else (currentUser?.email ?: "prasad.student@university.edu"),
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    BadgePill(
                        text = if (userRole == UserRole.ADMIN) "Role: ${activeAdmin?.adminCode ?: "ADMIN001"}" else "Verified Student • ${currentUser?.year ?: "3rd Year"}",
                        color = if (userRole == UserRole.ADMIN) IndigoLight else EmeraldSuccess
                    )
                }
            }
        }

        // Academic Affiliation
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Academic Credentials", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("Institution: ", color = TextMuted, fontSize = 12.sp, modifier = Modifier.width(100.dp))
                        Text(currentUser?.college ?: "Institute of Technology & Science", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("Course: ", color = TextMuted, fontSize = 12.sp, modifier = Modifier.width(100.dp))
                        Text(currentUser?.course ?: "Computer Science & Engineering", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("Academic Year: ", color = TextMuted, fontSize = 12.sp, modifier = Modifier.width(100.dp))
                        Text(currentUser?.year ?: "3rd Year (Semester 5)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Achievements Badges
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Badges & Achievements", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    val badges = listOf(
                        Triple("🔥 7-Day Streak", "Consistent study habits", AmberWarning),
                        Triple("⚡ Code Lab Pro", "Analyzed algorithms across 5 languages", CyanLight),
                        Triple("🏆 Quiz Champion", "80%+ average in CS fundamentals", EmeraldLight),
                        Triple("🚀 Hackathon Participant", "Campus AI Innovation 2026", IndigoLight)
                    )

                    badges.forEach { (title, desc, color) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            BadgePill(text = title, color = color)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(desc, color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Role Switching & Admin Gateway
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                border = BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Role-Based Access Control", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Separate role credentials for normal students and 50 pre-seeded admin proctor accounts (ADMIN001 - ADMIN050).",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (userRole == UserRole.STUDENT) {
                        Button(
                            onClick = { showAdminLoginDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_login_portal_button")
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Switch to Admin Portal (Enter Admin Code)", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.switchToStudentRole() },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Switch Back to Student Workspace", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { viewModel.logout() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Log Out / Switch Account", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Appearance & Accessibility Settings
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Appearance & Display", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Color Theme", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 13.sp)
                            Text(if (isDarkTheme) "Deep Midnight Dark (#0B1020)" else "Clean Professional Light (#F8FAFC)", color = TextSecondary, fontSize = 11.sp)
                        }

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { viewModel.toggleTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = IndigoPrimary,
                                uncheckedThumbColor = IndigoDark,
                                uncheckedTrackColor = LightBorder
                            )
                        )
                    }
                }
            }
        }

        // Privacy, Legal & GDPR Compliance
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Privacy & Data Governance", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Manage data minimization, personal export archives, and regulatory policies.",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { showLegalDialog = "privacy" },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Privacy Policy", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { showLegalDialog = "terms" },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Terms of Service", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { showExportDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanDark),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export Data", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RoseError.copy(alpha = 0.2f)),
                            border = BorderStroke(1.dp, RoseError.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = RoseError, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete Account", color = RoseError, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Admin Code Login Modal
    if (showAdminLoginDialog) {
        AlertDialog(
            onDismissRequest = {
                showAdminLoginDialog = false
                loginError = null
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = IndigoLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Admin Code Authentication", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Enter one of the 50 authorized campus Admin Codes (e.g. ADMIN001 to ADMIN050):",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = adminCodeInput,
                        onValueChange = {
                            adminCodeInput = it.uppercase()
                            loginError = null
                        },
                        placeholder = { Text("e.g. ADMIN001", fontFamily = FontFamily.Monospace) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = IndigoLight
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_code_input_field")
                    )

                    if (loginError != null) {
                        Text(text = loginError!!, color = RoseError, fontSize = 11.sp)
                    }

                    Text(
                        text = "Tip: ADMIN001 is Super Admin; ADMIN002–ADMIN050 are Faculty Proctors.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.loginWithAdminCode(
                            code = adminCodeInput,
                            onSuccess = {
                                showAdminLoginDialog = false
                                adminCodeInput = ""
                                loginError = null
                            },
                            onError = { err ->
                                loginError = err
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.testTag("admin_code_submit_button")
                ) {
                    Text("Authenticate")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAdminLoginDialog = false
                    loginError = null
                }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }

    // Legal Policy Modal (Privacy Policy & Terms of Service)
    if (showLegalDialog != null) {
        val isPrivacy = showLegalDialog == "privacy"
        AlertDialog(
            onDismissRequest = { showLegalDialog = null },
            title = {
                Text(
                    text = if (isPrivacy) "Privacy & Data Policy" else "Terms of Service",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                ) {
                    Text(
                        text = if (isPrivacy)
                            "1. Data Minimization: We collect only your name, institution, and study files strictly to power AI study analytics.\n\n" +
                            "2. Zero Model Training: Your uploaded study notes and private code sessions are never used to train foundational AI models.\n\n" +
                            "3. Student Isolation: Row Level Security guarantees that no other student or unverified proctor can access your personal documents or quiz logs.\n\n" +
                            "4. Rights: You retain full rights to export or delete your account records at any time."
                        else
                            "1. Permitted Use: AI Study Assistant is designed strictly for academic enrichment, self-assessment, and coding practice.\n\n" +
                            "2. Proctored Testing: Official tests require active session integrity. Attempts to bypass proctoring triggers academic audit flags.\n\n" +
                            "3. Content Ownership: You retain copyright over your authored notes and hackathon code submissions.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showLegalDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Close")
                }
            },
            containerColor = DarkSurfaceCard
        )
    }

    // User Data Export Dialog
    if (showExportDialog) {
        val exportJson = viewModel.exportUserDataJson()
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = {
                Text("Export Personal Records (GDPR/Data Privacy)", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Below is your complete portable JSON archive containing profile data, file counts, and study streak statistics:", color = TextSecondary, fontSize = 12.sp)
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkBgCanvas),
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text(
                            text = exportJson,
                            fontFamily = FontFamily.Monospace,
                            color = CodeString,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.showToast("Personal data archive exported successfully!")
                        showExportDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanDark)
                ) {
                    Text("Done")
                }
            },
            containerColor = DarkSurfaceCard
        )
    }

    // Account Deletion Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Confirm Account Deletion", color = RoseError, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "Are you sure you want to permanently delete your account? All your uploaded notes, AI chat history, quiz attempts, and hackathon registrations will be permanently purged.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteUserAccount()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Permanently Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }
}
