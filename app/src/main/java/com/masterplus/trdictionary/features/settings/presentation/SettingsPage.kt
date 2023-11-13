package com.masterplus.trdictionary.features.settings.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.premium.*
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.shared_features.premium.PremiumEvent
import com.masterplus.trdictionary.core.shared_features.premium.PremiumState
import com.masterplus.trdictionary.core.util.PreviewDesktop
import com.masterplus.trdictionary.core.util.PreviewTablet
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthEvent
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthState
import com.masterplus.trdictionary.features.settings.presentation.sections.*

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    onNavigateBack: ()->Unit,
    premiumState: PremiumState,
    onPremiumEvent: (PremiumEvent)->Unit,
    premiumProduct: PremiumProduct?,
    state: SettingState,
    onEvent: (SettingEvent)->Unit,
    authState: AuthState,
    onAuthEvent: (AuthEvent) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
){
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    SettingListeners(
        premiumState = premiumState,
        onPremiumEvent = onPremiumEvent,
        state = state,
        onEvent = onEvent,
        authState = authState,
        onAuthEvent = onAuthEvent
    )


    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.settings),
                onNavigateBack = onNavigateBack,
                scrollBehavior = topAppBarScrollBehavior
            )
        },
    ){paddings->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            columns = StaggeredGridCells.Adaptive(400.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalItemSpacing = 10.dp
        ){

            item(span = StaggeredGridItemSpan.FullLine) {
                ProfileSettingSection(
                    user = authState.user,
                    onEvent = onEvent
                )
            }
            item {
                GeneralSettingSection(
                    state = state,
                    onEvent = onEvent
                )
            }
            if(authState.user != null){
                item {
                    CloudBackupSection {
                        onEvent(it)
                    }
                }
            }

            item {
                PremiumSection(
                    premiumProduct = premiumProduct,
                    isPremium = premiumState.isPremium,
                    onEvent = onEvent
                )
            }
            item {
                AdvancedSettingSection(
                    state = state,
                    onEvent = onEvent
                )
            }
            item {
                ApplicationSettingSection()
            }
            if(authState.user != null){
                item {
                    TextButton(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        onClick = {
                            onEvent(SettingEvent.ShowDialog(SettingDialogEvent.AskSignOut))
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.sign_out),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
            }
        }
    }


    state.dialogEvent?.let { dialogEvent->
        ShowSettingDialog(
            state = state,
            onEvent = onEvent,
            onPremiumProductClicked = { product,offerToken->
                onPremiumEvent(PremiumEvent.Purchase(product, offerToken))
            },
            windowWidthSizeClass = windowWidthSizeClass,
            onAuthEvent = onAuthEvent,
            authState = authState,
            dialogEvent = dialogEvent
        )
    }

    state.sheetEvent?.let { sheetEvent->
        ShowSettingModal(
            sheetEvent = sheetEvent,
            onEvent = onEvent
        )
    }

    if(state.isLoading){
        LoadingDialog()
    }

    if(authState.isLoading){
        LoadingDialog()
    }

}


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@PreviewDesktop
@PreviewTablet
@Preview(showBackground = true)
@Composable
fun SettingsPagePreview() {
    SettingsPage(
        onEvent = {},
        onNavigateBack = {},
        onPremiumEvent = {},
        premiumState = PremiumState(),
        premiumProduct = null,
        state = SettingState(),
        windowWidthSizeClass = WindowWidthSizeClass.Compact,
        authState = AuthState(),
        onAuthEvent = {}
    )
}













