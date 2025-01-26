package com.masterplus.trdictionary.core.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListStatePagingWorkaround(): LazyListState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info:
    // https://issuetracker.google.com/issues/177245496
    // https://issuetracker.google.com/issues/179397301
    return when (itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) { LazyListState(0, 0) }
        // Return rememberLazyListState (normal case).
        else -> rememberLazyListState()
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyGridStatePagingWorkaround(): LazyGridState {
    return when (itemCount) {
        0 -> remember(this) { LazyGridState(0, 0) }
        else -> rememberLazyGridState()
    }
}


fun <T: Any> LazyPagingItems<T>.isEmptyResult(): Boolean{
    return itemCount == 0 && !isAnyItemLoading()
}

fun <T: Any> LazyPagingItems<T>.isNotEmptyResult(): Boolean{
    return !isEmptyResult()
}



fun <T: Any> LazyPagingItems<T>.isAppendItemLoading(): Boolean{
    val isLocalAppend = loadState.source.append is LoadState.Loading && itemCount != 0
    val isAppend = loadState.append is LoadState.Loading && itemCount != 0
    return isLocalAppend || isAppend
}

fun <T: Any> LazyPagingItems<T>.isPrependItemLoading(): Boolean{
    val isLocalPrepend = loadState.source.prepend is LoadState.Loading
    val isPrepend = loadState.prepend is LoadState.Loading
    return isLocalPrepend || isPrepend
}

fun <T: Any> LazyPagingItems<T>.isLoading(): Boolean{
    val isLocalRefresh = loadState.source.refresh is LoadState.Loading
    val isRefresh = loadState.refresh is LoadState.Loading
    val isFirstAppendLoading = loadState.append is LoadState.Loading && itemCount == 0
    return isLocalRefresh || isRefresh || isFirstAppendLoading
}

fun <T: Any> LazyPagingItems<T>.isAnyItemLoading(): Boolean{
    return isAppendItemLoading() || isPrependItemLoading()
}

fun <T: Any> LazyPagingItems<T>.isAnyLoading(): Boolean{
    return isAnyItemLoading() || isLoading()
}
