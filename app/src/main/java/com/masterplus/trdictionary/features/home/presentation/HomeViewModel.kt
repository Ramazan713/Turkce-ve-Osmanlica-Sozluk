package com.masterplus.trdictionary.features.home.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.ShortInfoWidgetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shortInfoWidgetUseCases: ShortInfoWidgetUseCases,
    private val shortInfoManager: ShortInfoManager
): ViewModel(){

    var state by mutableStateOf(HomeState())
        private set

    init {
        initLoading()
        listenDataChanges()
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
        shortInfoManager.refreshWord(shortInfoModel.shortInfo)
        shortInfoWidgetUseCases.refreshInfoModel(shortInfoModel.shortInfo,false)
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


    private fun initLoading(){
        viewModelScope.launch {
            shortInfoManager.checkDayForRefresh()
        }
    }

    private suspend fun loadInitData(){
        val wordInfoModel = ShortInfoModel(isLoading = true, shortInfo = ShortInfoEnum.Word)
        val proverbInfoModel = ShortInfoModel(isLoading = true, shortInfo = ShortInfoEnum.Proverb)
        val idiomInfoModel = ShortInfoModel(isLoading = true, shortInfo = ShortInfoEnum.Idiom)

        state = state.copy(
            wordShortInfo = wordInfoModel,
            proverbShortInfo = proverbInfoModel,
            idiomShortInfo = idiomInfoModel
        )

        val result = shortInfoManager.getWords(false)

        state = state.copy(
            wordShortInfo = wordInfoModel.copy(simpleWord = result.word,isLoading = false),
            proverbShortInfo = proverbInfoModel.copy(simpleWord = result.proverb,isLoading = false),
            idiomShortInfo = idiomInfoModel.copy(simpleWord = result.idiom,isLoading = false)
        )
    }

    private fun listenDataChanges(){
        viewModelScope.launch {
            loadInitData()
            shortInfoManager.getWordsFlow().collectLatest { result->
                state = state.copy(
                    wordShortInfo = state.wordShortInfo.copy(simpleWord = result.word,isLoading = false),
                    proverbShortInfo = state.proverbShortInfo.copy(simpleWord = result.proverb,isLoading = false),
                    idiomShortInfo = state.idiomShortInfo.copy(simpleWord = result.idiom,isLoading = false)
                )
            }
        }
    }
}