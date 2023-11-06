package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.features.select_list.list_menu_dia.SelectMenuWithListBottom

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun WordListShowModal(
    event: WordListSharedModalEvent?,
    onEvent: (WordListSharedEvent)->Unit,
    savePointDestination: SavePointDestination,
    listIdControl: Int? = null,
    savePointSubTitle: String
){
    fun close(){
        onEvent(WordListSharedEvent.ShowModal(false))
    }

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    CustomModalBottomSheet(
        onDismissRequest = {close()},
        skipHalfExpanded = true
    ){
        when(event){
            is WordListSharedModalEvent.ShowSelectBottomMenu -> {
                SelectMenuWithListBottom(
                    title = stringResource(R.string.n_for_number_word,event.pos + 1, event.simpleWord.word.word),
                    listIdControl = listIdControl,
                    items = WordListBottomMenu.values().toList(),
                    onClose = { close() },
                    onClickItem = {menuItem->
                        when(menuItem){
                            WordListBottomMenu.EditSavePoint->{
                                close()
                                onEvent(WordListSharedEvent.ShowDialog(
                                    true,WordListSharedDialogEvent.EditSavePoint(
                                        savePointDestination = savePointDestination,
                                        shortTitle = savePointSubTitle,
                                        pos = event.pos
                                    )
                                ))
                            }

                            WordListBottomMenu.ShareWord->{
                                close()
                                onEvent(WordListSharedEvent.ShowDialog(true,
                                    WordListSharedDialogEvent.ShowShareDialog(
                                        event.savePointDestination,event.savePointType,event.pos,
                                        event.simpleWord.word
                                    )
                                ))
                            }
//
//                            WordListBottomMenu.CopyText->{
//                                event.simpleWord.word.word.copyClipboardText(context, clipboardManager)
//                            }

                        }
                    },
                    wordId = event.simpleWord.wordId
                )
            }
            null -> {}
        }
    }
}