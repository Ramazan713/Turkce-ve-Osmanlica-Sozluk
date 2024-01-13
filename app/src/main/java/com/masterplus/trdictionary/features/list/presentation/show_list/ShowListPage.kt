package com.masterplus.trdictionary.features.list.presentation.show_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.extensions.isScrollingUp
import com.masterplus.trdictionary.core.presentation.selections.AdaptiveSelectSheetMenu
import com.masterplus.trdictionary.core.presentation.selections.CustomDropdownBarMenu
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.components.DefaultToolTip
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.selections.rememberAdaptiveSelectMenuState
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.utils.ShowLifecycleSnackBarMessage
import com.masterplus.trdictionary.core.presentation.utils.rememberDefaultSnackBar

import com.masterplus.trdictionary.features.list.presentation.components.ListViewItem
import com.masterplus.trdictionary.features.list.presentation.show_list.constants.ShowListBarMenuEnum
import com.masterplus.trdictionary.features.list.presentation.show_list.constants.ShowListBottomMenuEnum


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun ShowListPage(
    onNavigateToArchive: ()->Unit,
    onNavigateToDetailList: (listId: Int)->Unit,
    onNavigateToSelectSavePoint: (String,List<Int>,Int)->Unit,
    onNavigateToSettings: ()->Unit,
    state: ShowListState,
    onEvent: (ShowListEvent) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass
){

    val lazyGridState = rememberLazyGridState()
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isScrollingUp = lazyGridState.isScrollingUp()

    val selectSheetState = rememberAdaptiveSelectMenuState<ShowListBottomMenuEnum>(
        windowWidthSizeClass = windowWidthSizeClass,
        onItemChange = { menuItem, key->
            state.items.find { it.id?.toString() == key }?.let { item->
                handleSelectMenuItem(menuItem,onEvent,item)
            }
        }
    )

    val defaultSnackBar = rememberDefaultSnackBar()

    ShowLifecycleSnackBarMessage(
        message = state.message,
        snackBar = defaultSnackBar,
        onDismiss = { onEvent(ShowListEvent.ClearMessage) }
    )

    Scaffold(
        snackbarHost = defaultSnackBar.snackBarHost,
        topBar = {
            GetTopBar(
                topAppBarScrollBehavior = topAppBarScrollBehavior,
                onNavigateToArchive = onNavigateToArchive,
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToSelectSavePoint = onNavigateToSelectSavePoint
            )
        },
        floatingActionButton = {
            GetFab(
                isScrollingUp = isScrollingUp,
                onEvent = onEvent,
                windowWidthSizeClass = windowWidthSizeClass
            )
        },
    ){paddings->

        Box(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        ) {
            if(state.items.isEmpty()){
                Text(
                    stringResource(R.string.list_empty_text),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = lazyGridState,
                columns = GridCells.Adaptive(300.dp),
                content = {
                    items(
                        state.items,
                        key = {item->item.id?:0}
                    ){item->
                        ListViewItem(
                            listView = item,
                            onClick = {
                                onNavigateToDetailList(item.id?:0)
                            },
                            trailingItem = {
                                AdaptiveSelectSheetMenu(
                                    state = selectSheetState,
                                    items = ShowListBottomMenuEnum.from(item.isRemovable),
                                    key = item.id?.toString(),
                                    sheetTitle = stringResource(R.string.n_for_list_item, item.name),
                                    contentDescription = stringResource(id = R.string.menu_n, item.name)
                                )
                            },
                        )
                    }
                }
            )
        }
    }

    selectSheetState.showSheet()

    if(state.showDialog){
        ShowDialog(
            event = state.dialogEvent,
            onEvent = {onEvent(it)}
        )
    }

}


@Composable
private fun GetFab(
    isScrollingUp: Boolean,
    onEvent: (ShowListEvent) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    val expandedSizeClass = WindowWidthSizeClass.Expanded == windowWidthSizeClass
    val expanded = expandedSizeClass && isScrollingUp

    AnimatedVisibility(
        visible = isScrollingUp || expandedSizeClass,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        DefaultToolTip(
            tooltip = stringResource(R.string.add_list),
            enabled = !expanded
        ) {
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                icon = { Icon(Icons.Default.Add,contentDescription = stringResource(R.string.add_list)) },
                text = { Text(text = stringResource(id = R.string.add))},
                expanded = expanded,
                onClick = { onEvent(ShowListEvent.ShowDialog(true, ShowListDialogEvent.TitleToAddList)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GetTopBar(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onNavigateToArchive: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSelectSavePoint: (String,List<Int>,Int)->Unit,
) {
    val context = LocalContext.current

    CustomTopAppBar(
        title = stringResource(R.string.list),
        scrollBehavior = topAppBarScrollBehavior,
        actions = {

            DefaultToolTip(
                tooltip = stringResource(id = R.string.archive_n),
                spacingBetweenTooltipAndAnchor = 8.dp,
            ) {
                IconButton(onClick = onNavigateToArchive){
                    Icon(
                        painterResource(R.drawable.ic_baseline_archive_24),
                        contentDescription = stringResource(id = R.string.archive_n)
                    )
                }
            }

            CustomDropdownBarMenu(
                items = ShowListBarMenuEnum.values().toList(),
                onItemChange = {menuItem->
                    when(menuItem){
                        ShowListBarMenuEnum.ShowSelectSavePoint -> {
                            onNavigateToSelectSavePoint(
                                context.getString(R.string.list),
                                listOf(SavePointDestination.List.destinationId),
                                SavePointType.List.typeId
                            )
                        }
                        ShowListBarMenuEnum.Settings -> {onNavigateToSettings()}
                    }
                },
            )
        }
    )
}


private fun handleSelectMenuItem(
    menuEnum: ShowListBottomMenuEnum?,
    onEvent: (ShowListEvent)->Unit,
    listView: ListView
){
    when(menuEnum){
        ShowListBottomMenuEnum.Delete->{
            onEvent(
                ShowListEvent.ShowDialog(true,
                    ShowListDialogEvent.AskDelete(listView),
                ))
        }
        ShowListBottomMenuEnum.Rename -> {
            onEvent(
                ShowListEvent.ShowDialog(true,
                    ShowListDialogEvent.Rename(listView),
                ))
        }
        ShowListBottomMenuEnum.Archive -> {
            onEvent(
                ShowListEvent.ShowDialog(true,
                    ShowListDialogEvent.AskArchive(listView),
                ))
        }
        ShowListBottomMenuEnum.Copy -> {
            onEvent(
                ShowListEvent.ShowDialog(true,
                    ShowListDialogEvent.AskCopy(listView),
                ))
        }
        null -> {}
    }
}


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun ShowDialog(
    event: ShowListDialogEvent?,
    onEvent: (ShowListEvent)->Unit
){
    when(event){
        is ShowListDialogEvent.AskDelete -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_delete),
                content = stringResource(R.string.not_revertable),
                onApproved = {onEvent(ShowListEvent.Delete(event.listView))},
                onClosed = {onEvent(ShowListEvent.ShowDialog(false))}
            )
        }
        is ShowListDialogEvent.TitleToAddList -> {
            ShowGetTextDialog(
                title = stringResource(R.string.enter_title),
                onClosed = {onEvent(ShowListEvent.ShowDialog(false))},
                onApproved = {onEvent(ShowListEvent.AddNewList(it))}
            )
        }
        null -> {}
        is ShowListDialogEvent.AskArchive -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_archive_title),
                content = stringResource(R.string.question_archive_content),
                onApproved = {onEvent(ShowListEvent.Archive(event.listView))},
                onClosed = {onEvent(ShowListEvent.ShowDialog(false))}
            )
        }
        is ShowListDialogEvent.AskCopy -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_copy),
                onApproved = {onEvent(ShowListEvent.Copy(event.listView))},
                onClosed = {onEvent(ShowListEvent.ShowDialog(false))}
            )
        }
        is ShowListDialogEvent.Rename -> {
            ShowGetTextDialog(
                title = stringResource(R.string.rename),
                content = event.listView.name,
                onClosed = {onEvent(ShowListEvent.ShowDialog(false))},
                onApproved = {onEvent(ShowListEvent.Rename(event.listView,it))}
            )
        }
    }
}












