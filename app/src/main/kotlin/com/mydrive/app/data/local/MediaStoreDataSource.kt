package com.mydrive.app.data.local

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.mydrive.app.data.model.LocalMediaItem
import com.mydrive.app.data.model.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaStoreDataSource(context: Context) {

    private val resolver = context.applicationContext.contentResolver

    suspend fun queryAll(): List<LocalMediaItem> = withContext(Dispatchers.IO) {
        val photos = queryCollection(
            collection = imagesCollection(),
            type = MediaType.PHOTO,
            extraColumns = emptyArray()
        )
        val videos = queryCollection(
            collection = videosCollection(),
            type = MediaType.VIDEO,
            extraColumns = arrayOf(MediaStore.Video.Media.DURATION)
        )
        (photos + videos).sortedByDescending { it.dateTakenMillis }
    }

    private fun imagesCollection(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    private fun videosCollection(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
    }

    private fun queryCollection(
        collection: Uri,
        type: MediaType,
        extraColumns: Array<String>
    ): List<LocalMediaItem> {
        val projection = baseProjection + extraColumns
        val selection = pendingSelection()
        val items = mutableListOf<LocalMediaItem>()
        val cursor = try {
            resolver.query(
                collection,
                projection,
                selection,
                null,
                "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"
            )
        } catch (_: SecurityException) {
            null
        }
        cursor?.use {
            while (it.moveToNext()) {
                items += it.toLocalMediaItem(collection, type)
            }
        }
        return items
    }

    private fun pendingSelection(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${MediaStore.MediaColumns.IS_PENDING} = 0"
        } else {
            null
        }
    }

    private fun Cursor.toLocalMediaItem(collection: Uri, type: MediaType): LocalMediaItem {
        val id = longValue(MediaStore.MediaColumns._ID)
        val dateTaken = longValue(MediaStore.MediaColumns.DATE_TAKEN)
        val dateModifiedSeconds = longValue(MediaStore.MediaColumns.DATE_MODIFIED)
        val dateModifiedMillis = dateModifiedSeconds * 1000L
        val duration = if (type == MediaType.VIDEO) {
            longValue(MediaStore.Video.Media.DURATION).takeIf { it > 0L }
        } else {
            null
        }
        return LocalMediaItem(
            id = id,
            contentUri = ContentUris.withAppendedId(collection, id),
            displayName = stringValue(MediaStore.MediaColumns.DISPLAY_NAME)
                .ifBlank { "Media $id" },
            mimeType = stringValue(MediaStore.MediaColumns.MIME_TYPE).ifBlank {
                if (type == MediaType.VIDEO) "video/*" else "image/*"
            },
            sizeBytes = longValue(MediaStore.MediaColumns.SIZE),
            dateTakenMillis = when {
                dateTaken > 0L -> dateTaken
                dateModifiedMillis > 0L -> dateModifiedMillis
                else -> 0L
            },
            dateModifiedMillis = dateModifiedMillis,
            width = intValue(MediaStore.MediaColumns.WIDTH),
            height = intValue(MediaStore.MediaColumns.HEIGHT),
            durationMillis = duration,
            type = type,
            isFavorite = favoriteValue()
        )
    }

    private fun Cursor.favoriteValue(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return false
        val index = getColumnIndex(MediaStore.MediaColumns.IS_FAVORITE)
        return index >= 0 && !isNull(index) && getInt(index) != 0
    }

    private fun Cursor.longValue(column: String): Long {
        val index = getColumnIndex(column)
        return if (index >= 0 && !isNull(index)) getLong(index) else 0L
    }

    private fun Cursor.intValue(column: String): Int {
        val index = getColumnIndex(column)
        return if (index >= 0 && !isNull(index)) getInt(index) else 0
    }

    private fun Cursor.stringValue(column: String): String {
        val index = getColumnIndex(column)
        return if (index >= 0 && !isNull(index)) getString(index).orEmpty() else ""
    }

    private companion object {
        val baseProjection: Array<String> = buildList {
            add(MediaStore.MediaColumns._ID)
            add(MediaStore.MediaColumns.DISPLAY_NAME)
            add(MediaStore.MediaColumns.MIME_TYPE)
            add(MediaStore.MediaColumns.SIZE)
            add(MediaStore.MediaColumns.DATE_TAKEN)
            add(MediaStore.MediaColumns.DATE_MODIFIED)
            add(MediaStore.MediaColumns.WIDTH)
            add(MediaStore.MediaColumns.HEIGHT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                add(MediaStore.MediaColumns.IS_FAVORITE)
            }
        }.toTypedArray()
    }
}
