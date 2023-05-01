package com.masterplus.trdictionary.features.app.domain.model

import androidx.annotation.DrawableRes
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.util.UiText
import com.masterplus.trdictionary.features.category.presentation.category.navigation.RouteCategory
import com.masterplus.trdictionary.features.home.presentation.navigation.RouteHome
import com.masterplus.trdictionary.features.list.presentation.show_list.navigation.RouteList

sealed class BottomNavRoute(val route: String, val title: UiText, @DrawableRes val resourceId: Int) {
    object Home : BottomNavRoute(RouteHome,UiText.Resource(R.string.home), R.drawable.baseline_home_24)
    object List : BottomNavRoute(RouteList,UiText.Resource(R.string.list), R.drawable.baseline_view_list_24)
    object Category : BottomNavRoute(RouteCategory,UiText.Resource(R.string.category), R.drawable.ic_baseline_category_24)
}

val kBottomNavRoutes = listOf(
    BottomNavRoute.Category,
    BottomNavRoute.Home,
    BottomNavRoute.List,
)

val kBottomNavRouteNames = kBottomNavRoutes.map { it.route }
