package com.neuroos.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    onMenuClick: (() -> Unit)? = null,
    elevation: Int = 1, // 0: Surface, 1: Elevated, 2: Interactive
    content: @Composable ColumnScope.() -> Unit
) {
    val bgColor = when(elevation) {
        2 -> MaterialTheme.colorScheme.secondaryContainer
        1 -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surface
    }
    
    Surface(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        tonalElevation = (elevation * 4).dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (title != null || onMenuClick != null) {
                Row(
                    Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (title != null) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (onMenuClick != null) {
                        IconButton(onClick = onMenuClick, modifier = Modifier.size(48.dp)) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
            content()
        }
    }
}

@Composable
fun CyberGauge(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    valueText: String = "${(progress * 100).roundToInt()}",
    subValueText: String = "/100",
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 100.dp
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.size(size).semantics {
        contentDescription = "$label: $valueText$subValueText"
    }) {
        CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxSize(),
            color = color,
            strokeWidth = 8.dp,
            trackColor = color.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = valueText,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Black
            )
            Text(text = subValueText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun CyberLinearProgress(
    label: String,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.width(70.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CyberStatusPill(
    text: String,
    isActive: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (isActive) color else Color.Gray)
        )
        Text(
            text = text.uppercase(),
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun CyberBackgroundPattern(modifier: Modifier = Modifier, backgroundRes: Int? = null) {
    val primary = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
    val secondary = MaterialTheme.colorScheme.secondary.copy(alpha = 0.04f)
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    
    Box(modifier = modifier) {
        if (backgroundRes != null) {
            Image(
                painter = androidx.compose.ui.res.painterResource(id = backgroundRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(if (isDark) 0.03f else 0.02f),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        }
        
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 1.5.dp.toPx()
            
            if (isDark) {
                // Afterglow/Dark Mode: Abstract geometric linework
                drawLine(primary, androidx.compose.ui.geometry.Offset(size.width * 0.1f, 0f), androidx.compose.ui.geometry.Offset(size.width * 0.1f, size.height * 0.15f), strokeWidth)
                drawLine(primary, androidx.compose.ui.geometry.Offset(size.width * 0.1f, size.height * 0.15f), androidx.compose.ui.geometry.Offset(0f, size.height * 0.25f), strokeWidth)
                
                drawLine(secondary, androidx.compose.ui.geometry.Offset(size.width * 0.9f, size.height), androidx.compose.ui.geometry.Offset(size.width * 0.9f, size.height * 0.85f), strokeWidth)
                drawLine(secondary, androidx.compose.ui.geometry.Offset(size.width * 0.9f, size.height * 0.85f), androidx.compose.ui.geometry.Offset(size.width, size.height * 0.75f), strokeWidth)
                
                // Soft radial glow
                drawCircle(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(secondary.copy(alpha = 0.08f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(size.width * 0.8f, size.height * 0.2f),
                        radius = size.width * 0.6f
                    )
                )
            } else {
                // Daylight/Light Mode: Subtle geometric circuit-like lines
                drawLine(primary, androidx.compose.ui.geometry.Offset(size.width * 0.05f, 0f), androidx.compose.ui.geometry.Offset(size.width * 0.05f, size.height * 0.1f), strokeWidth)
                drawLine(primary, androidx.compose.ui.geometry.Offset(size.width * 0.05f, size.height * 0.1f), androidx.compose.ui.geometry.Offset(size.width * 0.15f, size.height * 0.15f), strokeWidth)
                
                drawLine(secondary, androidx.compose.ui.geometry.Offset(size.width * 0.95f, size.height), androidx.compose.ui.geometry.Offset(size.width * 0.95f, size.height * 0.9f), strokeWidth)
                drawLine(secondary, androidx.compose.ui.geometry.Offset(size.width * 0.95f, size.height * 0.9f), androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.85f), strokeWidth)
                
                // Soft gradient highlight
                drawCircle(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = 0.03f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(size.width * 0.2f, size.height * 0.8f),
                        radius = size.width * 0.5f
                    )
                )
            }
        }
    }
}
