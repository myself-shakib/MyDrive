package com.mydrive.app

import android.app.Application
import com.mydrive.app.data.local.MediaStoreDataSource
import com.mydrive.app.data.repository.LocalMediaRepository
import com.mydrive.app.data.repository.MediaRepository

class MyDriveApp : Application() {
    val mediaRepository: MediaRepository by lazy { MediaRepository() }
    val localMediaRepository: LocalMediaRepository by lazy {
        LocalMediaRepository(MediaStoreDataSource(this))
    }
}
