package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.trdictionary.core.extensions.visibleMiddlePosition
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.contents.WordListContent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager.components.WordsDetailTopBar
import com.masterplus.trdictionary.core.presentation.utils.SampleDatas
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.constants.WordsPagerTopBarMenu
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordsPagerListContent(
    modifier: Modifier = Modifier,
    onEvent: (WordsListDetailEvent) -> Unit,
    pagingWords: LazyPagingItems<WordWithSimilar>,
    lazyStaggeredState: LazyStaggeredGridState,
    title: String,
    listHeaderDescription: String? = null,
    onNavigateBack: (()->Unit)? = null,
    selectedPod: Int? = null,
    onTopBarMenuClick: (WordsPagerTopBarMenu) -> Unit,
    onListItemLongClick: (Int, WordDetailMeanings) -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val middlePos = lazyStaggeredState.visibleMiddlePosition()

    Scaffold(
        topBar = {
            WordsDetailTopBar(
                title = title,
                isDetail = false,
                scrollBehavior = scrollBehavior,
                onNavigateBack = onNavigateBack,
                onMenuClick = onTopBarMenuClick,
                onNavigatorClick = {
                    onEvent(
                        WordsListDetailEvent.ShowDialog(
                            WordsListDetailDialogEvent.ShowSelectNumber(
                                itemCount = pagingWords.itemCount,
                                currentPos = middlePos
                            ),
                        )
                    )
                }
            )
        }
    ) {paddings->
        WordListContent(
            modifier = modifier
                .padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            pagingWords = pagingWords,
            lazyStaggeredState = lazyStaggeredState,
            onEvent = onEvent,
            onListItemLongClick = onListItemLongClick,
            listHeaderDescription = listHeaderDescription,
            selectedPod = selectedPod
        )
    }
}



@Preview(showBackground = true)
@Composable
fun WordsListPagePreview() {

    val word1 = SampleDatas.generateWordWithSimilar(wordId = 1)
    val word2 = SampleDatas.generateWordWithSimilar(wordId = 10)

    val loadState = LoadStates(LoadState.NotLoading(true),LoadState.NotLoading(true),LoadState.NotLoading(true))

    val words = flow<PagingData<WordWithSimilar>> {
        emit(PagingData.from(listOf(word1,word2), sourceLoadStates = loadState))
    }.collectAsLazyPagingItems()

    WordsPagerListContent(
        pagingWords = words,
        onEvent = {},
        onTopBarMenuClick = {},
        onNavigateBack = {},
        onListItemLongClick = {i,w->},
        title = "title",
        lazyStaggeredState = LazyStaggeredGridState(),
    )
}
