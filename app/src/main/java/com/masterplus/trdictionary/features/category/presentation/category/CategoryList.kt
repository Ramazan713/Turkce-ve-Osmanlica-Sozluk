package com.masterplus.trdictionary.features.category.presentation.category

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.core.presentation.components.DefaultToolTip
import com.masterplus.trdictionary.features.category.domain.models.Category
import com.masterplus.trdictionary.features.category.presentation.components.CategoryItem
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryList(
    state: CategoryState,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateToSetting: ()-> Unit,
    onNavigateToDetail: (Category) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(text = stringResource(R.string.dict_category_c)) },
                actions = {
                    DefaultToolTip(tooltip = stringResource(id = R.string.settings)) {
                        IconButton(onClick = onNavigateToSetting){
                            Icon(painterResource(R.drawable.ic_baseline_settings_24),contentDescription = stringResource(id = R.string.settings))
                        }
                    }
                }
            )
        },
    ) { paddings->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ){
            items(state.categories){category->
                CategoryItem(
                    title = category.title.asString(context),
                    onClicked = { onNavigateToDetail(category) },
                    resourceId = category.resourceId,
                    selected = state.selectedCategory == category && state.isDetailOpen,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    val allDict = Category(
        categoryEnum = CategoryEnum.AllDict,
        title = UiText.Resource(R.string.all_dict_c),
        resourceId = R.drawable.all_dict
    )

    val trDict = Category(
        categoryEnum = CategoryEnum.TrDict,
        title = UiText.Resource(R.string.tr_dict_c),
        resourceId = R.drawable.tr_dict
    )

    val osmDict = Category(
        categoryEnum = CategoryEnum.OsmDict,
        title = UiText.Resource(R.string.osm_dict_c),
        resourceId = R.drawable.osm_dict
    )

    val proverbDict = Category(
        categoryEnum = CategoryEnum.ProverbDict,
        title = UiText.Resource(R.string.proverbs),
        resourceId = R.drawable.proverb_dict
    )

    val idiomDict = Category(
        categoryEnum = CategoryEnum.IdiomDict,
        title = UiText.Resource(R.string.idioms),
        resourceId = R.drawable.idiom_dict
    )

    val categories = listOf(allDict,trDict,osmDict,proverbDict,idiomDict)

    CategoryList(
        state = CategoryState(categories = categories),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        onNavigateToSetting = {},
        onNavigateToDetail = {}
    )
}

