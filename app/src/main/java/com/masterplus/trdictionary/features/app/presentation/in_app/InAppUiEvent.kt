package com.masterplus.trdictionary.features.app.presentation.in_app

import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager

sealed class InAppUiEvent{
    data class ShowReviewApi(val manager: ReviewManager, val reviewInfo: ReviewInfo): InAppUiEvent()
}
