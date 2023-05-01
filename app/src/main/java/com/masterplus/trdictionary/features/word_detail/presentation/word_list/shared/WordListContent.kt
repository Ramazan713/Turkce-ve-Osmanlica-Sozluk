package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.presentation.components.SimpleWordItem
import com.masterplus.trdictionary.core.presentation.extensions.isLoading

@ExperimentalFoundationApi
@Composable
fun WordListContent(
    modifier: Modifier = Modifier,
    pagingWords: LazyPagingItems<SimpleWordResult>,
    lazyListState: LazyListState,
    title: String? = null,
    onClickedWord: (Int, SimpleWordResult)->Unit,
    onLongClickedWord: (Int, SimpleWordResult)->Unit,
){

    Box (modifier = modifier){
        if(pagingWords.loadState.isLoading(false)){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }else if(pagingWords.itemCount == 0){
            Text(
                stringResource(R.string.not_fount_any_result),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }else{
            Column {
                if(title!=null){
                    Text(
                        title,
                        style = MaterialTheme.typography.bodyLarge
                            .copy(fontWeight = FontWeight.W500),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp, horizontal = 3.dp),
                        textAlign = TextAlign.Center
                    )
                }
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.weight(1f)
                ){
                    itemsIndexed(
                        pagingWords,
                        key = {_,w-> w.wordId}
                    ){i,word->
                        if (word!=null){
                            SimpleWordItem(
                                order = i + 1,
                                simpleWord = word,
                                onClicked = {
                                    onClickedWord(i,word)
                                },
                                modifier = Modifier.padding(vertical = 3.dp),
                                onLongClicked = {
                                    onLongClickedWord(i,word)
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