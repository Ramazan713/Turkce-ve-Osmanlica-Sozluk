package com.masterplus.trdictionary.features.app.presentation

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.masterplus.trdictionary.core.domain.enums.AppNavigationType
import com.masterplus.trdictionary.core.domain.enums.DevicePosture
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumViewModel
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute
import com.masterplus.trdictionary.features.app.domain.model.kAppNavRouteNames
import com.masterplus.trdictionary.features.app.domain.model.kAppNavRoutes
import com.masterplus.trdictionary.features.app.extensions.navigateToNavRoute
import com.masterplus.trdictionary.features.app.presentation.ad.AdEvent
import com.masterplus.trdictionary.features.app.presentation.ad.AdViewModel
import com.masterplus.trdictionary.features.app.presentation.app_navigations.AppBottomNavigationBar
import com.masterplus.trdictionary.features.app.presentation.app_navigations.AppModalDrawerNavigationBar
import com.masterplus.trdictionary.features.app.presentation.app_navigations.AppPersistentDrawerNavigationBar
import com.masterplus.trdictionary.features.app.presentation.app_navigations.AppRailNavigationBar
import com.masterplus.trdictionary.features.app.presentation.components.AdPremiumControl
import com.masterplus.trdictionary.features.app.presentation.components.InAppFeaturesControl
import com.masterplus.trdictionary.features.app.presentation.components.NavigationAnimatedVisibility
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppEvent
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppFeaturesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch

@ObsoleteCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun MyApp(
    navHostController: NavHostController = rememberNavController(),
    adViewModel: AdViewModel = hiltViewModel(),
    premiumViewModel: PremiumViewModel = hiltViewModel(),
    inAppFeaturesViewModel: InAppFeaturesViewModel = hiltViewModel(),
){

    val stackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute by remember(stackEntry) {
        derivedStateOf {
            stackEntry.value?.destination?.route
        }
    }

    val currentNavRoute by remember(currentRoute) {
        derivedStateOf {
            kAppNavRoutes.find { it.route == currentRoute }
        }
    }

    val navigationVisible by remember(currentRoute) {
        derivedStateOf {
            kAppNavRouteNames.find { it == currentRoute } != null
        }
    }

    LaunchedEffect(currentNavRoute){
        adViewModel.onEvent(AdEvent.CheckFromDestination(currentNavRoute?.route))
        inAppFeaturesViewModel.onEvent(InAppEvent.CheckDestination(currentNavRoute?.route))
    }

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

    val adState by adViewModel.state.collectAsStateWithLifecycle()

    MyApp(
        navigationVisible = navigationVisible,
        navHostController = navHostController,
        currentNavRoute = currentNavRoute,
        premiumViewModel = premiumViewModel
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MyApp(
    navigationVisible: Boolean,
    navHostController: NavHostController,
    currentNavRoute: AppNavRoute?,
    premiumViewModel: PremiumViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val windowSizeClass = calculateWindowSizeClass(activity = context as Activity).widthSizeClass
    val displayFeatures = calculateDisplayFeatures(activity = context)

    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
    val devicePosture = DevicePosture.from(foldingFeature)
    val navigationType = AppNavigationType.from(windowSizeClass, devicePosture)


    val modalDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(navigationType){
        if(modalDrawerState.isOpen && navigationType != AppNavigationType.RAIL){
            modalDrawerState.close()
        }
    }

    if(navigationType == AppNavigationType.DRAWER){
        PermanentNavigationDrawer(
            drawerContent = {
                NavigationAnimatedVisibility(
                    visible = navigationVisible,
                ) {
                    AppPersistentDrawerNavigationBar(
                        currentDestination = currentNavRoute,
                        onDestinationChange = { navHostController.navigateToNavRoute(it)}
                    )
                }
            }
        ) {
            AppContent(
                navigationVisible = navigationVisible,
                navigationType = navigationType,
                devicePosture = devicePosture,
                windowSizeClass = windowSizeClass,
                navHostController = navHostController,
                currentNavRoute = currentNavRoute,
                premiumViewModel = premiumViewModel,
                displayFeatures = displayFeatures,
                onDrawerClick = { scope.launch { modalDrawerState.open() } }
            )
        }
    }else{
        ModalNavigationDrawer(
            drawerState = modalDrawerState,
            gesturesEnabled = navigationVisible && navigationType == AppNavigationType.RAIL,
            drawerContent = {
                NavigationAnimatedVisibility(
                    visible = navigationVisible,
                ) {
                    AppModalDrawerNavigationBar(
                        currentDestination = currentNavRoute,
                        onDestinationChange = { navHostController.navigateToNavRoute(it) },
                        onDrawerMenuClick = { scope.launch { modalDrawerState.close() } }
                    )
                }
            }
        ) {
            AppContent(
                navigationVisible = navigationVisible,
                navigationType = navigationType,
                devicePosture = devicePosture,
                windowSizeClass = windowSizeClass,
                navHostController = navHostController,
                currentNavRoute = currentNavRoute,
                premiumViewModel = premiumViewModel,
                displayFeatures = displayFeatures,
                onDrawerClick = { scope.launch { modalDrawerState.open() } }
            )
        }

    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class
)
@Composable
fun AppContent(
    navigationVisible: Boolean,
    navigationType: AppNavigationType,
    devicePosture: DevicePosture,
    displayFeatures: List<DisplayFeature>,
    windowSizeClass: WindowWidthSizeClass,
    navHostController: NavHostController,
    currentNavRoute: AppNavRoute?,
    premiumViewModel: PremiumViewModel,
    onDrawerClick: () -> Unit
) {

    Scaffold(
        bottomBar = {
            NavigationAnimatedVisibility(
                navigationVisible && navigationType == AppNavigationType.BOTTOM_NAVIGATION,
            ){
                AppBottomNavigationBar(
                    currentDestination = currentNavRoute,
                    onDestinationChange = {
                        navHostController.navigateToNavRoute(it)
                    }
                )
            }
        },
    ) { innerPadding ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            NavigationAnimatedVisibility(
                navigationVisible && navigationType == AppNavigationType.RAIL,
            ){
                AppRailNavigationBar(
                    currentDestination = currentNavRoute,
                    onDestinationChange = {
                        navHostController.navigateToNavRoute(it)
                    },
                    onDrawerMenuClick = { onDrawerClick() }
                )
            }

            AppNavHost(
                modifier = Modifier.weight(1f),
                premiumViewModel = premiumViewModel,
                navController = navHostController,
                windowWidthSizeClass = windowSizeClass,
                devicePosture = devicePosture,
                displayFeatures = displayFeatures
            )
        }
    }
}

