package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.Device
import com.example.models.Method
import com.example.models.devices
import com.example.models.methods
import com.example.ui.theme.*

@Composable
fun SplitScreenGuideScreen() {
    var activeDeviceId by remember { mutableStateOf(devices.first().id) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Ambient Grid Background
        GridBackground()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 32.dp, bottom = 60.dp)
            ) {
                item {
                    Header()
                    Spacer(modifier = Modifier.height(36.dp))
                }

                item {
                    DeviceTabs(
                        activeId = activeDeviceId,
                        onTabSelected = { activeDeviceId = it }
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                }

                val currentMethods = methods[activeDeviceId] ?: emptyList()
                itemsIndexed(currentMethods) { index, method ->
                    MethodCard(method)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Footer()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "TAP ANY CARD TO EXPAND · SWITCH DEVICE TABS ABOVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = CosmicTextTertiary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}

@Composable
fun Header() {
    Column {
        Text(
            text = "◈ SYSTEM :: MULTI-WINDOW GUIDE",
            style = MaterialTheme.typography.labelSmall,
            color = CosmicGreen,
            letterSpacing = 4.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Split Screen &\nAI Flow Control",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    listOf(CosmicTextPrimary, CosmicGreen)
                )
            ),
            lineHeight = 42.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Stop tab-switching. Start flowing. Choose your device below.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic
            ),
            color = CosmicTextSecondary,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
fun DeviceTabs(activeId: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CosmicSurface, RoundedCornerShape(8.dp))
            .border(1.dp, CosmicBorder, RoundedCornerShape(8.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        devices.forEach { device ->
            val isActive = activeId == device.id
            val backgroundColor by animateColorAsState(
                if (isActive) CosmicGreen else Color.Transparent,
                label = "tabBackground"
            )
            val contentColor by animateColorAsState(
                if (isActive) CosmicBackground else CosmicTextTertiary,
                label = "tabContent"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(backgroundColor)
                    .clickable { onTabSelected(device.id) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = device.icon, fontSize = 18.sp)
                    Text(
                        text = device.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MethodCard(method: Method) {
    var expanded by remember { mutableStateOf(false) }
    val badgeColor = Color(android.graphics.Color.parseColor(method.badgeColorHex))
    
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = if (expanded) badgeColor.copy(alpha = 0.33f) else CosmicBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { expanded = !expanded }
            .animateContentSize()
            .testTag("method_card_${method.title.replace(" ", "_").lowercase()}"),
        colors = CardDefaults.cardColors(
            containerColor = if (expanded) CosmicSurface else CosmicSurfaceVariant
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp, 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                        .shadow(elevation = 8.dp, shape = CircleShape, ambientColor = badgeColor, spotColor = badgeColor)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = method.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Serif),
                            color = CosmicTextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier
                                .border(1.dp, badgeColor, RoundedCornerShape(3.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = method.badge,
                                style = MaterialTheme.typography.labelSmall,
                                color = badgeColor,
                                letterSpacing = 2.sp,
                                fontSize = 9.sp
                            )
                        }
                    }
                    Text(
                        text = method.shortcut,
                        style = MaterialTheme.typography.labelSmall,
                        color = CosmicTextTertiary,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color(0xFF444444),
                    modifier = Modifier.rotate(rotationState)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), color = CosmicBorder)
                    
                    method.steps.forEachIndexed { index, step ->
                        Row(
                            modifier = Modifier.padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(CosmicSurfaceVariant)
                                    .border(1.dp, badgeColor.copy(alpha = 0.27f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    color = badgeColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                color = CosmicTextAction,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CosmicBackground, RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                            .border(width = 1.dp, color = badgeColor.copy(alpha = 0.2f), shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                            .drawBehindBorderLeft(badgeColor, 3.dp)
                            .padding(10.dp, 14.dp)
                    ) {
                        Column {
                            Text(
                                text = "◈ SHORTCUT NOTE",
                                style = MaterialTheme.typography.labelSmall,
                                color = badgeColor,
                                letterSpacing = 2.sp,
                                fontSize = 9.sp
                            )
                            Text(
                                text = method.shortcutNote,
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextTertiary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CosmicSurfaceVariant, RoundedCornerShape(6.dp))
                            .padding(10.dp, 14.dp)
                    ) {
                        Text(
                            text = "💡 ${method.tip}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic
                            ),
                            color = CosmicBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Footer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(CosmicSurface, Color(0xFF111620))),
                RoundedCornerShape(10.dp)
            )
            .border(1.dp, CosmicBorder, RoundedCornerShape(10.dp))
            .padding(20.dp, 24.dp)
    ) {
        Text(
            text = "◈ RECOMMENDED NEXT STEP",
            style = MaterialTheme.typography.labelSmall,
            color = CosmicGreen,
            letterSpacing = 3.sp
        )
        Text(
            text = "Your fastest win: Chromebook snap (Alt+[) for immediate split screen.\nYour highest-leverage upgrade: a Dual-AI Prompt Router — one input box, two AI outputs side by side, no tab switching ever again. Ask me to build it.",
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Serif),
            color = CosmicTextSecondary,
            modifier = Modifier.padding(top = 8.dp),
            lineHeight = 22.sp
        )
    }
}

fun Modifier.drawBehindBorderLeft(color: Color, width: Dp) = this.drawBehind {
    val strokeWidth = width.toPx()
    drawLine(
        color = color,
        start = Offset(strokeWidth / 2, 0f),
        end = Offset(strokeWidth / 2, size.height),
        strokeWidth = strokeWidth
    )
}
