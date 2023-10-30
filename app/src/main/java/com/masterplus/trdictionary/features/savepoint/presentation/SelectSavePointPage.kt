package com.masterplus.trdictionary.features.savepoint.presentation
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.util.ToastHelper
import com.masterplus.trdictionary.core.presentation.selectors.CustomDropdownMenu
import com.masterplus.trdictionary.core.presentation.components.SavePointItem
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import kotlinx.coroutines.flow.collectLatest

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

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val currentOnNavigateToList by rememberUpdatedState(onNavigateToList)
    val currentOnNavigateToCatAll by rememberUpdatedState(onNavigateToCatAll)
    val currentOnNavigateToCatRandom by rememberUpdatedState(onNavigateToCatRandom)
    val currentOnNavigateToCatAlphabetic by rememberUpdatedState(onNavigateToCatAlphabetic)

    state.uiEvent?.let { uiEvent->
        LaunchedEffect(uiEvent,lifecycle){
            snapshotFlow { uiEvent }
                .flowWithLifecycle(lifecycle)
                .collectLatest { event->
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
                    onEvent(SelectSavePointEvent.ClearUiEvent)
                }
        }
    }

    state.message?.let { message->
        LaunchedEffect(message,lifecycle){
            snapshotFlow { message }
                .flowWithLifecycle(lifecycle)
                .collectLatest {
                    ToastHelper.showMessage(it,context)
                    onEvent(SelectSavePointEvent.ClearMessage)
                }
        }
    }


    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.n_savepoints_c,state.title),
                onNavigateBack = onNavigateBack,
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {paddings->
        Column(
            modifier = Modifier.padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {

                if(state.showDropdownMenu){
                    item {
                        CustomDropdownMenu(
                            items = state.dropdownItems,
                            currentItem = state.selectedDropdownItem,
                            onItemChange = {onEvent(SelectSavePointEvent.SelectDropdownMenuItem(it))}
                        )
                    }
                }

                if(state.savePoints.isEmpty()){
                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillParentMaxSize()
                        ) {
                            Text(
                                stringResource(R.string.empty_savepoint),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }else{
                    items(
                        state.savePoints,
                        key = {item->item.id?:0},
                    ){item: SavePoint ->
                        SavePointItem(
                            savePoint = item,
                            onClick = {onEvent(SelectSavePointEvent.Select(item))},
                            isSelected = state.selectedSavePoint == item,
                            onTitleEditClick = {
                                onEvent(SelectSavePointEvent.ShowDialog(true,
                                    SelectSavePointDialogEvent.EditTitle(item)))
                            },
                            onDeleteClick = {
                                onEvent(SelectSavePointEvent.ShowDialog(
                                        true, SelectSavePointDialogEvent.AskDelete(item)))
                            }
                        )
                    }
                }
            }
            PrimaryButton(
                title = stringResource(R.string.load_and_go),
                onClick = {onEvent(SelectSavePointEvent.LoadSavePoint)},
                modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp)
                    .fillMaxWidth()
            )
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