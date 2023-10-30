package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.selectors.SelectMenuItemDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowSelectNumberDialog
import com.masterplus.trdictionary.core.extensions.visibleMiddlePosition
import com.masterplus.trdictionary.core.presentation.features.edit_savepoint.EditSavePointPage
import com.masterplus.trdictionary.features.word_detail.domain.constants.ShareItemEnum

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun WordListSharedShowDialog(
    event: WordListSharedDialogEvent?,
    onEvent: (WordListSharedEvent)->Unit,
    lazyListState: LazyListState,
    onScrollTo: (Int)->Unit
){

    val visibleMiddlePos = lazyListState.visibleMiddlePosition()
    val maxPos by remember {
        derivedStateOf { lazyListState.layoutInfo.totalItemsCount }
    }

    fun close(){
        onEvent(WordListSharedEvent.ShowDialog(false))
    }

    when(event){
        is WordListSharedDialogEvent.ShowSelectNumber -> {
            ShowSelectNumberDialog(
                minValueParam = 1,
                maxValueParam = maxPos,
                onApprove = {onScrollTo(it - 1)},
                currentValue = visibleMiddlePos + 1,
                onClose = { close() }
            )
        }
        is WordListSharedDialogEvent.EditSavePoint->{
            EditSavePointPage(
                destinationId = event.savePointDestination.destinationId,
                saveKey = event.savePointDestination.toSaveKey(),
                pos = event.pos?:visibleMiddlePos,
                shortTitle = event.shortTitle,
                onClosed = {close()},
                onNavigateLoad = {savePoint ->
                    onScrollTo(savePoint.itemPosIndex)
                },
            )
        }
        is WordListSharedDialogEvent.ShowShareDialog->{
            SelectMenuItemDialog(
                items = ShareItemEnum.values().toList(),
                onClickItem = {menuItem->
                    onEvent(WordListSharedEvent.EvaluateShareWord(
                        event.savePointDestination,event.savePointType,
                        event.pos,event.word,menuItem
                    ))
                },
                onClosed = {close()},
                title = stringResource(R.string.share_choices_c)
            )
        }
        else -> {}
    }
}