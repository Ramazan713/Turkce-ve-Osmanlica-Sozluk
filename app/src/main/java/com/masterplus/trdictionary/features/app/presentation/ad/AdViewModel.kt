package com.masterplus.trdictionary.features.app.presentation.ad

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.navigation.RouteSingleWordDetail
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category.navigation.RouteWordListCategory
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list.navigation.RouteWordListForList
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_category.navigation.RouteWordsDetailCategory
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list.navigation.RouteWordsDetailList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@ObsoleteCoroutinesApi
@HiltViewModel
class AdViewModel @Inject constructor(

): ViewModel() {

    private var openingCountFlow = MutableStateFlow(0)
    private var consumeSecondsFlow = MutableStateFlow(0)
    private var premiumActiveFlow = MutableStateFlow(false)
    private val loadAdState = MutableStateFlow(false)

    private var timerJob: Job? = null
    private var loadAdJob: Job? = null

    private val adDestinations = listOf(
        RouteSingleWordDetail,
        RouteWordListCategory,
        RouteWordListForList,
        RouteWordsDetailList,
        RouteWordsDetailCategory
    )

    val state = combine(openingCountFlow,consumeSecondsFlow){openingCount,consumeSeconds->
        loadAdState.value = openingCount >= K.Ad.thresholdOpeningCount || consumeSeconds >= K.Ad.thresholdConsumeSeconds
        AdState(
            openingCount = openingCount,
            consumeSeconds = consumeSeconds,
        )
    }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdState())

    var uiEventState by mutableStateOf<AdUiEvent?>(null)
        private set


    init {
        listenAdShowState()
    }

    fun onEvent(event: AdEvent){
        when(event){
            AdEvent.Reset -> {
                openingCountFlow.value = 0
                consumeSecondsFlow.value = 0
            }
            is AdEvent.CheckFromDestination -> {
                if(premiumActiveFlow.value) return
                viewModelScope.launch {
                    val isAdDestination = adDestinations.contains(event.routeId)
                    if(isAdDestination){
                        start()
                    }else{
                        stop()
                    }
                    uiEventState = AdUiEvent.CheckAdShowState
                }

            }
            is AdEvent.SetPremiumActive -> {
                premiumActiveFlow.value = event.premiumActive
                if(event.premiumActive){
                    onEvent(AdEvent.Reset)
                    stop()
                }
            }
            is AdEvent.ClearUiEvent -> {
               uiEventState = null
            }
        }
    }

    private fun start(){
        setTimer()
        openingCountFlow.value += 1
    }

    private fun stop(){
        timerJob?.cancel()
    }

    private fun setTimer(){
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            ticker(K.Ad.consumeIntervalSeconds*1000L).receiveAsFlow().collectLatest {
                consumeSecondsFlow.emit(consumeSecondsFlow.value + K.Ad.consumeIntervalSeconds)
            }
        }
    }

    private fun listenAdShowState(){
        loadAdJob?.cancel()
        loadAdJob = viewModelScope.launch {
            loadAdState.collectLatest {loadAd->
                if(loadAd) uiEventState = AdUiEvent.LoadAd
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        loadAdJob?.cancel()
    }
}