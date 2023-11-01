package com.masterplus.trdictionary.features.app.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.features.category.presentation.category.navigation.RouteCategory
import com.masterplus.trdictionary.features.home.presentation.navigation.RouteHome
import com.masterplus.trdictionary.features.list.presentation.show_list.navigation.RouteList

sealed class AppNavRoute(
    val route: String,
    val title: UiText,
    private val defaultImageVector: ImageVector,
    private val selectedImageVector: ImageVector
) {
    data object Home : AppNavRoute(
        RouteHome,
        UiText.Resource(R.string.home),
        Icons.Outlined.Home,
        Icons.Default.Home
    )
    data object List : AppNavRoute(
        RouteList,
        UiText.Resource(R.string.list),
        Icons.Outlined.ViewList,
        Icons.Default.ViewList
    )
    data object Category : AppNavRoute(
        RouteCategory,
        UiText.Resource(R.string.category),
        Icons.Outlined.Category,
        Icons.Default.Category
    )

    fun getCurrentImageVector(selected: Boolean): ImageVector{
        return if(selected) selectedImageVector else defaultImageVector
    }
}

val kAppNavRoutes = listOf(
    AppNavRoute.Category,
    AppNavRoute.Home,
    AppNavRoute.List,
)

val kAppNavRouteNames = kAppNavRoutes.map { it.route }
