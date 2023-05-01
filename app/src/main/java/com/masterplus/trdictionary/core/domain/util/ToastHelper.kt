package com.masterplus.trdictionary.core.domain.util

import android.content.Context
import android.widget.Toast

class ToastHelper {

    companion object{
        private var toast: Toast? = null

        fun showMessage(message: String, context: Context,duration: Int = Toast.LENGTH_LONG){
            toast?.cancel()
            toast = Toast.makeText(context,message,Toast.LENGTH_LONG)
            toast?.show()
        }
        fun showMessage(uiText: UiText, context: Context, duration: Int = Toast.LENGTH_LONG){
            showMessage(uiText.asString(context), context, duration)
        }
    }

}