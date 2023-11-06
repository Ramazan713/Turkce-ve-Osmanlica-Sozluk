package com.masterplus.trdictionary.core.extensions

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*

@Composable
fun LazyListState.visibleMiddlePosition(): Int{
    return remember {
        derivedStateOf {
            (firstVisibleItemIndex + (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)) / 2
        }
    }.value
}

@Composable
fun LazyGridState.visibleMiddlePosition(): Int{
    return remember {
        derivedStateOf {
            (firstVisibleItemIndex + (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)) / 2
        }
    }.value
}


@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}


@Composable
fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}


fun LazyGridLayoutInfo.isNumberInRange(number: Int): Boolean?{
    visibleItemsInfo.let { info->
        val first = info.firstOrNull()?.index
        val last = info.lastOrNull()?.index
        if(first == null || last == null) return null
        return number in first..last
    }
}
