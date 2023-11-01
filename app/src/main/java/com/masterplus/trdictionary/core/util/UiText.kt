package com.masterplus.trdictionary.core.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


sealed class UiText{
    data class Resource(@StringRes val resId: Int,val formatArgs: List<Any> = emptyList()): UiText()
    data class Text(val content: String): UiText()


    fun asString(context: Context): String{
        return when(this){
            is Resource -> context.getString(resId,*formatArgs.toTypedArray())
            is Text -> content
        }
    }

    @Composable
    fun asString(): String{
        return when(this){
            is Resource -> stringResource(resId,*formatArgs.toTypedArray())
            is Text -> content
        }
    }
}