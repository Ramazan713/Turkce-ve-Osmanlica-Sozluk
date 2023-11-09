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
import com.masterplus.trdictionary.features.settings.domain.model.User
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
            if(state.user != null){
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
            if(state.user != null){
                item {
                    TextButton(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        onClick = {
                            onEvent(SettingEvent.ShowDialog(true,
                                SettingDialogEvent.AskSignOut))
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
        state = SettingState(user = User("asd","email@gmail.com",null,"myName")),
        windowWidthSizeClass = WindowWidthSizeClass.Compact
    )
}













