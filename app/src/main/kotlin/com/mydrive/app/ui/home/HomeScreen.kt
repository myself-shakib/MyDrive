package com.mydrive.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mydrive.app.data.model.ConnectionStatus
import com.mydrive.app.ui.components.AppCard
import com.mydrive.app.ui.components.MediaThumb
import com.mydrive.app.ui.components.PrimaryActionButton
import com.mydrive.app.ui.components.SectionHeader
import com.mydrive.app.ui.components.StatCard
import com.mydrive.app.ui.components.SyncStatusChip
import com.mydrive.app.ui.theme.Copper
import com.mydrive.app.ui.theme.Graphite
import com.mydrive.app.ui.theme.Ink
import com.mydrive.app.ui.theme.Ivory
import com.mydrive.app.ui.theme.IvoryMuted
import com.mydrive.app.ui.theme.Mist
import com.mydrive.app.ui.theme.Sage
import com.mydrive.app.ui.theme.Spacing
import com.mydrive.app.ui.theme.StatusAttention
import com.mydrive.app.ui.theme.Stroke
import com.mydrive.app.ui.util.formatTimeAgo
import com.mydrive.app.ui.util.greetingForHour

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSeeAll: () -> Unit,
    onMediaClick: (String) -> Unit,
    onBackupNow: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Ink),
        contentPadding = PaddingValues(
            start = Spacing.md,
            end = Spacing.md,
            top = Spacing.md,
            bottom = Spacing.lg
        ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        item(span = { GridItemSpan(2) }) {
            HomeHeader(
                greeting = greetingForHour(),
                name = state.profile.name
            )
        }
        item(span = { GridItemSpan(2) }) {
            BackupStatusCard(
                headline = state.overview.headline,
                description = state.overview.description,
                lastSync = state.overview.lastSyncLabel,
                telegramConnected = state.overview.telegramConnected,
                status = state.overview.status,
                progress = state.overview.progress
            )
        }
        item(span = { GridItemSpan(2) }) {
            SectionHeader(title = "Today")
        }
        item {
            StatCard(label = "Photos backed up", value = state.stats.photosBackedUp.toString(), accent = Copper)
        }
        item {
            StatCard(label = "Videos backed up", value = state.stats.videosBackedUp.toString(), accent = Sage)
        }
        item {
            StatCard(label = "Pending", value = state.stats.pending.toString(), accent = IvoryMuted)
        }
        item {
            StatCard(label = "Failed", value = state.stats.failed.toString(), accent = StatusAttention)
        }
        item(span = { GridItemSpan(2) }) {
            SectionHeader(title = "Recent Media", actionLabel = "See all", onAction = onSeeAll)
        }
        items(state.recentMedia, key = { it.id }) { item ->
            MediaThumb(item = item, onClick = { onMediaClick(item.id) })
        }
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(Spacing.xs))
            SectionHeader(title = "Recent Activity")
        }
        item(span = { GridItemSpan(2) }) {
            AppCard {
                state.activity.forEachIndexed { index, event ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Copper)
                        )
                        Spacer(Modifier.width(Spacing.sm))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(event.title, style = MaterialTheme.typography.bodyMedium, color = Ivory)
                            Text(
                                formatTimeAgo(event.timestampMillis),
                                style = MaterialTheme.typography.labelSmall,
                                color = Mist
                            )
                        }
                    }
                    if (index != state.activity.lastIndex) {
                        Spacer(Modifier.height(Spacing.md))
                    }
                }
            }
        }
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(Spacing.sm))
            PrimaryActionButton(
                text = "Backup Now",
                onClick = onBackupNow,
                icon = Icons.Outlined.Backup,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun HomeHeader(greeting: String, name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(greeting, style = MaterialTheme.typography.bodyMedium, color = Mist)
            Text(name, style = MaterialTheme.typography.headlineMedium, color = Ivory, fontWeight = FontWeight.SemiBold)
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Graphite)
                .border(1.dp, Stroke, CircleShape)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.NotificationsNone, contentDescription = "Notifications", tint = Ivory)
        }
        Spacer(Modifier.width(Spacing.sm))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Copper),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(1),
                style = MaterialTheme.typography.titleMedium,
                color = Ink,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BackupStatusCard(
    headline: String,
    description: String,
    lastSync: String,
    telegramConnected: Boolean,
    status: ConnectionStatus,
    progress: Float?
) {
    val chipLabel = when (status) {
        ConnectionStatus.CONNECTED -> "Connected"
        ConnectionStatus.SYNCING -> "Syncing"
        ConnectionStatus.ATTENTION -> "Attention needed"
    }
    AppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Backup Status",
                style = MaterialTheme.typography.labelLarge,
                color = Mist,
                modifier = Modifier.weight(1f)
            )
            SyncStatusChip(status = status, label = chipLabel)
        }
        Spacer(Modifier.height(Spacing.md))
        Text(headline, style = MaterialTheme.typography.headlineMedium, color = Ivory)
        Spacer(Modifier.height(Spacing.xxs))
        Text(description, style = MaterialTheme.typography.bodyMedium, color = IvoryMuted)
        Spacer(Modifier.height(Spacing.sm))
        Text(lastSync, style = MaterialTheme.typography.labelMedium, color = Mist)
        if (progress != null) {
            Spacer(Modifier.height(Spacing.sm))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape),
                color = Copper,
                trackColor = Ink.copy(alpha = 0.4f)
            )
        }
        Spacer(Modifier.height(Spacing.md))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(if (telegramConnected) Sage else Mist)
            )
            Spacer(Modifier.width(Spacing.xs))
            Text(
                text = if (telegramConnected) "Telegram · Connected" else "Telegram · Not connected",
                style = MaterialTheme.typography.labelMedium,
                color = if (telegramConnected) Sage else Mist
            )
        }
    }
}
