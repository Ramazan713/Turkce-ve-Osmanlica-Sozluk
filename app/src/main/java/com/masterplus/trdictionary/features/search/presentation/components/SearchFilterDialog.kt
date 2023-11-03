package com.masterplus.trdictionary.features.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.presentation.selections.CustomDropdownMenu
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind


@Composable
fun SearchFilterDialog(
    onClosed: ()->Unit,
    onApproved: (CategoryEnum, SearchKind)->Unit,
    categoryEnum: CategoryEnum? = null,
    defaultCategoryEnum: CategoryEnum = CategoryEnum.AllDict,
    searchKind: SearchKind? = null
){
    var currentCatItem by rememberSaveable(categoryEnum) {
        mutableStateOf(categoryEnum ?: CategoryEnum.AllDict)
    }

    var currentSearchType by rememberSaveable(searchKind) {
        mutableStateOf(searchKind ?: SearchKind.Word)
    }

    CustomDialog(onClosed = onClosed){
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.dict_type_c) + " :",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                    Box(
                        modifier = Modifier.weight(2f)
                    ) {
                        CustomDropdownMenu(
                            items = CategoryEnum.values().toList(),
                            onItemChange = { currentCatItem = it },
                            currentItem = currentCatItem,
                            useBorder = false,
                            useDefaultBackgroundColor = false,
                        )
                    }
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.search_in_c) + " :",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                    Box(
                        modifier = Modifier.weight(2f)
                    ) {
                        CustomDropdownMenu(
                            items = SearchKind.values().toList(),
                            onItemChange = { currentSearchType = it },
                            currentItem = currentSearchType,
                            useBorder = false,
                            useDefaultBackgroundColor = false
                        )
                    }
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .padding(top = 8.dp, bottom = 2.dp)
                ) {
                    TextButton(
                        onClick = {
                            currentSearchType = SearchKind.Word
                            currentCatItem = defaultCategoryEnum
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.reset))
                    }

                    Button(
                        onClick = {
                            onApproved(currentCatItem,currentSearchType)
                            onClosed()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.approve))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchFilterDialogPreview() {
    SearchFilterDialog(
        onClosed = {},
        onApproved = {x,y->},
    )
}


