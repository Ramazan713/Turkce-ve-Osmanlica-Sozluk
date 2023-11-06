package com.masterplus.trdictionary.features.word_detail.presentation.word_list_for_list_detail

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailSheetEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailState
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.pager.WordsPagerListDetailAdaptivePage
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilar

@Composable
fun WordListForListDetailPage(
    state: WordsListDetailState,
    onEvent: (WordsListDetailEvent) -> Unit,
    words: LazyPagingItems<WordWithSimilar>,
    listDetailContentType: ListDetailContentType,
    windowWidthSizeClass: WindowWidthSizeClass,
    displayFeatures: List<DisplayFeature>,
    onNavigateBack: (()->Unit)?,
    onRelatedWordClicked: (Int) -> Unit,
    initPos: Int,
    savePointDestination: SavePointDestination,
    listId: Int,
    listName: String
) {
    WordsPagerListDetailAdaptivePage(
        state = state,
        onEvent = onEvent,
        words = words,
        initPos = initPos,
        topBarTitle = listName,
        listHeaderDescription = null,
        listDetailContentType = listDetailContentType,
        windowWidthSizeClass = windowWidthSizeClass,
        displayFeatures = displayFeatures,
        onNavigateBack = onNavigateBack,
        onListItemLongClick = {i,w->
            onEvent(
                WordsListDetailEvent.ShowSheet(
                    WordsListDetailSheetEvent.ShowSelectBottomMenu(
                        word = w,
                        pos = i,
                        savePointDestination = savePointDestination,
                        listIdControl = listId,
                    )
                )
            )
        },
        onNavigateToRelatedWord = onRelatedWordClicked,
        onDetailFavoriteClick = {w->
            onEvent(WordsListDetailEvent.AddFavorite(w.wordId,listId,w.wordDetail.inFavorite))
        },
        onDetailSelectListPressed = {w->
            onEvent(WordsListDetailEvent.ShowSheet(
                WordsListDetailSheetEvent.ShowSelectList(w.wordId,listId))
            )
        },
        onSavePointClick = {pos->
            onEvent(
                WordsListDetailEvent.ShowDialog(
                    WordsListDetailDialogEvent.EditSavePoint(
                        savePointDestination = savePointDestination,
                        shortTitle = listName,
                        pos = pos
                    )
                )
            )
        }
    )
}