package com.masterplus.trdictionary.features.list.presentation.show_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.masterplus.trdictionary.core.domain.util.ToastHelper
import com.masterplus.trdictionary.core.extensions.isScrollingUp
import com.masterplus.trdictionary.core.presentation.selectors.CustomDropdownBarMenu
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.selectors.SelectMenuItemBottomContent
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog

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
    val context = LocalContext.current

    val lazyGridState = rememberLazyGridState()
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val isScrollingUp = lazyGridState.isScrollingUp()

    state.message?.let { message->
        LaunchedEffect(message){
            ToastHelper.showMessage(message,context)
            onEvent(ShowListEvent.ClearMessage)
        }
    }

    LaunchedEffect(key1 = isScrollingUp){
        println("isScrollingUp: $isScrollingUp")
    }


    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.list),
                scrollBehavior = topAppBarScrollBehavior,
                actions = {

                    IconButton(onClick = {
                        onNavigateToArchive()
                    }){
                        Icon(painterResource(R.drawable.ic_baseline_archive_24),contentDescription = null)
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
        },
        floatingActionButton = {
            GetFab(
                isVisible = lazyGridState.isScrollingUp(),
                onEvent = onEvent
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
                                if(windowWidthSizeClass == WindowWidthSizeClass.Expanded){
                                    CustomDropdownBarMenu(
                                        items = ShowListBottomMenuEnum.from(item.isRemovable),
                                        onItemChange = {menuItem->
                                            handleSelectMenuItem(menuItem,onEvent,item)
                                        }
                                    )
                                }else{
                                    IconButton(onClick = {
                                        onEvent(ShowListEvent.ShowModal(true,
                                            ShowListModelEvent.ShowSelectBottomMenu(item)))
                                    }){
                                        Icon(painter = painterResource(R.drawable.ic_baseline_more_vert_24),contentDescription = null)
                                    }
                                }
                            },
                        )
                    }
                }
            )
        }
    }

    if(state.showModal){
        ShowModal(
            state.modalEvent,
            onEvent = {onEvent(it)}
        )
    }else if(state.showDialog){
        ShowDialog(
            event = state.dialogEvent,
            onEvent = {onEvent(it)}
        )
    }

}


@Composable
private fun GetFab(
    isVisible: Boolean,
    onEvent: (ShowListEvent)->Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = {
                onEvent(
                    ShowListEvent.ShowDialog(true,
                        ShowListDialogEvent.TitleToAddList
                    ))
            },
        ){
            Icon(Icons.Default.Add,contentDescription = stringResource(R.string.add_list))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowModal(
    event: ShowListModelEvent?,
    onEvent: (ShowListEvent)->Unit
){
    CustomModalBottomSheet(
        onDismissRequest = {onEvent(ShowListEvent.ShowModal(false))},
        skipHalfExpanded = true
    ){
        when(event){
            is ShowListModelEvent.ShowSelectBottomMenu -> {
                SelectMenuItemBottomContent(
                    title = stringResource(R.string.n_for_list_item,event.listView.name),
                    items = ShowListBottomMenuEnum.from(event.listView.isRemovable),
                    onClickItem = {selected->
                        onEvent(ShowListEvent.ShowModal(false))
                        handleSelectMenuItem(selected,onEvent,event.listView)
                    }
                )
            }
            null -> {

            }
        }
    }
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












