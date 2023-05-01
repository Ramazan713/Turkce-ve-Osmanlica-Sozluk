package com.masterplus.trdictionary.core.presentation.components.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@ExperimentalMaterial3Api
@Composable
fun CustomTopAppBar(
    title: String = "",
    titleBody:  (@Composable () -> Unit)? = null,
    onNavigateBack: (()->Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable (RowScope.() -> Unit) = {},
){
    TopAppBar(
        title = titleBody ?: { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {if(onNavigateBack!=null) NavigationBackItem(onNavigateBack) },
        actions = actions
    )
}