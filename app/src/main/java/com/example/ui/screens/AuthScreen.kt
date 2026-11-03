package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: StudyViewModel,
    onLoginSuccess: () -> Unit
) {
    // 1. Admin and User Switching Option
    var selectedRole by remember { mutableStateOf("STUDENT") } // "STUDENT" or "ADMIN"

    // 2. Sign In vs Sign Up Option
    var isSignUp by remember { mutableStateOf(false) }

    // Form inputs - Student
    var email by remember { mutableStateOf("chinkaramu2006@gmail.com") }
    var password by remember { mutableStateOf("password123") }
    var fullName by remember { mutableStateOf("Ramu Chinka") }
    var college by remember { mutableStateOf("Institute of Technology") }
    var course by remember { mutableStateOf("Computer Science & AI") }
    var confirmPassword by remember { mutableStateOf("password123") }

    // Form inputs - Admin
    var adminCode by remember { mutableStateOf("ADMIN001") }
    var adminPassword by remember { mutableStateOf("AdminSecret2026!") }
    var adminFullName by remember { mutableStateOf("Dr. Sarah Jenkins") }
    var adminEmail by remember { mutableStateOf("admin.jenkins@ai-sa.edu") }
    var adminConfirmPassword by remember { mutableStateOf("AdminSecret2026!") }

    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        LightBgLavender,
                        LightBgIvory,
                        LightBgPaleLime
                    )
                )
            )
            .testTag("auth_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(LavenderDeep, LavenderPrimary)
                            )
                        )
                        .border(1.5.dp, Color.White, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AI",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AI Study Assistant",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = LightTextPrimary
                    )
                    Text(
                        text = "Single Sign-On & Access Gateway",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LavenderDeep
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Auth Glass Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.88f),
                border = BorderStroke(1.5.dp, Color.White),
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // --- 1. ADMIN VS USER SWITCHING OPTION ---
                    Text(
                        text = "SELECT WORKSPACE ROLE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightTextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LavenderPrimary.copy(alpha = 0.12f))
                            .padding(4.dp)
                    ) {
                        // Student Switcher
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedRole = "STUDENT"
                                    errorMessage = null
                                }
                                .testTag("role_student_tab"),
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedRole == "STUDENT") LavenderDeep else Color.Transparent,
                            shadowElevation = if (selectedRole == "STUDENT") 2.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (selectedRole == "STUDENT") Color.White else LightTextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Student",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedRole == "STUDENT") Color.White else LightTextSecondary
                                )
                            }
                        }

                        // Admin Switcher
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedRole = "ADMIN"
                                    errorMessage = null
                                }
                                .testTag("role_admin_tab"),
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedRole == "ADMIN") ChartreuseDeep else Color.Transparent,
                            shadowElevation = if (selectedRole == "ADMIN") 2.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = if (selectedRole == "ADMIN") Color.White else LightTextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Admin",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedRole == "ADMIN") Color.White else LightTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- 2. SIGN IN VS SIGN UP TABS ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.2f)), RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    isSignUp = false
                                    errorMessage = null
                                }
                                .testTag("auth_signin_tab"),
                            color = if (!isSignUp) LavenderPrimary.copy(alpha = 0.2f) else Color.Transparent
                        ) {
                            Text(
                                text = "Sign In",
                                fontSize = 12.sp,
                                fontWeight = if (!isSignUp) FontWeight.Bold else FontWeight.Medium,
                                color = if (!isSignUp) LavenderDeep else LightTextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    isSignUp = true
                                    errorMessage = null
                                }
                                .testTag("auth_signup_tab"),
                            color = if (isSignUp) LavenderPrimary.copy(alpha = 0.2f) else Color.Transparent
                        ) {
                            Text(
                                text = "Sign Up",
                                fontSize = 12.sp,
                                fontWeight = if (isSignUp) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSignUp) LavenderDeep else LightTextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Error Message Banner
                    AnimatedVisibility(visible = errorMessage != null) {
                        if (errorMessage != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = TomatoPrimary.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, TomatoPrimary.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = TomatoDeep,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = errorMessage!!,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TomatoDeep
                                    )
                                }
                            }
                        }
                    }

                    // --- 3. FORM FIELDS ---
                    if (selectedRole == "STUDENT") {
                        if (isSignUp) {
                            // Full Name
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Full Name") },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = LightTextSecondary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                                    .testTag("input_student_fullname"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // College / University
                            OutlinedTextField(
                                value = college,
                                onValueChange = { college = it },
                                label = { Text("College / University") },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.School, contentDescription = null, tint = LightTextSecondary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Course / Major
                            OutlinedTextField(
                                value = course,
                                onValueChange = { course = it },
                                label = { Text("Course / Major") },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = LightTextSecondary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Email ID
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = LightTextSecondary)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .testTag("input_student_email"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password (min 6 chars)") },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = LightTextSecondary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = LightTextSecondary
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .testTag("input_student_password"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (isSignUp) {
                            // Confirm Password
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = { Text("Confirm Password") },
                                singleLine = true,
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = LightTextSecondary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Student Submit Button
                        Button(
                            onClick = {
                                errorMessage = null
                                isSubmitting = true
                                if (isSignUp) {
                                    viewModel.registerStudent(
                                        fullName = fullName,
                                        email = email,
                                        college = college,
                                        course = course,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        onSuccess = {
                                            isSubmitting = false
                                            onLoginSuccess()
                                        },
                                        onError = {
                                            isSubmitting = false
                                            errorMessage = it
                                        }
                                    )
                                } else {
                                    viewModel.loginStudent(
                                        email = email,
                                        password = password,
                                        onSuccess = {
                                            isSubmitting = false
                                            onLoginSuccess()
                                        },
                                        onError = {
                                            isSubmitting = false
                                            errorMessage = it
                                        }
                                    )
                                }
                            },
                            enabled = !isSubmitting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_student_submit"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LavenderDeep)
                        ) {
                            Text(
                                text = if (isSubmitting) "Checking Credentials..." else if (isSignUp) "Create Student Account" else "Sign In to Student Dashboard",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        // ADMIN AUTHENTICATION
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ChartreusePrimary.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, ChartreuseDeep.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = ChartreuseDeep,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Requires verified Admin Code: ADMIN001 to ADMIN050",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ChartreuseDeep
                                )
                            }
                        }

                        if (isSignUp) {
                            OutlinedTextField(
                                value = adminFullName,
                                onValueChange = { adminFullName = it },
                                label = { Text("Admin Full Name") },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = LightTextSecondary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                shape = RoundedCornerShape(12.dp)
                            )

                            OutlinedTextField(
                                value = adminEmail,
                                onValueChange = { adminEmail = it },
                                label = { Text("Institutional Email") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = LightTextSecondary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Admin Code
                        OutlinedTextField(
                            value = adminCode,
                            onValueChange = { adminCode = it.uppercase() },
                            label = { Text("Admin Code (e.g. ADMIN001)") },
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Key, contentDescription = null, tint = LightTextSecondary)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .testTag("input_admin_code"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Admin Password
                        OutlinedTextField(
                            value = adminPassword,
                            onValueChange = { adminPassword = it },
                            label = { Text("Security Password") },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = LightTextSecondary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = LightTextSecondary
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .testTag("input_admin_password"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (isSignUp) {
                            OutlinedTextField(
                                value = adminConfirmPassword,
                                onValueChange = { adminConfirmPassword = it },
                                label = { Text("Confirm Security Password") },
                                singleLine = true,
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = LightTextSecondary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Admin Submit Button
                        Button(
                            onClick = {
                                errorMessage = null
                                isSubmitting = true
                                viewModel.loginWithAdminCode(
                                    code = adminCode,
                                    onSuccess = {
                                        isSubmitting = false
                                        onLoginSuccess()
                                    },
                                    onError = {
                                        isSubmitting = false
                                        errorMessage = it
                                    }
                                )
                            },
                            enabled = !isSubmitting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_admin_submit"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ChartreuseDeep)
                        ) {
                            Text(
                                text = if (isSubmitting) "Verifying..." else if (isSignUp) "Register Admin Account" else "Authenticate Admin Portal",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- 4. QUICK TEST CREDENTIAL BUTTONS ---
                    Text(
                        text = "1-CLICK DEMO FILL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightTextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                selectedRole = "STUDENT"
                                isSignUp = false
                                email = "chinkaramu2006@gmail.com"
                                password = "password123"
                                errorMessage = null
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_quick_student"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text("Student Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LavenderDeep)
                                Text("chinkaramu2006@gmail.com", fontSize = 9.sp, color = LightTextMuted)
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                selectedRole = "ADMIN"
                                isSignUp = false
                                adminCode = "ADMIN001"
                                adminPassword = "AdminSecret2026!"
                                errorMessage = null
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_quick_admin"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text("Admin Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ChartreuseDeep)
                                Text("ADMIN001 (Super Admin)", fontSize = 9.sp, color = LightTextMuted)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "End-to-End Encryption • Zero-Knowledge Architecture",
                fontSize = 10.sp,
                color = LightTextMuted
            )
        }
    }
}
