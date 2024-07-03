package com.masterplus.trdictionary.features.search.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DefaultToolTip
import com.masterplus.trdictionary.core.presentation.components.RotatableLaunchEffect
import com.masterplus.trdictionary.features.search.presentation.SearchDialogEvent
import com.masterplus.trdictionary.features.search.presentation.SearchEvent
import com.masterplus.trdictionary.features.search.presentation.SearchState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit,
    onBackPressed: ( )-> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    RotatableLaunchEffect {
        focusRequester.requestFocus()
    }


    TextField(
        value = state.queryFieldValue,
        onValueChange = {
            onEvent(SearchEvent.SetTextFieldValue(it))
        },
        modifier = modifier
            .focusRequester(focusRequester)
        ,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onEvent(SearchEvent.AddHistory(state.query))
                focusRequester.freeFocus()
                keyboardController?.hide()
            }
        ),
        leadingIcon = {
            DefaultToolTip(tooltip = stringResource(id = R.string.back)) {
                IconButton(onClick = {
                    if(state.hasSearchFocus){
                        focusRequester.freeFocus()
                    }else{
                        onBackPressed()
                    }
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            }
        },
        trailingIcon = {
            Row {
                DefaultToolTip(tooltip = stringResource(id = R.string.clear_v)) {
                    IconButton(onClick = {
                        onEvent(SearchEvent.AddHistory(state.query))
                        focusRequester.requestFocus()
                        onEvent(SearchEvent.ClearQuery)
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.clear_v)
                        )
                    }
                }

                BadgeIcon(
                    Icons.Default.FilterAlt,
                    badgeContent = state.badge,
                    onClicked = {
                        onEvent(SearchEvent.ShowDialog(SearchDialogEvent.ShowFilter))
                    },
                    contentDescription = stringResource(id = R.string.filter),
                    tooltip = stringResource(id = R.string.filter)
                )
            }
        },
        placeholder = {
            Text(
                stringResource(
                    R.string.n_search_in,
                    state.categoryFilter.title.asString()
                )
            )
        },
        singleLine = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreview() {
    SearchField(
        state = SearchState(

        ),
        onEvent = {},
        onBackPressed = {}
    )
}