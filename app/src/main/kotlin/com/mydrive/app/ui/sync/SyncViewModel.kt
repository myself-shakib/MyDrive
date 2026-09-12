package com.mydrive.app.ui.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mydrive.app.data.model.BackupState
import com.mydrive.app.data.model.MediaItem
import com.mydrive.app.data.model.SyncSummary
import com.mydrive.app.data.repository.MediaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class SyncUiState(
    val summary: SyncSummary,
    val inProgress: List<MediaItem>,
    val waiting: List<MediaItem>,
    val completed: List<MediaItem>,
    val failed: List<MediaItem>
)

class SyncViewModel(
    private val repository: MediaRepository
) : ViewModel() {

    val uiState: StateFlow<SyncUiState> = combine(
        repository.media,
        repository.syncSummary
    ) { media, summary ->
        SyncUiState(
            summary = summary,
            inProgress = media.filter {
                it.backupState == BackupState.UPLOADING ||
                    it.backupState == BackupState.PROCESSING ||
                    it.backupState == BackupState.SENDING_TELEGRAM
            },
            waiting = media.filter { it.backupState == BackupState.WAITING },
            completed = media.filter { it.backupState == BackupState.COMPLETED }.take(6),
            failed = media.filter { it.backupState == BackupState.FAILED }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SyncUiState(
            summary = repository.syncSummary.value,
            inProgress = emptyList(),
            waiting = emptyList(),
            completed = emptyList(),
            failed = emptyList()
        )
    )

    companion object {
        fun factory(repository: MediaRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SyncViewModel(repository) as T
                }
            }
    }
}
