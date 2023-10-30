package com.masterplus.trdictionary.core.domain.enums

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

enum class AppNavigationType {
    BOTTOM_NAVIGATION,
    RAIL,
    DRAWER;

    companion object{
        fun from(
            windowSizeClass: WindowWidthSizeClass,
            devicePosture: DevicePosture
        ): AppNavigationType {
            return when(windowSizeClass){
                WindowWidthSizeClass.Compact-> BOTTOM_NAVIGATION
                WindowWidthSizeClass.Medium -> RAIL
                WindowWidthSizeClass.Expanded -> {
                    if(devicePosture is DevicePosture.BookPosture) RAIL
                    else DRAWER
                }
                else -> BOTTOM_NAVIGATION
            }
        }
    }
}