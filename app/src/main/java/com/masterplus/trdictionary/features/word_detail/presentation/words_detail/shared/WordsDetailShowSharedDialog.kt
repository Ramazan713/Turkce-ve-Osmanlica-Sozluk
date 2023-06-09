package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowSelectNumberDialog
import com.masterplus.trdictionary.core.presentation.features.edit_savepoint.EditSavePointPage
import com.masterplus.trdictionary.features.word_detail.presentation.components.ShowSimpleWordsDialog

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun WordsDetailShowSharedDialog(
    event: WordsDetailSharedDialogEvent?,
    onEvent: (WordsDetailSharedEvent)->Unit,
    onNavigateToDetailWord: (Int)->Unit,
    currentPage: Int,
    maxPages: Int,
    onScrollTo: (Int)->Unit
){
    fun close(){
        onEvent(WordsDetailSharedEvent.ShowDialog(false))
    }

    when(event){
        WordsDetailSharedDialogEvent.ShowSelectNumber -> {
            ShowSelectNumberDialog(
                minValueParam = 1,
                maxValueParam = maxPages,
                onApprove = {onScrollTo(it - 1)},
                currentValue = currentPage + 1,
                onClose = { close() }
            )
        }
        null -> {}
        is WordsDetailSharedDialogEvent.ShowCompoundWords -> {
            ShowSimpleWordsDialog(
                title = stringResource(R.string.compound_words_c),
                words = event.words,
                onClosed = {close()},
                onClickedWord = {onNavigateToDetailWord(it.wordId)}
            )
        }
        is WordsDetailSharedDialogEvent.ShowProverbIdiomsWords -> {
            ShowSimpleWordsDialog(
                title = stringResource(R.string.proverb_idiom_text_c),
                words = event.words,
                onClosed = {close()},
                onClickedWord = {onNavigateToDetailWord(it.wordId)}
            )
        }
        is WordsDetailSharedDialogEvent.EditSavePoint -> {
            EditSavePointPage(
                destinationId = event.savePointDestination.destinationId,
                saveKey = event.savePointDestination.toSaveKey(),
                pos = event.pos?:currentPage,
                shortTitle = event.shortTitle,
                onClosed = {close()},
                onNavigateLoad = {savePoint ->
                    onScrollTo(savePoint.itemPosIndex)
                },
            )
        }
        else -> {}
    }
}