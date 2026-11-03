package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: AppScreen,
    userRole: UserRole,
    adminCode: String?,
    unreadNotifCount: Int,
    onSearchClick: () -> Unit,
    onRoleSwitchClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBgCanvas,
            titleContentColor = TextPrimary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                // AI-SA Hexagonal Brand Emblem
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(IndigoPrimary, CyanAccent)
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AI-SA",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = currentScreen.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "AI Study Assistant",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        },
        actions = {
            // Role Pill Indicator
            Surface(
                onClick = onRoleSwitchClick,
                shape = RoundedCornerShape(16.dp),
                color = if (userRole == UserRole.ADMIN) IndigoDark.copy(alpha = 0.4f) else DarkSurfaceElevated,
                border = BorderStroke(
                    1.dp,
                    if (userRole == UserRole.ADMIN) IndigoLight else CyanAccent.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .testTag("role_badge_pill")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (userRole == UserRole.ADMIN) IndigoLight else EmeraldSuccess)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (userRole == UserRole.ADMIN) (adminCode ?: "ADMIN") else "Student",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (userRole == UserRole.ADMIN) IndigoLight else EmeraldLight
                    )
                }
            }

            // Search Icon
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.testTag("top_search_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary
                )
            }

            // Notification Bell with Badge
            BadgedBox(
                badge = {
                    if (unreadNotifCount > 0) {
                        Badge(containerColor = CyanAccent, contentColor = Color.Black) {
                            Text(text = "$unreadNotifCount")
                        }
                    }
                },
                modifier = Modifier.padding(end = 6.dp)
            ) {
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.testTag("top_notif_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = TextSecondary
                    )
                }
            }
        }
    )
}

@Composable
fun AppBottomNav(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit,
    onOpenMoreMenu: () -> Unit
) {
    NavigationBar(
        containerColor = DarkSurfaceCard,
        tonalElevation = 6.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.testTag("bottom_navigation_bar")
    ) {
        val items = listOf(
            Triple(AppScreen.HOME, Icons.Default.Home, Icons.Outlined.Home),
            Triple(AppScreen.AI_CHAT, Icons.Default.AutoAwesome, Icons.Outlined.AutoAwesome),
            Triple(AppScreen.FILES, Icons.Default.Folder, Icons.Outlined.Folder),
            Triple(AppScreen.CODE_LAB, Icons.Default.Terminal, Icons.Outlined.Terminal),
            Triple(AppScreen.QUIZ, Icons.Default.Psychology, Icons.Outlined.Psychology)
        )

        items.forEach { (screen, activeIcon, inactiveIcon) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectScreen(screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) activeIcon else inactiveIcon,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(
                        text = when (screen) {
                            AppScreen.HOME -> "Home"
                            AppScreen.AI_CHAT -> "AI Chat"
                            AppScreen.FILES -> "Files"
                            AppScreen.CODE_LAB -> "Code Lab"
                            AppScreen.QUIZ -> "Quizzes"
                            else -> screen.title
                        },
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = IndigoLight,
                    indicatorColor = IndigoPrimary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }

        // "More" item to access Tests, Hackathons, Planner, Admin, Profile
        val isMoreSelected = currentScreen in listOf(
            AppScreen.TESTS,
            AppScreen.HACKATHONS,
            AppScreen.PLANNER,
            AppScreen.ADMIN_DASHBOARD,
            AppScreen.PROFILE
        )
        NavigationBarItem(
            selected = isMoreSelected,
            onClick = onOpenMoreMenu,
            icon = {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = "More"
                )
            },
            label = {
                Text(
                    text = "More",
                    fontSize = 11.sp,
                    fontWeight = if (isMoreSelected) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = CyanLight,
                indicatorColor = CyanDark,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )
    }
}

@Composable
fun AppNavigationRail(
    currentScreen: AppScreen,
    userRole: UserRole,
    onSelectScreen: (AppScreen) -> Unit
) {
    NavigationRail(
        containerColor = DarkSurfaceCard,
        contentColor = TextPrimary,
        header = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(listOf(IndigoPrimary, CyanAccent))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AI-SA",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        modifier = Modifier.fillMaxHeight()
    ) {
        val navItems = listOf(
            Triple(AppScreen.HOME, Icons.Default.Home, "Home"),
            Triple(AppScreen.AI_CHAT, Icons.Default.AutoAwesome, "Chat"),
            Triple(AppScreen.FILES, Icons.Default.Folder, "Files"),
            Triple(AppScreen.CODE_LAB, Icons.Default.Terminal, "Code"),
            Triple(AppScreen.QUIZ, Icons.Default.Psychology, "Quiz"),
            Triple(AppScreen.TESTS, Icons.AutoMirrored.Filled.Assignment, "Tests"),
            Triple(AppScreen.HACKATHONS, Icons.Default.EmojiEvents, "Hacks"),
            Triple(AppScreen.PLANNER, Icons.Default.CalendarToday, "Plan"),
            Triple(AppScreen.ADMIN_DASHBOARD, Icons.Default.AdminPanelSettings, "Admin")
        )

        navItems.forEach { (screen, icon, label) ->
            NavigationRailItem(
                selected = currentScreen == screen,
                onClick = { onSelectScreen(screen) },
                icon = { Icon(imageVector = icon, contentDescription = label) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = Color.White,
                    indicatorColor = IndigoPrimary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, DarkBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun BadgePill(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
