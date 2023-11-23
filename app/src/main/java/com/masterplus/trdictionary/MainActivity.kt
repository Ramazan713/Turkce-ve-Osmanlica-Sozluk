package com.masterplus.trdictionary

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.*
import com.google.android.gms.ads.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.masterplus.trdictionary.core.data.preferences.set
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.features.app.presentation.MyApp
import com.masterplus.trdictionary.ui.theme.TRDictionaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@OptIn(ObsoleteCoroutinesApi::class)
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPref: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        setUpRemoteConfig()

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


    private fun setUpRemoteConfig(){
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(mapOf(
            "consumeIntervalSeconds" to KPref.consumeIntervalSeconds.default,
            "thresholdConsumeSeconds" to KPref.thresholdConsumeSeconds.default,
            "thresholdOpeningCount" to KPref.thresholdOpeningCount.default
        ))

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener{
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate().addOnCompleteListener {
                    if(it.isSuccessful){
                        val consumeIntervalSeconds = remoteConfig.getValue("consumeIntervalSeconds").asString().toIntOrNull() ?: KPref.consumeIntervalSeconds.default
                        val thresholdConsumeSeconds = remoteConfig.getValue("thresholdConsumeSeconds").asString().toIntOrNull() ?: KPref.thresholdConsumeSeconds.default
                        val thresholdOpeningCount = remoteConfig.getValue("thresholdOpeningCount").asString().toIntOrNull() ?: KPref.thresholdOpeningCount.default

                        lifecycleScope.launch {
                            appPref.edit {pref->
                                pref[KPref.consumeIntervalSeconds] = consumeIntervalSeconds
                                pref[KPref.thresholdConsumeSeconds] = thresholdConsumeSeconds
                                pref[KPref.thresholdOpeningCount] = thresholdOpeningCount
                            }
                        }
                    }
                }
            }
            override fun onError(error: FirebaseRemoteConfigException) {}
        })
    }
}







