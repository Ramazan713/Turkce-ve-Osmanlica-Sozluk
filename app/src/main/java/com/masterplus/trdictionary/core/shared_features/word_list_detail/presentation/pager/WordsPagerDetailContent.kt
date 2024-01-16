package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.extensions.isLoading
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.constants.WordsPagerTopBarMenu
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.contents.WordsDetailAdaptiveContent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager.components.WordsDetailTopBar
import com.masterplus.trdictionary.core.presentation.utils.SampleDatas
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WordsPagerDetailContent(
    modifier: Modifier = Modifier,
    onEvent: (WordsListDetailEvent) -> Unit,
    audioState: AudioState,
    title: String = stringResource(R.string.word_list_c),
    pagingWords: LazyPagingItems<WordWithSimilar>,
    pagerState: PagerState,
    windowWidthSizeClass: WindowWidthSizeClass,
    isFullPage: Boolean,
    onNavigateBack: () -> Unit,
    onTopBarMenuClick: (WordsPagerTopBarMenu) -> Unit,
    onDetailFavoriteClick: (WordDetailMeanings) -> Unit,
    onDetailSelectListPressed: (WordDetailMeanings) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val pageInfo by remember {
        derivedStateOf {
            "${pagerState.currentPage + 1} / ${pagingWords.itemCount}"
        }
    }

    Scaffold(
        topBar = {
            WordsDetailTopBar(
                title = title,
                isDetail = true,
                scrollBehavior = topBarScrollBehavior,
                onNavigateBack = if(isFullPage) onNavigateBack else null,
                onMenuClick = onTopBarMenuClick,
                onNavigatorClick = {
                    onEvent(
                        WordsListDetailEvent.ShowDialog(
                            WordsListDetailDialogEvent.ShowSelectNumber(
                                itemCount = pagingWords.itemCount,
                                currentPos = pagerState.currentPage
                            )
                        )
                    )
                }
            )
        }
    ) {paddings->
        Column(
            modifier = modifier
                .padding(paddings)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
        ){
            Text(
                pageInfo,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                contentAlignment = Alignment.Center
            ){
                if(pagingWords.loadState.isLoading(includeAppend = false)){
                    CircularProgressIndicator(modifier = Modifier
                        .align(Alignment.Center)
                        .zIndex(1f))
                }

                HorizontalPager(
                    modifier = Modifier
                        .matchParentSize()
                        .semantics {
                            contentDescription = context.getString(R.string.horizontal_pager)
                        }
                    ,
                    state = pagerState,
                    contentPadding = PaddingValues(),
                    key = {index-> index},
                    verticalAlignment = Alignment.Top,
                ){index->
                    val wordWithSimilar = pagingWords[index]
                    if(wordWithSimilar != null){
                        WordsDetailAdaptiveContent(
                            wordWithSimilar = wordWithSimilar,
                            windowWidthSizeClass = windowWidthSizeClass,
                            audioState = audioState,
                            onEvent = onEvent,
                            onFavoritePressed = {
                                onDetailFavoriteClick(wordWithSimilar.wordDetailMeanings)
                            },
                            onDetailSelectListPressed = {
                                onDetailSelectListPressed(wordWithSimilar.wordDetailMeanings)
                            }
                        )
                    }
                }
            }
            if(isFullPage){
                GetButtons(
                    onNavigateToPos = { scope.launch { pagerState.animateScrollToPage(it) } },
                    currentPage = { pagerState.currentPage },
                    itemCount = { pagerState.pageCount }
                )
            }
        }
    }
}

@Composable
private fun GetButtons(
    onNavigateToPos: (Int) -> Unit,
    currentPage: () -> Int,
    itemCount: () -> Int
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)) {

        TextButton(
            onClick = {
               onNavigateToPos(currentPage() - 1)
            },
            modifier = Modifier.weight(1f),
            enabled = currentPage() != 0
        ) {
            Text(text = stringResource(R.string.previous))
        }

        TextButton(
            onClick = {
                onNavigateToPos(currentPage() + 1)
            },
            modifier = Modifier.weight(1f),
            enabled = itemCount() - 1 != currentPage()
        ) {
            Text(text = stringResource(R.string.next))
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
//@PreviewDesktop
//@PreviewTablet
@Preview(showBackground = true)
@Composable
fun WordsDetailContentPreview() {


    val word1 = SampleDatas.generateWordWithSimilar(wordId = 1, similarWords = listOf())
    val word2 = SampleDatas.generateWordWithSimilar(wordId = 10)

    val loadState = LoadStates(LoadState.NotLoading(true), LoadState.NotLoading(true), LoadState.NotLoading(true))

    val words = flow<PagingData<WordWithSimilar>> {
        emit(PagingData.from(listOf(word1), sourceLoadStates = loadState))
    }.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(initialPage = 0, pageCount = {words.itemCount})

    WordsPagerDetailContent(
        pagingWords = words,
        pagerState = pagerState,
        windowWidthSizeClass = WindowWidthSizeClass.Compact,
        isFullPage = true,
        onNavigateBack = {},
        onTopBarMenuClick = {},
        onEvent = {},
        audioState = AudioState(),
        onDetailFavoriteClick = {},
        onDetailSelectListPressed = {}
    )
}


