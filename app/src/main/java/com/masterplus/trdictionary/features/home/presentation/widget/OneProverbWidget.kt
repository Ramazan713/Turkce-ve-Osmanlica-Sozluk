package com.masterplus.trdictionary.features.home.presentation.widget
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.action
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import com.masterplus.trdictionary.MainActivity
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoPreferenceData
import com.masterplus.trdictionary.features.home.presentation.widget.components.ShortInfoGroupWidgetItem
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch


private val wordInfo = ShortInfoEnum.Proverb

class OneProverbWidget: GlanceAppWidget() {

    @OptIn(
        ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
        ExperimentalFoundationApi::class
    )
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val entryPoint = EntryPointAccessors.fromApplication(appContext,ShortInfoEntryPoint::class.java)

        val manager = entryPoint.getManager()
        val preferences = entryPoint.getPreferences()

        provideContent {
            val preferenceState by preferences.dataFlow.collectAsState(ShortInfoPreferenceData())

            var infoModel by remember {
                mutableStateOf(ShortInfoModel(isLoading = true, shortInfo = wordInfo))
            }
            val scope = rememberCoroutineScope()

            LaunchedEffect(preferenceState){
                preferenceState.getRandomNumber(wordInfo).let { randomNumber->
                    infoModel = ShortInfoModel(infoModel.simpleWord,true, wordInfo)
                    val word = manager.getWord(wordInfo,randomNumber)
                    infoModel = ShortInfoModel(word,false, wordInfo)
                }
            }

            ShortInfoGroupWidgetItem(
                modifier = GlanceModifier.fillMaxSize(),
                title = context.getString(R.string.one_proverb_c),
                infoModel = infoModel,
                onClicked = actionStartActivity(MainActivity::class.java),
                onRefreshClicked = action {
                    scope.launch {
                        manager.refreshWord(wordInfo)
                    }
                }
            )
        }
    }
}

class OneProverbWidgetReceiver : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget get() = OneProverbWidget()
}