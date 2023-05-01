package com.masterplus.trdictionary.features.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.features.home.domain.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.use_cases.ShortInfoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shortInfoUseCases: ShortInfoUseCases
): ViewModel(){

    var state by mutableStateOf(HomeState())
        private set

    init {
        viewModelScope.launch {
            loadShortsInfo()
        }
    }

    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.RefreshShortInfo -> {
                viewModelScope.launch {
                    refreshShortInfo(event.shortInfoModel)
                }
            }
        }
    }

    private suspend fun refreshShortInfo(shortInfoModel: ShortInfoModel){
        setState(shortInfoModel.copy(isLoading = true))
        val simpleWord = shortInfoUseCases.getShortInfo(shortInfoModel.shortInfo,true)
        setState(shortInfoModel.copy(simpleWord = simpleWord,isLoading = false))
    }

    private suspend fun loadShortsInfo(){
        val wordInfoModel = ShortInfoModel(isLoading = true, shortInfo = ShortInfoEnum.Word)
        val proverbInfoModel = ShortInfoModel(isLoading = true, shortInfo = ShortInfoEnum.Proverb)
        val idiomInfoModel = ShortInfoModel(isLoading = true, shortInfo = ShortInfoEnum.Idiom)

        state = state.copy(
            wordShortInfo = wordInfoModel,
            proverbShortInfo = proverbInfoModel,
            idiomShortInfo = idiomInfoModel
        )

        val result = shortInfoUseCases.getShortInfoResult()

        state = state.copy(
            wordShortInfo = wordInfoModel.copy(simpleWord = result.word,isLoading = false),
            proverbShortInfo = proverbInfoModel.copy(simpleWord = result.proverb,isLoading = false),
            idiomShortInfo = idiomInfoModel.copy(simpleWord = result.idiom,isLoading = false)
        )

    }

    private fun setState(shortInfoModel: ShortInfoModel){
        state = when(shortInfoModel.shortInfo){
            ShortInfoEnum.Proverb -> {
                state.copy(proverbShortInfo = shortInfoModel)
            }
            ShortInfoEnum.Idiom -> {
                state.copy(idiomShortInfo = shortInfoModel)
            }
            ShortInfoEnum.Word -> {
                state.copy(wordShortInfo = shortInfoModel)
            }
        }
    }

}