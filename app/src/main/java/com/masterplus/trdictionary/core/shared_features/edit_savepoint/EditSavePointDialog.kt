
package com.masterplus.trdictionary.core.shared_features.edit_savepoint

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.presentation.components.SavePointItem
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.utils.SampleDatas
import com.masterplus.trdictionary.core.presentation.utils.ShowLifecycleToastMessage


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun EditSavePointDialog(
    destinationId: Int,
    saveKey: String,
    pos: Int,
    shortTitle: String,
    onClosed: () -> Unit,
    onNavigateLoad: (SavePoint) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    editViewModel: EditSavePointViewModel = hiltViewModel()
){
    val state by editViewModel.state.collectAsStateWithLifecycle()
    EditSavePointDialog(
        destinationId = destinationId,
        saveKey = saveKey,
        pos = pos,
        shortTitle = shortTitle,
        onClosed = onClosed,
        onNavigateLoad = onNavigateLoad,
        windowWidthSizeClass = windowWidthSizeClass,
        state = state,
        onEvent = editViewModel::onEvent
    )
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun EditSavePointDialog(
    destinationId: Int,
    saveKey: String,
    pos: Int,
    shortTitle: String,
    onClosed: () -> Unit,
    onNavigateLoad: (SavePoint) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    state: EditSavePointState,
    onEvent: (EditSavePointEvent) -> Unit
){
    LaunchedEffect(saveKey){
        onEvent(EditSavePointEvent.LoadData(saveKey))
    }

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onEvent(EditSavePointEvent.ClearMessage) }
    )

    CustomDialog(
        onClosed = onClosed,
        modifier = Modifier,
        adaptiveWidthSizeClass = windowWidthSizeClass
    ){
        Column(
            modifier = Modifier
                .padding(horizontal = 2.dp, vertical = 2.dp)
        ){
            DialogHeader(
                title = stringResource(R.string.save_points_c),
                onIconClick = onClosed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f,fill = false),
                contentPadding = PaddingValues(bottom = 8.dp)
            ){
                item {
                    OutlinedButton(
                        onClick = {
                            onEvent(
                                EditSavePointEvent.ShowDialog(
                                    showDialog = true,
                                    EditSavePointDialogEvent.AddSavePointTitle(
                                        SavePoint.getTitle(
                                            shortTitle
                                        )
                                    )
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 7.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.add_new_savepoint))
                    }
                }
                if(state.savePoints.isEmpty()){
                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 24.dp)
                        ) {
                            Text(
                                stringResource(R.string.empty_savepoint),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }else{
                    items(
                        state.savePoints,
                        key = {item -> item.id ?: 0}
                    ){item->
                        SavePointItem(
                            savePoint = item,
                            isSelected = item == state.selectedSavePoint,
                            onClick = {
                                onEvent(EditSavePointEvent.Select(item))
                            },
                            onDeleteClick = {
                                onEvent(
                                    EditSavePointEvent.ShowDialog(
                                        showDialog = true,
                                        EditSavePointDialogEvent.AskDelete(item)
                                    )
                                )
                            },
                            onTitleEditClick = {
                                onEvent(
                                    EditSavePointEvent.ShowDialog(
                                        showDialog = true,
                                        EditSavePointDialogEvent.EditTitle(item)
                                    )
                                )
                            }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ){
                Button(
                    onClick = {onEvent(EditSavePointEvent.OverrideSavePoint(pos))  },
                    modifier = Modifier.weight(1f),
                    enabled = state.selectedSavePoint != null,
                ) {
                    Text(text = stringResource(R.string.override),)
                }
                Button(
                    onClick = {
                        state.selectedSavePoint?.let { savePoint ->
                            onNavigateLoad(savePoint)
                            onClosed()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = state.selectedSavePoint != null,
                ) {
                    Text(text = stringResource(R.string.load))
                }
            }
        }

        if(state.showDialog){
            ShowDialog(
                state.dialogEvent,
                onEvent = onEvent,
                pos, saveKey,destinationId
            )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun ShowDialog(
    event: EditSavePointDialogEvent?,
    onEvent: (EditSavePointEvent)->Unit,
    pos: Int, saveKey: String, destinationId: Int
){
    when(event){
        is EditSavePointDialogEvent.AskDelete -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_delete),
                content = stringResource(R.string.not_revertable),
                onApproved = {onEvent(EditSavePointEvent.Delete(event.savePoint))},
                onClosed = {onEvent(EditSavePointEvent.ShowDialog(false))}
            )
        }
        is EditSavePointDialogEvent.EditTitle -> {
            ShowGetTextDialog(
                title = stringResource(R.string.rename),
                content = event.savePoint.title,
                onApproved = {onEvent(EditSavePointEvent.EditTitle(it, event.savePoint))},
                onClosed = {onEvent(EditSavePointEvent.ShowDialog(false))}
            )
        }
        is EditSavePointDialogEvent.AddSavePointTitle -> {
            ShowGetTextDialog(
                title = stringResource(R.string.enter_title),
                content = event.title,
                onApproved = {
                    onEvent(
                        EditSavePointEvent.AddSavePoint(pos, it, destinationId, saveKey)
                    ) },
                onClosed = {onEvent(EditSavePointEvent.ShowDialog(false))}
            )
        }
        else -> {}
    }
}



@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Preview(showBackground = true, heightDp = 300)
@Composable
fun EditSavePointPagePreview() {
    EditSavePointDialog(
        destinationId = 1,
        saveKey = "saveKey",
        pos = 2,
        shortTitle = "shortTitle",
        onClosed = {  },
        onNavigateLoad = {  },
        state = EditSavePointState(savePoints = SampleDatas.savePoints),
        windowWidthSizeClass = WindowWidthSizeClass.Expanded,
        onEvent = {  }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun EditSavePointPagePreview2() {

    EditSavePointDialog(
        destinationId = 1,
        saveKey = "saveKey",
        pos = 2,
        shortTitle = "shortTitle",
        onClosed = {  },
        onNavigateLoad = {  },
        state = EditSavePointState(),
        windowWidthSizeClass = WindowWidthSizeClass.Expanded,
        onEvent = {  }
    )
}
