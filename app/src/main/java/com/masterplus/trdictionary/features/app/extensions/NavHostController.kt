package com.masterplus.trdictionary.features.app.extensions

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute

fun NavHostController.navigateToNavRoute(appNavRoute: AppNavRoute){
    navigate(appNavRoute.route){
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
