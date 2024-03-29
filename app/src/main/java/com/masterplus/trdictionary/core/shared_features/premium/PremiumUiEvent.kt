package com.masterplus.trdictionary.core.shared_features.premium

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams

sealed class PremiumUiEvent{
    data class LaunchBillingFlow(val billingFlowParams: BillingFlowParams, val billingClient: BillingClient):
        PremiumUiEvent()
}
