package com.masterplus.trdictionary.features.app.presentation.components

import androidx.compose.runtime.Composable
import com.masterplus.trdictionary.core.presentation.utils.EventHandler
import com.masterplus.trdictionary.core.presentation.utils.ListenEventLifecycle
import com.masterplus.trdictionary.core.shared_features.premium.PremiumEvent
import com.masterplus.trdictionary.core.shared_features.premium.PremiumState
import com.masterplus.trdictionary.features.app.presentation.ad.AdEvent
import com.masterplus.trdictionary.features.app.presentation.ad.AdMobInterstitialView
import com.masterplus.trdictionary.features.app.presentation.ad.AdUiEvent

@Composable
fun AdPremiumControl(
    premiumState: PremiumState,
    onPremiumEvent: (PremiumEvent)->Unit,
    adUiEvent: AdUiEvent?,
    onAdEvent: (AdEvent)->Unit
){
    ListenEventLifecycle(
        onResume = {
            onPremiumEvent(PremiumEvent.CheckPremium)
        }
    )

    EventHandler(event = premiumState.isPremium) { isPremium->
        onAdEvent(AdEvent.SetPremiumActive(isPremium))
    }

    if(!premiumState.isPremium){
        AdMobInterstitialView(
            adUiEventState = adUiEvent,
            onAdEvent = onAdEvent
        )
    }

}