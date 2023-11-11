package com.masterplus.trdictionary.features.home.presentation.widget.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel


@Composable
fun ShortInfoGroupWidgetItem(
    title: String,
    infoModel: ShortInfoModel,
    onClicked: Action,
    onRefreshClicked: Action,
    modifier: GlanceModifier = GlanceModifier,
){
    Column(
        modifier = modifier
            .clickable(onClicked)
            .padding(vertical = 7.dp, horizontal = 13.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        GetHeader(
            title = title,
            onRefreshClicked = onRefreshClicked,
            modifier = GlanceModifier.padding(bottom = 4.dp)
        )
        Spacer(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)

        )

        GetContent(
            infoModel,
            modifier = GlanceModifier.defaultWeight()
        )
    }
}

@Composable
private fun GetHeader(
    modifier: GlanceModifier = GlanceModifier,
    title: String,
    onRefreshClicked: Action
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Horizontal.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            modifier = GlanceModifier
                .padding(horizontal = 4.dp)
        )

        Box(
            modifier = GlanceModifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                text = LocalContext.current.getString(R.string.refresh),
                onClick = onRefreshClicked,
                colors = ButtonDefaults.buttonColors(
                    contentColor = GlanceTheme.colors.onSecondaryContainer,
                    backgroundColor = GlanceTheme.colors.secondaryContainer
                )
            )
        }
    }
}

@Composable
private fun GetContent(
    infoModel: ShortInfoModel,
    modifier: GlanceModifier = GlanceModifier,
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        contentAlignment = Alignment.TopStart
    ) {

        if(infoModel.isLoading){
            Box(modifier = GlanceModifier.fillMaxSize(),contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }else{
            infoModel.simpleWord?.let { simpleWord->
                LazyColumn {
                    item {
                        Text(
                            simpleWord.wordContent,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            modifier = GlanceModifier.padding(bottom = 7.dp)
                        )
                    }

                    items(simpleWord.meanings){ meaning->
                        Text(
                            "${meaning.orderItem}. ${meaning.meaning}",
                            modifier = GlanceModifier
                                .padding(vertical = 3.dp)
                        )
                    }
                }
            }?: run {
                Box(modifier = GlanceModifier.fillMaxSize(),contentAlignment = Alignment.Center){
                    Text(
                        "Bir hata oluştu",
                    )
                }
            }
        }
    }
}



