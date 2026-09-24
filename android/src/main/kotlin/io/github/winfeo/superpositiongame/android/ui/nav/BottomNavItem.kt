package io.github.winfeo.superpositiongame.android.ui.nav

import androidx.compose.ui.graphics.vector.ImageVector
import io.github.winfeo.superpositiongame.android.ui.nav.route.AppRoute

data class BottomNavItem(
    val route: AppRoute,
    val icon: ImageVector,
    val label: String
)
