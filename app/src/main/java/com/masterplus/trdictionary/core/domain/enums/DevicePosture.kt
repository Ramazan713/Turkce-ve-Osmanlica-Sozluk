package com.masterplus.trdictionary.core.domain.enums

import android.graphics.Rect
import androidx.window.layout.FoldingFeature
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface DevicePosture {
    data object NormalPosture : DevicePosture

    data class BookPosture(
        val hingePosition: Rect
    ) : DevicePosture

    data class Separating(
        val hingePosition: Rect,
        var orientation: FoldingFeature.Orientation
    ) : DevicePosture
    companion object{
        fun from(foldFeature: FoldingFeature?): DevicePosture {
            if(isBookPosture(foldFeature))
                return BookPosture(foldFeature.bounds)
            if(isSeparating(foldFeature))
                return Separating(foldFeature.bounds,foldFeature.orientation)
            return NormalPosture
        }
    }
}


@OptIn(ExperimentalContracts::class)
private fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
}

@OptIn(ExperimentalContracts::class)
private fun isSeparating(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
}