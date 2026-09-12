package com.mydrive.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mydrive.app.data.model.ActivityEvent
import com.mydrive.app.data.model.BackupOverview
import com.mydrive.app.data.model.MediaItem
import com.mydrive.app.data.model.TodayStats
import com.mydrive.app.data.model.UserProfile
import com.mydrive.app.data.repository.MediaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val profile: UserProfile,
    val overview: BackupOverview,
    val stats: TodayStats,
    val recentMedia: List<MediaItem>,
    val activity: List<ActivityEvent>
)

class HomeViewModel(
    private val repository: MediaRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        repository.profile,
        repository.overview,
        repository.todayStats,
        repository.media,
        repository.activity
    ) { profile, overview, stats, media, activity ->
        HomeUiState(
            profile = profile,
            overview = overview,
            stats = stats,
            recentMedia = media.take(6),
            activity = activity
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(
            profile = repository.profile.value,
            overview = repository.overview.value,
            stats = repository.todayStats.value,
            recentMedia = repository.media.value.take(6),
            activity = repository.activity.value
        )
    )

    companion object {
        fun factory(repository: MediaRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository) as T
                }
            }
    }
}
