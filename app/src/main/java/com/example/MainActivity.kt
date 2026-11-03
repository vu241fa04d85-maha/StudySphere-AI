package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppNavigationRail
import com.example.ui.components.AppTopBar
import com.example.ui.components.BadgePill
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppPhase
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudyViewModel
import com.example.ui.viewmodel.UserRole

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: StudyViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            AIStudyAssistantTheme(darkTheme = isDarkTheme) {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    viewModel: StudyViewModel = viewModel()
) {
    val appPhase by viewModel.appPhase.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val activeAdmin by viewModel.activeAdmin.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val allNotifications by viewModel.allNotifications.collectAsStateWithLifecycle()
    val cookieAccepted by viewModel.cookieConsentAccepted.collectAsStateWithLifecycle()

    // 1. Dedicated 5-Second Splash Loading Animation
    if (appPhase == AppPhase.SPLASH) {
        SplashScreen(
            onFinish = { viewModel.finishSplash() },
            durationMs = 5000L
        )
        return
    }

    // 2. Dedicated Auth Screen with Email/Password check, Sign-in/Sign-up, and Student/Admin switching
    if (appPhase == AppPhase.AUTH) {
        AuthScreen(
            viewModel = viewModel,
            onLoginSuccess = { viewModel.setAppPhase(AppPhase.MAIN) }
        )
        return
    }

    var showMoreSheet by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showNotifSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val unreadNotifs = allNotifications.count { !it.isRead }

    // Android Hardware/Gesture Back Navigation
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(DarkBgCanvas)) {
        val isExpandedLayout = maxWidth >= 600.dp

        Row(modifier = Modifier.fillMaxSize()) {
            // Tablet / Desktop Navigation Rail
            if (isExpandedLayout) {
                AppNavigationRail(
                    currentScreen = currentScreen,
                    userRole = userRole,
                    onSelectScreen = { screen ->
                        viewModel.navigateTo(screen)
                    }
                )
            }

            // Main Screen Scaffold
            Scaffold(
                containerColor = DarkBgCanvas,
                topBar = {
                    AppTopBar(
                        currentScreen = currentScreen,
                        userRole = userRole,
                        adminCode = activeAdmin?.adminCode,
                        unreadNotifCount = unreadNotifs,
                        onSearchClick = { showSearchDialog = true },
                        onRoleSwitchClick = { viewModel.navigateTo(AppScreen.PROFILE) },
                        onNotificationsClick = { showNotifSheet = true }
                    )
                },
                bottomBar = {
                    if (!isExpandedLayout) {
                        AppBottomNav(
                            currentScreen = currentScreen,
                            onSelectScreen = { screen ->
                                viewModel.navigateTo(screen)
                            },
                            onOpenMoreMenu = { showMoreSheet = true }
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentScreen) {
                        AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                        AppScreen.AI_CHAT -> AIChatScreen(viewModel = viewModel)
                        AppScreen.FILES -> FilesScreen(viewModel = viewModel)
                        AppScreen.CODE_LAB -> CodeLabScreen(viewModel = viewModel)
                        AppScreen.QUIZ -> QuizScreen(viewModel = viewModel)
                        AppScreen.TESTS -> TestsScreen(viewModel = viewModel)
                        AppScreen.HACKATHONS -> HackathonsScreen(viewModel = viewModel)
                        AppScreen.PLANNER -> PlannerScreen(viewModel = viewModel)
                        AppScreen.ADMIN_DASHBOARD -> AdminScreen(viewModel = viewModel)
                        AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                    }

                    // Floating Toast Banner
                    androidx.compose.animation.AnimatedVisibility(
                        visible = toastMessage != null,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        if (toastMessage != null) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = DarkSurfaceElevated,
                                border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
                                shadowElevation = 8.dp
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = CyanAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = toastMessage!!,
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    // Cookie & Privacy Consent Banner
                    if (!cookieAccepted) {
                        Surface(
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            color = DarkSurfaceCard,
                            border = BorderStroke(1.dp, DarkBorder),
                            shadowElevation = 12.dp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "🍪 We use cookies and local cache for session integrity and performance.",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { viewModel.acceptCookieConsent() },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Accept", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // "More" Tools Bottom Sheet
    if (showMoreSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMoreSheet = false },
            containerColor = DarkSurfaceCard,
            dragHandle = { BottomSheetDefaults.DragHandle(color = DarkBorder) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .navigationBarsPadding()
            ) {
                Text(
                    text = "AI Study Assistant Modules",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                val moreItems = listOf(
                    Triple(AppScreen.TESTS, Icons.AutoMirrored.Filled.Assignment, "Official Timed Tests"),
                    Triple(AppScreen.HACKATHONS, Icons.Default.EmojiEvents, "Campus Hackathons"),
                    Triple(AppScreen.PLANNER, Icons.Default.CalendarToday, "Study Planner & Tasks"),
                    Triple(AppScreen.ADMIN_DASHBOARD, Icons.Default.AdminPanelSettings, "Admin Portal (ADMIN001-050)"),
                    Triple(AppScreen.PROFILE, Icons.Default.Person, "My Profile & Role Switch")
                )

                moreItems.forEach { (screen, icon, title) ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                viewModel.navigateTo(screen)
                                showMoreSheet = false
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Icon(imageVector = icon, contentDescription = null, tint = CyanAccent)
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(text = title, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Global Search Modal
    if (showSearchDialog) {
        val allFiles by viewModel.allFiles.collectAsStateWithLifecycle()
        val allQuizzes by viewModel.allQuizzes.collectAsStateWithLifecycle()
        val allTests by viewModel.allTests.collectAsStateWithLifecycle()
        val allHackathons by viewModel.allHackathons.collectAsStateWithLifecycle()

        AlertDialog(
            onDismissRequest = {
                showSearchDialog = false
                searchQuery = ""
            },
            title = {
                Text("Search AI Study Assistant", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search files, quizzes, tests, hackathons...", color = TextMuted) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CyanAccent) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CyanAccent
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("global_search_input")
                    )

                    val matchingFiles = allFiles.filter { it.fileName.contains(searchQuery, true) || it.subject.contains(searchQuery, true) }
                    val matchingQuizzes = allQuizzes.filter { it.title.contains(searchQuery, true) }
                    val matchingTests = allTests.filter { it.title.contains(searchQuery, true) }

                    LazyColumn(modifier = Modifier.heightIn(max = 240.dp)) {
                        if (searchQuery.isNotBlank()) {
                            item {
                                Text("Search Results (${matchingFiles.size + matchingQuizzes.size + matchingTests.size})", color = CyanLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            items(matchingFiles) { f ->
                                Text(
                                    text = "📄 ${f.fileName} (${f.subject})",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.selectFile(f)
                                            viewModel.navigateTo(AppScreen.FILES)
                                            showSearchDialog = false
                                        }
                                        .padding(vertical = 4.dp)
                                )
                            }
                            items(matchingQuizzes) { q ->
                                Text(
                                    text = "📝 Quiz: ${q.title}",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.startQuiz(q)
                                            showSearchDialog = false
                                        }
                                        .padding(vertical = 4.dp)
                                )
                            }
                            items(matchingTests) { t ->
                                Text(
                                    text = "⏱️ Test: ${t.title}",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.startOfficialTest(t)
                                            showSearchDialog = false
                                        }
                                        .padding(vertical = 4.dp)
                                )
                            }
                        } else {
                            item {
                                Text("Type any keyword to search across the platform.", color = TextMuted, fontSize = 12.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showSearchDialog = false
                    searchQuery = ""
                }) {
                    Text("Close", color = CyanAccent)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }

    // Notifications Bottom Sheet
    if (showNotifSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNotifSheet = false },
            containerColor = DarkSurfaceCard,
            dragHandle = { BottomSheetDefaults.DragHandle(color = DarkBorder) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .navigationBarsPadding()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Notifications & Broadcasts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    BadgePill(text = "${allNotifications.size} Total", color = CyanAccent)
                }
                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 300.dp)) {
                    items(allNotifications) { notif ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                            border = BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(notif.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(notif.message, color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
