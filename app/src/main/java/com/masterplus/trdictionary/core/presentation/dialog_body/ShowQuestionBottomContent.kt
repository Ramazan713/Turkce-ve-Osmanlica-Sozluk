package com.masterplus.trdictionary.core.presentation.dialog_body

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.presentation.components.buttons.NegativeFilledButton
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
import com.masterplus.trdictionary.R

@Composable
fun ShowQuestionDialog(
    title: String? = null,
    content: String? = null,
    onApproved: () -> Unit,
    allowDismiss: Boolean = true,
    onClosed: () -> Unit,
    negativeTitle: String = stringResource(R.string.cancel),
    positiveTitle: String = stringResource(R.string.approve)
){

    CustomDialog(
        onClosed = onClosed,
        allowDismiss = allowDismiss
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .padding(top = 16.dp, bottom = 2.dp)
        ) {
            if(title != null){
                Text(
                    title,
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 3.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if(content != null){
                Text(
                    content,
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 3.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 7.dp, top = 13.dp)
                    .padding(horizontal = 3.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                TextButton(
                    onClick = { onClosed() },
                    modifier = Modifier.weight(1f)
                ){
                    Text(negativeTitle)
                }

                FilledTonalButton(
                    onClick = {
                        onApproved()
                        onClosed()
                    },
                    modifier = Modifier.weight(1f)
                ){
                    Text(positiveTitle)
                }
            }
        }
    }

}