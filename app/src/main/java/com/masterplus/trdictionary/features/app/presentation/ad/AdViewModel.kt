package com.masterplus.trdictionary.features.app.presentation.ad

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.data.preferences.get
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.features.search.presentation.navigation.RouteSearch
import com.masterplus.trdictionary.features.word_detail.single_word_detail.navigation.RouteSingleWordDetail
import com.masterplus.trdictionary.features.word_detail.word_category.navigation.RouteListDetailCategoryWords
import com.masterplus.trdictionary.features.word_detail.word_list_for_list_detail.navigation.RouteWordListForListDetail
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
    private val appPreferences: AppPreferences
): ViewModel() {

    private var thresholdOpeningCount = KPref.thresholdOpeningCount.default
    private var consumeIntervalSeconds = KPref.consumeIntervalSeconds.default
    private var thresholdConsumeSeconds = KPref.thresholdConsumeSeconds.default

    private var premiumActiveFlow = MutableStateFlow(false)

    private var timerJob: Job? = null
    private var loadAdJob: Job? = null




    private val _state = MutableStateFlow(AdState())

    private val _uiEventState = MutableStateFlow<AdUiEvent?>(null)
    val uiEventState: StateFlow<AdUiEvent?> = _uiEventState.asStateFlow()

    init {
        listenAdShowState()
        listenPrefValues()
    }

    fun onEvent(event: AdEvent){
        when(event){
            AdEvent.Reset -> {
                _state.update { AdState() }
            }
            is AdEvent.CheckFromDestination -> {
                val destination = event.routeId
                if(premiumActiveFlow.value) return
                if(destination == _state.value.currentDestination){
                    checkAdIfNotLoaded()
                    return
                }
                viewModelScope.launch {
                    val isAdDestination = adFactors.keys.contains(destination)
                    if(isAdDestination && destination != null){
                        start(destination)
                    }else{
                        stop()
                    }
                    _uiEventState.update { AdUiEvent.CheckAdShowState }
                }

            }
            is AdEvent.SetPremiumActive -> {
                premiumActiveFlow.update { event.premiumActive }
                if(event.premiumActive){
                    onEvent(AdEvent.Reset)
                    stop()
                }
            }
            is AdEvent.ClearUiEvent -> {
                _uiEventState.update { null }
            }
        }
    }

    private fun start(destination: String){
        val unit = adFactors[destination]?.openingCountFactor ?: 1
        _state.update { it.copy(
            openingCount = it.openingCount + unit,
            currentDestination = destination)
        }
        setTimer()
    }

    private fun stop(){
        _state.update { it.copy(currentDestination = null) }
        timerJob?.cancel()
    }

    private fun setTimer(){
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            ticker(consumeIntervalSeconds * 1000L).receiveAsFlow().collectLatest {
                val unit = adFactors[_state.value.currentDestination]?.consumeSecondsFactor ?: 1
                val changedUnit = consumeIntervalSeconds * unit
                _state.update { it.copy(consumeSeconds = it.consumeSeconds + changedUnit) }
            }
        }
    }

    private fun listenAdShowState(){
        loadAdJob?.cancel()
        loadAdJob = viewModelScope.launch {
            _state.map {state->
                    checkThreshold(state)
                }
                .distinctUntilChanged()
                .filter { it }
                .collectLatest { _ ->
                    _uiEventState.update { AdUiEvent.LoadAd }
            }
        }

    }


    private fun listenPrefValues(){
        viewModelScope.launch {
            appPreferences.dataFlow.collectLatest { pref->
                consumeIntervalSeconds = pref[KPref.consumeIntervalSeconds]
                thresholdConsumeSeconds = pref[KPref.thresholdConsumeSeconds]
                thresholdOpeningCount = pref[KPref.thresholdOpeningCount]
            }
        }
    }

    private fun checkThreshold(state: AdState): Boolean{
        return state.openingCount >= thresholdOpeningCount || state.consumeSeconds >= thresholdConsumeSeconds
    }

    private fun checkAdIfNotLoaded(){
        if(_uiEventState.value == null && checkThreshold(_state.value)){
            _uiEventState.update { AdUiEvent.LoadAd }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        loadAdJob?.cancel()
    }
}

private val adFactors = mapOf(
    RouteSingleWordDetail to AdFactor(
        openingCountFactor = 1,
        consumeSecondsFactor = 2
    ),
    RouteSearch to AdFactor(
        openingCountFactor = 3,
        consumeSecondsFactor = 2
    ),
    RouteWordListForListDetail to AdFactor(
        openingCountFactor = 2,
        consumeSecondsFactor = 1
    ),
    RouteListDetailCategoryWords to AdFactor(
        openingCountFactor = 2,
        consumeSecondsFactor = 1
    ),
)

private data class AdFactor(
    val openingCountFactor: Int,
    val consumeSecondsFactor: Int
)

