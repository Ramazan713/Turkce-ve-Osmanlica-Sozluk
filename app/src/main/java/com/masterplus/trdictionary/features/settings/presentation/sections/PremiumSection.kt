package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.premium.PremiumProduct
import com.masterplus.trdictionary.core.extensions.launchUrl
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.components.SettingItem
import com.masterplus.trdictionary.features.settings.presentation.components.SettingSectionItem

@Composable
fun PremiumSection(
    premiumProduct: PremiumProduct?,
    isPremium: Boolean,
    onEvent: (SettingEvent)->Unit,
){
    val context = LocalContext.current
    SettingSectionItem(
        title = stringResource(R.string.premium_settings_c)
    ){
        AnimatedVisibility(!isPremium){
            SettingItem(
                title = stringResource(R.string.premium),
                onClick = {
                    onEvent(SettingEvent.ShowDialog(true,SettingDialogEvent.ShowPremiumDia(premiumProduct)))
                },
                resourceId = R.drawable.workspace_premium_black_24dp
            )
        }

        AnimatedVisibility(isPremium){
            SettingItem(
                title = stringResource(R.string.manage_subscrition_c),
                onClick = {
                    val url ="https://play.google.com/store/account/subscriptions?package=${context.packageName}"
                    context.launchUrl(url)
                },
                resourceId = R.drawable.baseline_manage_accounts_24
            )
        }
    }
}