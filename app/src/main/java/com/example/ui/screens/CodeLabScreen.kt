package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun CodeLabScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val codeLanguage by viewModel.codeLanguage.collectAsStateWithLifecycle()
    val codeContent by viewModel.codeContent.collectAsStateWithLifecycle()
    val codeOutput by viewModel.codeOutput.collectAsStateWithLifecycle()
    val timeComplexity by viewModel.timeComplexity.collectAsStateWithLifecycle()
    val spaceComplexity by viewModel.spaceComplexity.collectAsStateWithLifecycle()
    val codeExplanation by viewModel.codeExplanation.collectAsStateWithLifecycle()
    val codeSuggestions by viewModel.codeSuggestions.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzingCode.collectAsStateWithLifecycle()

    val languages = listOf(
        "Python", "Java", "C++", "C", "JavaScript", "TypeScript",
        "Go", "Rust", "Kotlin", "Swift", "SQL", "PHP", "HTML", "CSS"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBgCanvas)
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp)
    ) {
        // Header & Language Selector
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "AI-SA Code Lab",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Interactive Multi-Language Sandbox & Complexity Analyzer",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = { viewModel.analyzeAndRunCode() },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanDark),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isAnalyzing,
                    modifier = Modifier.testTag("run_analyze_code_button")
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Analyze", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Language Selector Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(languages) { lang ->
                    val isSelected = lang == codeLanguage
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setCodeLanguage(lang) },
                        label = { Text(lang, fontSize = 11.sp) },
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

        // Problem & Constraints Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Problem: Two Sum Array Indices",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                        BadgePill(text = "LeetCode #1", color = CyanAccent)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to target.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Constraints: 2 <= nums.length <= 10⁴ • -10⁹ <= nums[i] <= 10⁹",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Code Editor
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CodeEditorBg),
                border = BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(RoseError, RoundedCornerShape(4.dp)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(modifier = Modifier.size(8.dp).background(AmberWarning, RoundedCornerShape(4.dp)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(modifier = Modifier.size(8.dp).background(EmeraldSuccess, RoundedCornerShape(4.dp)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("editor.$codeLanguage", color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                        Text("Monokai Dark", color = TextMuted, fontSize = 10.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = codeContent,
                        onValueChange = { viewModel.updateCodeContent(it) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CodeString,
                            unfocusedTextColor = CodeString,
                            focusedContainerColor = CodeEditorBg,
                            unfocusedContainerColor = CodeEditorBg,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp, max = 280.dp)
                            .testTag("code_editor_field")
                    )
                }
            }
        }

        // Complexity Audit Cards
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.3f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Time Complexity", color = TextMuted, fontSize = 11.sp)
                        Text(timeComplexity, fontWeight = FontWeight.Bold, color = CyanLight, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.3f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Space Complexity", color = TextMuted, fontSize = 11.sp)
                        Text(spaceComplexity, fontWeight = FontWeight.Bold, color = EmeraldLight, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // AI Explanation & Suggestions
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IndigoLight, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AI Algorithmic Audit", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = codeExplanation,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = codeSuggestions,
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Output Console
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CodeEditorBg),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Execution Output", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = codeOutput,
                        color = EmeraldLight,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
