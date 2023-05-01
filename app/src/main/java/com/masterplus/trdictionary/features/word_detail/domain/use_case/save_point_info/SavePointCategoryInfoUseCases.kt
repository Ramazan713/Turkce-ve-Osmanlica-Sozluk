package com.masterplus.trdictionary.features.word_detail.domain.use_case.save_point_info

import android.app.Application
import androidx.compose.ui.text.buildAnnotatedString
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import javax.inject.Inject

class SavePointCategoryInfoUseCases @Inject constructor(
    private val context: Application
){

    operator fun invoke(catId: Int, subCatId: Int, c: String?): SavePointCategoryInfo{
        val catEnum = CategoryEnum.fromCatId(catId)
        val subCatEnum = SubCategoryEnum.fromSubCatId(subCatId)

        val savePointTitle = getTitle(catEnum, subCatEnum, c)

        val savePointDestination = getSavePointDestination(catEnum, subCatEnum, c)

        return SavePointCategoryInfo(
            catEnum = catEnum,
            subCategoryEnum = subCatEnum,
            savePointDestination = savePointDestination,
            savePointTitle = savePointTitle
        )
    }

    private fun getSavePointDestination(
        catEnum: CategoryEnum, subCatEnum: SubCategoryEnum, c: String?
    ): SavePointDestination{
        val type = SavePointType.fromCategory(catEnum)
        return when(subCatEnum){
            SubCategoryEnum.All -> {
                SavePointDestination.CategoryAll(type)
            }
            SubCategoryEnum.Random -> {
                SavePointDestination.CategoryRandom(type)
            }
            SubCategoryEnum.Alphabetic -> {
                SavePointDestination.CategoryAlphabetic(type,c?:"a")
            }
        }
    }

    private fun getTitle(catEnum: CategoryEnum, subCatEnum: SubCategoryEnum, c: String?): String{
        return buildAnnotatedString {
            append(catEnum.title.asString(context))
            append(" - ${subCatEnum.description.asString(context)}")
            if(c!=null){
                append(" - $c")
            }
        }.text
    }


    data class SavePointCategoryInfo(
        val catEnum: CategoryEnum,
        val subCategoryEnum: SubCategoryEnum,
        val savePointDestination: SavePointDestination,
        val savePointTitle: String
    )

}