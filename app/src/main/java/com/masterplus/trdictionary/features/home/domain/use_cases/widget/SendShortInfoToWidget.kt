package com.masterplus.trdictionary.features.home.domain.use_cases.widget

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.masterplus.trdictionary.core.domain.JsonParser
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel
import javax.inject.Inject

class SendShortInfoToWidget @Inject constructor(
    private val application: Application,
    private val jsonParser: JsonParser
){

    suspend operator fun invoke(
        infoModel: ShortInfoModel,
        glanceIds: List<GlanceId>,
        glanceAppWidget: GlanceAppWidget,
        prefKey: Preferences.Key<String>
    ){
        val jsonData = jsonParser.toJson(infoModel)
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(application,glanceId){pref->
                pref[prefKey] = jsonData
            }
        }
        glanceAppWidget.updateAll(application)
    }

}