package com.masterplus.trdictionary.features.settings.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.premium.*
import com.masterplus.trdictionary.core.presentation.components.buttons.NegativeButton
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.presentation.features.premium.*
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
    windowWidthSizeClass: WindowWidthSizeClass,
){
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    SettingListeners(
        premiumState = premiumState,
        onPremiumEvent = onPremiumEvent,
        state = state,
        onEvent = onEvent
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.settings),
                onNavigateBack = onNavigateBack,
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ){paddings->
        LazyColumn(
            modifier = Modifier.padding(paddings)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ){

            item {
                ProfileSettingSection(
                    state = state,
                    onEvent = onEvent
                )
            }
            item {
                GeneralSettingSection(
                    state = state,
                    onEvent = onEvent
                )
            }
            item {
                if(state.user!=null){
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
            item {
                if(state.user!=null){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        NegativeButton(
                            title = stringResource(R.string.sign_out),
                            onClick = {
                                onEvent(SettingEvent.ShowDialog(true,
                                SettingDialogEvent.AskSignOut))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

            }
        }
    }

    if(state.isLoading){
        LoadingDialog()
    } else if(state.showDialog){
        ShowSettingDialog(
            state = state,
            onEvent = onEvent,
            onPremiumProductClicked = { product,offerToken->
                onPremiumEvent(PremiumEvent.Purchase(product, offerToken))
            },
            windowWidthSizeClass = windowWidthSizeClass
        )
    }else if(state.showModal){
        ShowSettingModal(
            state = state,
            onEvent = onEvent
        )
    }

}
















