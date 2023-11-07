package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.get_detail_words

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowCompoundWordsDia(
    wordId: Int,
    onClosed: ()->Unit,
    onClickedWord: (SimpleWordResult)->Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    getDetailWordsViewModel: GetDetailWordsViewModel = hiltViewModel()
) {
    LaunchedEffect(wordId){
        getDetailWordsViewModel.loadCompoundWords(wordId)
    }

    ShowSimpleWordsDialog(
        title = stringResource(R.string.compound_words_c),
        words = getDetailWordsViewModel.compoundWords,
        onClosed = onClosed,
        onClickedWord = onClickedWord,
        windowWidthSizeClass = windowWidthSizeClass
    )
}