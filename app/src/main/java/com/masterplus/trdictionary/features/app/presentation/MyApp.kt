package com.masterplus.trdictionary.features.app.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumViewModel
import com.masterplus.trdictionary.features.app.domain.model.kBottomNavRouteNames
import com.masterplus.trdictionary.features.app.presentation.ad.AdEvent
import com.masterplus.trdictionary.features.app.presentation.ad.AdViewModel
import com.masterplus.trdictionary.features.app.presentation.components.AdPremiumControl
import com.masterplus.trdictionary.features.app.presentation.components.InAppFeaturesControl
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppEvent
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppFeaturesViewModel
import com.masterplus.trdictionary.features.app.presentation.navigation.AppNavHost
import com.masterplus.trdictionary.features.app.presentation.navigation.BottomBarVisibilityViewModel
import com.masterplus.trdictionary.features.app.presentation.navigation.NavigationBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun MyApp(
    navController: NavHostController = rememberNavController(),
    adViewModel: AdViewModel = hiltViewModel(),
    premiumViewModel: PremiumViewModel = hiltViewModel(),
    inAppFeaturesViewModel: InAppFeaturesViewModel = hiltViewModel(),
    bottomBarVisibilityViewModel: BottomBarVisibilityViewModel = hiltViewModel(),
){

    LaunchedEffect(true){
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            val isBottomItemsVisible = kBottomNavRouteNames.contains(navDestination.route)
            bottomBarVisibilityViewModel.setVisibility(isBottomItemsVisible)

            adViewModel.onEvent(AdEvent.CheckFromDestination(navDestination.route))
            inAppFeaturesViewModel.onEvent(InAppEvent.CheckDestination(navDestination.route))
        }
    }

    val adState by adViewModel.state.collectAsStateWithLifecycle()

    AdPremiumControl(
        onAdEvent = adViewModel::onEvent,
        adUiEvent = adViewModel.uiEventState,
        premiumState = premiumViewModel.state,
        onPremiumEvent = premiumViewModel::onEvent
    )

    InAppFeaturesControl(
        state = inAppFeaturesViewModel.state,
        onEvent = inAppFeaturesViewModel::onEvent
    )

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                bottomBarVisibilityViewModel.isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ){
                NavigationBar(navController)
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { innerPadding ->
        AppNavHost(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            premiumViewModel = premiumViewModel,
            navController = navController,
        )
    }
}