package com.masterplus.trdictionary.features.app.presentation.in_app

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.util.DateFormatHelper
import com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.navigation.RouteSingleWordDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InAppFeaturesViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    application: Application
): ViewModel(){

    private val reviewManager = ReviewManagerFactory.create(application)

    var state by mutableStateOf(InAppFeaturesState())
        private set

    private var reviewInfoFlow = MutableStateFlow<ReviewInfo?>(null)

    private val destinations = listOf(
        RouteSingleWordDetail
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
                state = state.copy(uiEvent = null)
            }
        }
    }


    private fun checkReviewApi(routeId: String?){
        if(!state.enabledReviewApi) return

        if(destinations.contains(routeId)){
            state = state.copy(reviewApiDestCount = state.reviewApiDestCount + 1)
            if(state.reviewApiDestCount >= K.ReviewApi.reviewApiDestinationThreshold){
                loadReviewInfo()
                state = state.copy(reviewApiDestCount = 0)
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
                reviewInfoFlow.value = task.result
            }
        }
        appPreferences.setItem(KPref.inAppReviewDay,DateFormatHelper.toDateMillis(Date().time))
    }

    private fun showReviewApi(){
        viewModelScope.launch {
            reviewInfoFlow.value?.let { reviewInfo ->
                state = state.copy(uiEvent = InAppUiEvent.ShowReviewApi(reviewManager,reviewInfo))
                reviewInfoFlow.value = null
                state = state.copy(enabledReviewApi = false)
            }
        }
    }

    private fun checkEnabledApis(){
        val prefDayMillis = appPreferences.getItem(KPref.inAppReviewDay)
        val currentDayMillis = DateFormatHelper.toDateMillis(Date().time)
        val inAppReviewEnabled = prefDayMillis != currentDayMillis

        state = state.copy(
            enabledReviewApi = inAppReviewEnabled
        )
    }

}