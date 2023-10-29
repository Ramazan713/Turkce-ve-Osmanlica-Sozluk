package com.masterplus.trdictionary.features.word_detail.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.features.word_detail.domain.model.MeaningExamples


@Composable
fun MeaningItem(
    meaningExamples: MeaningExamples,
    modifier: Modifier = Modifier,
){
    val meaning = meaningExamples.meaning
    Column(
        modifier = modifier
            .padding(horizontal = 3.dp, vertical = 3.dp),
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                    append("${meaning.orderItem}. ")
                }
                if(meaning.feature!=null){
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.W600, color = MaterialTheme.colorScheme.error)){
                        append("${meaning.feature} ")
                    }
                }
                append(meaning.meaning)
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        meaningExamples.examples.forEach { exampleAuthor ->
            Text(
                buildAnnotatedString {
                    append(exampleAuthor.content)
                    withStyle(SpanStyle(fontWeight = FontWeight.W500)){
                        append(" - ${exampleAuthor.authorName}")
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}