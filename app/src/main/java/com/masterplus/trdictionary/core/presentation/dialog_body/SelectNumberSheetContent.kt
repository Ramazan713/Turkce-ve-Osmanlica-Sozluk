package com.masterplus.trdictionary.core.presentation.dialog_body

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.RotatableLaunchEffect
import com.masterplus.trdictionary.core.presentation.utils.PreviewDesktop


@Composable
fun ShowSelectNumberDialog(
    minPos: Int,
    maxPos: Int,
    onApprove: (Int) -> Unit,
    onClose:() -> Unit,
    currentPos: Int = minPos,
){
    var textState by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) {
        mutableStateOf(
            TextFieldValue(currentPos.plus(1).toString(),
            TextRange(currentPos.plus(1).toString().length))
        )
    }
    var errorState by rememberSaveable{
        mutableStateOf<String?>(null)
    }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

//    RotatableLaunchEffect {
//        focusRequester.requestFocus()
//    }

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                stringResource(R.string.error_mismatch_range_with_values,minPos + 1,maxPos + 1),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        text =  {
            OutlinedTextField(
                value = textState,
                onValueChange = {textState = it},
                keyboardActions = KeyboardActions(
                    onDone = {
                        checkNumberAndApprove(
                            context,
                            onSetError = { errorState = it },
                            minValue = minPos,
                            maxValue = maxPos,
                            text = textState.text,
                            onApprove = {
                                onApprove(it)
                                onClose()
                            }
                        )
                    },
                ),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                isError = errorState != null,
                label = { errorState?.let { Text(it) } },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .semantics {
                        contentDescription = context.getString(R.string.text_field)
                    },
            )
        },
        dismissButton = {
            TextButton(
                onClick = onClose,
            ){
                Text(stringResource(R.string.cancel),)
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    checkNumberAndApprove(
                        context,
                        onSetError = { errorState = it },
                        minValue = minPos,
                        maxValue = maxPos,
                        text = textState.text,
                        onApprove = {
                            onApprove(it)
                            onClose()
                        }
                    )
                },
            ){
                Text(stringResource(R.string.approve),)
            }
        },
    )
}

private fun checkNumberAndApprove(
    context: Context,
    onSetError: (String?) -> Unit,
    minValue: Int,
    maxValue: Int,
    text: String,
    onApprove: (Int) -> Unit
){
    val num = text.toIntOrNull()?.minus(1) ?: return onSetError(context.getString(R.string.error_only_enter_number))

    if(num < minValue || num > maxValue){
        onSetError(context.getString(R.string.error_mismatch_range))
        return
    }
    onSetError(null)
    onApprove(num)
}

@PreviewDesktop
@Preview(showBackground = true)
@Composable
fun ShowSelectNumberDialogPreview() {
    ShowSelectNumberDialog(
        minPos = 10,
        maxPos = 100,
        onApprove = {},
        onClose = {}
    )
}
