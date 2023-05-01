package com.masterplus.trdictionary.core.presentation.components
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope

@Composable
fun RotatableLaunchEffect(
    block: suspend (CoroutineScope) -> Unit
){
    var isExecuted by rememberSaveable{
        mutableStateOf(false)
    }

    LaunchedEffect(true){
        if(!isExecuted){
            isExecuted = true
            block(this)
        }
    }
}