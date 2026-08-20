package com.neuroos.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class OverviewUiState(
    val focusScore: Int = 86,
    val cognitiveLoad: String = "Moderate",
    val sessionsToday: Int = 7,
    val neuroBalance: NeuroBalanceState = NeuroBalanceState(),
    val streakDays: Int = 12,
    val recentActivity: List<ActivityEntry> = emptyList()
)

data class NeuroBalanceState(
    val focus: Float = 0.8f,
    val memory: Float = 0.68f,
    val resilience: Float = 0.69f
)

data class ActivityEntry(
    val title: String,
    val duration: String,
    val time: String,
    val color: Color
)

@Composable
fun OverviewScreen(state: OverviewUiState = OverviewUiState()) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        val isWide = maxWidth > 600.dp
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isWide) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FocusScoreCard(score = state.focusScore, modifier = Modifier.weight(1f))
                    CognitiveLoadCard(loadLabel = state.cognitiveLoad, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SessionsCard(count = state.sessionsToday, modifier = Modifier.weight(1f))
                    NeuroBalanceCard(balance = state.neuroBalance, modifier = Modifier.weight(1.1f))
                    StreakCard(days = state.streakDays, modifier = Modifier.weight(0.7f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    RecentActivityCard(activities = state.recentActivity, modifier = Modifier.weight(1f))
                    InsightsCard(modifier = Modifier.weight(1f))
                }
            } else {
                // Stacked layout for compact widths (common on phones or folded foldables)
                FocusScoreCard(score = state.focusScore)
                CognitiveLoadCard(loadLabel = state.cognitiveLoad)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SessionsCard(count = state.sessionsToday, modifier = Modifier.weight(1f))
                    StreakCard(days = state.streakDays, modifier = Modifier.weight(1f))
                }
                NeuroBalanceCard(balance = state.neuroBalance)
                RecentActivityCard(activities = state.recentActivity)
                InsightsCard()
            }
        }
    }
}

@Composable
private fun FocusScoreCard(score: Int, modifier: Modifier = Modifier) {
    CyberCard(modifier = modifier, title = stringResource(R.string.neuro_focus_score), onMenuClick = {}) {
        CyberGauge(
            progress = score / 100f,
            label = stringResource(R.string.neuro_focus_score),
            valueText = score.toString(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Spacer(Modifier.height(16.dp))
        Text(
            text = "↑ 12% vs yesterday",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CognitiveLoadCard(loadLabel: String, modifier: Modifier = Modifier) {
    CyberCard(modifier = modifier, title = stringResource(R.string.neuro_cognitive_load), onMenuClick = {}) {
        Text(
            text = loadLabel,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black
        )
        Text(
            text = stringResource(R.string.neuro_optimal_range),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(20.dp))
        
        // Dynamic Waveform Placeholder
        val waveColor = MaterialTheme.colorScheme.secondary
        Canvas(modifier = Modifier.fillMaxWidth().height(44.dp)) {
            val width = size.width
            val height = size.height
            val points = 30
            val path = androidx.compose.ui.graphics.Path()
            path.moveTo(0f, height / 2)
            for (i in 1..points) {
                val x = i * (width / points)
                val y = height / 2 + (Math.sin(i.toDouble() * 0.7 + System.currentTimeMillis() * 0.002) * 12).toFloat()
                path.lineTo(x, y)
            }
            drawPath(
                path = path,
                color = waveColor,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun SessionsCard(count: Int, modifier: Modifier = Modifier) {
    CyberCard(modifier = modifier, title = stringResource(R.string.neuro_sessions)) {
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Today",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        // Session activity bars
        Row(
            Modifier.height(40.dp).fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            val heights = listOf(0.4f, 0.2f, 0.8f, 0.5f, 0.7f, 0.3f, 0.9f)
            heights.forEach { h ->
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(h)
                        .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                )
            }
        }
    }
}

@Composable
private fun NeuroBalanceCard(balance: NeuroBalanceState, modifier: Modifier = Modifier) {
    CyberCard(modifier = modifier, title = stringResource(R.string.neuro_neuro_balance)) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp).align(Alignment.CenterHorizontally)) {
            CircularProgressIndicator(
                progress = { (balance.focus + balance.memory + balance.resilience) / 3f },
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 6.dp,
                trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            Text(
                text = "${((balance.focus + balance.memory + balance.resilience) / 3f * 100).toInt()}%",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CyberLinearProgress("Focus", balance.focus, MaterialTheme.colorScheme.primary)
            CyberLinearProgress("Memory", balance.memory, MaterialTheme.colorScheme.secondary)
            CyberLinearProgress("Resilience", balance.resilience, MaterialTheme.colorScheme.tertiary)
        }
    }
}

@Composable
private fun StreakCard(days: Int, modifier: Modifier = Modifier) {
    CyberCard(modifier = modifier, title = stringResource(R.string.neuro_streak)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = days.toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Days",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecentActivityCard(activities: List<ActivityEntry>, modifier: Modifier = Modifier) {
    CyberCard(modifier = modifier, title = stringResource(R.string.neuro_recent_activity)) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            val list = if (activities.isEmpty()) {
                listOf(
                    ActivityEntry(stringResource(R.string.neuro_focus_training), "45 min", "10:42 AM", MaterialTheme.colorScheme.primary),
                    ActivityEntry(stringResource(R.string.neuro_memory_recall), "30 min", "09:15 AM", MaterialTheme.colorScheme.secondary),
                    ActivityEntry(stringResource(R.string.neuro_breathwork), "15 min", "Yesterday", MaterialTheme.colorScheme.primary)
                )
            } else activities
            
            list.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically, 
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(item.color))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = item.title, 
                            color = MaterialTheme.colorScheme.onSurface, 
                            fontSize = 13.sp, 
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(item.duration, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                    Text(
                        text = item.time, 
                        color = MaterialTheme.colorScheme.onSurfaceVariant, 
                        fontSize = 10.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightsCard(modifier: Modifier = Modifier) {
    CyberCard(modifier = modifier, title = stringResource(R.string.neuro_insights)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.neuro_focus_improving),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = stringResource(R.string.neuro_momentum_message),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
            TextButton(
                onClick = { /* Full report */ },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.neuro_view_full_report),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF070A12)
@Composable
fun OverviewPreview() {
    MaterialTheme {
        Box(Modifier.background(Color(0xFF070A12)).padding(16.dp)) {
            OverviewScreen()
        }
    }
}
