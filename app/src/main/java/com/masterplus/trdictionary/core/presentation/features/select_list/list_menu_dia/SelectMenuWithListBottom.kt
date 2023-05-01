package com.masterplus.trdictionary.core.presentation.features.select_list.list_menu_dia

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.dialog_body.SelectMenuItemBottomContent
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia.SelectListBottomContent
import com.masterplus.trdictionary.core.presentation.features.select_list.constants.SelectListMenuEnum

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun SelectMenuWithListBottom(
    items: List<IMenuItemEnum>,
    wordId: Int,
    listIdControl: Int? = null,
    onClickItem:(IMenuItemEnum?)->Unit,
    title: String? = null,
    listViewModel: SelectListMenuViewModel = hiltViewModel()
){

    val state = listViewModel.state

    val showSelectListDia = rememberSaveable{
        mutableStateOf(false)
    }

    val newItems = remember {
        mutableStateOf(emptyList<IMenuItemEnum>())
    }

    LaunchedEffect(wordId,listIdControl){
        listViewModel.onEvent(SelectListMenuEvent.LoadData(wordId, listIdControl))
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
        onClickItem = {menuItem->
            when(menuItem){
                SelectListMenuEnum.AddList->{
                    showSelectListDia.value = true
                }
                SelectListMenuEnum.AddedList->{
                    showSelectListDia.value = true
                }
                SelectListMenuEnum.AddFavorite->{
                    listViewModel.onEvent(SelectListMenuEvent.AddToFavorite(wordId))
                }
                SelectListMenuEnum.AddedFavorite->{
                    listViewModel.onEvent(SelectListMenuEvent.AddOrAskFavorite(wordId))
                }
                else->{
                    onClickItem(menuItem)
                }
            }
        }
    )

    if(showSelectListDia.value){
        CustomModalBottomSheet(
            onDismissRequest = {
                showSelectListDia.value = false
            },
        ){
            SelectListBottomContent(
                wordId = wordId,
                listIdControl = listIdControl,
                onClosed = {showSelectListDia.value = false}
            )
        }
    }

    if(state.showDialog){
        when(val event = state.dialogEvent){
            is SelectListMenuDialogEvent.AskFavoriteDelete -> {
                ShowQuestionDialog(
                    title = stringResource(R.string.question_remove_list_from_favorite),
                    content = stringResource(R.string.affect_current_list),
                    onClosed = {listViewModel.onEvent(SelectListMenuEvent.ShowDialog(false))},
                    onApproved = {
                        listViewModel.onEvent(SelectListMenuEvent.AddToFavorite(event.wordId))
                    }
                )
            }
            null -> {}
        }
    }


}