package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.CustomDropdownBarMenu
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordListSharedTopBar(
    title: String,
    onEvent: (WordListSharedEvent)->Unit,
    onSavePointClicked: (()->Unit)? = null,
    onNavigateBack: ()->Unit,
    scrollBehavior: TopAppBarScrollBehavior
){
    CustomTopAppBar(
        title = title,
        onNavigateBack = onNavigateBack,
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(
                onClick = {
                    onEvent(
                        WordListSharedEvent.ShowDialog(true,
                            WordListSharedDialogEvent.ShowSelectNumber
                        )
                    )
                },
            ){
                Icon(painter = painterResource(R.drawable.baseline_map_24),
                    contentDescription = stringResource(R.string.navigator)
                )
            }

            CustomDropdownBarMenu(
                items = WordListTopBarMenu.values().toList(),
                onItemChange = {menuItem->
                    when(menuItem){
                        WordListTopBarMenu.SavePoint -> {
                            onSavePointClicked?.invoke()
                        }
                    }
                }
            )

        }
    )
}