package com.masterplus.trdictionary.custom_init

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.masterplus.trdictionary.features.app.presentation.MyApp
import com.masterplus.trdictionary.ui.theme.TRDictionaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class, ObsoleteCoroutinesApi::class
)
@AndroidEntryPoint
class CustomInitTestActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            val initViewModel = hiltViewModel<InitTestViewModel>()
            val state by initViewModel.state.collectAsStateWithLifecycle()

            val lifecycleOwner = LocalLifecycleOwner.current

            val navController = rememberNavController()

            LaunchedEffect(state.destinationRoute, lifecycleOwner.lifecycle){
                snapshotFlow { state.destinationRoute }
                    .filterNotNull()
                    .flowWithLifecycle(lifecycleOwner.lifecycle)
                    .collectLatest { destRoute->
                        initViewModel.clearNavigation()
                        navController.navigate(destRoute)
                    }
            }


            TRDictionaryTheme { _,_ ->
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MyApp(
                        windowSizeClass = state.widthSizeClass,
                        displayFeatures = state.displayFeatures,
                        navHostController = navController,
                    )
                }
            }
        }
    }
}