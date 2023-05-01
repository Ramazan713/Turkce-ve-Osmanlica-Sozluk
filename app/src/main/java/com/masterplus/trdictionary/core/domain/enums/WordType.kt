package com.masterplus.trdictionary.core.domain.enums

import androidx.annotation.DrawableRes
import com.masterplus.trdictionary.R

enum class WordType(val dictId: Int,@DrawableRes val resourceId: Int){
    TR(1, R.drawable.tr_icon),
    OSM(2,R.drawable.osm_icon);

    companion object{
        fun fromDicId(dictId: Int): WordType{
            return when(dictId){
                1-> TR
                else -> OSM
            }
        }
    }
}
