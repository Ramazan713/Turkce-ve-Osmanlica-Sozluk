package com.masterplus.trdictionary.features.list.presentation.archive_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.features.list.presentation.archive_list.constants.ArchiveListBottomMenuEnum
import com.masterplus.trdictionary.features.list.presentation.components.ListViewItem
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.presentation.selections.AdaptiveSelectSheetMenu
import com.masterplus.trdictionary.core.presentation.selections.rememberAdaptiveSelectMenuState
import com.masterplus.trdictionary.core.util.ShowLifecycleSnackBarMessage
import com.masterplus.trdictionary.core.util.rememberDefaultSnackBar

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ArchiveListPage(
    onNavigateBack: ()->Unit,
    onNavigateToDetailList: (listId: Int)->Unit,
    state: ArchiveListState,
    onEvent: (ArchiveListEvent) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass
){
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val selectSheetState = rememberAdaptiveSelectMenuState<ArchiveListBottomMenuEnum>(
        windowWidthSizeClass = windowWidthSizeClass,
        onItemChange = { menuItem, key->
            state.items.find { it.id?.toString() == key }?.let { item->
                handleMenu(menuItem,onEvent,item)
            }
        }
    )


    val defaultSnackBar = rememberDefaultSnackBar()

    ShowLifecycleSnackBarMessage(
        message = state.message,
        snackBar = defaultSnackBar,
        onDismiss = { onEvent(ArchiveListEvent.ClearMessage) }
    )

    Scaffold(
        snackbarHost = defaultSnackBar.snackBarHost,
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.archive_n),
                onNavigateBack = onNavigateBack,
                scrollBehavior = topAppBarScrollBehavior
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
                    stringResource(R.string.archive_empty_text),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
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
                                    items = ArchiveListBottomMenuEnum.values().toList(),
                                    sheetTitle = stringResource(R.string.n_for_list_item,item.name),
                                    key = item.id?.toString()
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


private fun handleMenu(
    menuItem: ArchiveListBottomMenuEnum,
    onEvent: (ArchiveListEvent) -> Unit,
    listView: ListView
){
    when(menuItem){
        ArchiveListBottomMenuEnum.Delete->{
            onEvent(
                ArchiveListEvent.ShowDialog(true,
                    ArchiveListDialogEvent.AskDelete(listView),
                ))
        }
        ArchiveListBottomMenuEnum.Rename -> {
            onEvent(
                ArchiveListEvent.ShowDialog(true,
                    ArchiveListDialogEvent.Rename(listView),
                ))
        }
        ArchiveListBottomMenuEnum.UnArchive -> {
            onEvent(
                ArchiveListEvent.ShowDialog(true,
                    ArchiveListDialogEvent.AskUnArchive(listView),
                ))
        }
    }
}


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun ShowDialog(
    event: ArchiveListDialogEvent?,
    onEvent: (ArchiveListEvent)->Unit
){
    when(event){
        is ArchiveListDialogEvent.AskDelete -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_delete),
                content = stringResource(R.string.not_revertable),
                onApproved = {onEvent(ArchiveListEvent.Delete(event.listView))},
                onClosed = {onEvent(ArchiveListEvent.ShowDialog(false))}
            )
        }
        null -> {}
        is ArchiveListDialogEvent.AskUnArchive -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_unarchive),
                onApproved = {onEvent(ArchiveListEvent.UnArchive(event.listView))},
                onClosed = {onEvent(ArchiveListEvent.ShowDialog(false))}
            )
        }
        is ArchiveListDialogEvent.Rename -> {
            ShowGetTextDialog(
                title = stringResource(R.string.rename),
                content = event.listView.name,
                onClosed = {onEvent(ArchiveListEvent.ShowDialog(false))},
                onApproved = {onEvent(ArchiveListEvent.Rename(event.listView,it))}
            )
        }
    }
}
