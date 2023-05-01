package com.masterplus.trdictionary.core.domain.enums

enum class SavePointType(val typeId: Int){

    AllDict(1),
    TrDict(2),
    OsmDict(3) ,
    ProverbDict(4),
    IdiomDict(5),
    List(6);

    fun toCategoryEnum(): CategoryEnum? {
        return when(this){
            AllDict -> CategoryEnum.AllDict
            TrDict -> CategoryEnum.TrDict
            OsmDict -> CategoryEnum.OsmDict
            ProverbDict -> CategoryEnum.ProverbDict
            IdiomDict -> CategoryEnum.IdiomDict
            List -> null
        }
    }

    companion object{
        fun fromTypeId(typeId: Int): SavePointType {
            return when(typeId){
                1 -> AllDict
                2 -> TrDict
                3 -> OsmDict
                4 -> ProverbDict
                5 -> IdiomDict
                6 -> List
                else -> AllDict
            }
        }

        fun fromCategory(categoryEnum: CategoryEnum): SavePointType {
            return when(categoryEnum){
                CategoryEnum.AllDict -> AllDict
                CategoryEnum.TrDict -> TrDict
                CategoryEnum.OsmDict -> OsmDict
                CategoryEnum.ProverbDict -> ProverbDict
                CategoryEnum.IdiomDict -> IdiomDict
            }
        }

    }

}
