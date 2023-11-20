package com.masterplus.trdictionary.core.presentation.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.domain.utils.UiText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

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


@Composable
fun ShowLifecycleToastMessage(
    message: UiText?,
    onDismiss: () -> Unit
) {

    val currentOnDismiss by rememberUpdatedState(newValue = onDismiss)

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(message,lifecycleOwner.lifecycle){
        snapshotFlow { message }
            .filterNotNull()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {message->
                ToastHelper.showMessage(message, context)
                currentOnDismiss()
            }
    }
}