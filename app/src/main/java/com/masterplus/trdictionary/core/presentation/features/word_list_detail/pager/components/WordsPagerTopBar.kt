package com.masterplus.trdictionary.core.presentation.features.word_list_detail.pager.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DefaultToolTip
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.selections.CustomDropdownBarMenu
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordsDetailTopBarMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordsDetailTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onMenuClick: (WordsDetailTopBarMenu) -> Unit,
    onNavigatorClick: () -> Unit,
    onNavigateBack: (() -> Unit)? = null
) {
    CustomTopAppBar(
        scrollBehavior = scrollBehavior,
        title = title,
        onNavigateBack = onNavigateBack,
        actions = {
            DefaultToolTip(tooltip = stringResource(R.string.navigator)) {
                IconButton(
                    onClick = onNavigatorClick,
                ){
                    Icon(
                        painter = painterResource(R.drawable.baseline_map_24),
                        contentDescription = stringResource(R.string.navigator)
                    )
                }
            }
            CustomDropdownBarMenu(
                items = WordsDetailTopBarMenu.values().toList(),
                onItemChange = {menuItem->
                    onMenuClick(menuItem)
                }
            )
        }
    )
}