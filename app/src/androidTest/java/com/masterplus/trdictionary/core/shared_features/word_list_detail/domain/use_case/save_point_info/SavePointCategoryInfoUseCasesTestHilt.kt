package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.save_point_info

import android.app.Application
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.masterplus.trdictionary.shared_test.HiltBaseClassForTest
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Test

@HiltAndroidTest
class SavePointCategoryInfoUseCasesTestHilt: HiltBaseClassForTest(){

    private lateinit var useCases: SavePointCategoryInfoUseCases

    override fun setUp() {
        super.setUp()
        useCases = SavePointCategoryInfoUseCases(context as Application)
    }

    @Test
    fun shouldReturnCategoryEnumInfoFromCatId() = runTest {
        CategoryEnum.entries.forEach { catEnum ->
            val result = useCases.invoke(catEnum.catId,1,null)
            assertThat(result.catEnum).isEqualTo(catEnum)
        }
    }

    @Test
    fun shouldReturnSubCategoryEnumInfoFromSubCatId() = runTest {
        SubCategoryEnum.entries.forEach { subCatEnum ->
            val result = useCases.invoke(1,subCatEnum.subCatId,null)
            assertThat(result.subCategoryEnum).isEqualTo(subCatEnum)
        }
    }

    @Test
    fun shouldTitleContainsAllParamInfo() = runTest {
        val cValues = listOf<String?>("a","bc",null)
        cValues.forEach { c->
            val catEnum = CategoryEnum.AllDict
            val subCatEnum = SubCategoryEnum.All
            val result = useCases.invoke(catEnum.catId,subCatEnum.subCatId,c)

            assertThat(result.savePointTitle).contains(catEnum.title.asString(context))
            assertThat(result.savePointTitle).contains(subCatEnum.description.asString(context))
            if(c!=null){
                assertThat(result.savePointTitle).contains(c)
            }
        }
        SavePointDestination.CategoryAll(SavePointType.List)
    }

    @Test
    fun whenSubCategoryIsAll_shouldReturnCategoryAllDestination() = runTest {
        val catEnum = CategoryEnum.AllDict
        val result = useCases.invoke(catEnum.catId,SubCategoryEnum.All.subCatId,null)
        assertThat(result.savePointDestination).isEqualTo(SavePointDestination.CategoryAll(SavePointType.fromCategory(catEnum)))
    }

    @Test
    fun whenSubCategoryIsRandom_shouldReturnCategoryRandomDestination() = runTest {
        val catEnum = CategoryEnum.AllDict
        val result = useCases.invoke(catEnum.catId,SubCategoryEnum.Random.subCatId,null)
        assertThat(result.savePointDestination).isEqualTo(SavePointDestination.CategoryRandom(SavePointType.fromCategory(catEnum)))
    }

    @Test
    fun whenSubCategoryIsAlphabetic_shouldReturnCategoryAlphabeticDestination() = runTest {
        val catEnum = CategoryEnum.AllDict
        val c = "asc"
        val result = useCases.invoke(catEnum.catId,SubCategoryEnum.Alphabetic.subCatId,c)
        assertThat(result.savePointDestination).isEqualTo(SavePointDestination.CategoryAlphabetic(SavePointType.fromCategory(catEnum),c))
    }

    @Test
    fun whenSubCategoryIsAlphabeticAndCIsNull_shouldReturnCategoryAlphabeticDestinationWithDefaultA() = runTest {
        val catEnum = CategoryEnum.AllDict
        val result = useCases.invoke(catEnum.catId,SubCategoryEnum.Alphabetic.subCatId,null)
        assertThat(result.savePointDestination).isEqualTo(SavePointDestination.CategoryAlphabetic(SavePointType.fromCategory(catEnum),"a"))
    }

}