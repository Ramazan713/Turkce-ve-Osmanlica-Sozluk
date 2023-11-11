package com.masterplus.trdictionary.features.home.domain.use_cases.widget

import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel
import com.masterplus.trdictionary.features.home.domain.use_cases.GetShortInfo
import javax.inject.Inject

class LoadShortInfoWidget @Inject constructor(
    private val sendShortInfoToWidget: SendShortInfoToWidget,
    private val getShortInfo: GetShortInfo,
) {

    suspend operator fun invoke(
        shortInfoEnum: ShortInfoEnum,
        refresh: Boolean = false,
        glanceIds: List<GlanceId>,
        glanceAppWidget: GlanceAppWidget,
        prefKey: Preferences.Key<String>
    ){
        val shortInfoLoadingModel = ShortInfoModel(isLoading = true, shortInfo = shortInfoEnum)
        sendShortInfoToWidget(shortInfoLoadingModel,glanceIds, glanceAppWidget,prefKey)

        val word = getShortInfo(shortInfoEnum,refresh)

        val shortInfoResultModel = ShortInfoModel(simpleWord = word,shortInfo = shortInfoEnum)
        sendShortInfoToWidget(shortInfoResultModel,glanceIds, glanceAppWidget,prefKey)
    }
}