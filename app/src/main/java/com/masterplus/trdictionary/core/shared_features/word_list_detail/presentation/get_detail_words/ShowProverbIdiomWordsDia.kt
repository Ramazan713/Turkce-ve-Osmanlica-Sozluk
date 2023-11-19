package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.get_detail_words

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowProverbIdiomWordsDia(
    wordId: Int,
    onClosed: ()->Unit,
    onClickedWord: (SimpleWordResult)->Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    getDetailWordsViewModel: GetDetailWordsViewModel = hiltViewModel()
) {

    LaunchedEffect(wordId){
        getDetailWordsViewModel.loadProverbIdiomWords(wordId)
    }
    val words by getDetailWordsViewModel.state.collectAsStateWithLifecycle()

    ShowSimpleWordsDialog(
        title = stringResource(R.string.proverb_idiom_text_c),
        words = words.proverbIdiomWords,
        onClosed = onClosed,
        onClickedWord = onClickedWord,
        windowWidthSizeClass = windowWidthSizeClass
    )
}