package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.handlers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.shared_features.select_list.list_menu_dia.SelectMenuWithListBottom
import com.masterplus.trdictionary.core.shared_features.select_list.select_list_dia.SelectListBottomContent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.constants.WordsPagerListBottomMenu
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailSheetEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WordsDetailSheetEventsHandler(
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
                    items = WordsPagerListBottomMenu.values().toList(),
                    onClose = ::close,
                    onClickItem = {menuItem->
                        when(menuItem){
                            WordsPagerListBottomMenu.ShareWordsWordPager -> {
                                onEvent(
                                    WordsListDetailEvent.ShowDialog(
                                    WordsListDetailDialogEvent.ShowShareDialog(sheetEvent.word.wordDetail)
                                ))
                            }
                            WordsPagerListBottomMenu.EditSavePoint -> {
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
                    onClosed = ::close,
                    listIdControl = sheetEvent.listIdControl
                )
            }
        }
    }
}