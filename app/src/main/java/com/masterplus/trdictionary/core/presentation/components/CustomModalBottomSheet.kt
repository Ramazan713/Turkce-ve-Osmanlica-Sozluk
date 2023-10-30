package com.masterplus.trdictionary.core.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetStateParam: SheetState? = null,
    skipHalfExpanded: Boolean = false,
    content: @Composable (ColumnScope.() -> Unit)
){

    val sheetState = sheetStateParam ?: rememberModalBottomSheetState(
        skipPartiallyExpanded = skipHalfExpanded
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ){
        content()
    }
}