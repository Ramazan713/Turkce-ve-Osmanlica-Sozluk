package com.masterplus.trdictionary.core.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar

@Composable
fun EmptySearchField(
    title: String,
    onClicked: ()->Unit,
    modifier: Modifier = Modifier,
    onBackPressed: (()->Unit)? = null,
    margins: PaddingValues = PaddingValues(vertical = 5.dp, horizontal = 5.dp),
    isVisible: Boolean
){
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        EmptySearchField(
            title = title,
            onClicked = onClicked,
            modifier = modifier,
            onBackPressed = onBackPressed,
            margins = margins
        )
    }
}


@Composable
fun EmptySearchField(
    title: String,
    onClicked: ()->Unit,
    modifier: Modifier = Modifier,
    onBackPressed: (()->Unit)? = null,
    margins: PaddingValues = PaddingValues(vertical = 5.dp, horizontal = 5.dp),
){
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .padding(margins)
            .clip(MaterialTheme.shapes.large)
            .background(
                backgroundColor,
                MaterialTheme.shapes.large
            )
            .clickable {
                onClicked()
            }
            .padding(horizontal = 4.dp, vertical = 8.dp)
                ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ){
        onBackPressed?.let { backPressed->
            DefaultToolTip(tooltip = stringResource(id = R.string.back)) {
                IconButton(onClick = { backPressed() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                        tint = contentColor,
                    )
                }
            }
        }?: kotlin.run {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
            )
        }
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium
                .copy(color = contentColor),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun EmptySearchFieldPreview() {
    CustomTopAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        EmptySearchField(
            title = stringResource(R.string.n_search_in,CategoryEnum.OsmDict.title.asString()),
            onBackPressed = { },
            modifier = Modifier.weight(1f),
            onClicked = {  }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun EmptySearchFieldPreview2() {
    CustomTopAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        EmptySearchField(
            title = stringResource(R.string.n_search_in,CategoryEnum.OsmDict.title.asString()),
            onBackPressed = null,
            modifier = Modifier.fillMaxWidth(),
            onClicked = {  }
        )
    }
}