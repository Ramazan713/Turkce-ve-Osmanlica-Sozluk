package com.masterplus.trdictionary.core.presentation.features.word_list_detail.handlers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.features.select_list.list_menu_dia.SelectMenuWithListBottom
import com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia.SelectListBottomContent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailSheetEvent
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.WordListBottomMenu

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WordsDetailHandleSheetEvents(
    sheetEvent: WordsListDetailSheetEvent,
    onEvent: (WordsListDetailEvent) -> Unit,
    onSavePointClick: (Int?) -> Unit,
) {
    fun close(){
        onEvent(WordsListDetailEvent.ShowSheet(null))
    }

    CustomModalBottomSheet(
        onDismissRequest = ::close
    ) {
        when(sheetEvent){
            is WordsListDetailSheetEvent.ShowSelectBottomMenu -> {
                SelectMenuWithListBottom(
                    title = stringResource(R.string.n_for_number_word,sheetEvent.pos + 1, sheetEvent.word.wordDetail.word),
                    listIdControl = sheetEvent.listIdControl,
                    items = WordListBottomMenu.values().toList(),
                    onClose = ::close,
                    onClickItem = {menuItem->
                        when(menuItem){
                            WordListBottomMenu.ShareWord -> {
                                onEvent(WordsListDetailEvent.ShowDialog(
                                    WordsListDetailDialogEvent.ShowShareDialog(sheetEvent.word.wordDetail)
                                ))
                            }
                            WordListBottomMenu.EditSavePoint -> {
                                onSavePointClick(sheetEvent.pos)
                            }
                        }
                    },
                    wordId = sheetEvent.word.wordId
                )
            }
            is WordsListDetailSheetEvent.ShowSelectList -> {
                SelectListBottomContent(
                    wordId = sheetEvent.wordId,
                    onClosed = ::close
                )
            }
        }
    }
}