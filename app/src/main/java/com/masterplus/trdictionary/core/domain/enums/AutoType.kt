package com.masterplus.trdictionary.core.domain.enums

sealed class AutoType(val typeId: Int,val label: String?){
    object Default: AutoType(1,null)

    object Auto: AutoType(2,"Auto")

    companion object{
        fun fromTypeId(typeId: Int): AutoType {
            return when(typeId){
                1-> Default
                2-> Auto
                else -> Default
            }
        }
    }

}
