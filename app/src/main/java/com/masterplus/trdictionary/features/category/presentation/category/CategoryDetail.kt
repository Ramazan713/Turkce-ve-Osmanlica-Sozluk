package com.masterplus.trdictionary.features.category.presentation.category

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.domain.util.UiText
import com.masterplus.trdictionary.core.extensions.isScrollingUp
import com.masterplus.trdictionary.core.presentation.components.EmptySearchField
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.features.category.domain.models.Category
import com.masterplus.trdictionary.features.category.presentation.components.SubCategoryItem

data class SavePointNavigationArgs(
    val title: String,
    val destinationIds: List<Int>,
    val savePointTypeId: Int
)


@Composable
fun CategoryDetail(
    category: Category?,
    onNavigateToAlphabeticCat: (CategoryEnum)-> Unit,
    onNavigateToWordList: (CategoryEnum, SubCategoryEnum)->Unit,
    onNavigateToSearch: (CategoryEnum)-> Unit,
    onNavigateToSavePoints: (SavePointNavigationArgs) -> Unit,
    isFullScreen: Boolean,
    lazyListState: LazyListState,
    onNavigateToBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold {paddings->
        Box(
            modifier = Modifier
                .padding(paddings)
                .padding(horizontal = 7.dp)
                .fillMaxSize(),
        ) {

            EmptySearchField(
                title = stringResource(R.string.n_search_in,category?.categoryEnum?.title?.asString() ?: ""),
                onBackPressed = if(isFullScreen) onNavigateToBack else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(2f)
                    .align(Alignment.TopCenter),
                onClicked = { category?.categoryEnum?.let { onNavigateToSearch(it) } },
                isVisible = lazyListState.isScrollingUp()
            )

            if(category == null){
                ShowText(
                    stringResource(id = R.string.category_not_found),
                    modifier = Modifier
                        .padding(vertical = 72.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }else{
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(top = 72.dp, bottom = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ){

                    item {
                        ShowText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            title = category.categoryEnum.title.asString()
                        )
                    }

                    item {
                        SubCategoryItem(
                            title = stringResource(R.string.read_all_c),
                            resourceId = R.drawable.ic_baseline_all_inclusive_24,
                            onClicked = {onNavigateToWordList(category.categoryEnum, SubCategoryEnum.All)}
                        )
                    }

                    item {
                        SubCategoryItem(
                            title = stringResource(R.string.random_read_c),
                            resourceId = R.drawable.ic_baseline_shuffle_24,
                            onClicked = {onNavigateToWordList(category.categoryEnum, SubCategoryEnum.Random)}
                        )
                    }

                    item {
                        SubCategoryItem(
                            title = stringResource(R.string.alphabet_read_c),
                            resourceId = R.drawable.ic_baseline_sort_by_alpha_24,
                            onClicked = {onNavigateToAlphabeticCat(category.categoryEnum)}
                        )
                    }

                    item {
                        SubCategoryItem(
                            title = stringResource(R.string.save_points_c),
                            resourceId = R.drawable.ic_baseline_save_24,
                            onClicked = {
                                onNavigateToSavePoints(
                                    SavePointNavigationArgs(
                                        category.title.asString(context),
                                        SavePointDestination.categoryDestinationIds,
                                        SavePointType.fromCategory(category.categoryEnum).typeId
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ShowText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        title,
        modifier = modifier
            .padding(horizontal = 1.dp, vertical = 4.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge
    )
}


@SuppressLint("ResourceType")
@Preview
@Composable
fun CategoryDetailPreview() {
    CategoryDetail(
        category = Category(CategoryEnum.OsmDict,UiText.Text("asd"),2),
        lazyListState = LazyListState(),
        onNavigateToAlphabeticCat = {},
        onNavigateToWordList = {categoryEnum, subCategoryEnum ->  },
        onNavigateToSearch = {},
        onNavigateToSavePoints = {args->},
        isFullScreen = true,
        onNavigateToBack = {  }
    )
}
