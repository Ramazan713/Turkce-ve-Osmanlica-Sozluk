package com.masterplus.trdictionary.core.presentation.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.util.ToastHelper
import com.masterplus.trdictionary.core.domain.util.UiText
import com.masterplus.trdictionary.features.word_detail.domain.model.WordMeanings


fun String.shareText(context: Context){
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, this@shareText)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

fun String.copyClipboardText(context: Context,clipboardManager: ClipboardManager){
    clipboardManager.setText(buildAnnotatedString { append(this@copyClipboardText) })
    ToastHelper.showMessage(UiText.Resource(R.string.copied),context)
}

fun WordMeanings.share(context: Context){
    val wordMeanings = this
    val wordContent = wordMeanings.word.word
    val content = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
            append("$wordContent\n\n")
        }
        wordMeanings.meanings.forEachIndexed { index, meaningExamples ->
            val meaning = meaningExamples.meaning
            append("${index+1}. ")
            if(meaning.feature!=null){
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.W600)){
                    append("[${meaning.feature}] ")
                }
            }
            append("${meaningExamples.meaning.meaning}\n")
            meaningExamples.examples.forEach { exampleAuthor ->
                append("* ${exampleAuthor.example.content}")
                withStyle(SpanStyle(fontWeight = FontWeight.W500)){
                    append(" - ${exampleAuthor.author.name}")
                }
            }
            append("\n")
        }
    }.text
    content.shareText(context)
}

fun Context.shareApp(){
    val url = "http://play.google.com/store/apps/details?id=$packageName"
    url.shareText(this)
}

fun Context.launchUrl(url: String){
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        setPackage("com.android.vending")
    }
    startActivity(intent)
}
fun Context.launchPlayApp(){
    val url = "http://play.google.com/store/apps/details?id=$packageName"
    launchUrl(url)
}

