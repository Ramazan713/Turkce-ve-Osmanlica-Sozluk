package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.*
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.model.*
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.presentation.extensions.addPrefixZeros
import com.masterplus.trdictionary.core.presentation.extensions.share
import com.masterplus.trdictionary.core.presentation.extensions.shareText
import com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia.SelectListBottomContent
import com.masterplus.trdictionary.features.word_detail.domain.constants.ShareItemEnum
import com.masterplus.trdictionary.features.word_detail.presentation.components.ShowSimpleWordsDialog
import com.masterplus.trdictionary.features.word_detail.presentation.components.WordDetailItem

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleWordDetailPage(
    onNavigateBack: ()->Unit,
    onRelatedWordClicked: (Int)->Unit,
    state: SingleWordDetailState,
    onEvent: (SingleWordDetailEvent) -> Unit
){

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "",
                onNavigateBack = onNavigateBack,
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ){padding->
        LazyColumn(
            modifier = Modifier.padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ){
            if(state.isLoading){
                item {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                }
            }else{
                itemsIndexed(
                    state.wordModels,
                    key = {_,it->it.wordId}
                ){index,wordModel->
                    WordDetailItem(
                        wordModel = wordModel,
                        showButtons = index == 0,
                        modifier = Modifier.padding(vertical = 7.dp),
                        onFavoritePressed = {
                            onEvent(SingleWordDetailEvent.AddFavorite(wordModel.wordId))
                        },
                        onSelectListPressed = {
                            onEvent(SingleWordDetailEvent.ShowModal(true,
                                    SingleWordDetailModalEvent.ShowSelectList(wordModel.wordId)))
                        },
                        onVolumePressed = {
                            onEvent(SingleWordDetailEvent.ListenWord(wordModel.wordListInfo.wordMeaning.word))
                        },
                        onProverbIdiomWordsClicked = {
                            onEvent(SingleWordDetailEvent.ShowDialog(true,
                                    SingleWordDetailDialogEvent.ShowProverbIdiomsWords(wordModel.proverbIdioms)
                                )
                            )
                        },
                        onCompoundWordsClicked = {
                            onEvent(SingleWordDetailEvent.ShowDialog(true,
                                SingleWordDetailDialogEvent.ShowCompoundWords(wordModel.compoundWords)
                            ))
                        },
                        onShareMenuItemClicked = {sharedItem->
                            when(sharedItem){
                                ShareItemEnum.ShareWord -> {
                                    wordModel.word.word.shareText(context)
                                }
                                ShareItemEnum.ShareWordMeaning -> {
                                    wordModel.wordListInfo.wordMeaning.share(context)
                                }
                                ShareItemEnum.ShareLink -> {
                                    val wordIdStr = wordModel.wordId.addPrefixZeros(K.DeepLink.numberZerosLength)
                                    "${K.DeepLink.singleWordBaseUrl}/${wordIdStr}".shareText(context)
                                }
                            }
                        },
                        audioState = state.audioState
                    )
                }
            }
        }
    }

    if(state.showDialog){
        ShowDialog(
            state = state,
            onEvent = onEvent,
            onNavigateToDetailWord = onRelatedWordClicked
        )
    }

    if(state.showModal){
        ShowModal(
            state = state,
            onEvent = onEvent
        )
    }

}



@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
private fun ShowModal(
    state: SingleWordDetailState,
    onEvent: (SingleWordDetailEvent)->Unit,
){
    fun close(){
        onEvent(SingleWordDetailEvent.ShowModal(false))
    }

    CustomModalBottomSheet(
        onDismissRequest = {close()},
        skipHalfExpanded = false
    ){
        when(val event = state.modalEvent){
            is SingleWordDetailModalEvent.ShowSelectList -> {
                SelectListBottomContent(
                    wordId = event.wordId,
                    onClosed = {close()}
                )
            }
            null -> {}
        }
    }
}



@ExperimentalFoundationApi
@Composable
private fun ShowDialog(
    state: SingleWordDetailState,
    onEvent: (SingleWordDetailEvent)->Unit,
    onNavigateToDetailWord: (Int)->Unit,
){
    fun close(){
        onEvent(SingleWordDetailEvent.ShowDialog(false))
    }

    when(val event = state.dialogEvent){
        is SingleWordDetailDialogEvent.ShowCompoundWords -> {
            ShowSimpleWordsDialog(
                title = stringResource(R.string.compound_words_c),
                words = event.words,
                onClosed = {close()},
                onClickedWord = {onNavigateToDetailWord(it.wordId)}
            )
        }
        is SingleWordDetailDialogEvent.ShowProverbIdiomsWords -> {
            ShowSimpleWordsDialog(
                title = stringResource(R.string.proverb_idiom_text_c),
                words = event.words,
                onClosed = {close()},
                onClickedWord = {onNavigateToDetailWord(it.wordId)}
            )
        }
        null -> {}
    }
}