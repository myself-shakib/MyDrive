package com.mydrive.app.ui.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberMediaThumbnail(uri: Uri?, sizePx: Int = 256): ImageBitmap? {
    val context = LocalContext.current
    val thumbnail by produceState<ImageBitmap?>(initialValue = null, uri, sizePx) {
        value = null
        if (uri == null) return@produceState
        value = withContext(Dispatchers.IO) {
            loadMediaThumbnail(context, uri, sizePx)
        }
    }
    return thumbnail
}

fun loadMediaThumbnail(context: Context, uri: Uri, sizePx: Int): ImageBitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver
                .loadThumbnail(uri, Size(sizePx, sizePx), null)
                .asImageBitmap()
        } else {
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input, null, BitmapFactory.Options().apply {
                    inSampleSize = 4
                })?.asImageBitmap()
            }
        }
    } catch (_: SecurityException) {
        null
    } catch (_: Exception) {
        null
    }
}
