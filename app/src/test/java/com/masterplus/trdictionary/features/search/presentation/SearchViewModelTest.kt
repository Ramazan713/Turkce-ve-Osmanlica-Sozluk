package com.masterplus.trdictionary.features.search.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.doesNotContain
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.utils.extensions.toDictType
import com.masterplus.trdictionary.core.utils.extensions.toRevDictType
import com.masterplus.trdictionary.core.utils.extensions.toWordType
import com.masterplus.trdictionary.core.utils.sample_data.history
import com.masterplus.trdictionary.core.utils.sample_data.meaning
import com.masterplus.trdictionary.core.utils.sample_data.meaningExamples
import com.masterplus.trdictionary.core.utils.sample_data.wordDetail
import com.masterplus.trdictionary.core.utils.sample_data.wordDetailMeanings
import com.masterplus.trdictionary.core.utils.sample_data.wordWithSimilar
import com.masterplus.trdictionary.features.search.data.HistoryRepoFake
import com.masterplus.trdictionary.features.search.data.SearchRepoFake
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.repo.HistoryRepo
import com.masterplus.trdictionary.features.search.domain.repo.SearchRepo
import com.masterplus.trdictionary.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.experimental.categories.Category
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherRule::class)
class SearchViewModelTest {

    private lateinit var searchRepo: SearchRepoFake
    private lateinit var historyRepo: HistoryRepoFake
    private lateinit var viewModel: SearchViewModel

    @BeforeEach
    fun setUp() {
        initSearchViewModel()
    }


