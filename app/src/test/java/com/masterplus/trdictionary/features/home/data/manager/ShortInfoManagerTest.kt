package com.masterplus.trdictionary.features.home.data.manager

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.utils.sample_data.simpleWordResult
import com.masterplus.trdictionary.core.utils.toWordType
import com.masterplus.trdictionary.features.home.data.repo.ShortInfoPreferenceFake
import com.masterplus.trdictionary.features.home.data.repo.ShortInfoRepoFake
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoPreferenceData
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoPreference
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import kotlinx.coroutines.test.runTest
import com.masterplus.trdictionary.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.Calendar
import kotlin.math.absoluteValue
import kotlin.random.Random

class ShortInfoManagerTest {

    private lateinit var shortInfoRepo: ShortInfoRepoFake
    private lateinit var preference: ShortInfoPreferenceFake

    private lateinit var manager: ShortInfoManager


    private var words = listOf(
        simpleWordResult(wordId = 1, wordType = WordType.Idiom),
        simpleWordResult(wordId = 2, wordType = WordType.Proverb),
        simpleWordResult(wordId = 5, wordType = WordType.Proverb),
        simpleWordResult(wordId = 3, wordType = WordType.Default),
        simpleWordResult(wordId = 4, wordType = WordType.Idiom),
        simpleWordResult(wordId = 6, wordType = WordType.Proverb),
        simpleWordResult(wordId = 7, wordType = WordType.Idiom),
        simpleWordResult(wordId = 8, wordType = WordType.Idiom),
    )


    @BeforeEach
    fun setUp() {

        shortInfoRepo = ShortInfoRepoFake()
        preference = ShortInfoPreferenceFake()

        manager = ShortInfoManagerImpl(shortInfoRepo,preference)

        shortInfoRepo.words = words
    }


    @ParameterizedTest
    @EnumSource(ShortInfoEnum::class)
    fun getWord_whenRefreshFalse_getFirstWord(shortInfoEnum: ShortInfoEnum) = runTest {
        val firstWord = simpleWordResult(wordId = 1, wordType = shortInfoEnum.toWordType())
        shortInfoRepo.words = listOf(
            firstWord,
            simpleWordResult(wordId = 2, wordType = shortInfoEnum.toWordType())
        )

        val word = manager.getWord(shortInfoEnum,false)
        assertThat(word).isEqualTo(firstWord)
    }

