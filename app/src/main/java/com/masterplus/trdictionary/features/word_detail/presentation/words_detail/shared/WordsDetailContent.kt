package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.compose.LazyPagingItems
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
import com.masterplus.trdictionary.core.presentation.extensions.isLoading
import com.masterplus.trdictionary.features.word_detail.domain.constants.ShareItemEnum
import com.masterplus.trdictionary.features.word_detail.domain.model.AudioState
import com.masterplus.trdictionary.features.word_detail.domain.model.WordCompletedInfo
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailInfoModel
import com.masterplus.trdictionary.features.word_detail.presentation.components.WordDetailItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordsDetailContent(
    pagingWords: LazyPagingItems<WordCompletedInfo>,
    pagerState: PagerState,
    audioState: AudioState,
    onEvent: (WordsDetailSharedEvent)->Unit,
    onFavoriteClicked: (Int, WordDetailInfoModel)->Unit,
    onShareMenuItemClicked: (Int, Int, Int, ShareItemEnum) -> Unit,
    modifier: Modifier = Modifier
){

    val scope = rememberCoroutineScope()

    val pageInfo by remember {
        derivedStateOf {
            "${pagerState.currentPage + 1} / ${pagingWords.itemCount}"
        }
    }

    Column(modifier = modifier){
        Text(
            pageInfo,
            modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )

        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){

            if(pagingWords.loadState.isLoading(includeAppend = false)){
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.Center).zIndex(1f))
            }
            HorizontalPager(
                pageCount = pagingWords.itemCount,
                state = pagerState,
                key = {index->index}
            ){index->
                val completedWord = pagingWords[index]
                if(completedWord != null){
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ){
                        itemsIndexed(
                            completedWord.allWordInfos,
                            key = {_,it->it.wordId}
                        ){index,wordDetail->
                            WordDetailItem(
                                wordModel = wordDetail,
                                showButtons = index == 0,
                                audioState = audioState,
                                onShareMenuItemClicked = {
                                    val randomOrder = completedWord.wordInfo.word.randomOrder
                                    onShareMenuItemClicked(index,wordDetail.word.id,randomOrder,it)
                                },
                                onFavoritePressed = {
                                    onFavoriteClicked(index,wordDetail)
                                },
                                onSelectListPressed = {
                                    onEvent(
                                        WordsDetailSharedEvent.ShowModal(true,
                                            WordsDetailSharedModalEvent.ShowSelectList(wordDetail.wordId)
                                        )
                                    )
                                },
                                onVolumePressed = {
                                    onEvent(WordsDetailSharedEvent.ListenWord(
                                        wordDetail.wordListInfo.wordMeaning.word
                                    ))
                                },
                                onProverbIdiomWordsClicked = {
                                    onEvent(
                                        WordsDetailSharedEvent.ShowDialog(true,
                                            WordsDetailSharedDialogEvent.ShowProverbIdiomsWords(wordDetail.proverbIdioms)
                                        )
                                    )
                                },
                                onCompoundWordsClicked = {
                                    onEvent(
                                        WordsDetailSharedEvent.ShowDialog(true,
                                            WordsDetailSharedDialogEvent.ShowCompoundWords(wordDetail.compoundWords)
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            PrimaryButton(
                title = stringResource(R.string.previous),
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }},
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                title = stringResource(R.string.next),
                onClick = {scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }},
                modifier = Modifier.weight(1f)
            )
        }
    }

}