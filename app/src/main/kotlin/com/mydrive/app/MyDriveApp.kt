package com.mydrive.app

import android.app.Application
import com.mydrive.app.data.repository.MediaRepository

class MyDriveApp : Application() {
    val mediaRepository: MediaRepository by lazy { MediaRepository() }
}
