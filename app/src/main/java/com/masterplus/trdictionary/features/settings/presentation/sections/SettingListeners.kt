package com.masterplus.trdictionary.features.settings.presentation.sections

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.domain.util.ToastHelper
import com.masterplus.trdictionary.core.extensions.refreshApp
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumEvent
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumState
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumUiEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState
import com.masterplus.trdictionary.features.settings.presentation.SettingUiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SettingListeners(
    premiumState: PremiumState,
    onPremiumEvent: (PremiumEvent)->Unit,
    state: SettingState,
    onEvent: (SettingEvent)->Unit
){
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        onEvent(SettingEvent.SignInWithGoogle(result))
    }

    LaunchedEffect(true){
        onEvent(SettingEvent.LoadData)
    }

    LaunchedEffect(state,lifecycle){
        snapshotFlow { state }
            .filter { it.settingUiEvent != null }
            .flowWithLifecycle(lifecycle)
            .collectLatest {
                when(val event = it.settingUiEvent){
                    is SettingUiEvent.LaunchGoogleSignIn -> {
                        activityResult.launch(event.intent)
                    }
                    is SettingUiEvent.RefreshApp -> {
                        context.refreshApp()
                    }
                    null -> {}
                }
                onEvent(SettingEvent.ClearUiEvent)
            }
    }

    state.message?.let { message->
        LaunchedEffect(message){
            ToastHelper.showMessage(message,context)
            onEvent(SettingEvent.ClearMessage)
        }
    }

    LaunchedEffect(premiumState,lifecycle){
        snapshotFlow { premiumState }
            .flowWithLifecycle(lifecycle)
            .collectLatest { preState->
                when(val uiEvent = preState.uiEvent){
                    is PremiumUiEvent.LaunchBillingFlow -> {
                        uiEvent.billingClient.launchBillingFlow(context as Activity,uiEvent.billingFlowParams)
                        onPremiumEvent(PremiumEvent.ClearUiEvent)
                    }
                    null -> {}
                }

                preState.message?.let { message->
                    ToastHelper.showMessage(message,context)
                    onPremiumEvent(PremiumEvent.ClearMessage)
                }
            }
    }

    val isPremiumPurchased by remember(premiumState.isPremium) {
        derivedStateOf {
            premiumState.isPremium &&
                    state.showDialog &&
                    (state.dialogEvent is SettingDialogEvent.ShowPremiumDia)
        }
    }

    LaunchedEffect(isPremiumPurchased,lifecycle){
        snapshotFlow { isPremiumPurchased }
            .flowWithLifecycle(lifecycle)
            .collectLatest {
                if(it){
                    onEvent(SettingEvent.ShowDialog(false))
                }
            }
    }

}
