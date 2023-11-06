package com.masterplus.trdictionary.core.presentation.features.word_list_detail.handlers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowSelectNumberDialog
import com.masterplus.trdictionary.core.presentation.features.edit_savepoint.EditSavePointDialog
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.get_detail_words.ShowCompoundWordsDia
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.get_detail_words.ShowProverbIdiomWordsDia

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun WordsDetailHandleModalEvents(
    dialogEvent: WordsListDetailDialogEvent,
    onEvent: (WordsListDetailEvent) -> Unit,
    onNavigateToRelatedWord: (Int) -> Unit,
    currentPos: Int,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    fun close(){
        onEvent(WordsListDetailEvent.ShowDialog(null))
    }

    when(val event = dialogEvent){
        is WordsListDetailDialogEvent.EditSavePoint -> {
            EditSavePointDialog(
                destinationId = event.savePointDestination.destinationId,
                saveKey = event.savePointDestination.toSaveKey(),
                pos = event.pos ?: currentPos,
                shortTitle = event.shortTitle,
                onClosed = ::close,
                onNavigateLoad = {savePoint ->
                    onEvent(WordsListDetailEvent.NavigateToPos(savePoint.itemPosIndex))
                },
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
        is WordsListDetailDialogEvent.ShowCompoundWordsList -> {
            ShowCompoundWordsDia(
                wordId = event.wordDetailMeanings.wordId,
                onClosed = ::close,
                onClickedWord = { onNavigateToRelatedWord(event.wordDetailMeanings.wordId) },
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
        is WordsListDetailDialogEvent.ShowProverbIdiomsWordsList -> {
            ShowProverbIdiomWordsDia(
                wordId = event.wordDetailMeanings.wordId,
                onClosed = ::close,
                onClickedWord = { onNavigateToRelatedWord(event.wordDetailMeanings.wordId) },
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
        is WordsListDetailDialogEvent.ShowShareDialog -> {

        }
        is WordsListDetailDialogEvent.ShowSelectNumber -> {
            ShowSelectNumberDialog(
                minValue = 1,
                maxValue = event.itemCount - 1,
                onApprove = {pos->
                    onEvent(WordsListDetailEvent.NavigateToPos(pos))
                },
                currentValue = event.currentPos,
                onClose = ::close
            )
        }
    }
}