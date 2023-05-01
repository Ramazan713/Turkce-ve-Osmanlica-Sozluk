package com.masterplus.trdictionary.core.presentation.dialog_body

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
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.RotatableLaunchEffect
import com.masterplus.trdictionary.core.presentation.components.buttons.NegativeFilledButton
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton


@Composable
fun ShowSelectNumberDialog(
    minValueParam: Int, maxValueParam: Int, onApprove: (Int)->Unit,
    onClose:()->Unit,
    currentValue: Int = minValueParam,
){

    val minValue by rememberUpdatedState(minValueParam)
    val maxValue by rememberUpdatedState(maxValueParam)

    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(currentValue.toString(),
            TextRange(currentValue.toString().length)))
    }
    var errorState by rememberSaveable{
        mutableStateOf<String?>(null)
    }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    RotatableLaunchEffect {
        focusRequester.requestFocus()
    }

    fun checkNumberAndApprove(){
        val num=textState.text.toIntOrNull()

        if(num==null){
            errorState = context.getString(R.string.error_only_enter_number)
            return
        }
        if(num<minValue || num > maxValue){
            errorState = context.getString(R.string.error_mismatch_range)
            return
        }
        errorState = null
        onApprove(num)
        onClose()
    }

    CustomDialog(
        onClosed = onClose,
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp, horizontal = 3.dp)

        ) {
            item {
                Text(
                    stringResource(R.string.error_mismatch_range_with_values,minValue,maxValue),
                    style = MaterialTheme.typography.titleLarge,
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
                            checkNumberAndApprove()
                        },
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                        autoCorrect = false,
                    ),
                    isError = errorState!=null,
                    label = { errorState?.let { Text(it) } },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.focusRequester(focusRequester).fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {

                    NegativeFilledButton(
                        title = stringResource(R.string.cancel),
                        onClick = onClose,
                        modifier = Modifier.weight(1f)
                    )

                    PrimaryButton(
                        title = stringResource(R.string.approve),
                        onClick = ::checkNumberAndApprove,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}