    @Test
    fun getWord_whenRefreshTrue_shouldPreferencesSaveRandomNumber() = runTest {
        mockkObject(preference)
        mockkObject(shortInfoRepo)

        manager.getWord(ShortInfoEnum.Idiom,true)
        coVerify {
            shortInfoRepo.countWordsByTypeId(ProverbIdiomEnum.Idiom)
            preference.updateData(any())
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [0,1,2])
    fun getWord_whenRandomNumberSameInPreferences_getWordWithRandomNumber(randomNumber: Int) = runTest {
        preference.updateData { it.copy(idiomRandomNumber = randomNumber) }
        val word = manager.getWord(ShortInfoEnum.Idiom,false)

        val filteredResultWord = shortInfoRepo.words.filter { it.word.wordType == WordType.Idiom }[randomNumber]
        assertThat(word).isEqualTo(filteredResultWord)
    }

    @ParameterizedTest
    @ValueSource(ints = [0,1,2])
    fun getWord_whenParameterHasRandomNumber_getWordWithRandomNumber(randomNumber: Int) = runTest {
        val word = manager.getWord(ShortInfoEnum.Idiom,randomNumber)
        val filteredResultWord = shortInfoRepo.words.filter { it.word.wordType == WordType.Idiom }[randomNumber]
        assertThat(word).isEqualTo(filteredResultWord)
    }


    @Test
    fun getWords_whenRefreshFalse_getCollectionByUsingPreferenceRandomOrder() = runTest {
        val wordRandomNumber = 0
        val idiomRandomNumber = 1
        val proverbRandomNumber = 2
        preference.updateData { it.copy(idiomRandomNumber = idiomRandomNumber, wordRandomNumber = wordRandomNumber, proverbRandomNumber = proverbRandomNumber) }

        val collectionResult = manager.getWords(false)

        assertThat(collectionResult.word).isEqualTo(manager.getWord(ShortInfoEnum.Word,wordRandomNumber))
        assertThat(collectionResult.idiom).isEqualTo(manager.getWord(ShortInfoEnum.Idiom,idiomRandomNumber))
        assertThat(collectionResult.proverb).isEqualTo(manager.getWord(ShortInfoEnum.Proverb,proverbRandomNumber))
    }

    @Test
    fun getWords_whenRefreshTrue_getCollectionByUsingGeneratedRandomOrder() = runTest {
        val wordRandomNumber = 4
        val idiomRandomNumber = 1
        val proverbRandomNumber = 1

        mockRandomClassForGenerateRandomNumber(wordRandomNumber, idiomRandomNumber, proverbRandomNumber)
        mockkObject(preference)

        val collectionResult = manager.getWords(true)

        assertThat(collectionResult.word).isEqualTo(manager.getWord(ShortInfoEnum.Word,wordRandomNumber))
        assertThat(collectionResult.idiom).isEqualTo(manager.getWord(ShortInfoEnum.Idiom,idiomRandomNumber))
        assertThat(collectionResult.proverb).isEqualTo(manager.getWord(ShortInfoEnum.Proverb,proverbRandomNumber))

        coVerify(exactly = 3) {
            preference.updateData(any())
        }
    }

    @Test
    fun refreshNumber_refreshNumberShouldBeLessThanTotalSize() = runTest {
        val proverbWordSize = words.filter { it.word.wordType == WordType.Proverb }.size
        val idiomWordSize = words.filter { it.word.wordType == WordType.Idiom }.size

        mockRandomClassForGenerateRandomNumber(
            wordRandomNumber = words.size,
            proverbRandomNumber = proverbWordSize,
            idiomRandomNumber = idiomWordSize,
            includeLastNumber = true
        )

        mockRandomClassForGenerateRandomNumber(
            wordRandomNumber = words.size - 1,
            proverbRandomNumber = proverbWordSize - 1,
            idiomRandomNumber = idiomWordSize - 1,
            includeLastNumber = false
        )

        manager.refreshWords()

        val prefData = preference.getData()
        assertThat(prefData.wordRandomNumber).isEqualTo(words.size - 1)
        assertThat(prefData.idiomRandomNumber).isEqualTo(idiomWordSize - 1)
        assertThat(prefData.proverbRandomNumber).isEqualTo(proverbWordSize - 1)
    }

    @Test
    fun refreshNumber_refreshNumberShouldBeZero() = runTest {
        preference.updateData { it.copy(wordRandomNumber = 2, proverbRandomNumber = 2, idiomRandomNumber = 2) }
        mockRandomClassForGenerateRandomNumber(
            wordRandomNumber = 0,
            proverbRandomNumber = 0,
            idiomRandomNumber = 0,
        )
        manager.refreshWords()

        val prefData = preference.getData()
        assertThat(prefData.wordRandomNumber).isEqualTo(0)
        assertThat(prefData.idiomRandomNumber).isEqualTo(0)
        assertThat(prefData.proverbRandomNumber).isEqualTo(0)
    }


    @Test
    fun getWordsFlow_whenPreferenceChange_shouldResponseNewCollection() = runTest {
        manager.getWordsFlow().test {
            val firstCollection = awaitItem()
            preference.updateData { it.copy(wordRandomNumber = 4) }
            val lastCollection = awaitItem()

            assertThat(firstCollection).isNotEqualTo(lastCollection)
        }
    }

    @Test
    fun refreshWords_shouldPreferenceValuesChange() = runTest {
        preference.updateData { ShortInfoPreferenceData() }
        val wordRandomNumber = 5
        val idiomRandomNumber = 0
        val proverbRandomNumber = 3
        mockRandomClassForGenerateRandomNumber(wordRandomNumber, idiomRandomNumber, proverbRandomNumber)

        manager.refreshWords()

        val prefData = preference.getData()
        assertThat(prefData.wordRandomNumber).isEqualTo(wordRandomNumber)
        assertThat(prefData.idiomRandomNumber).isEqualTo(idiomRandomNumber)
        assertThat(prefData.proverbRandomNumber).isEqualTo(proverbRandomNumber)
    }


    @ParameterizedTest
    @EnumSource(ShortInfoEnum::class)
    fun refreshWord_shouldPreferenceValueChange(shortInfoEnum: ShortInfoEnum) = runTest {
        val wordRandomNumber = 5
        val idiomRandomNumber = 1
        val proverbRandomNumber = 3
        mockRandomClassForGenerateRandomNumber(wordRandomNumber, idiomRandomNumber, proverbRandomNumber)

        manager.refreshWord(shortInfoEnum)

        val prefData = preference.getData()
        assertThat(prefData.wordRandomNumber).isEqualTo(if(shortInfoEnum == ShortInfoEnum.Word) wordRandomNumber else 0)
        assertThat(prefData.idiomRandomNumber).isEqualTo(if(shortInfoEnum == ShortInfoEnum.Idiom) idiomRandomNumber else 0)
        assertThat(prefData.proverbRandomNumber).isEqualTo(if(shortInfoEnum == ShortInfoEnum.Proverb) proverbRandomNumber else 0)
    }


    private fun mockRandomClassForGenerateRandomNumber(
        wordRandomNumber: Int = 4,
        idiomRandomNumber: Int = 1,
        proverbRandomNumber: Int = 1,
        includeLastNumber: Boolean = false
    ){
        val proverbWordSize = words.filter { it.word.wordType == WordType.Proverb }.size
        val idiomWordSize = words.filter { it.word.wordType == WordType.Idiom }.size

        mockkObject(Random.Default) // must be every size different each other

        if(includeLastNumber){
            every { (0..idiomWordSize).random() } returns idiomRandomNumber
            every { (0..proverbWordSize).random() } returns proverbRandomNumber
            every { (0..words.size).random() } returns wordRandomNumber
        }else{
            every { (0..<idiomWordSize).random() } returns idiomRandomNumber
            every { (0..<proverbWordSize).random() } returns proverbRandomNumber
            every { (0..< words.size).random() } returns wordRandomNumber
        }
    }
}