package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.selectors.CustomDropdownBarMenu
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar

@ExperimentalMaterial3Api
@Composable
fun WordsDetailSharedTopBar(
    title: String,
    onEvent: (WordsDetailSharedEvent)->Unit,
    onSavePointClicked: (()->Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateBack: ()->Unit,
){
    CustomTopAppBar(
        title = title,
        onNavigateBack = onNavigateBack,
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(
                onClick = {
                    onEvent(
                        WordsDetailSharedEvent.ShowDialog(true,
                            WordsDetailSharedDialogEvent.ShowSelectNumber
                        )
                    )
                },
            ){
                Icon(painter = painterResource(R.drawable.baseline_map_24),
                    contentDescription = stringResource(R.string.navigator)
                )
            }
            CustomDropdownBarMenu(
                items = WordsDetailTopBarMenu.values().toList(),
                onItemChange = {menuItem->
                    when(menuItem){
                        WordsDetailTopBarMenu.SavePoint -> {
                            onSavePointClicked?.invoke()
                        }
                    }
                }
            )
        }
    )
}