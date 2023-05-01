package com.masterplus.trdictionary.core.presentation.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*


@Composable
fun LazyListState.visibleMiddlePosition(): Int{
    return remember {
        derivedStateOf {
            (
                    firstVisibleItemIndex +
                    (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)
            ) / 2
        }
    }.value
}
