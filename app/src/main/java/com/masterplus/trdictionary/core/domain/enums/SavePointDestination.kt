package com.masterplus.trdictionary.core.domain.enums

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText

sealed class SavePointDestination(val destinationId: Int, val description: UiText, open val type: SavePointType) {

    data class CategoryAll(override val type: SavePointType):
        SavePointDestination(destinationId, UiText.Resource(R.string.all_subCat),type) {

        override fun toSaveKey(): String {
            return "1-${type.typeId}-${SubCategoryEnum.All.subCatId}"
        }

        companion object{
            val destinationId get() = 1
            fun fromSaveKey(saveKey: String): CategoryAll? {
                val arr = saveKey.split("-")
                if(arr.size!=3)return null
                val typeId = arr[1].toIntOrNull() ?: return null
                return CategoryAll(SavePointType.fromTypeId(typeId))
            }
        }
    }

    data class CategoryRandom(override val type: SavePointType): SavePointDestination(destinationId, UiText.Resource(R.string.random),type) {
        override fun toSaveKey(): String {
            return "1-${type.typeId}-${SubCategoryEnum.Random.subCatId}"
        }
        companion object{
            val destinationId get() = 2
            fun fromSaveKey(saveKey: String): CategoryRandom? {
                val arr = saveKey.split("-")
                if(arr.size!=3)return null
                val typeId = arr[1].toIntOrNull() ?: return null
                return CategoryRandom(SavePointType.fromTypeId(typeId))
            }
        }
    }

    data class CategoryAlphabetic(override val type: SavePointType, val c: String):
            SavePointDestination(
                destinationId, UiText.Resource(R.string.alphabetic),type
        ) {
        override fun toSaveKey(): String {
            return "1-${type.typeId}-${SubCategoryEnum.Alphabetic.subCatId}-${c}"
        }
        companion object{
            val destinationId get() = 3
            fun fromSaveKey(saveKey: String): CategoryAlphabetic? {
                val arr = saveKey.split("-")
                if(arr.size!=4)return null
                val typeId = arr[1].toIntOrNull() ?: return null
                val c = arr[3]
                return CategoryAlphabetic(SavePointType.fromTypeId(typeId),c)
            }
        }
    }

    data class List(val listId: Int): SavePointDestination(
        destinationId, UiText.Resource(R.string.list),
        SavePointType.List
    ) {
        override fun toSaveKey(): String {
            return "1-${type.typeId}-$listId"
        }
        companion object{
            val destinationId get() = 4
            fun fromSaveKey(saveKey: String): List? {
                val arr = saveKey.split("-")
                if(arr.size!=3)return null
                val listId = arr[2].toIntOrNull() ?: return null
                return List(listId)
            }
        }
    }

    abstract fun toSaveKey(): String


    fun toSubCategory(): SubCategoryEnum?{
        return when(this){
            is CategoryAll -> SubCategoryEnum.All
            is CategoryAlphabetic -> SubCategoryEnum.Alphabetic
            is CategoryRandom -> SubCategoryEnum.Random
            is List -> null
        }
    }

    companion object{
        fun fromDestinationId(destinationId: Int, saveKey: String): SavePointDestination?{
            return when(destinationId){
                1-> CategoryAll.fromSaveKey(saveKey)
                2-> CategoryRandom.fromSaveKey(saveKey)
                3-> CategoryAlphabetic.fromSaveKey(saveKey)
                4-> List.fromSaveKey(saveKey)
                else-> null
            }
        }

        val categoryDestinationIds get() = listOf(
            CategoryAll.destinationId,
            CategoryRandom.destinationId,
            CategoryAlphabetic.destinationId
        )
    }

}