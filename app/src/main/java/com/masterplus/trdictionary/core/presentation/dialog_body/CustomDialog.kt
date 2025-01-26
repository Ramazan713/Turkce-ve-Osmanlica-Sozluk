package com.masterplus.trdictionary.core.presentation.dialog_body

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Composable
fun CustomDialog(
    onClosed: () -> Unit,
    modifier: Modifier = Modifier,
    allowDismiss: Boolean = true,
    useDefaultWidth: Boolean = true,
    adaptiveWidthSizeClass: WindowWidthSizeClass? = null,
    content: @Composable () -> Unit,
){
    val isEdgeToEdgeEnabledByDefault = Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM

    val platformDefaultWidth = if(adaptiveWidthSizeClass == null) useDefaultWidth
        else adaptiveWidthSizeClass != WindowWidthSizeClass.Compact

    Dialog(
        onDismissRequest = onClosed,
        properties = DialogProperties(
            usePlatformDefaultWidth = platformDefaultWidth || isEdgeToEdgeEnabledByDefault,
            dismissOnBackPress = allowDismiss,
            dismissOnClickOutside = allowDismiss,
            decorFitsSystemWindows = !isEdgeToEdgeEnabledByDefault
        )
    ){
        if(!platformDefaultWidth && isEdgeToEdgeEnabledByDefault){
            SetUpEdgeToEdgeDialog()
        }
        Surface(
            modifier = modifier
                .safeDrawingPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.medium),
            color = MaterialTheme.colorScheme.surfaceVariant
        ){
            content()
        }
    }
}

@Composable
private fun SetUpEdgeToEdgeDialog() {
    val parentView = LocalView.current.parent as View
    val window = (parentView as DialogWindowProvider).window

    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.attributes.fitInsetsTypes = 0
        window.attributes.fitInsetsSides = 0
    }
}