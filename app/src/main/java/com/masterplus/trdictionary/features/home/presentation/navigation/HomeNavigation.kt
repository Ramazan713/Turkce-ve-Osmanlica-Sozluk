package com.masterplus.trdictionary.features.home.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.trdictionary.core.shared_features.premium.PremiumViewModel
import com.masterplus.trdictionary.features.home.presentation.HomePage
import com.masterplus.trdictionary.features.home.presentation.HomeViewModel

const val RouteHome = "Home"


fun NavController.navigateToHome(){
    this.navigate(RouteHome)
}


@ExperimentalMaterial3Api
@ExperimentalFoundationApi
fun NavGraphBuilder.homePage(
    onNavigateToWordDetail: (Int)->Unit,
    onNavigateToSearch: ()->Unit,
    onNavigateToSetting: ()->Unit,
    premiumViewModel: PremiumViewModel,
){
    composable(RouteHome) {
        val homeViewModel: HomeViewModel = hiltViewModel()

        val state by homeViewModel.state.collectAsStateWithLifecycle()
        val premiumState by premiumViewModel.state.collectAsStateWithLifecycle()

        HomePage(
            onNavigateToWordDetail = onNavigateToWordDetail,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToSetting = onNavigateToSetting,
            state = state,
            onEvent = homeViewModel::onEvent,
            isPremium = premiumState.isPremium
        )
    }
}
