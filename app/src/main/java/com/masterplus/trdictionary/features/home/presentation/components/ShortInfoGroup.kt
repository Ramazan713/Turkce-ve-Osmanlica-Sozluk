package com.masterplus.trdictionary.features.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DefaultToolTip
import com.masterplus.trdictionary.features.home.presentation.ShortInfoModel


@Composable
fun ShortInfoGroup(
    title: String,
    infoModel: ShortInfoModel,
    onClicked: (Int) -> Unit,
    onRefreshClicked: () -> Unit,
    modifier: Modifier = Modifier,
    paddings: PaddingValues = PaddingValues(vertical = 7.dp, horizontal = 13.dp)
){

    val shape = RoundedCornerShape(
        topStart = 29.dp, bottomEnd = 29.dp,
        topEnd = 7.dp, bottomStart = 7.dp
    )

    OutlinedCard(
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
            .clip(shape)
            .clickable { infoModel.simpleWord?.let { onClicked(it.wordId) } },
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddings)
        ) {
            GetHeader(
                title = title,
                onRefreshClicked = onRefreshClicked
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 1.dp, bottom = 5.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
            )

            GetContent(infoModel)
        }
    }
}

@Composable
private fun GetHeader(
    title: String,
    onRefreshClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 50.dp)
        )

        DefaultToolTip(
            tooltip = stringResource(id = R.string.refresh),
            modifier = Modifier
                .align(Alignment.CenterEnd),
        ) {
            IconButton(
                onClick = onRefreshClicked,
                modifier = Modifier
                    .align(Alignment.CenterEnd),
            ){
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_refresh_24),
                    contentDescription = stringResource(id = R.string.refresh),
                    modifier = Modifier.size(27.dp)
                )
            }
        }

    }
}

@Composable
private fun GetContent(
    infoModel: ShortInfoModel,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {

        if(infoModel.isLoading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }else{
            infoModel.simpleWord?.let { simpleWord->
                Column {
                    Text(
                        simpleWord.wordContent,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        modifier = Modifier.padding(bottom = 7.dp)
                    )

                    simpleWord.meanings.forEach { meaning->
                        Text(
                            "${meaning.orderItem}. ${meaning.meaning}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(vertical = 3.dp)
                        )
                    }
                }
            }?: kotlin.run {
                Text(
                    stringResource(R.string.error_occur),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

