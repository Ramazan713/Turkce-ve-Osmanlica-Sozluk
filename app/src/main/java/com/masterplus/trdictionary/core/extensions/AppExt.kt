package com.masterplus.trdictionary.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.masterplus.trdictionary.MainActivity


@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
fun Context.refreshApp(){
    val activity = this as Activity
    activity.finish()
    activity.startActivity(Intent(activity, MainActivity::class.java))
}


fun CombinedLoadStates.isLoading(includeAppend: Boolean = true): Boolean{

    return (this.prepend is LoadState.Loading) ||
            (this.append is LoadState.Loading && includeAppend) || (this.refresh is LoadState.Loading)
}

fun Modifier.noRippleClickable(enabled: Boolean = true,onClicked: ()->Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClicked
    )
}


fun Int.addPrefixZeros(totalLength: Int): String{
    val numStr = this.toString()
    val prefix = (totalLength-numStr.length).let { num->
        if(num>0) "0".repeat(num) else ""
    }
    return prefix + numStr
}
