package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DefaultIconToggleButton
import com.masterplus.trdictionary.core.presentation.selections.AdaptiveSelectSheetMenu
import com.masterplus.trdictionary.core.presentation.selections.rememberAdaptiveSelectMenuState
import com.masterplus.trdictionary.core.presentation.utils.SampleDatas
import com.masterplus.trdictionary.core.shared_features.share.domain.enums.ShareItemEnum
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetail
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings


@Composable
fun WordDetailItem(
    wordMeanings: WordDetailMeanings,
    modifier: Modifier = Modifier,
    onProverbIdiomWordsClicked: (WordDetailMeanings) -> Unit,
    onCompoundWordsClicked: (WordDetailMeanings) -> Unit,
    onVolumePressed: (()->Unit)? = null,
    onFavoritePressed: (()->Unit)? = null,
    onSelectListPressed: (()->Unit)? = null,
    audioState: AudioState? = null,
    onShareMenuItemClicked: (ShareItemEnum)->Unit,
    showButtons: Boolean = true,
    windowWidthSizeClass: WindowWidthSizeClass
){
    val word = wordMeanings.wordDetail
    val shape = MaterialTheme.shapes.medium

    val selectSheetState = rememberAdaptiveSelectMenuState<ShareItemEnum>(
        windowWidthSizeClass = windowWidthSizeClass,
        onItemChange = { menuItem, _->
            onShareMenuItemClicked(menuItem)
        }
    )

    Card(
        modifier
            .clip(shape),
        shape = shape,
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 7.dp, horizontal = 9.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(word.dictType.resourceId),
                    modifier = Modifier.size(40.dp),
                    contentDescription = null
                )

                AdaptiveSelectSheetMenu(
                    state = selectSheetState,
                    items = ShareItemEnum.values().toList(),
                )
            }

            Text(
                word.allWordContent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp, horizontal = 1.dp)
                    .padding(top = 5.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            if(showButtons){
                GetButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .padding(bottom = 3.dp),
                    word = word,
                    onVolumePressed = onVolumePressed,
                    onFavoritePressed = onFavoritePressed,
                    onSelectListPressed = onSelectListPressed,
                    audioState = audioState
                )
            }

            wordMeanings.meanings.let { meanings->
                meanings.forEach { meaning->
                    MeaningItem(
                        meaningExamples = meaning,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    )
                }
            }

            selectSheetState.showSheet()

            if(word.hasProverbIdioms || word.hasCompoundWords){
                Spacer(Modifier.padding(vertical = 7.dp))
            }

            if(word.hasProverbIdioms){
                OutlinedButton(
                    onClick = { onProverbIdiomWordsClicked(wordMeanings) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.proverb_idiom_text_c))
                }
            }

            if(word.hasCompoundWords){
                OutlinedButton(
                    onClick = { onCompoundWordsClicked(wordMeanings) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.compound_words_c))
                }
            }
        }
    }
}


@Composable
private fun GetButtons(
    modifier: Modifier = Modifier,
    word: WordDetail,
    onVolumePressed: (()->Unit)?,
    onFavoritePressed: (()->Unit)?,
    onSelectListPressed: (()->Unit)?,
    audioState: AudioState?,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .padding(bottom = 3.dp)
    ) {
        AnimatedVisibility(visible = word.showTTS && audioState?.isVisible != false) {
            DefaultIconToggleButton(
                value = true,
                onValueChange = { onVolumePressed?.invoke() },
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                enabled = audioState?.isPlaying != true
            )
        }
        DefaultIconToggleButton(
            value = word.inFavorite,
            onValueChange = { onFavoritePressed?.invoke() },
            imageVector = Icons.Default.FavoriteBorder,
            selectedImageVector = Icons.Default.Favorite,
            selectedIconTint = MaterialTheme.colorScheme.error
        )

        DefaultIconToggleButton(
            value = word.inAnyList,
            onValueChange = { onSelectListPressed?.invoke() },
            imageVector = Icons.Outlined.LibraryAdd,
            selectedImageVector = Icons.Filled.LibraryAddCheck,
        )
    }
}




@Preview(showBackground = true)
@Composable
fun WordDetailItemPreview() {
    WordDetailItem(
        wordMeanings = SampleDatas.generateWordDetailMeanings(
            wordDetail = SampleDatas.generateWordDetail(hasCompoundWords = true)
        ),
        onProverbIdiomWordsClicked = {},
        onCompoundWordsClicked = {},
        onFavoritePressed = {},
        onSelectListPressed = {},
        onShareMenuItemClicked = {},
        onVolumePressed = {},
        windowWidthSizeClass = WindowWidthSizeClass.Compact
    )
}







