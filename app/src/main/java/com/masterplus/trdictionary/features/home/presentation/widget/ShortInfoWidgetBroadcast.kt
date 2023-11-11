package com.masterplus.trdictionary.features.home.presentation.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.ShortInfoWidgetUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShortInfoWidgetBroadcast: BroadcastReceiver() {

    @Inject lateinit var shortInfoWidgetUseCases: ShortInfoWidgetUseCases

    @Inject lateinit var scope: CoroutineScope

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null || intent == null) return

        val infoKey = intent.getStringExtra(shortInfoEnumKey) ?: return
        val info = ShortInfoEnum.from(infoKey)

        scope.launch {
            shortInfoWidgetUseCases.refreshInfoModel(info,true)
        }
    }


    companion object {
        const val shortInfoEnumKey = "shortInfoEnumKey"
    }
}