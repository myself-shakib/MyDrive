package com.mydrive.app.data.repository

import com.mydrive.app.data.mock.MockMediaData
import com.mydrive.app.data.model.ActivityEvent
import com.mydrive.app.data.model.BackupOverview
import com.mydrive.app.data.model.BackupPreferences
import com.mydrive.app.data.model.MediaItem
import com.mydrive.app.data.model.StorageSummary
import com.mydrive.app.data.model.SyncSummary
import com.mydrive.app.data.model.TelegramSettings
import com.mydrive.app.data.model.TodayStats
import com.mydrive.app.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MediaRepository {

    private val _media = MutableStateFlow(MockMediaData.mediaItems)
    val media: StateFlow<List<MediaItem>> = _media.asStateFlow()

    private val _overview = MutableStateFlow(MockMediaData.backupOverview)
    val overview: StateFlow<BackupOverview> = _overview.asStateFlow()

    private val _todayStats = MutableStateFlow(MockMediaData.todayStats)
    val todayStats: StateFlow<TodayStats> = _todayStats.asStateFlow()

    private val _activity = MutableStateFlow(MockMediaData.recentActivity)
    val activity: StateFlow<List<ActivityEvent>> = _activity.asStateFlow()

    private val _profile = MutableStateFlow(MockMediaData.profile)
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    private val _preferences = MutableStateFlow(MockMediaData.backupPreferences)
    val preferences: StateFlow<BackupPreferences> = _preferences.asStateFlow()

    private val _telegram = MutableStateFlow(MockMediaData.telegramSettings)
    val telegram: StateFlow<TelegramSettings> = _telegram.asStateFlow()

    private val _storage = MutableStateFlow(MockMediaData.storageSummary)
    val storage: StateFlow<StorageSummary> = _storage.asStateFlow()

    private val _syncSummary = MutableStateFlow(MockMediaData.syncSummary)
    val syncSummary: StateFlow<SyncSummary> = _syncSummary.asStateFlow()

    fun mediaById(id: String): MediaItem? = _media.value.firstOrNull { it.id == id }

    fun toggleFavorite(id: String) {
        _media.update { items ->
            items.map { item ->
                if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
            }
        }
    }

    fun updatePreferences(transform: (BackupPreferences) -> BackupPreferences) {
        _preferences.update(transform)
    }

    fun disconnectTelegram() {
        _telegram.update { it.copy(connected = false, chatId = "") }
    }

    fun connectTelegram() {
        _telegram.update {
            it.copy(connected = true, chatId = if (it.chatId.isBlank()) "48291037" else it.chatId)
        }
    }
}
