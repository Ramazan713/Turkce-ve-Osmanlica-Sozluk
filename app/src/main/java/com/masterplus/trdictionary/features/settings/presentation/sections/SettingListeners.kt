package com.masterplus.trdictionary.features.settings.presentation.sections

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import com.masterplus.trdictionary.core.extensions.refreshApp
import com.masterplus.trdictionary.core.presentation.utils.EventHandler
import com.masterplus.trdictionary.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthEvent
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthState
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthUiAction
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.handlers.ListenAuthUiAction
import com.masterplus.trdictionary.core.shared_features.premium.PremiumEvent
import com.masterplus.trdictionary.core.shared_features.premium.PremiumState
import com.masterplus.trdictionary.core.shared_features.premium.PremiumUiEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingSheetEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState

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
) {
    val context = LocalContext.current

    LaunchedEffect(true) {
        onEvent(SettingEvent.LoadData)
    }

    ListenAuthUiAction(
        uiAction = authState.uiAction,
        onClose = { action ->
            onAuthEvent(AuthEvent.ClearUiAction)
            when (action) {
                AuthUiAction.RefreshApp -> {
                    context.refreshApp()
                }

                AuthUiAction.ShowBackupSectionForLogin -> {
                    onEvent(
                        SettingEvent.ShowSheet(
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

    EventHandler(event = premiumState.uiEvent) { uiEvent ->
        when (uiEvent) {
            is PremiumUiEvent.LaunchBillingFlow -> {
                uiEvent.billingClient.launchBillingFlow(
                    context as Activity,
                    uiEvent.billingFlowParams
                )
                onPremiumEvent(PremiumEvent.ClearUiEvent)
            }
        }
    }

    ShowLifecycleToastMessage(
        message = premiumState.message,
        onDismiss = { onPremiumEvent(PremiumEvent.ClearMessage) }
    )

    val isPremiumPurchased by remember(premiumState.isPremium) {
        derivedStateOf {
            premiumState.isPremium &&
                    (state.dialogEvent is SettingDialogEvent.ShowPremiumDia)
        }
    }

    EventHandler(event = isPremiumPurchased) {
        if (it) {
            onEvent(SettingEvent.ShowDialog(null))
        }
    }
}
