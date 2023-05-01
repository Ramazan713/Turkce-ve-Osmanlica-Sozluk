package com.masterplus.trdictionary.features.category.presentation.sub_category

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.presentation.components.EmptySearchField
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.features.category.presentation.components.SubCategoryItem

@ExperimentalMaterial3Api
@Composable
fun SubCategoryPage(
    catId: Int,
    onNavigateToAlphabeticCat: (CategoryEnum)-> Unit,
    onNavigateToWordList: (CategoryEnum, SubCategoryEnum)->Unit,
    onNavigateToSearch: (CategoryEnum)->Unit,
    onNavigateToSavePoints: (String,List<Int>,Int)->Unit,
    onNavigateBack: ()->Unit
){

    val catEnum = remember(catId){
        derivedStateOf { CategoryEnum.fromCatId(catId) }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = scrollBehavior
            ) {
                EmptySearchField(
                    title = stringResource(R.string.n_search_in,catEnum.value.title.asString()),
                    onBackPressed = onNavigateBack,
                    modifier = Modifier.fillMaxWidth(),
                    onClicked = {onNavigateToSearch(catEnum.value)}
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ){paddings->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 7.dp)
                .fillMaxSize()
        ){
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 19.dp)
                        .padding(bottom = 19.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .border(1.dp,MaterialTheme.colorScheme.outline,MaterialTheme.shapes.medium)
                        .padding(horizontal = 1.dp, vertical = 30.dp)
                    ,
                ){
                    Text(
                        stringResource(R.string.n_sub_categories_c,catEnum.value.title.asString()),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
            }

            item {
                SubCategoryItem(
                    title = stringResource(R.string.read_all_c),
                    resourceId = R.drawable.ic_baseline_all_inclusive_24,
                    onClicked = {onNavigateToWordList(catEnum.value, SubCategoryEnum.All)}
                )
            }

            item {
                SubCategoryItem(
                    title = stringResource(R.string.random_read_c),
                    resourceId = R.drawable.ic_baseline_shuffle_24,
                    onClicked = {onNavigateToWordList(catEnum.value, SubCategoryEnum.Random)}
                )
            }

            item {
                SubCategoryItem(
                    title = stringResource(R.string.alphabet_read_c),
                    resourceId = R.drawable.ic_baseline_sort_by_alpha_24,
                    onClicked = {onNavigateToAlphabeticCat(CategoryEnum.fromCatId(catId))}
                )
            }

            item {
                SubCategoryItem(
                    title = stringResource(R.string.save_points_c),
                    resourceId = R.drawable.ic_baseline_save_24,
                    onClicked = {
                        onNavigateToSavePoints(
                            catEnum.value.title.asString(context),
                            SavePointDestination.categoryDestinationIds,
                            SavePointType.fromCategory(catEnum.value).typeId
                        )
                    }
                )
            }
        }
    }

}