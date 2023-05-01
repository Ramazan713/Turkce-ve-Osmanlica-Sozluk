package com.masterplus.trdictionary.features.app.presentation.ad

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AdMobInterstitialView(
    adUiEventState: AdUiEvent?,
    onAdEvent: (AdEvent)->Unit
){
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val adMobInterstitial = remember { AdMobInterstitial(context) }


    adUiEventState?.let { uiEvent->
        LaunchedEffect(uiEvent,lifecycle){
            snapshotFlow { uiEvent }
                .flowWithLifecycle(lifecycle)
                .collectLatest { event->
                    when(event){
                        is AdUiEvent.CheckAdShowState -> {
                            adMobInterstitial.showAd()
                        }
                        is AdUiEvent.LoadAd -> {
                            adMobInterstitial.loadAd {
                                onAdEvent(AdEvent.Reset)
                            }
                        }
                    }
                    onAdEvent(AdEvent.ClearUiEvent)
                }
        }
    }
}