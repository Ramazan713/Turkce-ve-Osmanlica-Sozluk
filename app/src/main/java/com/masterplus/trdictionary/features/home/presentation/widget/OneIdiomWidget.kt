package com.masterplus.trdictionary.features.home.presentation.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.fillMaxSize
import com.google.gson.reflect.TypeToken
import com.masterplus.trdictionary.MainActivity
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.data.GsonParser
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.ShortInfoWidgetUseCases
import com.masterplus.trdictionary.features.home.presentation.widget.components.ShortInfoGroupWidgetItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


private val wordInfo = ShortInfoEnum.Idiom


class OneIdiomWidget: GlanceAppWidget() {

    companion object{
        val wordDataKey = stringPreferencesKey("simpleOneIdiom")
    }

    @OptIn(
        ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
        ExperimentalFoundationApi::class
    )
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val wordData = currentState(key = wordDataKey)

            val infoModel: ShortInfoModel = wordData?.let { data->
                GsonParser().fromJson<ShortInfoModel>(data, TypeToken.get(ShortInfoModel::class.java).type)
            } ?: ShortInfoModel(isLoading = false)

            ShortInfoGroupWidgetItem(
                modifier = GlanceModifier.fillMaxSize(),
                title = context.getString(R.string.one_idiom_c),
                infoModel = infoModel,
                onClicked = actionStartActivity(MainActivity::class.java),
                onRefreshClicked = actionSendBroadcast(
                    Intent().also { intent->
                        intent.setClass(context,ShortInfoWidgetBroadcast::class.java)
                        intent.putExtra(ShortInfoWidgetBroadcast.shortInfoEnumKey, wordInfo.saveKey)
                    }
                )
            )
        }
    }
}




@AndroidEntryPoint
class OneIdiomWidgetReceiver : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget get() = OneIdiomWidget()

    @Inject
    lateinit var shortInfoWidgetUseCases: ShortInfoWidgetUseCases

    @Inject
    lateinit var scope: CoroutineScope

    private fun loadData(context: Context){
        scope.launch {
            GlanceAppWidgetManager(context).getGlanceIds(glanceAppWidget::class.java).let { glanceIds->
                shortInfoWidgetUseCases.loadInfoModel(
                    shortInfoEnum = wordInfo,
                    refresh = false,
                    glanceIds = glanceIds,
                    glanceAppWidget = glanceAppWidget,
                    prefKey = OneIdiomWidget.wordDataKey
                )
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        loadData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        loadData(context)
    }
}