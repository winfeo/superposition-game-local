package io.github.winfeo.superpositiongame.android.ui.nav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.winfeo.superpositiongame.android.ui.nav.route.LibraryRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.AiRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.LobbyRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.ProfileRoute
import io.github.winfeo.superpositiongame.R

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem(
            route = LobbyRoute,
            icon = ImageVector.vectorResource(R.drawable.ic_gamepad),
            label = "Лобби"
        ),

        BottomNavItem(
            route = AiRoute,
            icon = ImageVector.vectorResource(R.drawable.ic_bot),
            label = "Игра с AI"
        ),

        BottomNavItem(
            route = LibraryRoute,
            icon = ImageVector.vectorResource(R.drawable.ic_graduation_cap),
            label = "Картотека"
        ),

        BottomNavItem(
            route = ProfileRoute,
            icon = ImageVector.vectorResource(R.drawable.ic_user),
            label = "Профиль"
        )
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(currentRoute) {
        val index = items.indexOfFirst { it.route::class.qualifiedName == currentRoute }
        if (index >= 0) { selectedIndex = index }
    }

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val navBarWidth = screenWidthDp - 32.dp
    val itemWidth = navBarWidth / items.size

    val indicatorPosition by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "indicator"
    )

    val indicatorOffsetDp = with(density) {
        val itemWidthPx = itemWidth.toPx()
        val indicatorWidthPx = 80.dp.toPx()
        val offsetPx =
            (indicatorPosition * itemWidthPx) +
                (itemWidthPx / 2f) -
                (indicatorWidthPx / 2f)

        offsetPx.toDp()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF161221).copy(alpha = 0.95f))
        ) {
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffsetDp)
                    .padding(vertical = 6.dp)
                    .width(80.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6C8CFF).copy(alpha = 0.30f),
                                Color(0xFF6C8CFF).copy(alpha = 0.10f)
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route::class.qualifiedName
                    val iconColor by animateColorAsState(
                        targetValue =
                            if (isSelected) Color(0xFF9DB2FF)
                            else Color.White.copy(alpha = 0.45f),
                        animationSpec = tween(300),
                        label = "iconColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = iconColor
                        )
                    }
                }
            }
        }
    }
}
