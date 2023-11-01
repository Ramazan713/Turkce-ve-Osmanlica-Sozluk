package com.masterplus.trdictionary.core.presentation.dialog_body

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.util.PreviewDesktop


@Composable
fun ShowSelectNumberDialog(
    minValue: Int,
    maxValue: Int,
    onApprove: (Int) -> Unit,
    onClose:() -> Unit,
    currentValue: Int = minValue,
){
    var textState by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) {
        mutableStateOf(
            TextFieldValue(currentValue.toString(),
            TextRange(currentValue.toString().length))
        )
    }
    var errorState by rememberSaveable{
        mutableStateOf<String?>(null)
    }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    CustomDialog(
        onClosed = onClose,
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .padding(top = 16.dp, bottom = 2.dp)
        ) {
            item {
                Text(
                    stringResource(R.string.error_mismatch_range_with_values,minValue,maxValue),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
            }
            item {
                OutlinedTextField(
                    value = textState,
                    onValueChange = {textState = it},
                    keyboardActions = KeyboardActions(
                        onDone = {
                            checkNumberAndApprove(
                                context,
                                onSetError = { errorState = it },
                                minValue = minValue,
                                maxValue = maxValue,
                                text = textState.text,
                                onApprove = {
                                    onApprove(it)
                                    onClose()
                                }
                            )
                        },
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                        autoCorrect = false,
                    ),
                    isError = errorState != null,
                    label = { errorState?.let { Text(it) } },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {

                    TextButton(
                        onClick = onClose,
                        modifier = Modifier.weight(1f)
                    ){
                        Text(stringResource(R.string.cancel),)
                    }

                    FilledTonalButton(
                        onClick = {
                            checkNumberAndApprove(
                                context,
                                onSetError = { errorState = it },
                                minValue = minValue,
                                maxValue = maxValue,
                                text = textState.text,
                                onApprove = {
                                    onApprove(it)
                                    onClose()
                                }
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ){
                        Text(stringResource(R.string.approve),)
                    }
                }
            }
        }
    }
}

private fun checkNumberAndApprove(
    context: Context,
    onSetError: (String?) -> Unit,
    minValue: Int,
    maxValue: Int,
    text: String,
    onApprove: (Int) -> Unit
){
    val num = text.toIntOrNull()

    if(num == null){
        onSetError(context.getString(R.string.error_only_enter_number))
        return
    }
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
        minValue = 10,
        maxValue = 100,
        onApprove = {},
        onClose = {}
    )
}
