package com.masterplus.trdictionary.core.presentation.components.loading

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.masterplus.trdictionary.R


@Composable
fun SharedLoadingLazyVerticalGrid(
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    columns: StaggeredGridCells,
    verticalSpaceBy: Dp = 4.dp,
    horizontalSpaceBy: Dp = 4.dp,
    isEmptyResult: Boolean = false,
    emptyMessage: String = stringResource(id = R.string.not_fount_any_result),
    emptyContent: @Composable ((BoxScope.() -> Unit))? = null,
    stickHeaderContent:  (LazyStaggeredGridScope.() -> Unit)? = null,
    content:   LazyStaggeredGridScope.() -> Unit,
) {
    val showEmptyResult by remember(isEmptyResult, isLoading) {
        derivedStateOf {
            !isLoading && isEmptyResult
        }
    }

    Box(
        modifier = modifier,
    ) {
        if (isLoading) {
            SharedCircularProgress(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
                    .clickable(enabled = false, onClick = {})
            )
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier,
            columns = columns,
            state = state,
            verticalItemSpacing = verticalSpaceBy,
            horizontalArrangement = Arrangement.spacedBy(horizontalSpaceBy),
            contentPadding = contentPadding
        ) {
            stickHeaderContent?.invoke(this)

            if (showEmptyResult) {
                item(
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Box(
                        modifier = Modifier
                            .heightIn(min = 100.dp)
                            .zIndex(2f)
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        if(emptyContent != null){
                            emptyContent()
                        }else{
                            Text(
                                emptyMessage,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            content()
        }
    }
}