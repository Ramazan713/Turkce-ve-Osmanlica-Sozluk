package com.masterplus.trdictionary.custom_init

import androidx.lifecycle.ViewModel
import com.masterplus.trdictionary.custom_init.InitRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitTestViewModel @Inject constructor(
    private val initRepo: InitRepo
): ViewModel() {

    val state = initRepo.state

    fun clearNavigation(){
        initRepo.clearNavigation()
    }
}