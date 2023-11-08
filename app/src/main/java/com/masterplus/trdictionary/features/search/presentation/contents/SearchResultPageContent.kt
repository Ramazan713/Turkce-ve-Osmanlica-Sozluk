package com.masterplus.trdictionary.features.search.presentation.contents

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.extensions.clearFocusOnTap
import com.masterplus.trdictionary.core.presentation.components.SimpleWordItem
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.features.search.presentation.SearchEvent
import com.masterplus.trdictionary.features.search.presentation.SearchState
import com.masterplus.trdictionary.features.search.presentation.components.HistoryItem
import com.masterplus.trdictionary.features.search.presentation.components.SearchField
import com.masterplus.trdictionary.features.search.presentation.components.SearchKeyBoard

@Composable
fun SearchResultPageContent(
    onBackPressed: () -> Unit,
    state: SearchState,
    onEvent: (SearchEvent) -> Unit,
    gridState: LazyGridState,
    isFullPage: Boolean
) {

    val hasSearchActive by remember(state.query.text) {
        derivedStateOf {
            state.query.text.isNotBlank()
        }
    }

    Scaffold {paddings->
        Column(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize()
                .clearFocusOnTap()
        ) {

            SearchField(
                state = state,
                onEvent = onEvent,
                onBackPressed = onBackPressed,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if(hasSearchActive){
                    SearchLoadingInfo(
                        modifier = Modifier
                            .fillMaxWidth()
                            .matchParentSize()
                            .zIndex(1f)
                            .padding(vertical = 48.dp),
                        state = state
                    )
                }

                LazyVerticalGrid(
                    state = gridState,
                    contentPadding = PaddingValues(horizontal = 5.dp, vertical = 3.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    columns = GridCells.Fixed(1),
                ){

                    item {
                        SearchKeyboard(state, onEvent)
                    }

                    if(hasSearchActive){
                        searchResults(state, isFullPage = isFullPage){word->
                            onEvent(SearchEvent.ShowDetail(word))
                        }
                    }else{
                        histories(state, onEvent)
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchLoadingInfo(
    modifier: Modifier = Modifier,
    state: SearchState
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if(state.searchLoading){
            CircularProgressIndicator()
        }else if(state.wordResults.isEmpty()){
            Text(
                stringResource(R.string.not_fount_any_result),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun SearchKeyboard(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit
){
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            SearchKeyBoard(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .fillMaxWidth()
            ) {text->
                onEvent(SearchEvent.SearchQuery(queryText = state.query.text + text))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.searchResults(
    state: SearchState,
    isFullPage: Boolean,
    onClick: (WordWithSimilar) -> Unit
){


    itemsIndexed(
        state.wordResults,
        key = {_,w -> w.wordId}
    ){ i,word->
        SimpleWordItem(
            onClicked = { onClick(word) },
            modifier = Modifier.animateItemPlacement(),
            word = word,
            meaningMaxLines = 3,
            selected = state.selectedWordId == word.wordId && !isFullPage,
            order = i + 1
        )
    }
}


private fun LazyGridScope.histories(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit
){
    items(
        state.histories,
        key = { it.id ?: 0}
    ){history->
        HistoryItem(history,
            onClick = {
                onEvent(SearchEvent.HistoryClicked(history))
            },
            onDeleteClick = {
                onEvent(SearchEvent.DeleteHistory(history))
            },
            modifier = Modifier
                .padding(vertical = 3.dp)
                .fillMaxWidth()
        )
    }
}






@Preview(showBackground = true)
@Composable
fun SearchResultPageContentPreview() {
    SearchResultPageContent(
        onEvent = {},
        onBackPressed = {},
        state = SearchState(
            query = TextFieldValue("asd"),
            searchLoading = true
        ),
        isFullPage = true,
        gridState = LazyGridState()
    )
}