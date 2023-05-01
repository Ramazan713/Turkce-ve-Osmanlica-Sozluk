package com.masterplus.trdictionary.features.search.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.RotatableLaunchEffect
import com.masterplus.trdictionary.core.presentation.components.SimpleWordItem
import com.masterplus.trdictionary.features.search.presentation.components.BadgeIcon
import com.masterplus.trdictionary.features.search.presentation.components.HistoryItem
import com.masterplus.trdictionary.features.search.presentation.components.SearchFilterDialog
import com.masterplus.trdictionary.features.search.presentation.components.SearchKeyBoard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchPage(
    onBackPressed: ()->Unit,
    onNavigateToWordDetail: (Int,Boolean)->Unit,
    state: SearchState,
    onEvent: (SearchEvent) -> Unit
){
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val focusRequester = remember { FocusRequester() }

    RotatableLaunchEffect {
        focusRequester.requestFocus()
    }

    val currentOnNavigateToWordDetail by rememberUpdatedState(onNavigateToWordDetail)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(state,lifecycle){
        snapshotFlow { state }
            .filter { state.searchUiEvent!=null }
            .flowWithLifecycle(lifecycle)
            .collectLatest {
                when(val event = state.searchUiEvent){
                    is SearchUiEvent.NavigateToDetailWord -> {
                        currentOnNavigateToWordDetail(event.wordId,event.popCurrentPage)
                        onEvent(SearchEvent.ClearUiEvent)
                    }
                    null -> {}
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(backgroundColor),
    ) {
        TextField(
            state.query,
            onValueChange = {
                onEvent(SearchEvent.ChangeQuery(it))
            },
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            leadingIcon = {
                IconButton(onClick = onBackPressed){
                    Icon(Icons.Default.ArrowBack,contentDescription = null)
                }
            },
            trailingIcon = {
                Row {
                    IconButton(onClick = {
                        focusRequester.requestFocus()
                        onEvent(SearchEvent.SetQuery(""))
                    }){
                        Icon(Icons.Default.Clear,contentDescription = null)
                    }
                    BadgeIcon(
                        R.drawable.ic_baseline_filter_alt_24,
                        badgeContent = state.badge,
                        onClicked = {
                            onEvent(SearchEvent.ShowDialog(true,SearchDialogEvent.ShowFilter))
                        }
                    )
                }
            },
            placeholder = { Text(stringResource(R.string.n_search_in,state.categoryFilter.title.asString())) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = backgroundColor
            ),
            singleLine = true
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 3.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
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
                            onEvent(SearchEvent.SetQuery(state.query.text+text))
                        }
                    }
                }
            }

            if(state.query.text.isNotBlank()){

                if(state.searchLoading){
                    item {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 19.dp)
                                .fillMaxWidth()
                                .animateItemPlacement()
                            ,
                        ){
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }
                    }
                }else if(state.wordResults.isEmpty()){
                    item {
                        Text(
                            stringResource(R.string.not_fount_any_result),
                            modifier = Modifier
                                .padding(vertical = 19.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }else{
                    items(
                        state.wordResults,
                        key = {it.wordId}
                    ){ simpleWord->
                        SimpleWordItem(
                            onClicked = {
                                onEvent(SearchEvent.NavigateToDetailWord(
                                    simpleWord.wordContent,simpleWord.wordId))
                            },
                            modifier = Modifier.animateItemPlacement(),
                            simpleWord = simpleWord,
                            meaningMaxLines = 2
                        )
                    }
                }

            }else{
                items(
                    state.histories,
                    key = {it.id?:it.wordId}
                ){history->
                    HistoryItem(history,
                        onClick = {
                            onEvent(SearchEvent.HistoryClicked(history))
                        },
                        onDeleteClick = {
                            onEvent(SearchEvent.DeleteHistory(history))
                        },
                        modifier = Modifier.padding(vertical = 3.dp).fillMaxWidth()
                    )
                }
            }
        }
    }



    if(state.showDialog){
        ShowDialog(
            state = state,
            onEvent = onEvent
        )
    }

}


@Composable
private fun ShowDialog(
    state: SearchState,
    onEvent: (SearchEvent)->Unit
){
    fun close(){
        onEvent(SearchEvent.ShowDialog(false))
    }

    when(state.dialogEvent){
        SearchDialogEvent.ShowFilter -> {
            SearchFilterDialog(
                onClosed = {close()},
                onApproved = {categoryEnum, searchKind ->
                    onEvent(SearchEvent.ChangeFilter(categoryEnum,searchKind))
                },
                categoryEnum = state.categoryFilter,
                searchKind = state.searchKind,
                defaultCategoryEnum = state.defaultCategory
            )
        }
        null -> {}
    }

}



