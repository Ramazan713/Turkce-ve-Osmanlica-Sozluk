package com.masterplus.trdictionary.features.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.presentation.selectors.CustomDropdownMenu
import com.masterplus.trdictionary.core.presentation.components.buttons.NegativeButton
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
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
    val currentCatItem = rememberSaveable(categoryEnum) {
        mutableStateOf(categoryEnum ?: CategoryEnum.AllDict)
    }

    val currentSearchType = rememberSaveable(searchKind) {
        mutableStateOf(searchKind ?: SearchKind.Word)
    }

    CustomDialog(onClosed = onClosed){
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 13.dp, vertical = 7.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
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
                            onItemChange = {currentCatItem.value = it},
                            currentItem = currentCatItem.value,
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
                            onItemChange = {currentSearchType.value = it},
                            currentItem = currentSearchType.value,
                            useBorder = false,
                            useDefaultBackgroundColor = false
                        )
                    }
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(13.dp),
                    modifier = Modifier
                        .padding(vertical = 7.dp, horizontal = 7.dp)
                ) {
                    NegativeButton (
                        title = stringResource(R.string.reset),
                        onClick = {
                            currentSearchType.value = SearchKind.Word
                            currentCatItem.value = defaultCategoryEnum
                        },
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        title = stringResource(R.string.approve),
                        onClick = {
                            onApproved(currentCatItem.value,currentSearchType.value)
                            onClosed()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}