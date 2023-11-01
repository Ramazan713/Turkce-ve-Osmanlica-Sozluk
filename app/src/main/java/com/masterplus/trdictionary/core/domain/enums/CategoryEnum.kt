package com.masterplus.trdictionary.core.domain.enums

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.util.UiText

enum class CategoryEnum(val catId: Int, override val title: UiText): IMenuItemEnum {

    AllDict(1, UiText.Resource(R.string.all_dict_c)) {
        override val iconInfo: IconInfo?
            get() = null
    },
    TrDict(2, UiText.Resource(R.string.tr_dict_c)) {
        override val iconInfo: IconInfo?
            get() = null
    },
    OsmDict(3, UiText.Resource(R.string.osm_dict_c)) {
        override val iconInfo: IconInfo?
            get() = null
    },
    ProverbDict(4, UiText.Resource(R.string.proverbs)) {
        override val iconInfo: IconInfo?
            get() = null
    },
    IdiomDict(5, UiText.Resource(R.string.idioms)) {
        override val iconInfo: IconInfo?
            get() = null
    };

    companion object{
        fun fromCatId(catId: Int): CategoryEnum {
            return when(catId){
                1 -> AllDict
                2 -> TrDict
                3 -> OsmDict
                4 -> ProverbDict
                5 -> IdiomDict
                else -> AllDict
            }
        }
    }

}