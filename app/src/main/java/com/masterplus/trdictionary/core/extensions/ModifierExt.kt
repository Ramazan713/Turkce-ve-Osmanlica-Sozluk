package com.masterplus.trdictionary.core.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.useBackground(
    backgroundColor: Color?,
    shape: Shape = RectangleShape
): Modifier{
    if(backgroundColor == null) return this
    return this.background(backgroundColor, shape)
}

@Composable
fun Modifier.useBorder(
    borderWidth: Dp?,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    shape: Shape = RectangleShape
): Modifier{
    if(borderWidth == null) return this
    return this.border(borderWidth,color, shape)
}

fun Modifier.clearFocusOnTap(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    Modifier.pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown(pass = PointerEventPass.Initial)
            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
            if (upEvent != null) {
                focusManager.clearFocus()
            }
        }
    }
}