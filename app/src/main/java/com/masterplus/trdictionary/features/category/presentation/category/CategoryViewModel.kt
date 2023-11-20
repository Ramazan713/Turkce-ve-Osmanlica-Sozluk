package com.masterplus.trdictionary.features.category.presentation.category

import androidx.lifecycle.ViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.features.category.domain.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(

): ViewModel() {

    private val _state = MutableStateFlow(CategoryState())
    val state: StateFlow<CategoryState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: CategoryEvent){
        when(event){
            CategoryEvent.CloseDetail -> {
                _state.update { it.copy(isDetailOpen = false)}
            }
            is CategoryEvent.OpenDetail -> {
                _state.update { state-> state.copy(
                    selectedCategory = event.category,
                    isDetailOpen = true
                )}
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
        _state.update { state-> state.copy(
            categories = categories,
            selectedCategory = categories.first()
        )}
    }
}