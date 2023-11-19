package com.masterplus.trdictionary.core.shared_features.premium

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


    private val _state = MutableStateFlow(PremiumState())
    val state: StateFlow<PremiumState> = _state.asStateFlow()

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
                _state.update { it.copy(message = null)}
            }
            is PremiumEvent.ClearUiEvent -> {
                _state.update { it.copy(uiEvent = null)}
            }
            is PremiumEvent.Purchase -> {
                viewModelScope.launch {
                    val billingClient = premiumRepo.billingClient
                    val billingFlowParams = premiumRepo.getBillingFlowParams(event.premiumProduct.product ?: return@launch,event.offerToken)
                    _state.update { state-> state.copy(
                        uiEvent = PremiumUiEvent.LaunchBillingFlow(
                            billingFlowParams,
                            billingClient
                        )
                    )}
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
            premiumRepo.premiumActive.collectLatest {isPremium->
                _state.update { it.copy(isPremium = isPremium)}
            }
        }
    }

    private fun messageListener(){
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            premiumRepo.messages.collectLatest { message->
                _state.update { it.copy(message = message)}
            }
        }
    }
}