package com.mydrive.app.data.local

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object MediaPermissions {

    fun required(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    fun hasAccess(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                isGranted(context, Manifest.permission.READ_MEDIA_IMAGES) ||
                    isGranted(context, Manifest.permission.READ_MEDIA_VIDEO) ||
                    isGranted(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                isGranted(context, Manifest.permission.READ_MEDIA_IMAGES) ||
                    isGranted(context, Manifest.permission.READ_MEDIA_VIDEO)
            }
            else -> isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun isGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) ==
            PackageManager.PERMISSION_GRANTED
    }
}
