package com.masterplus.trdictionary.core.presentation.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.presentation.components.DefaultToolTip


class DefaultSnackBar{

    private val snackbarHostState = SnackbarHostState()

    suspend fun showMessage(
        message: String,
        withDismissAction: Boolean = false,
        actionLabel: String? = null,
        onActionResult: (() -> Unit)? = null,
        duration: SnackbarDuration = if(actionLabel != null) SnackbarDuration.Indefinite else SnackbarDuration.Short
    ){
        snackbarHostState.currentSnackbarData?.dismiss()

        val result = snackbarHostState.showSnackbar(
            message = message,
            withDismissAction = withDismissAction,
            actionLabel = actionLabel,
            duration = duration,
        )
        if(result == SnackbarResult.ActionPerformed){
            onActionResult?.invoke()
        }
    }

    val snackBarHost:  @Composable () -> Unit
        get() {
            return {
                SnackbarHost(snackbarHostState){data->
                    GetSnackBar(data = data)
                }
            }
        }
}


@Composable
fun rememberDefaultSnackBar(): DefaultSnackBar {
    return remember {
        DefaultSnackBar()
    }
}


@Composable
private fun GetSnackBar(data: SnackbarData) {
    Snackbar(
        modifier = Modifier
            .padding(12.dp),
        dismissAction = {
            if(data.visuals.withDismissAction) {
                DefaultToolTip(tooltip = stringResource(id = R.string.close)) {
                    IconButton(onClick = { data.dismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }
                }
            }
        },
        action = {
            data.visuals.actionLabel?.let { actionLabel->
                TextButton(
                    onClick = { data.performAction() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.inversePrimary
                    )
                ) {
                    Text(text = actionLabel)
                }
            }
        }
    ) {
        Text(data.visuals.message, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}


@Composable
fun ShowLifecycleSnackBarMessage(
    message: UiText?,
    snackBar: DefaultSnackBar,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    EventHandler(
        event = message,
        onEvent = { uiText ->
            snackBar.showMessage(uiText.asString(context), withDismissAction = true)
            onDismiss()
        }
    )
}

