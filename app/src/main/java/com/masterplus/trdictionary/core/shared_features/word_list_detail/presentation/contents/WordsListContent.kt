package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.contents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.extensions.isLoading
import com.masterplus.trdictionary.core.presentation.components.SimpleWordItem
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent


@Composable
fun WordListContent(
    modifier: Modifier = Modifier,
    pagingWords: LazyPagingItems<WordWithSimilar>,
    lazyStaggeredState: LazyStaggeredGridState,
    onEvent: (WordsListDetailEvent) -> Unit,
    listHeaderDescription: String? = null,
    selectedPod: Int? = null,
    onListItemLongClick: (Int, WordDetailMeanings) -> Unit,
){
    WordListContent(
        modifier = modifier,
        pagingWords = pagingWords,
        lazyStaggeredState = lazyStaggeredState,
        listHeaderDescription = listHeaderDescription,
        selectedPod = selectedPod,
        onListItemLongClick = onListItemLongClick,
        onListItemClick = {i,w->
            onEvent(
                WordsListDetailEvent.ShowSelectedWords(w, i)
            )
        }
    )
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordListContent(
    modifier: Modifier = Modifier,
    pagingWords: LazyPagingItems<WordWithSimilar>,
    lazyStaggeredState: LazyStaggeredGridState,
    listHeaderDescription: String? = null,
    selectedPod: Int? = null,
    onListItemLongClick: (Int, WordDetailMeanings) -> Unit,
    onListItemClick: (Int, WordWithSimilar) -> Unit,
) {
    val context = LocalContext.current

    Box (
        modifier = modifier
    ){
        if(pagingWords.loadState.isLoading(false)){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }else if(pagingWords.itemCount == 0){
            Text(
                stringResource(R.string.not_fount_any_result),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }else{
            Column {
                if(listHeaderDescription != null){
                    Text(
                        listHeaderDescription,
                        style = MaterialTheme.typography.bodyLarge
                            .copy(fontWeight = FontWeight.W500),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp, horizontal = 3.dp),
                        textAlign = TextAlign.Center
                    )
                }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(350.dp),
                    state = lazyStaggeredState,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalItemSpacing = 5.dp,
                    modifier = Modifier.semantics {
                        contentDescription = context.getString(R.string.lazy_vertical_list)
                    }
                ){
                    items(
                        pagingWords.itemCount,
                        key = { i -> pagingWords[i]?.wordId ?: i },
                    ){i->
                        val word = pagingWords[i]
                        if (word != null){
                            SimpleWordItem(
                                order = i + 1,
                                word = word,
                                selected = selectedPod == i,
                                onClicked = {
                                    onListItemClick(i,word)
                                },
                                onLongClicked = {
                                    onListItemLongClick(i,word.wordDetailMeanings)
                                }
                            )
                        }else{
                            Text("Loading")
                        }
                    }
                }
            }
        }
    }
}