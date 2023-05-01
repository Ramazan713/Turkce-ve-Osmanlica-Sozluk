package com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.buttons.SecondaryLightButton
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.features.select_list.components.SelectListItem

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
fun SelectListBottomContent(
    wordId: Int,
    listIdControl: Int? = null,
    onClosed: ()->Unit,
    listViewModel: SelectListViewModel = hiltViewModel()
){

    val showTitleDialog = rememberSaveable{
        mutableStateOf(false)
    }
    val state = listViewModel.state

    LaunchedEffect(wordId,listIdControl){
        listViewModel.onEvent(SelectListEvent.LoadData(wordId,listIdControl))
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onClosed){
                Icon(Icons.Default.Close,contentDescription = null)
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            item {
                SecondaryLightButton(
                    title = stringResource(R.string.add_list),
                    useBorder = true,
                    onClick = {showTitleDialog.value = true},
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                        .fillMaxWidth()
                )
            }

            item {
                if(state.items.isEmpty()){
                    Text(
                        stringResource(R.string.list_empty_text),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 70.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(
                state.items,
                key = {item->item.listView.id?:0}
            ){item->
                SelectListItem(
                    selectableListView = item,
                    isSelected = state.listIdControl == item.listView.id,
                    onChecked = {
                        listViewModel.onEvent(SelectListEvent.AddOrAskToList(wordId,item))
                    },
                    modifier = Modifier.padding(horizontal = 3.dp,
                        vertical = 1.dp).fillMaxWidth()
                )
            }

        }
    }

    if(showTitleDialog.value){
        ShowGetTextDialog(
            title = stringResource(R.string.enter_title),
            onClosed = {showTitleDialog.value = false},
            onApproved = {listViewModel.onEvent(SelectListEvent.NewList(it))},
        )
    }

    if(state.showDialog){
        when(val event = state.dialogEvent){
            is SelectListDialogEvent.AskListDelete -> {
                ShowQuestionDialog(
                    title = stringResource(R.string.question_remove_item_from_list),
                    content = stringResource(R.string.affect_current_list),
                    onClosed = {listViewModel.onEvent(SelectListEvent.ShowDialog(false))},
                    onApproved = {
                        listViewModel.onEvent(SelectListEvent.AddToList(event.wordId,event.listView))
                    }
                )
            }
            null -> {}
        }
    }

}


