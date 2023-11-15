package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState
import com.masterplus.trdictionary.features.settings.presentation.components.SettingItem
import com.masterplus.trdictionary.features.settings.presentation.components.SettingSectionItem
import com.masterplus.trdictionary.features.settings.presentation.components.SwitchItem


@Composable
fun GeneralSettingSection(
    state: SettingState,
    onEvent: (SettingEvent)->Unit,
){
    SettingSectionItem(
        title = stringResource(R.string.general_setting)
    ){
        SettingItem(
            title = stringResource(R.string.theme_mode),
            subTitle = state.themeModel.themeEnum.title.asString(),
            onClick = {
                onEvent(
                    SettingEvent.ShowDialog(SettingDialogEvent.SelectThemeEnum))
            },
            resourceId = R.drawable.ic_baseline_palette_24
        )

        SettingItem(
            title = stringResource(R.string.search_result_counts),
            subTitle = state.searchResult.toString(),
            onClick = {
                onEvent(
                    SettingEvent.ShowDialog(SettingDialogEvent.SelectSearchResult(9,99))
                )
            },
            resourceId = R.drawable.baseline_search_24
        )

        if(state.themeModel.enabledDynamicColor){
            SwitchItem(
                title = stringResource(R.string.use_dynamic_colors),
                value = state.themeModel.useDynamicColor,
                onValueChange = {onEvent(SettingEvent.SetDynamicTheme(it))}
            )
        }
    }
}
