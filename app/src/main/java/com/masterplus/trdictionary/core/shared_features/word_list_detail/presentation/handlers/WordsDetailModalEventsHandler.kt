package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.handlers

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowSelectNumberDialog
import com.masterplus.trdictionary.core.shared_features.edit_savepoint.EditSavePointDialog
import com.masterplus.trdictionary.core.shared_features.share.domain.enums.ShareItemEnum
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.get_detail_words.ShowCompoundWordsDia
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.get_detail_words.ShowProverbIdiomWordsDia
import com.masterplus.trdictionary.core.presentation.selections.SelectMenuItemDialog

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun WordsDetailModalEventsHandler(
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
                onClickedWord = { onNavigateToRelatedWord(it.wordId) },
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
        is WordsListDetailDialogEvent.ShowProverbIdiomsWordsList -> {
            ShowProverbIdiomWordsDia(
                wordId = event.wordDetailMeanings.wordId,
                onClosed = ::close,
                onClickedWord = { onNavigateToRelatedWord(it.wordId) },
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
        is WordsListDetailDialogEvent.ShowShareDialog -> {
            SelectMenuItemDialog(
                title = stringResource(id = R.string.share_choices_c),
                items = ShareItemEnum.values().toList(),
                onClickItem = {
                    onEvent(WordsListDetailEvent.ShareWord(it,event.word))
                },
                onClosed = ::close
            )
        }
        is WordsListDetailDialogEvent.ShowSelectNumber -> {
            ShowSelectNumberDialog(
                minPos = 0,
                maxPos = event.itemCount - 1,
                onApprove = {pos->
                    onEvent(WordsListDetailEvent.NavigateToPos(pos))
                },
                currentPos = event.currentPos,
                onClose = ::close
            )
        }

        is WordsListDetailDialogEvent.AskFavoriteDelete -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_remove_list_from_favorite),
                content = stringResource(R.string.affect_current_list),
                onClosed = ::close,
                onApproved = {
                    onEvent(WordsListDetailEvent.AddFavorite(event.wordId))
                }
            )
        }
    }
}