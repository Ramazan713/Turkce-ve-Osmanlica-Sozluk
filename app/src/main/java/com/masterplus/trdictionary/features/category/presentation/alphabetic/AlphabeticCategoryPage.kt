package com.masterplus.trdictionary.features.category.presentation.alphabetic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.features.category.presentation.components.AlphabeticItem
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.navigation.NavigationBackItem

@ExperimentalMaterial3Api
@Composable
fun AlphabeticCategoryPage(
    catId: Int,
    onNavigateBack: ()->Unit,
    onNavigateToWordList: (CategoryEnum, String)->Unit
){

    val catEnum = remember(catId){
        derivedStateOf { CategoryEnum.fromCatId(catId) }
    }

    val alphabets = remember {
        derivedStateOf {
            listOf('a','b','c','ç','d','e','f','g','ğ','h','ı','i','j',
                'k','l','m','n','o','ö','p','r','s','ş',
                't','u','ü','v','y','z')
        }
    }
    val context = LocalContext.current
    val locale = Locale.current

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val title by remember {
        derivedStateOf {
            val alphabet = context.getString(R.string.alphabet)
            "${catEnum.value.title.asString(context)} - $alphabet".capitalize(locale)
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = title) },
                navigationIcon = { NavigationBackItem(onNavigateBack) },
                scrollBehavior = scrollBehavior
            )
        },
    ){paddings->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 7.dp)
                .padding(bottom = 7.dp),
            columns = GridCells.Adaptive(100.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(
                alphabets.value,
                key = { it }
            ){c->
                AlphabeticItem(
                    c.toString().toUpperCase(locale),
                    onClicked = {onNavigateToWordList(catEnum.value,c.toString())}
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AlphabeticCategoryPagePreview() {
    AlphabeticCategoryPage(
        catId = CategoryEnum.OsmDict.catId,
        onNavigateBack = {},
        onNavigateToWordList = {x,y->}
    )
}
