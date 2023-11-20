package com.masterplus.trdictionary.core.domain.model.premium

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText


sealed class BillingPeriod(open val period: Int, val title: UiText, val shortName: UiText){

    data class Daily(override val period: Int): BillingPeriod(
        period,
        UiText.Resource(R.string.daily_subs_c),
        UiText.Resource(R.string.day_l)
    )

    data class Weekly(override val period: Int): BillingPeriod(
        period,
        UiText.Resource(R.string.weekly_subs_c),
        UiText.Resource(R.string.week_l)
    )

    data class Monthly(override val period: Int): BillingPeriod(
        period,
        UiText.Resource(R.string.monthly_subs_c),
        UiText.Resource(R.string.month_l)
    )

    data class Annually(override val period: Int): BillingPeriod(
        period,
        UiText.Resource(R.string.annually_subs_c),
        UiText.Resource(R.string.year_l)
    )

    companion object{
        fun fromBillingPeriod(billingPeriod: String): BillingPeriod?{
            val regex = Regex("P(\\d+)(.+)")
            val groups = regex.find(billingPeriod)?.groups ?: return null
            val duration = groups[1]?.value?.toIntOrNull() ?: return null
            val periodType = groups[2]?.value ?: return null

            return when(periodType){
                "D" -> Daily(duration)
                "W" -> Weekly(duration)
                "M" -> Monthly(duration)
                "Y" -> Annually(duration)
                else -> null
            }
        }
    }
}