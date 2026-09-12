package com.mydrive.app.ui.navigation

sealed class AppDestination(val route: String) {
    data object Home : AppDestination("home")
    data object Gallery : AppDestination("gallery")
    data object Sync : AppDestination("sync")
    data object Settings : AppDestination("settings")
    data object MediaDetails : AppDestination("media/{mediaId}") {
        fun create(mediaId: String) = "media/$mediaId"
    }
    data object TelegramSettings : AppDestination("settings/telegram")
}

val bottomDestinations = listOf(
    AppDestination.Home,
    AppDestination.Gallery,
    AppDestination.Sync,
    AppDestination.Settings
)
