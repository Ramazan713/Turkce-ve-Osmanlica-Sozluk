package com.masterplus.trdictionary.core.presentation.features.select_list.list_menu_dia

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.selections.SelectMenuItemBottomContent
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia.SelectListBottomContent
import com.masterplus.trdictionary.core.presentation.features.select_list.constants.SelectListMenuEnum


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun <T: IMenuItemEnum> SelectMenuWithListBottom(
    items: List<T>,
    wordId: Int,
    listIdControl: Int? = null,
    onClickItem: (T) -> Unit,
    onClose: () -> Unit,
    title: String? = null,
    selectListTitle: String = stringResource(id = R.string.add_to_list),
    listViewModel: SelectListMenuViewModel = hiltViewModel()
){

    SelectMenuWithListBottom(
        items = items,
        wordId = wordId,
        listIdControl = listIdControl,
        onClickItem = onClickItem,
        title = title,
        selectListTitle = selectListTitle,
        onClose = onClose,
        state = listViewModel.state,
        onEvent = listViewModel::onEvent
    )
}

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun <T: IMenuItemEnum> SelectMenuWithListBottom(
    items: List<T>,
    wordId: Int,
    listIdControl: Int? = null,
    onClickItem: (T) -> Unit,
    onClose: () -> Unit,
    title: String? = null,
    selectListTitle: String = stringResource(id = R.string.add_to_list),
    state: SelectListMenuState,
    onEvent: (SelectListMenuEvent) -> Unit,
){
    var showSelectListDia by rememberSaveable{
        mutableStateOf(false)
    }

    val newItems = remember {
        mutableStateOf(emptyList<IMenuItemEnum>())
    }

    LaunchedEffect(wordId,listIdControl){
        onEvent(SelectListMenuEvent.LoadData(wordId, listIdControl))
    }

    LaunchedEffect(items,state.listMenuItems){
        newItems.value = mutableListOf<IMenuItemEnum>().apply {
            addAll(state.listMenuItems)
            addAll(items)
        }
    }

    SelectMenuItemBottomContent(
        items = newItems.value,
        title = title,
        onClose = onClose,
        onClickItem = {menuItem->
            when(menuItem){
                SelectListMenuEnum.AddList->{
                    showSelectListDia = true
                }
                SelectListMenuEnum.AddedList->{
                    showSelectListDia = true
                }
                SelectListMenuEnum.AddFavorite->{
                    onEvent(SelectListMenuEvent.AddToFavorite(wordId))
                }
                SelectListMenuEnum.AddedFavorite->{
                    onEvent(SelectListMenuEvent.AddOrAskFavorite(wordId))
                }
                else->{
                    (menuItem as? T)?.let(onClickItem)
                }
            }
        }
    )

    if(showSelectListDia){
        CustomModalBottomSheet(
            onDismissRequest = {
                showSelectListDia = false
            },
        ){
            SelectListBottomContent(
                wordId = wordId,
                listIdControl = listIdControl,
                title = selectListTitle,
                onClosed = {showSelectListDia = false}
            )
        }
    }

    if(state.showDialog){
        when(val event = state.dialogEvent){
            is SelectListMenuDialogEvent.AskFavoriteDelete -> {
                ShowQuestionDialog(
                    title = stringResource(R.string.question_remove_list_from_favorite),
                    content = stringResource(R.string.affect_current_list),
                    onClosed = {onEvent(SelectListMenuEvent.ShowDialog(false))},
                    onApproved = {
                        onEvent(SelectListMenuEvent.AddToFavorite(event.wordId))
                    }
                )
            }
            null -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SelectMenuWithListBottomPreview() {
    SelectMenuWithListBottom(
        items = listOf(),
        wordId = 1,
        onClickItem = {},
        state = SelectListMenuState(),
        onEvent = {},
        onClose = {}
    )
}



