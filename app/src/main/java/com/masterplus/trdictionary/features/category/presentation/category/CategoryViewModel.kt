package com.masterplus.trdictionary.features.category.presentation.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.features.category.domain.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf<CategoryState>(CategoryState())
        private set

    init {
        loadData()
    }

    fun onEvent(event: CategoryEvent){
        when(event){
            CategoryEvent.CloseDetail -> {
                state = state.copy(
                    isDetailOpen = false
                )
            }
            is CategoryEvent.OpenDetail -> {
                state = state.copy(
                    selectedCategory = event.category,
                    isDetailOpen = true
                )
            }
        }
    }

    private fun loadData(){
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
        state = state.copy(
            categories = categories,
            selectedCategory = categories.first()
        )
    }
}