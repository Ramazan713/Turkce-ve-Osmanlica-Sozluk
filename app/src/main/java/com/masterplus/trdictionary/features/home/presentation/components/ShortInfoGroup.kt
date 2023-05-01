package com.masterplus.trdictionary.features.home.presentation.components

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
import com.masterplus.trdictionary.features.home.presentation.ShortInfoModel


@Composable
fun ShortInfoGroup(
    title: String,
    infoModel: ShortInfoModel,
    onClicked: (Int)->Unit,
    onRefreshClicked: ()->Unit,
    modifier: Modifier = Modifier
){

    val shape = RoundedCornerShape(
        topStart = 29.dp, bottomEnd = 29.dp,
        topEnd = 7.dp, bottomStart = 7.dp
    )
    Column(
        modifier = modifier
            .padding(1.dp)
            .clip(shape)
            .border(2.dp, MaterialTheme.colorScheme.outline,shape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { infoModel.simpleWord?.let { onClicked(it.wordId) } }
            .padding(vertical = 7.dp, horizontal = 13.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 50.dp)
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = onRefreshClicked,
            ){
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_refresh_24),
                    contentDescription = null,
                    modifier = Modifier.size(27.dp)
                )
            }
        }
        Divider(
            modifier = Modifier
                .padding(top = 1.dp, bottom = 5.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
        )

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

}