package com.masterplus.trdictionary.features.word_detail.domain.use_case.share

import androidx.compose.ui.text.buildAnnotatedString
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.domain.model.Word
import com.masterplus.trdictionary.core.extensions.addPrefixZeros
import com.masterplus.trdictionary.features.word_detail.domain.constants.ShareItemEnum
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordDetailRepo
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordListRepo
import javax.inject.Inject

class ShareWordList @Inject constructor(
    private val wordDetailRepo: WordDetailRepo,
    private val wordListRepo: WordListRepo
) {


    suspend operator fun invoke(
        savePointDestination: SavePointDestination,
        savePointType: SavePointType?,
        pos: Int?,
        word: Word,
        shareItemEnum: ShareItemEnum,
        urlBase: String
    ): ShareWordUseCases.ShareWordResult {
        return invoke(
            savePointDestination,
            savePointType,
            pos = pos,
            wordId = word.id,
            wordRandomOrder = word.randomOrder,
            shareItemEnum = shareItemEnum,
            urlBase = urlBase
        )
    }

    suspend operator fun invoke(
        savePointDestination: SavePointDestination,
        savePointType: SavePointType?,
        pos: Int?,
        wordId: Int,
        wordRandomOrder: Int,
        shareItemEnum: ShareItemEnum,
        urlBase: String
    ): ShareWordUseCases.ShareWordResult{

        return when(shareItemEnum){
            ShareItemEnum.ShareWord -> {
                val wordMeanings = wordDetailRepo.getWordMeaningsWithWordId(wordId)
                ShareWordUseCases.ShareWordResult.ShareWord(wordMeanings)
            }
            ShareItemEnum.ShareWordMeaning -> {
                val wordMeanings = wordDetailRepo.getWordMeaningsWithWordId(wordId)
                ShareWordUseCases.ShareWordResult.ShareWordWithMeanings(wordMeanings)
            }
            ShareItemEnum.ShareLink -> {

                val subCatEnum = savePointDestination.toSubCategory() ?: SubCategoryEnum.Random
                val catEnum = savePointType?.toCategoryEnum() ?: CategoryEnum.AllDict

                val c = (savePointDestination as? SavePointDestination.CategoryAlphabetic)?.c

                val destinationPos: Int = if(
                    savePointType?.typeId == SavePointType.List.typeId ||
                    savePointType == null
                ){
                    wordListRepo.getAllDictPosByRandomOrder(wordRandomOrder)
                }else{
                    pos ?: 0
                }
                val link = makeWordLink(catEnum,subCatEnum,c,destinationPos,urlBase)
                ShareWordUseCases.ShareWordResult.ShareLink(link)
            }
        }
    }




    private fun makeWordLink(
        catEnum: CategoryEnum,
        subCatEnum: SubCategoryEnum,
        c: String?,
        pos: Int,
        urlBase: String
    ): String{
        val preNum = K.DeepLink.numberZerosLength
        return buildAnnotatedString {
            append(urlBase)
            append("/${catEnum.catId.addPrefixZeros(preNum)}")
            append("/${subCatEnum.subCatId.addPrefixZeros(preNum)}")
            append("/${c?:"*"}")
            append("/${pos.addPrefixZeros(preNum)}")
        }.text
    }

}