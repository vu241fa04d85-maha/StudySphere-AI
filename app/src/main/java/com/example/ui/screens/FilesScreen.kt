package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.StudyFile
import com.example.ui.components.BadgePill
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun FilesScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val allFiles by viewModel.allFiles.collectAsStateWithLifecycle()
    val selectedFile by viewModel.selectedFile.collectAsStateWithLifecycle()
    val activeSummary by viewModel.activeSummary.collectAsStateWithLifecycle()

    var filterType by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showUploadDialog by remember { mutableStateOf(false) }

    // Upload Dialog State
    var newFileName by remember { mutableStateOf("") }
    var newFileType by remember { mutableStateOf("PDF") }
    var newSubject by remember { mutableStateOf("Computer Science") }
    var newDesc by remember { mutableStateOf("") }

    val fileTypes = listOf("All", "PDF", "PPT", "DOC", "TXT")

    val filteredFiles = allFiles.filter { file ->
        val matchesType = (filterType == "All" || file.fileType.equals(filterType, ignoreCase = true))
        val matchesSearch = searchQuery.isBlank() ||
                file.fileName.contains(searchQuery, ignoreCase = true) ||
                file.subject.contains(searchQuery, ignoreCase = true)
        matchesType && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBgCanvas)
    ) {
        // Document Detail / Flashcard Modal if a file is selected
        if (selectedFile != null) {
            val file = selectedFile!!
            val summary = activeSummary

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { viewModel.selectFile(null) }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = file.fileName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${file.subject} • ${file.fileType} (${file.fileSizeKb} KB)",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        IconButton(onClick = { viewModel.deleteFile(file.id) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = RoseError)
                        }
                    }
                }

                // AI Action Buttons
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                viewModel.startNewConversation("Q&A on ${file.fileName}")
                                viewModel.navigateTo(AppScreen.AI_CHAT)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ask AI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.navigateTo(AppScreen.QUIZ)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Generate Quiz", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Summary Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Summarize, contentDescription = null, tint = CyanAccent)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI Document Summary", fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = summary?.summaryText ?: "Analyzing document contents...",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                // Key Takeaways List
                if (summary != null && summary.keyPoints.isNotEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Key Core Concepts", fontWeight = FontWeight.Bold, color = TextPrimary)
                                Spacer(modifier = Modifier.height(10.dp))
                                summary.keyPoints.forEachIndexed { idx, pt ->
                                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                        Text("${idx + 1}. ", color = CyanAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(pt, color = TextSecondary, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Flashcard Interactive Deck
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Style, contentDescription = null, tint = EmeraldLight)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Interactive Flashcards", fontWeight = FontWeight.Bold, color = TextPrimary)
                                }
                                BadgePill(text = "5 Cards", color = EmeraldSuccess)
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            val cards = summary?.flashcardsJson?.split(";;")?.filter { it.contains("::") } ?: emptyList()
                            if (cards.isEmpty()) {
                                Text("Flashcards generated automatically upon file inspection.", color = TextMuted, fontSize = 12.sp)
                            } else {
                                cards.take(3).forEachIndexed { idx, pair ->
                                    val parts = pair.split("::")
                                    var revealed by remember { mutableStateOf(false) }

                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = DarkBgCanvas),
                                        border = BorderStroke(1.dp, DarkBorder),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable { revealed = !revealed }
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("Q${idx + 1}: ${parts[0]}", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 12.sp)
                                                Text(if (revealed) "Tap to hide" else "Tap for answer", color = CyanLight, fontSize = 10.sp)
                                            }
                                            if (revealed) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text("Answer: ${parts[1]}", color = EmeraldLight, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Main File Explorer View
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
            ) {
                // Header with Upload Button
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Study Material Library",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${filteredFiles.size} documents indexed",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        Button(
                            onClick = { showUploadDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("upload_file_button")
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search lecture notes, books, topics...", color = TextMuted) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkSurfaceCard,
                            unfocusedContainerColor = DarkSurfaceCard
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // File Type Filter Pills
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(fileTypes) { type ->
                            val isSelected = type == filterType
                            FilterChip(
                                selected = isSelected,
                                onClick = { filterType = type },
                                label = { Text(type, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanDark,
                                    selectedLabelColor = Color.White,
                                    containerColor = DarkSurfaceElevated,
                                    labelColor = TextSecondary
                                ),
                                border = BorderStroke(1.dp, if (isSelected) CyanLight else DarkBorder)
                            )
                        }
                    }
                }

                // Document Cards
                if (filteredFiles.isEmpty()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp)
                        ) {
                            Icon(Icons.Default.FolderOpen, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No study documents found", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                            Text("Upload PDF, PPT, or DOC notes to start AI summarization", color = TextMuted, fontSize = 12.sp)
                        }
                    }
                } else {
                    items(filteredFiles) { file ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectFile(file) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when (file.fileType) {
                                                "PDF" -> RoseError.copy(alpha = 0.2f)
                                                "PPT" -> AmberWarning.copy(alpha = 0.2f)
                                                "DOC" -> CyanAccent.copy(alpha = 0.2f)
                                                else -> EmeraldSuccess.copy(alpha = 0.2f)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (file.fileType) {
                                            "PDF" -> Icons.Default.PictureAsPdf
                                            "PPT" -> Icons.Default.Slideshow
                                            "DOC" -> Icons.Default.Description
                                            else -> Icons.AutoMirrored.Filled.Article
                                        },
                                        contentDescription = null,
                                        tint = when (file.fileType) {
                                            "PDF" -> RoseError
                                            "PPT" -> AmberWarning
                                            "DOC" -> CyanLight
                                            else -> EmeraldLight
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = file.fileName,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${file.subject} • ${file.fileSizeKb} KB",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    BadgePill(text = "Analyzed", color = EmeraldSuccess)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Upload New File Modal
    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = { Text("Upload Study Material", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newFileName,
                        onValueChange = { newFileName = it },
                        label = { Text("File Name (e.g., Computer_Vision_Lecture.pdf)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newSubject,
                        onValueChange = { newSubject = it },
                        label = { Text("Subject / Course") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("PDF", "PPT", "DOC", "TXT").forEach { t ->
                            FilterChip(
                                selected = newFileType == t,
                                onClick = { newFileType = t },
                                label = { Text(t, fontSize = 11.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = newDesc,
                        onValueChange = { newDesc = it },
                        label = { Text("Brief notes / description") },
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
                        val name = if (newFileName.isNotBlank()) newFileName else "Lecture_Notes_${System.currentTimeMillis() % 1000}.${newFileType.lowercase()}"
                        val desc = if (newDesc.isNotBlank()) newDesc else "Lecture document covering theoretical and practical foundations."
                        viewModel.uploadStudyFile(name, newFileType, newSubject, desc, desc)
                        showUploadDialog = false
                        newFileName = ""
                        newDesc = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Upload & Analyze")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }
}
