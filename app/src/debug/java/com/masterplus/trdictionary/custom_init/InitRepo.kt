package com.masterplus.trdictionary.custom_init

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.window.layout.DisplayFeature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class InitRepo @Inject constructor(

) {

    private val _state = MutableStateFlow(InitState())
    val state = _state.asStateFlow()


    fun init(
        windowWidthSizeClass: WindowWidthSizeClass,
        displayFeatures: List<DisplayFeature> = emptyList(),
        destinationRoute: String? = null
    ){
        _state.update {
            it.copy(
                widthSizeClass = windowWidthSizeClass,
                displayFeatures = displayFeatures,
                destinationRoute = destinationRoute
            )
        }
    }

    fun navigateTo(destinationRoute: String){
        _state.update {
            it.copy(destinationRoute = destinationRoute)
        }
    }

    fun clearNavigation(){
        _state.update {
            it.copy(destinationRoute = null)
        }
    }

    fun changeWindowWidthClass(windowWidthSizeClass: WindowWidthSizeClass){
        _state.update {
            it.copy(widthSizeClass = windowWidthSizeClass)
        }
    }

}