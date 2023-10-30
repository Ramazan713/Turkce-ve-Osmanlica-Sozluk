package com.masterplus.trdictionary.features.category.presentation.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.features.category.presentation.components.CategoryItem

@ExperimentalMaterial3Api
@Composable
fun CategoryPage(
    onNavigateToSetting: ()->Unit,
    onNavigateToSubCategory: (CategoryEnum)->Unit,
){

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.categories),
                scrollBehavior = scrollBehavior
            ){
                IconButton(onClick = onNavigateToSetting){
                    Icon(painterResource(R.drawable.ic_baseline_settings_24),contentDescription = null)
                }
            }
        },
    ) { paddings->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ){
            item {
                Text(
                    text = stringResource(R.string.dict_category_c),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp)
                )
            }

            item {
                CategoryItem(
                    title = stringResource(R.string.all_dict_c),
                    onClicked = {onNavigateToSubCategory(CategoryEnum.AllDict)},
                    resourceId = R.drawable.all_dict,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                CategoryItem(
                    title = stringResource(R.string.tr_dict_c),
                    resourceId = R.drawable.tr_dict,
                    onClicked = { onNavigateToSubCategory(CategoryEnum.TrDict) },
                )
            }

            item {
                CategoryItem(
                    title = stringResource(R.string.osm_dict_c),
                    resourceId = R.drawable.osm_dict,
                    onClicked = {onNavigateToSubCategory(CategoryEnum.OsmDict)},
                )
            }

            item {
                CategoryItem(
                    title = stringResource(R.string.proverbs),
                    resourceId = R.drawable.proverb_dict,
                    onClicked = {onNavigateToSubCategory(CategoryEnum.ProverbDict)},
                )
            }

            item {
                CategoryItem(
                    title = stringResource(R.string.idioms),
                    resourceId = R.drawable.idiom_dict,
                    onClicked = {onNavigateToSubCategory(CategoryEnum.IdiomDict)},
                )
            }

        }
    }
}