    @Test
    fun clearQuery_whenEvent_clearQuery() = runTest {
        val firstQuery = "query 1"
        viewModel.onEvent(SearchEvent.SearchQuery(queryText = firstQuery))

        advanceUntilIdle()
        val firstState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.ClearQuery)

        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(firstState.query.text).isEqualTo(firstQuery)
        assertThat(lastState.query.text).isEmpty()
    }

    @Test
    fun searchQuery_whenQueryIsSet_shouldBeQueryInStateAndSelectedWordIdSetNull() = runTest {
        val query = "query 1"
        viewModel.onEvent(SearchEvent.ShowDetail(wordWithSimilar = wordWithSimilar()))
        advanceUntilIdle()
        val firstState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(firstState.query.text).isEmpty()
        assertThat(firstState.selectedWordId).isNotNull()
        assertThat(lastState.query.text).isEqualTo(query)
        assertThat(lastState.selectedWordId).isNull()
    }

    @Test
    fun searchQuery_whenQueryIsSameWithOldQuery_shouldBeNothingHappened() = runTest {
        val query = "query 1"
        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        viewModel.onEvent(SearchEvent.ShowDetail(wordWithSimilar = wordWithSimilar()))
        advanceUntilIdle()
        val firstState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(firstState.selectedWordId).isNotNull()
        assertThat(lastState.selectedWordId).isNotNull()
        assertThat(firstState.query.text).isEqualTo(query)
        assertThat(lastState.query.text).isEqualTo(query)
    }

    @ParameterizedTest
    @CsvSource(
        "'',true",
        "a,true",
        "x,true",
        "word,false"
    )
    fun searchQuery_whenQueryLenZeroOrOne_shouldBeSearchingCancelled(query: String, isCancelled: Boolean) = runTest {
        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        if(isCancelled){
            assertThat(lastState.query.text).isEqualTo(query)
            assertThat(lastState.searchLoading).isFalse()
            assertThat(lastState.selectedWordId).isNull()
            assertThat(lastState.wordResults).isEmpty()
        }else{
            assertThat(lastState.query.text).isEqualTo(query)
            assertThat(lastState.wordResults).isNotEmpty()
        }

    }

    @Test
    fun searchQuery_whenQueryChange_insertNewHistory() = runTest {
        val query = "query test"
        val firstState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(firstState.histories.map { it.content }).doesNotContain(query)
        assertThat(lastState.histories.map { it.content }).contains(query)
    }

    @Test
    fun searchQuery_whenSearchingIsSuccess_shouldChangeStateInOrderLikeLoading() = runTest {
        initSearchViewModel {
            searchRepo.data = listOf(
                wordWithSimilar(word = "word x"),
                wordWithSimilar(word = "word y")
            )
        }
        advanceUntilIdle()
        viewModel.state.test {
            awaitItem() // init state
            val query = "word"
            viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
            val changedQueryState = awaitItem()
            val loadingState = awaitItem()
            awaitItem() // insert history state
            val finishedState = awaitItem()

            assertThat(changedQueryState.searchLoading).isFalse()
            assertThat(changedQueryState.query.text).isEqualTo(query)
            assertThat(loadingState.searchLoading).isTrue()
            assertThat(finishedState.searchLoading).isFalse()
            assertThat(finishedState.wordResults.size).isEqualTo(2)
            assertThat(finishedState.selectedWordId).isEqualTo(finishedState.wordResults.first().wordId)
        }
    }

    @ParameterizedTest
    @EnumSource(CategoryEnum::class)
    fun searchQuery_whenCategoryEnumIsDifferent_shouldBeResultDifferent(catEnum: CategoryEnum) = runTest {
        initSearchViewModel {
            searchRepo.data = listOf(
                wordWithSimilar(word = "word x", wordType = catEnum.toWordType(), dictType = catEnum.toDictType()),
                wordWithSimilar(word = "word y", wordType = catEnum.toWordType(), dictType = catEnum.toDictType()),
                wordWithSimilar(word = "word z", wordType = WordType.PureWord, dictType = catEnum.toRevDictType()),
            )
        }

        val initState = viewModel.state.first()
        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = catEnum, searchKind = initState.searchKind))
        advanceUntilIdle()
        val query = "word"

        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        if(catEnum == CategoryEnum.AllDict){
            assertThat(lastState.wordResults.size).isEqualTo(3)
        }else{
            assertThat(lastState.wordResults.size).isEqualTo(2)
            assertThat(lastState.wordResults.map { it.wordDetail.wordType }).containsAll(catEnum.toWordType())
            assertThat(lastState.wordResults.map { it.wordDetail.dictType }).containsAll(catEnum.toDictType())
        }
    }

    @ParameterizedTest
    @EnumSource(SearchKind::class)
    fun searchQuery_whenSearchKindIsDifferent_shouldBeResultDifferent(searchKind: SearchKind) = runTest {
        initSearchViewModel {
            searchRepo.data = listOf(
                getWordWithSimilarWithWordAndMeaning(word = "word 1", meaning = "meaning 1"),
                getWordWithSimilarWithWordAndMeaning(word = "word 2", meaning = "meaning 2"),
                getWordWithSimilarWithWordAndMeaning(word = "meaning 3", meaning = "word 3"),
            )
        }
        val initState = viewModel.state.first()
        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = initState.categoryFilter, searchKind = searchKind))
        advanceUntilIdle()
        val query = "word"

        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        if(searchKind == SearchKind.Word){
            assertThat(lastState.wordResults.size).isEqualTo(2)
        }else{
            assertThat(lastState.wordResults.size).isEqualTo(1)
        }
    }


    @Test
    fun changeFilter_whenCategoryFilterChange_shouldChangeExistsResult() = runTest {
        initSearchViewModel {
            searchRepo.data = listOf(
                wordWithSimilar(word = "word x", wordType = WordType.Proverb),
                wordWithSimilar(word = "word y", wordType = WordType.Proverb),
                wordWithSimilar(word = "word z", wordType = WordType.Idiom),
            )
        }
        val query = "word"
        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        val initState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = CategoryEnum.ProverbDict, searchKind = initState.searchKind))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(initState.wordResults.size).isEqualTo(3)
        assertThat(lastState.wordResults.size).isEqualTo(2)
        assertThat(lastState.wordResults.map { it.wordDetail.wordType }).containsAll(WordType.Proverb)
    }

    @Test
    fun changeFilter_whenSearchKindFilterChange_shouldChangeExistsResult() = runTest {
        initSearchViewModel {
            searchRepo.data = listOf(
                getWordWithSimilarWithWordAndMeaning(word = "word 1", meaning = "meaning 1"),
                getWordWithSimilarWithWordAndMeaning(word = "word 2", meaning = "meaning 2"),
                getWordWithSimilarWithWordAndMeaning(word = "meaning 3", meaning = "word 3"),
            )
        }

        val query = "word"
        viewModel.onEvent(SearchEvent.SearchQuery(queryText = query))
        advanceUntilIdle()
        val initState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = initState.categoryFilter, searchKind = SearchKind.Meaning))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(initState.wordResults.size).isEqualTo(2)
        assertThat(lastState.wordResults.size).isEqualTo(1)
    }

    @Test
    fun changeFilter_whenSearchKindFilterChange_shouldChangeState() = runTest {
        val destSearchKind = SearchKind.Meaning
        val firstState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = firstState.categoryFilter, searchKind = destSearchKind))
        advanceUntilIdle()

        val lastState = viewModel.state.first()
        assertThat(firstState.searchKind).isNotEqualTo(lastState.searchKind)
        assertThat(lastState.searchKind).isEqualTo(destSearchKind)
    }

    @Test
    fun changeFilter_whenCategoryFilterChange_shouldChangeState() = runTest {
        val destCategoryEnum = CategoryEnum.ProverbDict
        val firstState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = destCategoryEnum, searchKind = firstState.searchKind))
        advanceUntilIdle()

        val lastState = viewModel.state.first()
        assertThat(firstState.categoryFilter).isNotEqualTo(lastState.categoryFilter)
        assertThat(lastState.categoryFilter).isEqualTo(destCategoryEnum)
    }

    @ParameterizedTest
    @EnumSource(CategoryEnum::class)
    fun changeFilter_whenCategoryFilterChangeAndNotDefault_shouldBadgeChange(catEnum: CategoryEnum) = runTest {
        val firstState = viewModel.state.first()
        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = catEnum, searchKind = firstState.searchKind))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        if(lastState.defaultCategory != catEnum){
            assertThat(firstState.badge).isNull()
            assertThat(lastState.badge).isEqualTo("1")
        }else{
            assertThat(lastState.badge).isNull()
        }
    }

    @Test
    fun changeFilter_whenSearchKindFilterChange_shouldBadgeChange() = runTest {
        advanceUntilIdle()
        val firstState = viewModel.state.first()
        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = firstState.categoryFilter, searchKind = SearchKind.Meaning))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(firstState.badge).isNull()
        assertThat(lastState.badge).isEqualTo("1")
    }

    @ParameterizedTest
    @EnumSource(CategoryEnum::class)
    fun changeFilter_whenBothFilterChange_shouldBadgeChange(catEnum: CategoryEnum) = runTest {
        val firstState = viewModel.state.first()
        viewModel.onEvent(SearchEvent.ChangeFilter(catEnum = catEnum, searchKind = SearchKind.Meaning))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        if(lastState.defaultCategory != catEnum){
            assertThat(firstState.badge).isNull()
            assertThat(lastState.badge).isEqualTo("2")
        }else{
            assertThat(lastState.badge).isEqualTo("1")
        }
    }


    @ParameterizedTest
    @EnumSource(CategoryEnum::class)
    fun setDefaultFilter_whenCategoryParamDifferent_shouldBeCategoryDefaultInState(catEnum: CategoryEnum) = runTest {
        initSearchViewModel(catId = catEnum.catId)
        advanceUntilIdle()
        val state = viewModel.state.first()

        assertThat(state.defaultCategory).isEqualTo(catEnum)
        assertThat(state.categoryFilter).isEqualTo(catEnum)
    }

    @Test
    fun deleteHistory_shouldDeleteHistory() = runTest {
        advanceUntilIdle()
        val initState = viewModel.state.first()
        val deletedHistory = initState.histories.first()

        viewModel.onEvent(SearchEvent.DeleteHistory(deletedHistory))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(initState.histories).contains(deletedHistory)
        assertThat(lastState.histories).doesNotContain(deletedHistory)
    }

    @Test
    fun historyClicked_whenHistoryClick_shouldSearch() = runTest {
        initSearchViewModel {
            historyRepo.setInitData(listOf(
                history(content = "word")
            ))
            searchRepo.data = listOf(
                wordWithSimilar(word = "word 1")
            )
        }
        advanceUntilIdle()
        val initState = viewModel.state.first()
        val history = initState.histories.first()

        viewModel.onEvent(SearchEvent.HistoryClicked(history))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(initState.query.text).isEmpty()
        assertThat(lastState.query.text).isEqualTo(history.content)
        assertThat(lastState.wordResults).isNotEmpty()
    }

    @Test
    fun hideDetail_whenDetailOpen_shouldBeStateInHide() = runTest {
        viewModel.onEvent(SearchEvent.ShowDetail(wordWithSimilar()))
        advanceUntilIdle()
        val firstState = viewModel.state.first()

        viewModel.onEvent(SearchEvent.HideDetail)
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(firstState.isDetailOpen).isTrue()
        assertThat(firstState.selectedWordId).isNotNull()
        assertThat(lastState.isDetailOpen).isFalse()
        assertThat(lastState.selectedWordId).isNull()
    }

    @Test
    fun showDetail_whenDetailHide_shouldShowDetail() = runTest {
        advanceUntilIdle()
        val firstState = viewModel.state.first()
        val word = wordWithSimilar()

        viewModel.onEvent(SearchEvent.ShowDetail(word))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(firstState.isDetailOpen).isFalse()
        assertThat(firstState.selectedWordId).isNull()
        assertThat(lastState.isDetailOpen).isTrue()
        assertThat(lastState.selectedWordId).isEqualTo(word.wordId)
    }

    private fun getWordWithSimilarWithWordAndMeaning(
        word: String,
        meaning: String
    ): WordWithSimilar {
        return wordWithSimilar(
            wordDetailMeanings = wordDetailMeanings(
                wordDetail = wordDetail(
                    word = word
                ),
                meanings = listOf(
                    meaningExamples(meaning = meaning(meaning = meaning))
                )
            )
        )
    }

    private fun initSearchViewModel(
        catId: Int = CategoryEnum.AllDict.catId,
        funcBeforeViewModelInit: ( ()->Unit )? = null
    ){
        searchRepo = SearchRepoFake()
        historyRepo = HistoryRepoFake()

        funcBeforeViewModelInit?.invoke()

        viewModel = SearchViewModel(
            historyRepo,
            searchRepo,
            SavedStateHandle(mapOf("catId" to catId))
        )
    }

}