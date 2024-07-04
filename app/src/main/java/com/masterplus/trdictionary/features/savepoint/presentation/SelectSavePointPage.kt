package com.masterplus.trdictionary.features.savepoint.presentation
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.presentation.components.SavePointItem
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.selections.CustomDropdownMenu
import com.masterplus.trdictionary.core.presentation.utils.EventHandler
import com.masterplus.trdictionary.core.presentation.utils.PreviewDesktop
import com.masterplus.trdictionary.core.presentation.utils.SampleDatas
import com.masterplus.trdictionary.core.presentation.utils.ShowLifecycleSnackBarMessage
import com.masterplus.trdictionary.core.presentation.utils.rememberDefaultSnackBar

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun SelectSavePointPage(
    onNavigateBack: ()->Unit,
    onNavigateToList: (listId: Int,pos: Int)->Unit,
    onNavigateToCatRandom: (CategoryEnum,Int)->Unit,
    onNavigateToCatAll: (CategoryEnum,Int)->Unit,
    onNavigateToCatAlphabetic: (CategoryEnum,String,Int)->Unit,
    state: SelectSavePointState,
    onEvent: (SelectSavePointEvent) -> Unit
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val currentOnNavigateToList by rememberUpdatedState(onNavigateToList)
    val currentOnNavigateToCatAll by rememberUpdatedState(onNavigateToCatAll)
    val currentOnNavigateToCatRandom by rememberUpdatedState(onNavigateToCatRandom)
    val currentOnNavigateToCatAlphabetic by rememberUpdatedState(onNavigateToCatAlphabetic)
    val currentOnEvent by rememberUpdatedState(newValue = onEvent)

    EventHandler(event = state.uiEvent) { event ->
        when(event){
            is SelectSavePointUiEvent.NavigateToList -> {
                currentOnNavigateToList(event.listId,event.pos)
            }
            is SelectSavePointUiEvent.NavigateToCategoryAll -> {
                currentOnNavigateToCatAll(event.catEnum,event.pos)
            }
            is SelectSavePointUiEvent.NavigateToCategoryRandom -> {
                currentOnNavigateToCatRandom(event.catEnum,event.pos)
            }
            is SelectSavePointUiEvent.NavigateToCategoryAlphabetic -> {
                currentOnNavigateToCatAlphabetic(event.catEnum,event.c,event.pos)
            }
        }
        currentOnEvent(SelectSavePointEvent.ClearUiEvent)
    }

    val defaultSnackBar = rememberDefaultSnackBar()

    ShowLifecycleSnackBarMessage(
        message = state.message,
        snackBar = defaultSnackBar,
        onDismiss = { onEvent(SelectSavePointEvent.ClearMessage) }
    )

    Scaffold(
        snackbarHost = defaultSnackBar.snackBarHost,
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.n_savepoints_c,state.title),
                onNavigateBack = onNavigateBack,
                scrollBehavior = scrollBehavior
            )
        },
    ) {paddings->
        Box(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
            ,
        ) {

            if(state.savePoints.isEmpty()){
                Text(
                    stringResource(R.string.empty_savepoint),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    if(state.showDropdownMenu){
                        CustomDropdownMenu(
                            items = state.dropdownItems,
                            currentItem = state.selectedDropdownItem,
                            onItemChange = { onEvent(SelectSavePointEvent.SelectDropdownMenuItem(it)) }
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { onEvent(SelectSavePointEvent.LoadSavePoint) },
                        enabled = state.currentSelectedSavePoint != null
                    ) {
                        Text(text = stringResource(R.string.load_and_go))
                    }
                }

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Adaptive(350.dp)
                ) {
                    items(
                        state.savePoints,
                        key = {item -> item.id ?: 0},
                    ){item: SavePoint ->
                        SavePointItem(
                            savePoint = item,
                            onClick = {onEvent(SelectSavePointEvent.Select(item))},
                            isSelected = state.currentSelectedSavePoint == item,
                            onTitleEditClick = {
                                onEvent(SelectSavePointEvent.ShowDialog(true,
                                    SelectSavePointDialogEvent.EditTitle(item)))
                            },
                            onDeleteClick = {
                                onEvent(
                                    SelectSavePointEvent.ShowDialog(true, SelectSavePointDialogEvent.AskDelete(item))
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if(state.showDialog){
        ShowDialog(
            state.modalDialog,
            onEvent = onEvent
        )
    }

}


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun ShowDialog(
    event: SelectSavePointDialogEvent?,
    onEvent: (SelectSavePointEvent)->Unit
){
    when(event){
        is SelectSavePointDialogEvent.AskDelete ->{
            ShowQuestionDialog(
                title = stringResource(R.string.question_delete),
                content = stringResource(R.string.not_revertable),
                onApproved = {onEvent(SelectSavePointEvent.Delete(event.savePoint))},
                onClosed = {onEvent(SelectSavePointEvent.ShowDialog(false))}
            )
        }
        is SelectSavePointDialogEvent.EditTitle ->{
            ShowGetTextDialog(
                title = stringResource(R.string.edit_save_point),
                content = event.savePoint.title,
                onApproved = {onEvent(SelectSavePointEvent.EditTitle(it, event.savePoint))},
                onClosed = {onEvent(SelectSavePointEvent.ShowDialog(false))}
            )
        }
        else -> {}
    }
}



@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@PreviewDesktop
@Preview(showBackground = true)
@Composable
fun SelectSavePointPagePreview() {
    SelectSavePointPage(
        onNavigateBack = {},
        onEvent = {},
        onNavigateToCatAll = {x,y->},
        onNavigateToCatAlphabetic = {x,y,z->},
        onNavigateToCatRandom = {x,y->},
        onNavigateToList = {x,y->},
        state = SelectSavePointState(
            savePoints = SampleDatas.savePoints,
            showDropdownMenu = true
        )
    )
}
