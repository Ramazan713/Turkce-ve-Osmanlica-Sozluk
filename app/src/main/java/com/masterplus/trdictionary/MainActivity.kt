package com.masterplus.trdictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.*
import com.google.android.gms.ads.*
import com.masterplus.trdictionary.features.app.presentation.MyApp
import com.masterplus.trdictionary.ui.theme.TRDictionaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.util.*

@OptIn(ObsoleteCoroutinesApi::class)
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}

        setContent {
            val navController = rememberNavController()

            TRDictionaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MyApp(
                        navController,
                    )
                }
            }
        }
    }
}







