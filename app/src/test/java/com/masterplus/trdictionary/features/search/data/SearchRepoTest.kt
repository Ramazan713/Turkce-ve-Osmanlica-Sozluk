package com.masterplus.trdictionary.features.search.data

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import com.masterplus.trdictionary.core.data.local.services.SearchDao
import com.masterplus.trdictionary.core.data.local.services.SearchDaoFake
import com.masterplus.trdictionary.core.data.preferences.SettingsPreferencesFake
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo.WordDetailRepoFake
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo.WordListDetailRepoImpl
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.GetCategoryCompletedWordsPaging
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.GetCompletedWordFlow
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.GetCompletedWordInfo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.core.utils.extensions.toDictType
import com.masterplus.trdictionary.core.utils.extensions.toRevDictType
import com.masterplus.trdictionary.core.utils.extensions.toWordType
import com.masterplus.trdictionary.core.utils.sample_data.meaning
import com.masterplus.trdictionary.core.utils.sample_data.meaningEntity
import com.masterplus.trdictionary.core.utils.sample_data.meaningExamples
import com.masterplus.trdictionary.core.utils.sample_data.meaningExamplesRelation
import com.masterplus.trdictionary.core.utils.sample_data.wordDetail
import com.masterplus.trdictionary.core.utils.sample_data.wordDetailMeanings
import com.masterplus.trdictionary.core.utils.sample_data.wordDetailMeaningsRelation
import com.masterplus.trdictionary.core.utils.sample_data.wordDetailView
import com.masterplus.trdictionary.core.utils.sample_data.wordWithSimilar
import com.masterplus.trdictionary.core.utils.sample_data.wordWithSimilarRelation
import com.masterplus.trdictionary.features.home.data.repo.ShortInfoPreferenceFake
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.repo.SearchRepo
import com.masterplus.trdictionary.features.search.presentation.SearchEvent
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

class SearchRepoTest {

    private lateinit var wordDetailRepo: WordDetailRepoFake
    private lateinit var wordDetailUseCases: WordDetailsCompletedUseCases

    private lateinit var searchDao: SearchDaoFake
    private lateinit var settingsPreference: SettingsPreferencesFake
    private lateinit var searchRepo: SearchRepoImpl

    @ParameterizedTest
    @EnumSource(CategoryEnum::class)
    fun whenCategoryEnumIsDifferent_shouldBeResultDifferent(catEnum: CategoryEnum) = runTest {
        init {
            searchDao.initData = listOf(
                wordWithSimilarRelation(word = "word 1", dictType = catEnum.toDictType(), wordType = catEnum.toWordType()),
                wordWithSimilarRelation(word = "word 2", dictType = catEnum.toDictType(), wordType = catEnum.toWordType()),
                wordWithSimilarRelation(word = "word 3", dictType = catEnum.toRevDictType(), wordType = WordType.Default),
                wordWithSimilarRelation(word = "cat x", dictType = catEnum.toRevDictType(), wordType = WordType.Default),
            )
        }
        val query = "word"

        val result = searchRepo.search(query,catEnum,SearchKind.Word).first()

        if(catEnum == CategoryEnum.AllDict){
            assertThat(result.size).isEqualTo(3)
        }else{
            assertThat(result.size).isEqualTo(2)
            assertThat(result.map { it.wordDetail.wordType }).containsAll(catEnum.toWordType())
            assertThat(result.map { it.wordDetail.dictType }).containsAll(catEnum.toDictType())
        }
    }


    @ParameterizedTest
    @EnumSource(SearchKind::class)
    fun whenSearchKindIsDifferent_shouldBeResultDifferent(searchKind: SearchKind) = runTest {
        init {
            searchDao.initData = listOf(
                getWordWithSimilarWithWordAndMeaning(word = "word 1", meaning = "meaning 1"),
                getWordWithSimilarWithWordAndMeaning(word = "word 2", meaning = "meaning 2"),
                getWordWithSimilarWithWordAndMeaning(word = "meaning 3", meaning = "word 3"),
                getWordWithSimilarWithWordAndMeaning(word = "cat x 3", meaning = "cat x 3"),
            )
        }
        val query = "word"

        val result = searchRepo.search(query,CategoryEnum.AllDict,searchKind).first()

        if(searchKind == SearchKind.Word){
            assertThat(result.size).isEqualTo(2)
        }else{
            assertThat(result.size).isEqualTo(1)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [2,3,4,5])
    fun whenSearchCountIsNotNull_shouldReturnSearchCountItemsSize(itemCount: Int) = runTest {
        init {
            searchDao.initData = (1..10).map { index->
                wordWithSimilarRelation(word = "word $index")
            }
        }

        val query = "word"
        val result = searchRepo.search(query,CategoryEnum.AllDict,SearchKind.Word,itemCount).first()

        assertThat(result.size).isEqualTo(itemCount)
    }

    @ParameterizedTest
    @ValueSource(ints = [2,3,4,5])
    fun whenSearchCountIsNull_shouldReturnSettingsResultCountItemsSize(itemCount: Int) = runTest {
        init {
            searchDao.initData = (1..10).map { index->
                wordWithSimilarRelation(word = "word $index")
            }
            settingsPreference.updateData { it.copy(searchResultCount = itemCount) }
        }

        val query = "word"
        val result = searchRepo.search(query,CategoryEnum.AllDict,SearchKind.Word,null).first()

        assertThat(result.size).isEqualTo(itemCount)
    }






    private suspend fun init(onBeforeRepoInit: (suspend () -> Unit)? = null){
        wordDetailRepo = WordDetailRepoFake()
        wordDetailUseCases = WordDetailsCompletedUseCases(
            getCompletedWordFlow = mockk(),
            getCategoryCompletedWordsPaging = mockk(),
            getListCompletedWordsPaging = mockk(),
            completedWordInfo = GetCompletedWordInfo(wordDetailRepo)
        )

        settingsPreference = SettingsPreferencesFake()
        searchDao = SearchDaoFake()

        onBeforeRepoInit?.invoke()

        searchRepo = SearchRepoImpl(searchDao,settingsPreference,wordDetailUseCases)
    }


    private fun getWordWithSimilarWithWordAndMeaning(
        word: String,
        meaning: String
    ): WordWithSimilarRelation {
        return wordWithSimilarRelation(
            wordDetailMeanings = wordDetailMeaningsRelation(
                wordDetail = wordDetailView(
                    word = word
                ),
                meanings = listOf(
                    meaningExamplesRelation(meaning = meaningEntity(meaning = meaning))
                )
            )
        )
    }

}