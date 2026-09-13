package com.mydrive.app.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mydrive.app.data.model.MediaItem
import com.mydrive.app.data.model.MediaType
import com.mydrive.app.data.model.toMediaItem
import com.mydrive.app.data.repository.LocalMediaLoadState
import com.mydrive.app.data.repository.LocalMediaRepository
import com.mydrive.app.ui.util.dateGroupLabel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class GalleryFilter {
    ALL, PHOTOS, VIDEOS, FAVORITES
}

data class MediaGroup(
    val label: String,
    val items: List<MediaItem>
)

data class GalleryUiState(
    val filter: GalleryFilter,
    val query: String,
    val groups: List<MediaGroup>,
    val isLoading: Boolean = false,
    val needsPermission: Boolean = false,
    val errorMessage: String? = null
)

class GalleryViewModel(
    private val localMediaRepository: LocalMediaRepository
) : ViewModel() {

    private val filter = MutableStateFlow(GalleryFilter.ALL)
    private val query = MutableStateFlow("")
    private val permissionGranted = MutableStateFlow<Boolean?>(null)
    private var refreshJob: Job? = null

    val uiState: StateFlow<GalleryUiState> = combine(
        localMediaRepository.loadState,
        filter,
        query,
        permissionGranted
    ) { loadState, currentFilter, currentQuery, granted ->
        when (granted) {
            null -> GalleryUiState(
                filter = currentFilter,
                query = currentQuery,
                groups = emptyList(),
                isLoading = true
            )
            false -> GalleryUiState(
                filter = currentFilter,
                query = currentQuery,
                groups = emptyList(),
                needsPermission = true
            )
            true -> {
                val media = (loadState as? LocalMediaLoadState.Ready)
                    ?.items
                    ?.map { it.toMediaItem() }
                    .orEmpty()
                val filtered = media
                    .filter { item ->
                        when (currentFilter) {
                            GalleryFilter.ALL -> true
                            GalleryFilter.PHOTOS -> item.type == MediaType.PHOTO
                            GalleryFilter.VIDEOS -> item.type == MediaType.VIDEO
                            GalleryFilter.FAVORITES -> item.isFavorite
                        }
                    }
                    .filter { item ->
                        currentQuery.isBlank() || item.filename.contains(currentQuery, ignoreCase = true)
                    }
                    .sortedByDescending { it.capturedAtMillis }
                val groups = filtered
                    .groupBy { dateGroupLabel(it.capturedAtMillis) }
                    .map { (label, items) -> MediaGroup(label, items) }
                GalleryUiState(
                    filter = currentFilter,
                    query = currentQuery,
                    groups = groups,
                    isLoading = loadState is LocalMediaLoadState.Loading ||
                        loadState is LocalMediaLoadState.Idle,
                    errorMessage = (loadState as? LocalMediaLoadState.Error)?.message
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GalleryUiState(GalleryFilter.ALL, "", emptyList(), isLoading = true)
    )

    fun onPermissionResult(granted: Boolean) {
        permissionGranted.value = granted
        if (granted) refreshLocalMedia()
    }

    fun retry() {
        if (permissionGranted.value == true) refreshLocalMedia()
    }

    private fun refreshLocalMedia() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch { localMediaRepository.refresh() }
    }

    fun setFilter(value: GalleryFilter) {
        filter.value = value
    }

    fun setQuery(value: String) {
        query.update { value }
    }

    companion object {
        fun factory(localMediaRepository: LocalMediaRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GalleryViewModel(localMediaRepository) as T
                }
            }
    }
}
