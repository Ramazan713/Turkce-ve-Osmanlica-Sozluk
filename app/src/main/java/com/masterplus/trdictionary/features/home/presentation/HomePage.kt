package com.masterplus.trdictionary.features.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.CustomDropdownBarMenu
import com.masterplus.trdictionary.core.presentation.components.EmptySearchField
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.features.premium.ShowPremiumActiveDialog
import com.masterplus.trdictionary.features.home.domain.constants.HomeTopBarMenuEnum
import com.masterplus.trdictionary.features.home.presentation.components.*


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun HomePage(
    onNavigateToWordDetail: (Int)->Unit,
    onNavigateToSearch: ()->Unit,
    onNavigateToSetting: ()->Unit,
    state: HomeState,
    onEvent: (HomeEvent)->Unit,
    isPremium: Boolean
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val showPremiumActive = rememberSaveable{
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                actions = {
                    AnimatedVisibility(isPremium){
                        IconButton(onClick = {showPremiumActive.value = true}){
                            Icon(painter = painterResource(R.drawable.workspace_premium_black_24dp),
                                contentDescription = stringResource(R.string.premium_active_c))
                        }
                    }
                    EmptySearchField(
                        title = stringResource(R.string.search),
                        onClicked = onNavigateToSearch,
                        modifier = Modifier.weight(1f)
                    )
                    CustomDropdownBarMenu(
                        items = HomeTopBarMenuEnum.values().toList(),
                        onItemChange = {menuItem->
                            when(menuItem){
                                HomeTopBarMenuEnum.Setting -> onNavigateToSetting()
                            }
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
//        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ){paddings->

        LazyColumn(
            modifier = Modifier.padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection)

                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(13.dp),
            contentPadding = PaddingValues(horizontal = 7.dp, vertical = 13.dp)
        ){
            item {
                state.wordShortInfo.let { info->
                    ShortInfoGroup(
                        title = stringResource(R.string.one_word_c),
                        infoModel = info,
                        modifier = Modifier.animateContentSize(),
                        onClicked = {
                            onNavigateToWordDetail(it)
                        },
                        onRefreshClicked = {
                            onEvent(HomeEvent.RefreshShortInfo(info))
                        }
                    )
                }
            }

            item {
                state.proverbShortInfo.let { info->
                    ShortInfoGroup(
                        title = stringResource(R.string.one_proverb_c),
                        infoModel = info,
                        modifier = Modifier
                            .animateContentSize()
                            .animateItemPlacement(),
                        onClicked = {
                            onNavigateToWordDetail(it)
                        },
                        onRefreshClicked = {
                            onEvent(HomeEvent.RefreshShortInfo(info))
                        }
                    )
                }
            }

            item {
                state.idiomShortInfo.let { info->
                    ShortInfoGroup(
                        title = stringResource(R.string.one_idiom_c),
                        infoModel = info,
                        modifier = Modifier
                            .animateContentSize()
                            .animateItemPlacement(),
                        onClicked = {
                            onNavigateToWordDetail(it)
                        },
                        onRefreshClicked = {
                            onEvent(HomeEvent.RefreshShortInfo(info))
                        }
                    )
                }
            }
        }
    }
    if(showPremiumActive.value){
        ShowPremiumActiveDialog { showPremiumActive.value = false }
    }

}