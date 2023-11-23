package com.masterplus.trdictionary.features.app.presentation.in_app

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.utils.DateFormatHelper
import com.masterplus.trdictionary.features.search.presentation.navigation.RouteSearch
import com.masterplus.trdictionary.features.word_detail.single_word_detail.navigation.RouteSingleWordDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InAppFeaturesViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    application: Application
): ViewModel(){

    private val reviewManager = ReviewManagerFactory.create(application)


    private val _state = MutableStateFlow(InAppFeaturesState())
    val state: StateFlow<InAppFeaturesState> = _state.asStateFlow()

    private var reviewInfoFlow = MutableStateFlow<ReviewInfo?>(null)

    private val destinations = listOf(
        RouteSingleWordDetail,
        RouteSearch
    )

    init {
        checkEnabledApis()
    }

    fun onEvent(event: InAppEvent){
        when(event){
            is InAppEvent.CheckDestination -> {
                checkReviewApi(event.routeId)
            }
            is InAppEvent.ClearUiEvent -> {
                _state.update { it.copy(uiEvent = null)}
            }
        }
    }


    private fun checkReviewApi(routeId: String?){
        if(!_state.value.enabledReviewApi || _state.value.currentDestination == routeId) return
        _state.update { it.copy(currentDestination = routeId)}

        if(destinations.contains(routeId)){
            _state.update {state -> state.copy(reviewApiDestCount = state.reviewApiDestCount + 1)}
            if(_state.value.reviewApiDestCount >= K.ReviewApi.reviewApiDestinationThreshold){
                loadReviewInfo()
                _state.update { it.copy(reviewApiDestCount = 0)}
            }
        }else{
            showReviewApi()
        }
    }

    private fun loadReviewInfo(){
        if(reviewInfoFlow.value != null) return
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reviewInfoFlow.update { task.result }
            }
        }
        viewModelScope.launch {
            appPreferences.setItem(KPref.inAppReviewDay, DateFormatHelper.toDateMillis(Date().time))
        }
    }

    private fun showReviewApi(){
        viewModelScope.launch {
            reviewInfoFlow.value?.let { reviewInfo ->
                _state.update { it.copy(uiEvent = InAppUiEvent.ShowReviewApi(reviewManager,reviewInfo))}
                reviewInfoFlow.update {null}
                _state.update { it.copy(enabledReviewApi = false)}
            }
        }
    }

    private fun checkEnabledApis(){
        viewModelScope.launch {
            val prefDayMillis = appPreferences.getItem(KPref.inAppReviewDay)
            val currentDayMillis = DateFormatHelper.toDateMillis(Date().time)
            val inAppReviewEnabled = prefDayMillis != currentDayMillis
            _state.update { it.copy(enabledReviewApi = inAppReviewEnabled)}
        }
    }

}