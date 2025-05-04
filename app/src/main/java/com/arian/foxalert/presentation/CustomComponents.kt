package com.arian.foxalert.presentation

import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String) {
    object Event: Screen("event_screen")
    object Calendar: Screen("calendar_screen")
    object Category: Screen("category_screen")
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)
