package com.masterplus.trdictionary.features.home.domain.use_cases.widget

data class ShortInfoWidgetUseCases(
    val loadInfoModel: LoadShortInfoWidget,
    val sendInfoModel: SendShortInfoToWidget,
    val refreshInfoModel: RefreshShortInfoWidget
)
