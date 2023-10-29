package com.masterplus.trdictionary.features.word_detail.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.data.local.mapper.toWord
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.presentation.components.CustomDropdownBarMenu
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
import com.masterplus.trdictionary.features.word_detail.domain.constants.ShareItemEnum
import com.masterplus.trdictionary.features.word_detail.domain.model.AudioState
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailMeanings


@Composable
fun WordDetailItem(
    wordMeanings: WordDetailMeanings,
    modifier: Modifier = Modifier,
    onProverbIdiomWordsClicked: ()->Unit,
    onCompoundWordsClicked: ()->Unit,
    onVolumePressed: (()->Unit)? = null,
    onFavoritePressed: (()->Unit)? = null,
    onSelectListPressed: (()->Unit)? = null,
    audioState: AudioState? = null,
    onShareMenuItemClicked: (ShareItemEnum)->Unit,
    showButtons: Boolean = true,
){
    val word = wordMeanings.wordDetail
    val shape = MaterialTheme.shapes.medium

    val favoriteIconInfo = remember(word.inFavorite) {
        derivedStateOf {
            val isFavorite = word.inFavorite
            IconInfo(if(isFavorite)R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24,
                tintColor = if(isFavorite) Color.Red else null)
        }
    }

    val listIconInfo = remember(word.inAnyList) {
        derivedStateOf {
            val inList = word.inAnyList
            IconInfo(if(inList) R.drawable.ic_baseline_library_add_check_24 else R.drawable.ic_outline_library_add_24)
        }
    }


    Column(
        modifier = modifier
            .padding(all = 3.dp)
            .clip(shape)
            .border(2.dp, MaterialTheme.colorScheme.outline,shape)
            .background(MaterialTheme.colorScheme.primaryContainer,shape)
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
            CustomDropdownBarMenu(
                items = ShareItemEnum.values().toList(),
                onItemChange = {onShareMenuItemClicked(it)}
            )
        }

        Text(
            word.allWordContent,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 1.dp, horizontal = 1.dp)
                .padding(top = 5.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        if(showButtons){
            Row(
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .padding(bottom = 3.dp)
            ) {
                if(word.showTTS && audioState?.isVisible != false){
                    WordActionButton(
                        onClicked = onVolumePressed?:{},
                        iconInfo = IconInfo(R.drawable.ic_outline_volume_up_24),
                        enabled = audioState?.isPlaying != true
                    )
                }

                WordActionButton(
                    onClicked = onFavoritePressed?:{},
                    iconInfo = favoriteIconInfo.value
                )

                WordActionButton(
                    onClicked = onSelectListPressed?:{},
                    iconInfo = listIconInfo.value
                )
            }
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

        if(word.hasProverbIdioms || word.hasCompoundWords){
            Spacer(Modifier.padding(vertical = 7.dp))
        }

        if(word.hasProverbIdioms){
            PrimaryButton(
                title = stringResource(R.string.proverb_idiom_text_c),
                onClick = onProverbIdiomWordsClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if(word.hasCompoundWords){
            PrimaryButton(
                title = stringResource(R.string.compound_words_c),
                onClick = onCompoundWordsClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}












