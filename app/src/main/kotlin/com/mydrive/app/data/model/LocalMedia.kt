package com.mydrive.app.data.model

import android.net.Uri

data class LocalMediaItem(
    val id: Long,
    val contentUri: Uri,
    val displayName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val dateTakenMillis: Long,
    val dateModifiedMillis: Long,
    val width: Int,
    val height: Int,
    val durationMillis: Long? = null,
    val type: MediaType,
    val isFavorite: Boolean = false
)

fun LocalMediaItem.mediaItemId(): String = "${type.name.lowercase()}-$id"

fun LocalMediaItem.toMediaItem(): MediaItem {
    val durationSeconds = durationMillis
        ?.div(1000L)
        ?.toInt()
        ?.takeIf { type == MediaType.VIDEO && it > 0 }
    val resolution = if (width > 0 && height > 0) "$width x $height" else "Unknown"
    return MediaItem(
        id = mediaItemId(),
        filename = displayName,
        type = type,
        fileSizeBytes = sizeBytes,
        capturedAtMillis = dateTakenMillis,
        device = "This device",
        resolution = resolution,
        durationSeconds = durationSeconds,
        isFavorite = isFavorite,
        backupState = BackupState.WAITING,
        cloudBackupCompleted = false,
        telegramCompleted = false,
        thumbnailSeed = (id % Int.MAX_VALUE).toInt(),
        contentUri = contentUri
    )
}
