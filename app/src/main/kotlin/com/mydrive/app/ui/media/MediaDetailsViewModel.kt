package com.mydrive.app.ui.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mydrive.app.data.model.MediaItem
import com.mydrive.app.data.model.mediaItemId
import com.mydrive.app.data.model.toMediaItem
import com.mydrive.app.data.repository.LocalMediaLoadState
import com.mydrive.app.data.repository.LocalMediaRepository
import com.mydrive.app.data.repository.MediaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class MediaDetailsUiState(
    val item: MediaItem?
)

class MediaDetailsViewModel(
    private val repository: MediaRepository,
    private val localMediaRepository: LocalMediaRepository,
    private val mediaId: String
) : ViewModel() {

    val uiState: StateFlow<MediaDetailsUiState> = combine(
        repository.media,
        localMediaRepository.loadState
    ) { media, localState ->
        val fromMock = media.firstOrNull { it.id == mediaId }
        val fromLocal = (localState as? LocalMediaLoadState.Ready)
            ?.items
            ?.firstOrNull { it.mediaItemId() == mediaId }
            ?.toMediaItem()
        MediaDetailsUiState(fromMock ?: fromLocal)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MediaDetailsUiState(
            repository.mediaById(mediaId) ?: localMediaRepository.mediaById(mediaId)?.toMediaItem()
        )
    )

    fun toggleFavorite() {
        repository.toggleFavorite(mediaId)
    }

    companion object {
        fun factory(
            repository: MediaRepository,
            localMediaRepository: LocalMediaRepository,
            mediaId: String
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MediaDetailsViewModel(repository, localMediaRepository, mediaId) as T
                }
            }
    }
}
