package com.masterplus.trdictionary.features.home.domain.use_cases.widget

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.presentation.widget.OneIdiomWidget
import com.masterplus.trdictionary.features.home.presentation.widget.OneProverbWidget
import com.masterplus.trdictionary.features.home.presentation.widget.OneWordWidget
import javax.inject.Inject

class RefreshShortInfoWidget @Inject constructor(
    private val loadShortInfoWidget: LoadShortInfoWidget,
    private val application: Application
) {

    suspend operator fun invoke(
        shortInfoEnum: ShortInfoEnum,
        refresh: Boolean = true
    ){
        var glanceIds: List<GlanceId> = emptyList()
        var glanceAppWidget: GlanceAppWidget? = null
        val prefKey: Preferences.Key<String>

        when(shortInfoEnum){
            ShortInfoEnum.Proverb -> {
                glanceAppWidget = OneProverbWidget()
                prefKey = OneProverbWidget.wordDataKey
                glanceIds = GlanceAppWidgetManager(application).getGlanceIds(glanceAppWidget::class.java)
            }
            ShortInfoEnum.Idiom -> {
                glanceAppWidget = OneIdiomWidget()
                prefKey = OneIdiomWidget.wordDataKey
                glanceIds = GlanceAppWidgetManager(application).getGlanceIds(glanceAppWidget::class.java)
            }
            ShortInfoEnum.Word -> {
                glanceAppWidget = OneWordWidget()
                prefKey = OneWordWidget.wordDataKey
                glanceIds = GlanceAppWidgetManager(application).getGlanceIds(glanceAppWidget::class.java)
            }
        }

        if(glanceIds.isNotEmpty()){
            loadShortInfoWidget(
                shortInfoEnum = shortInfoEnum,
                refresh = refresh,
                glanceIds = glanceIds,
                glanceAppWidget = glanceAppWidget,
                prefKey = prefKey
            )
        }
    }
}