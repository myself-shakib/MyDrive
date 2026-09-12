package com.mydrive.app.ui.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mydrive.app.data.model.MediaItem
import com.mydrive.app.data.repository.MediaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MediaDetailsUiState(
    val item: MediaItem?
)

class MediaDetailsViewModel(
    private val repository: MediaRepository,
    private val mediaId: String
) : ViewModel() {

    val uiState: StateFlow<MediaDetailsUiState> = repository.media
        .map { items -> MediaDetailsUiState(items.firstOrNull { it.id == mediaId }) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MediaDetailsUiState(repository.mediaById(mediaId))
        )

    fun toggleFavorite() {
        repository.toggleFavorite(mediaId)
    }

    companion object {
        fun factory(repository: MediaRepository, mediaId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MediaDetailsViewModel(repository, mediaId) as T
                }
            }
    }
}
