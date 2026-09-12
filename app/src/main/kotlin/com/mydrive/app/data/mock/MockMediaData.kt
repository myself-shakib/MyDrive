package com.mydrive.app.data.mock

import com.mydrive.app.data.model.ActivityEvent
import com.mydrive.app.data.model.BackupOverview
import com.mydrive.app.data.model.BackupPreferences
import com.mydrive.app.data.model.BackupState
import com.mydrive.app.data.model.ConnectionStatus
import com.mydrive.app.data.model.MediaItem
import com.mydrive.app.data.model.MediaType
import com.mydrive.app.data.model.StorageSummary
import com.mydrive.app.data.model.SyncSummary
import com.mydrive.app.data.model.TelegramSettings
import com.mydrive.app.data.model.TodayStats
import com.mydrive.app.data.model.UserProfile

object MockMediaData {

    private const val DAY_MS = 24L * 60L * 60L * 1000L
    private val now = System.currentTimeMillis()

    val profile = UserProfile(
        name = "Elena Voss",
        email = "elena@atelier.local",
        accountStatus = "Active"
    )

    val backupOverview = BackupOverview(
        status = ConnectionStatus.CONNECTED,
        headline = "All caught up",
        description = "Your recent media is safely backed up",
        lastSyncLabel = "Last sync 12 min ago",
        telegramConnected = true,
        progress = null
    )

    val todayStats = TodayStats(
        photosBackedUp = 18,
        videosBackedUp = 3,
        pending = 2,
        failed = 1
    )

    val telegramSettings = TelegramSettings(
        connected = true,
        botTokenMasked = "••••••••••••••••••",
        chatId = "48291037"
    )

    val backupPreferences = BackupPreferences(
        automaticBackup = true,
        backupPhotos = true,
        backupVideos = true,
        wifiOnly = true,
        uploadWhileCharging = false
    )

    val storageSummary = StorageSummary(
        totalMedia = 248,
        photos = 211,
        videos = 37,
        pendingUploads = 2
    )

    val syncSummary = SyncSummary(
        inProgressCount = 3,
        completedToday = 12
    )

    val mediaItems: List<MediaItem> = listOf(
        MediaItem(
            id = "m1",
            filename = "IMG_20260912_183421.jpg",
            type = MediaType.PHOTO,
            fileSizeBytes = 4_812_400,
            capturedAtMillis = now - 40 * 60 * 1000,
            device = "Pixel 8",
            resolution = "4080 x 3072",
            isFavorite = true,
            backupState = BackupState.COMPLETED,
            thumbnailSeed = 12
        ),
        MediaItem(
            id = "m2",
            filename = "VID_20260912_171102.mp4",
            type = MediaType.VIDEO,
            fileSizeBytes = 48_220_000,
            capturedAtMillis = now - 2 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "1920 x 1080",
            durationSeconds = 46,
            backupState = BackupState.SENDING_TELEGRAM,
            cloudBackupCompleted = true,
            telegramCompleted = false,
            thumbnailSeed = 31,
            progress = 0.72f
        ),
        MediaItem(
            id = "m3",
            filename = "IMG_20260912_142210.jpg",
            type = MediaType.PHOTO,
            fileSizeBytes = 3_240_800,
            capturedAtMillis = now - 5 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "4000 x 3000",
            backupState = BackupState.COMPLETED,
            thumbnailSeed = 7
        ),
        MediaItem(
            id = "m4",
            filename = "IMG_20260911_214455.jpg",
            type = MediaType.PHOTO,
            fileSizeBytes = 2_980_100,
            capturedAtMillis = now - DAY_MS - 90 * 60 * 1000,
            device = "Pixel 8",
            resolution = "4032 x 3024",
            isFavorite = true,
            backupState = BackupState.COMPLETED,
            thumbnailSeed = 44
        ),
        MediaItem(
            id = "m5",
            filename = "VID_20260911_193012.mp4",
            type = MediaType.VIDEO,
            fileSizeBytes = 92_440_000,
            capturedAtMillis = now - DAY_MS - 4 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "3840 x 2160",
            durationSeconds = 128,
            backupState = BackupState.COMPLETED,
            thumbnailSeed = 19
        ),
        MediaItem(
            id = "m6",
            filename = "IMG_20260911_091133.jpg",
            type = MediaType.PHOTO,
            fileSizeBytes = 5_110_200,
            capturedAtMillis = now - DAY_MS - 10 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "4080 x 3072",
            backupState = BackupState.WAITING,
            cloudBackupCompleted = false,
            telegramCompleted = false,
            thumbnailSeed = 52,
            progress = 0f
        ),
        MediaItem(
            id = "m7",
            filename = "IMG_20260910_180021.jpg",
            type = MediaType.PHOTO,
            fileSizeBytes = 3_660_000,
            capturedAtMillis = now - 2 * DAY_MS - 2 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "4000 x 3000",
            backupState = BackupState.COMPLETED,
            thumbnailSeed = 27
        ),
        MediaItem(
            id = "m8",
            filename = "VID_20260910_154410.mp4",
            type = MediaType.VIDEO,
            fileSizeBytes = 31_200_000,
            capturedAtMillis = now - 2 * DAY_MS - 5 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "1920 x 1080",
            durationSeconds = 22,
            isFavorite = true,
            backupState = BackupState.COMPLETED,
            thumbnailSeed = 61
        ),
        MediaItem(
            id = "m9",
            filename = "IMG_20260910_091802.jpg",
            type = MediaType.PHOTO,
            fileSizeBytes = 4_010_500,
            capturedAtMillis = now - 2 * DAY_MS - 11 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "4032 x 3024",
            backupState = BackupState.FAILED,
            cloudBackupCompleted = false,
            telegramCompleted = false,
            thumbnailSeed = 9,
            errorMessage = "Upload interrupted"
        ),
        MediaItem(
            id = "m10",
            filename = "IMG_20260912_090014.jpg",
            type = MediaType.PHOTO,
            fileSizeBytes = 2_440_000,
            capturedAtMillis = now - 9 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "3024 x 4032",
            backupState = BackupState.UPLOADING,
            cloudBackupCompleted = false,
            telegramCompleted = false,
            thumbnailSeed = 38,
            progress = 0.41f
        ),
        MediaItem(
            id = "m11",
            filename = "IMG_20260909_201155.jpg",
            type = MediaType.PHOTO,
            fileSizeBytes = 3_880_300,
            capturedAtMillis = now - 3 * DAY_MS - 1 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "4080 x 3072",
            isFavorite = true,
            backupState = BackupState.COMPLETED,
            thumbnailSeed = 73
        ),
        MediaItem(
            id = "m12",
            filename = "VID_20260912_112233.mp4",
            type = MediaType.VIDEO,
            fileSizeBytes = 18_900_000,
            capturedAtMillis = now - 7 * 60 * 60 * 1000,
            device = "Pixel 8",
            resolution = "1920 x 1080",
            durationSeconds = 14,
            backupState = BackupState.PROCESSING,
            cloudBackupCompleted = false,
            telegramCompleted = false,
            thumbnailSeed = 15,
            progress = 0.58f
        )
    )

    val recentActivity: List<ActivityEvent> = listOf(
        ActivityEvent("a1", "12 photos backed up", now - 18 * 60 * 1000),
        ActivityEvent("a2", "Video backup completed", now - 42 * 60 * 1000),
        ActivityEvent("a3", "Telegram sync completed", now - 55 * 60 * 1000),
        ActivityEvent("a4", "2 files waiting for upload", now - 80 * 60 * 1000)
    )
}
