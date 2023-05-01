package com.masterplus.trdictionary.core.presentation.dialog_body

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.RotatableLaunchEffect
import com.masterplus.trdictionary.core.presentation.components.buttons.NegativeFilledButton
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ShowGetTextDialog(
    title: String,
    content: String = "",
    onApproved: (String)->Unit,
    onClosed: ()->Unit,
){

    val textField = rememberSaveable(stateSaver = TextFieldValue.Saver){
        mutableStateOf( TextFieldValue(text = content, selection = TextRange(content.length)))
    }
    val error = rememberSaveable{
        mutableStateOf<String?>(null)
    }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val shape = MaterialTheme.shapes.medium

    RotatableLaunchEffect{
        focusRequester.requestFocus()
    }

    fun checkText(){
        val text = textField.value.text
        if(text.isBlank()){
            error.value = context.getString(R.string.error_not_empty_field)
            return
        }
        onApproved(text)
        onClosed()
    }

    CustomDialog(
        onClosed = onClosed
    ){
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 3.dp)
                .fillMaxWidth()
        ) {
            item {
                Text(
                    title,
                    modifier = Modifier.fillMaxWidth().padding(top = 7.dp, bottom = 5.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                OutlinedTextField(
                    value = textField.value,
                    onValueChange = {textField.value = it},
                    isError = error.value!=null,
                    singleLine = true,
                    label = { error.value?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            checkText()
                        }
                    ),
                    shape = shape,
                    placeholder = { Text(stringResource(R.string.text_field)) },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(horizontal = 1.dp, vertical = 13.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 3.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ){

                    NegativeFilledButton(
                        title = stringResource(R.string.cancel),
                        onClick = onClosed,
                        modifier = Modifier.weight(1f)
                    )

                    PrimaryButton(
                        title = stringResource(R.string.approve),
                        onClick = ::checkText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

}