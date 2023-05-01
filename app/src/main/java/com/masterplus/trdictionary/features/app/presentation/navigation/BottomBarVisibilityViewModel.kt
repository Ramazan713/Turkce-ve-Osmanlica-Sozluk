package com.masterplus.trdictionary.features.app.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomBarVisibilityViewModel @Inject constructor(

):ViewModel(){

    var isVisible by mutableStateOf(true)
        private set

    private var job: Job? = null

    fun setVisibility(visible: Boolean){
        job?.cancel()
        job = viewModelScope.launch {
            delay(200)
            isVisible = visible
        }

    }
}