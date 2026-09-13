package com.mydrive.app.data.repository

import com.mydrive.app.data.local.MediaStoreDataSource
import com.mydrive.app.data.model.LocalMediaItem
import com.mydrive.app.data.model.mediaItemId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface LocalMediaLoadState {
    data object Idle : LocalMediaLoadState
    data object Loading : LocalMediaLoadState
    data class Ready(val items: List<LocalMediaItem>) : LocalMediaLoadState
    data class Error(val message: String) : LocalMediaLoadState
}

class LocalMediaRepository(
    private val dataSource: MediaStoreDataSource
) {

    private val _loadState = MutableStateFlow<LocalMediaLoadState>(LocalMediaLoadState.Idle)
    val loadState: StateFlow<LocalMediaLoadState> = _loadState.asStateFlow()

    suspend fun refresh() {
        if (_loadState.value !is LocalMediaLoadState.Ready) {
            _loadState.value = LocalMediaLoadState.Loading
        }
        _loadState.value = try {
            LocalMediaLoadState.Ready(dataSource.queryAll())
        } catch (_: SecurityException) {
            LocalMediaLoadState.Error("Permission required to access photos and videos.")
        } catch (e: Exception) {
            LocalMediaLoadState.Error(e.message ?: "Unable to load media from this device.")
        }
    }

    fun mediaById(id: String): LocalMediaItem? {
        val current = _loadState.value as? LocalMediaLoadState.Ready ?: return null
        return current.items.firstOrNull { it.mediaItemId() == id }
    }
}
