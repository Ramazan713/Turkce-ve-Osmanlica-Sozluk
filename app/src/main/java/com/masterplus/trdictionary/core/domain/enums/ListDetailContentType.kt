package com.masterplus.trdictionary.core.domain.enums

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

enum class ListDetailContentType {
    SINGLE_PANE,
    DUAL_PANE;
    companion object{
        fun from(
            windowSizeClass: WindowWidthSizeClass,
            devicePosture: DevicePosture
        ): ListDetailContentType {
            return when(windowSizeClass){
                WindowWidthSizeClass.Compact-> SINGLE_PANE
                WindowWidthSizeClass.Medium -> {
                    if(devicePosture != DevicePosture.NormalPosture) DUAL_PANE
                    else SINGLE_PANE
                }
                WindowWidthSizeClass.Expanded -> DUAL_PANE
                else -> SINGLE_PANE
            }
        }
    }
}