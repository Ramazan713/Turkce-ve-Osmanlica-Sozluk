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
    enabledBackHandler: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)
){

    val func = remember {
        mutableStateOf(object: (SheetValue)->Boolean{
            override fun invoke(it: SheetValue): Boolean {
                if(it == SheetValue.Hidden){
                    onDismissRequest()
                }
                return true
            }
        })
    }

    BackHandler(enabledBackHandler){
        onDismissRequest()
    }

    val sheetState = sheetStateParam ?: rememberSheetState(
        skipHalfExpanded = skipHalfExpanded,
        confirmValueChange = func.value
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