package com.masterplus.trdictionary.core.domain.enums

import com.masterplus.trdictionary.core.domain.preferences.model.IEnumPrefValue
import com.masterplus.trdictionary.core.util.UiText

enum class SearchResultEnum (
    override val title: UiText,
    override val keyValue: Int,
    val resultNum: Int
): IEnumPrefValue, IMenuItemEnum {

    N13(
        title = UiText.Text("13"),
        keyValue = 1,
        resultNum = 13
    ) {
        override val iconInfo: IconInfo?
            get() = null
    },
    N37(
        title = UiText.Text("37"),
        keyValue = 2,
        resultNum = 37
    ) {
        override val iconInfo: IconInfo?
            get() = null
    },
    N61(
        title = UiText.Text("61"),
        keyValue = 3,
        resultNum = 61
    ) {
        override val iconInfo: IconInfo?
            get() = null
    },
    N101(
        title = UiText.Text("101"),
        keyValue = 4,
        resultNum = 101
    ) {
        override val iconInfo: IconInfo?
            get() = null
    };


    companion object{

        val defaultValue = N13

        fun fromKeyValue(keyValue: Int): SearchResultEnum {
            return when(keyValue){
                1-> N13
                2-> N37
                3-> N61
                4-> N101
                else -> defaultValue
            }
        }
    }

}