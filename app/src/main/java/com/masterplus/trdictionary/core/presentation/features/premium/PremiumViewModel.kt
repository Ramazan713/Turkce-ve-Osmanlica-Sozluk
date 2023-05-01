package com.masterplus.trdictionary.core.presentation.features.premium

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.data.repo.PremiumRepo
import com.masterplus.trdictionary.core.domain.model.premium.PremiumProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val premiumRepo: PremiumRepo
): ViewModel(){

    val firstPremiumProduct = premiumRepo.products.map {
        PremiumProduct(it.firstOrNull())
    }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),null)

    var state by mutableStateOf(PremiumState())
        private set

    private var premiumJob: Job? = null
    private var messageJob: Job? = null

    init {
        premiumRepo.startConnection()
        premiumListener()
        messageListener()
    }

    fun onEvent(event: PremiumEvent){
        when(event){
            is PremiumEvent.ClearMessage -> {
                state = state.copy(message = null)
            }
            is PremiumEvent.ClearUiEvent -> {
                state = state.copy(uiEvent = null)
            }
            is PremiumEvent.Purchase -> {
                viewModelScope.launch {
                    val billingClient = premiumRepo.billingClient
                    val billingFlowParams = premiumRepo.getBillingFlowParams(event.premiumProduct.product ?: return@launch,event.offerToken)
                    state = state.copy(uiEvent = PremiumUiEvent.LaunchBillingFlow(billingFlowParams, billingClient))
                }
            }
            is PremiumEvent.CheckPremium -> {
                viewModelScope.launch {
                    premiumRepo.checkPremium()
                }
            }
        }
    }

    private fun premiumListener(){
        premiumJob?.cancel()
        premiumJob = viewModelScope.launch {
            premiumRepo.premiumActive.collectLatest {
                state = state.copy(isPremium = it)
            }
        }
    }

    private fun messageListener(){
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            premiumRepo.messages.collectLatest { message->
                state = state.copy(message = message)
            }
        }
    }
}