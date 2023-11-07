package com.masterplus.trdictionary.features.word_detail.word_category

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.save_point_info.SavePointCategoryInfoUseCases
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailSheetEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager.WordsPagerListDetailAdaptivePage

@Composable
fun WordCategoryPage(
    state: WordsListDetailState,
    onEvent: (WordsListDetailEvent) -> Unit,
    words: LazyPagingItems<WordWithSimilar>,
    listDetailContentType: ListDetailContentType,
    windowWidthSizeClass: WindowWidthSizeClass,
    displayFeatures: List<DisplayFeature>,
    savePointInfo: SavePointCategoryInfoUseCases.SavePointCategoryInfo,
    onNavigateBack: (()->Unit)?,
    onRelatedWordClicked: (Int) -> Unit,
    initPos: Int
) {
    WordsPagerListDetailAdaptivePage(
        state = state,
        onEvent = onEvent,
        words = words,
        initPos = initPos,
        topBarTitle = stringResource(R.string.word_list_c),
        listHeaderDescription = savePointInfo.savePointTitle,
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
                        savePointDestination = savePointInfo.savePointDestination,
                        listIdControl = null,
                    )
                )
            )
        },
        onNavigateToRelatedWord = onRelatedWordClicked,
        onDetailFavoriteClick = {w->
            onEvent(WordsListDetailEvent.AddFavorite(w.wordId))
        },
        onDetailSelectListPressed = {w->
            onEvent(
                WordsListDetailEvent.ShowSheet(
                WordsListDetailSheetEvent.ShowSelectList(w.wordId,null))
            )
        },
        onSavePointClick = {pos->
            onEvent(
                WordsListDetailEvent.ShowDialog(
                    WordsListDetailDialogEvent.EditSavePoint(
                        savePointDestination = savePointInfo.savePointDestination,
                        shortTitle = savePointInfo.savePointTitle,
                        pos = pos
                    )
                )
            )
        },
    )
}
