package com.masterplus.trdictionary.features.app.presentation.ad

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.masterplus.trdictionary.core.presentation.utils.EventHandler

@Composable
fun AdMobInterstitialView(
    adUiEventState: AdUiEvent?,
    onAdEvent: (AdEvent)->Unit
){
    val context = LocalContext.current
    val adMobInterstitial = remember { AdMobInterstitial(context) }

    EventHandler(event = adUiEventState) { event->
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