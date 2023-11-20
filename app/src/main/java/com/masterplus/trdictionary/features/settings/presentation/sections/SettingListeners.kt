package com.masterplus.trdictionary.features.settings.presentation.sections

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.presentation.utils.ToastHelper
import com.masterplus.trdictionary.core.extensions.refreshApp
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthEvent
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthState
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthUiAction
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.handlers.ListenAuthUiAction
import com.masterplus.trdictionary.core.shared_features.premium.PremiumEvent
import com.masterplus.trdictionary.core.shared_features.premium.PremiumState
import com.masterplus.trdictionary.core.shared_features.premium.PremiumUiEvent
import com.masterplus.trdictionary.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingSheetEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SettingListeners(
    premiumState: PremiumState,
    onPremiumEvent: (PremiumEvent)->Unit,
    state: SettingState,
    onEvent: (SettingEvent)->Unit,
    authState: AuthState,
    onAuthEvent: (AuthEvent) -> Unit
){
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(true){
        onEvent(SettingEvent.LoadData)
    }

    ListenAuthUiAction(
        uiAction = authState.uiAction,
        onClose = {action->
            onAuthEvent(AuthEvent.ClearUiAction)
            when(action){
                AuthUiAction.RefreshApp -> {
                    context.refreshApp()
                }
                AuthUiAction.ShowBackupSectionForLogin -> {
                    onEvent(SettingEvent.ShowSheet(
                        SettingSheetEvent.BackupSectionInit {
                            onAuthEvent(AuthEvent.LoadLastBackup)
                        })
                    )
                }
            }
        }
    )

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onEvent(SettingEvent.ClearMessage) }
    )

    ShowLifecycleToastMessage(
        message = authState.message,
        onDismiss = { onAuthEvent(AuthEvent.ClearMessage) }
    )

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
                    (state.dialogEvent is SettingDialogEvent.ShowPremiumDia)
        }
    }

    LaunchedEffect(isPremiumPurchased,lifecycle){
        snapshotFlow { isPremiumPurchased }
            .flowWithLifecycle(lifecycle)
            .collectLatest {
                if(it){
                    onEvent(SettingEvent.ShowDialog(null))
                }
            }
    }

}
