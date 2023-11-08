package com.masterplus.trdictionary.features.search.presentation.contents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.contents.WordsDetailAdaptiveContent
import com.masterplus.trdictionary.core.util.SampleDatas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDetailPageContent(
    onNavigateBack: (()->Unit)?,
    audioState: AudioState,
    onEvent: (WordsListDetailEvent) -> Unit,
    wordWithSimilar: WordWithSimilar?,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = wordWithSimilar?.wordDetail?.word ?: "",
                onNavigateBack = onNavigateBack,
                scrollBehavior = scrollBehavior,
            )
        },
    ){padding->
        Box(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxWidth()
        ) {
            if(wordWithSimilar == null){
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(id = R.string.no_result),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }else{
                WordsDetailAdaptiveContent(
                    modifier = Modifier.fillMaxWidth(),
                    wordWithSimilar = wordWithSimilar,
                    windowWidthSizeClass = windowWidthSizeClass,
                    onEvent = onEvent,
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp),
                    audioState = audioState,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchDetailContentPreview() {
    SearchDetailPageContent(
        onEvent = {},
        onNavigateBack = {},
        audioState = AudioState(),
        windowWidthSizeClass = WindowWidthSizeClass.Compact,
        wordWithSimilar = SampleDatas.generateWordWithSimilar()
    )
}