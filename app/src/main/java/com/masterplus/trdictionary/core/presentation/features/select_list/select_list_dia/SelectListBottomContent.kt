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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.components.buttons.SecondaryLightButton
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.features.select_list.components.SelectListItem
import com.masterplus.trdictionary.core.util.SampleDatas
import com.masterplus.trdictionary.features.list.domain.model.SelectableListView
import java.util.UUID
import kotlin.random.Random


@ExperimentalFoundationApi
@Composable
fun SelectListBottomContent(
    wordId: Int,
    listIdControl: Int? = null,
    onClosed: () -> Unit,
    title: String = stringResource(id = R.string.add_to_list),
    listViewModel: SelectListViewModel = hiltViewModel()
){
    SelectListBottomContent(
        wordId = wordId,
        listIdControl = listIdControl,
        onClosed = onClosed,
        title = title,
        state = listViewModel.state,
        onEvent = listViewModel::onEvent
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
fun SelectListBottomContent(
    wordId: Int,
    listIdControl: Int? = null,
    onClosed: () -> Unit,
    title: String = stringResource(id = R.string.add_to_list),
    state: SelectListState,
    onEvent: (SelectListEvent) -> Unit,
){
    var showTitleDialog by rememberSaveable{
        mutableStateOf(false)
    }

    LaunchedEffect(wordId,listIdControl){
        onEvent(SelectListEvent.LoadData(wordId,listIdControl))
    }

    Column(
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        DialogHeader(
            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp),
            title = title,
            onIconClick = onClosed
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            item {

                OutlinedButton(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .padding(bottom = 4.dp)
                        .fillMaxWidth(),
                    onClick = { showTitleDialog = true }
                ) {
                    Text(text = stringResource(R.string.add_list),)
                }
            }

            item {
                if(state.items.isEmpty()){
                    Text(
                        stringResource(R.string.list_empty_text),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
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
                        onEvent(SelectListEvent.AddOrAskToList(wordId,item))
                    },
                    modifier = Modifier
                        .padding(vertical = 1.dp)
                        .fillMaxWidth()
                )
            }

        }
    }

    if(showTitleDialog){
        ShowGetTextDialog(
            title = stringResource(R.string.enter_title),
            onClosed = {showTitleDialog = false},
            onApproved = {onEvent(SelectListEvent.NewList(it))},
        )
    }

    if(state.showDialog){
        when(val event = state.dialogEvent){
            is SelectListDialogEvent.AskListDelete -> {
                ShowQuestionDialog(
                    title = stringResource(R.string.question_remove_item_from_list),
                    content = stringResource(R.string.affect_current_list),
                    onClosed = {onEvent(SelectListEvent.ShowDialog(false))},
                    onApproved = {
                        onEvent(SelectListEvent.AddToList(event.wordId,event.listView))
                    }
                )
            }
            null -> {}
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun SelectListBottomContentPreview() {
    val items = SampleDatas.selectableListViewArr

    SelectListBottomContent(
        1,
        2,
        onClosed = {},
        state = SelectListState(items = items, listIdControl = 2),
        onEvent = {}
    )
}

