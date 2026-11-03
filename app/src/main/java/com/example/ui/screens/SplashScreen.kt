package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinish: () -> Unit,
    durationMs: Long = 5000L
) {
    var elapsedMs by remember { mutableLongStateOf(0L) }

    val statusMessages = listOf(
        "Booting AI Study Engine & Neural Core...",
        "Connecting Knowledge Graph & Syllabi...",
        "Configuring Interactive Code Lab & Compilers...",
        "Calibrating Security Tokens & Readying Login..."
    )

    LaunchedEffect(Unit) {
        val step = 50L
        while (elapsedMs < durationMs) {
            delay(step)
            elapsedMs += step
        }
        onFinish()
    }

    val progress = (elapsedMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
    val secondsLeft = ((durationMs - elapsedMs + 999) / 1000).coerceAtLeast(1)
    val stageIndex = ((progress * statusMessages.size).toInt()).coerceIn(0, statusMessages.size - 1)

    // Pulsing animation for outer logo ring
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

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
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Floating Frosted Glass Card
        Surface(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .widthIn(max = 440.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White.copy(alpha = 0.82f),
            border = BorderStroke(1.5.dp, Color.White),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Central Glowing AI-SA Emblem
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    // Outer ambient blur glow
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        LavenderPrimary.copy(alpha = 0.45f),
                                        ChartreusePrimary.copy(alpha = 0.25f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Central Badge
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(LavenderDeep, LavenderPrimary)
                                )
                            )
                            .border(2.dp, Color.White, RoundedCornerShape(22.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "AI-SA",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "COPILOT",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChartreusePrimary,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                // App Title
                Text(
                    text = "AI Study Assistant",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = LightTextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Smart Full-Stack Learning System",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = LightTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                )

                // 5-Second Countdown Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LavenderPrimary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = LavenderDeep,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Loading workspace (${secondsLeft}s left)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LavenderDeep
                        )
                    }
                }

                // Progress Bar with Percentage
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = LavenderDeep,
                    trackColor = LavenderPrimary.copy(alpha = 0.2f),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "System Initializing",
                        fontSize = 11.sp,
                        color = LightTextMuted
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LavenderDeep
                    )
                }

                // Status Message Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.7f),
                    border = BorderStroke(1.dp, Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                ) {
                    Text(
                        text = statusMessages[stageIndex],
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LightTextPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }

                // Footnote
                Text(
                    text = "5.0s Security & Local Database Preparation",
                    fontSize = 10.sp,
                    color = LightTextMuted,
                    modifier = Modifier.padding(top = 14.dp)
                )
            }
        }
    }
}
