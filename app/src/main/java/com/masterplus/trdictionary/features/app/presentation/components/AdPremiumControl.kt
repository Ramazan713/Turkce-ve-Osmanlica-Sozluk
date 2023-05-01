package com.masterplus.trdictionary.features.app.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumEvent
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumState
import com.masterplus.trdictionary.features.app.presentation.ad.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AdPremiumControl(
    premiumState: PremiumState,
    onPremiumEvent: (PremiumEvent)->Unit,
    adUiEvent: AdUiEvent?,
    onAdEvent: (AdEvent)->Unit
){
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner){
        val lifecycle = lifecycleOwner.lifecycle

        val observer = LifecycleEventObserver { _, event ->
            when(event){
                Lifecycle.Event.ON_RESUME -> {
                    onPremiumEvent(PremiumEvent.CheckPremium)
                }
                else-> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }


    LaunchedEffect(premiumState.isPremium,lifecycleOwner.lifecycle){
        snapshotFlow { premiumState.isPremium }
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { isPremium->
                onAdEvent(AdEvent.SetPremiumActive(isPremium))
            }
    }

    if(!premiumState.isPremium){
        AdMobInterstitialView(
            adUiEventState = adUiEvent,
            onAdEvent = onAdEvent
        )
    }

}