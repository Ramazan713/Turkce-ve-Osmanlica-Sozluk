package com.masterplus.trdictionary.core.presentation.components.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@ExperimentalMaterial3Api
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    titleBody:  (@Composable () -> Unit)? = null,
    onNavigateBack: (()->Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable (RowScope.() -> Unit) = {},
){
    TopAppBar(
        modifier = modifier,
        title = titleBody ?: { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {if(onNavigateBack!=null) NavigationBackItem(onNavigateBack) },
        actions = actions
    )
}