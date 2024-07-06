package com.masterplus.trdictionary.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shortInfoManager: ShortInfoManager
): ViewModel(){

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()


    init {
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
    }


    private fun setState(shortInfoModel: ShortInfoModel){
        _state.update { state->
            when(shortInfoModel.shortInfo){
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

    private fun listenDataChanges(){
        viewModelScope.launch {
            shortInfoManager.checkDayForRefresh()
            shortInfoManager
                .getWordsFlow()
                .onEach { result->
                    _state.update { state->
                        state.copy(
                            wordShortInfo = state.wordShortInfo.copy(simpleWord = result.word,isLoading = false),
                            proverbShortInfo = state.proverbShortInfo.copy(simpleWord = result.proverb,isLoading = false),
                            idiomShortInfo = state.idiomShortInfo.copy(simpleWord = result.idiom,isLoading = false)
                        )
                    }
                }
                .launchIn(this)
        }
    }
}