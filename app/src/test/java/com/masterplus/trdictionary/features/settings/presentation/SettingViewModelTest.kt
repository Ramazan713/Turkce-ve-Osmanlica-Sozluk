package com.masterplus.trdictionary.features.settings.presentation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.data.preferences.SettingsPreferencesFake
import com.masterplus.trdictionary.core.data.repo.ThemeRepoImpl
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import com.masterplus.trdictionary.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherRule::class)
class SettingViewModelTest {

    private lateinit var settingsPreferences: SettingsPreferencesFake
    private lateinit var themeRepo: ThemeRepo

    private lateinit var settingViewModel: SettingViewModel


    @BeforeEach
    fun setUp(){
        settingsPreferences = SettingsPreferencesFake()
        themeRepo = ThemeRepoImpl(settingsPreferences)

        settingViewModel = SettingViewModel(themeRepo, settingsPreferences)
    }

    @ParameterizedTest
    @CsvSource(
        "false,97,true,1",
        "true,93,true,2",
        "false,13,false,3",
        "false,19,true,1"
    )
    fun init_whenSettingsPrefHasDifferentValue_shouldStateStartWith(
        useArchiveAsList: Boolean,
        searchResultCount: Int,
        useDynamicColor: Boolean,
        themeEnumKeyValue: Int
    ) = runTest {
        val themeEnum = ThemeEnum.fromKeyValue(themeEnumKeyValue)

        settingsPreferences.updateData {
            it.copy(
                useArchiveLikeList = useArchiveAsList,
                searchResultCount = searchResultCount,
                useThemeDynamic = useDynamicColor,
                themeEnum = themeEnum
            )
        }

        settingViewModel = SettingViewModel(themeRepo, settingsPreferences)

        advanceUntilIdle()
        val state = settingViewModel.state.value

        assertThat(state.useArchiveAsList).isEqualTo(useArchiveAsList)
        assertThat(state.searchResult).isEqualTo(searchResultCount)
        assertThat(state.themeModel.useDynamicColor).isEqualTo(useDynamicColor)
        assertThat(state.themeModel.themeEnum).isEqualTo(themeEnum)
    }


    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun setDynamicTheme_ifSucceed_shouldStateChange(initUseDynamicColor: Boolean) = runTest {
        settingViewModel.onEvent(SettingEvent.SetDynamicTheme(initUseDynamicColor))

        advanceUntilIdle()
        val lastUseDynamicColor = settingViewModel.state.value.themeModel.useDynamicColor
        assertThat(lastUseDynamicColor).isEqualTo(initUseDynamicColor)
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun setDynamicTheme_ifSucceed_shouldPreferencesChange(initUseDynamicColor: Boolean) = runTest {
        settingViewModel.onEvent(SettingEvent.SetDynamicTheme(initUseDynamicColor))

        advanceUntilIdle()
        val lastUseDynamicColor = settingsPreferences.getData().useThemeDynamic
        assertThat(lastUseDynamicColor).isEqualTo(initUseDynamicColor)
    }

    @ParameterizedTest
    @EnumSource(ThemeEnum::class)
    fun setThemeEnum_ifSucceed_shouldStateChange(themeEnum: ThemeEnum) = runTest {
        settingViewModel.onEvent(SettingEvent.SetThemeEnum(themeEnum))

        advanceUntilIdle()
        val lastThemeEnum = settingViewModel.state.value.themeModel.themeEnum
        assertThat(themeEnum).isEqualTo(lastThemeEnum)
    }

    @ParameterizedTest
    @EnumSource(ThemeEnum::class)
    fun setThemeEnum_ifSucceed_shouldPreferencesChange(themeEnum: ThemeEnum) = runTest {
        settingViewModel.onEvent(SettingEvent.SetThemeEnum(themeEnum))

        advanceUntilIdle()
        val lastThemeEnum = settingsPreferences.getData().themeEnum
        assertThat(themeEnum).isEqualTo(lastThemeEnum)
    }

    @ParameterizedTest
    @CsvSource(
        "false,97,true,1",
        "true,93,true,2",
        "false,13,false,3",
        "false,19,true,1"
    )
    fun resetDefaultValues_ifSucceed_shouldStateAndPreferencesReset(
        useArchiveAsList: Boolean,
        searchResultCount: Int,
        useDynamicColor: Boolean,
        themeEnumKeyValue: Int
    ) = runTest {
        val themeEnum = ThemeEnum.fromKeyValue(themeEnumKeyValue)
        settingsPreferences.updateData {
            it.copy(
                useArchiveLikeList = useArchiveAsList,
                searchResultCount = searchResultCount,
                useThemeDynamic = useDynamicColor,
                themeEnum = themeEnum
            )
        }
        settingViewModel = SettingViewModel(themeRepo, settingsPreferences)
        advanceUntilIdle()
        val firstState = settingViewModel.state.value
        val firstPrefData = settingsPreferences.getData()

        settingViewModel.onEvent(SettingEvent.ResetDefaultValues)

        advanceUntilIdle()
        val lastState = settingViewModel.state.value
        val lastPrefData = settingsPreferences.getData()

        assertThat(firstState).isNotEqualTo(lastState)
        assertThat(firstPrefData).isNotEqualTo(lastPrefData)
        assertThat(lastPrefData).isEqualTo(SettingsData())
    }


    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun useArchiveAsList_ifSucceed_shouldPreferencesChange(useArchiveAsList: Boolean) = runTest {
        settingViewModel.onEvent(SettingEvent.UseArchiveAsList(useArchiveAsList))

        advanceUntilIdle()
        val lastUseArchiveAsList = settingsPreferences.getData().useArchiveLikeList
        assertThat(lastUseArchiveAsList).isEqualTo(useArchiveAsList)
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun useArchiveAsList_ifSucceed_shouldStateChange(useArchiveAsList: Boolean) = runTest {
        settingViewModel.onEvent(SettingEvent.UseArchiveAsList(useArchiveAsList))

        advanceUntilIdle()
        val lastUseArchiveAsList = settingViewModel.state.value.useArchiveAsList
        assertThat(lastUseArchiveAsList).isEqualTo(useArchiveAsList)
    }


    @Test
    fun notShowBackupInitDialog_ifSucceed_shouldPreferencesChange() = runTest {
        settingsPreferences.updateData { it.copy(showBackupSectionForLogin = true) }
        advanceUntilIdle()
        val firstValue = settingsPreferences.getData().showBackupSectionForLogin

        settingViewModel.onEvent(SettingEvent.NotShowBackupInitDialog)

        advanceUntilIdle()
        val lastUseArchiveAsList = settingsPreferences.getData().showBackupSectionForLogin

        assertThat(firstValue).isTrue()
        assertThat(lastUseArchiveAsList).isEqualTo(false)
    }



    @ParameterizedTest
    @ValueSource(ints = [19,13,65,97,89])
    fun setSearchResult_ifSucceed_shouldPreferencesChange(searchResult: Int) = runTest {
        settingViewModel.onEvent(SettingEvent.SetSearchResultEnum(searchResult))

        advanceUntilIdle()
        val lastSearchResultCount = settingsPreferences.getData().searchResultCount
        assertThat(lastSearchResultCount).isEqualTo(searchResult)
    }

    @ParameterizedTest
    @ValueSource(ints = [19,13,65,97,89])
    fun setSearchResult_ifSucceed_shouldStateChange(searchResult: Int) = runTest {
        settingViewModel.onEvent(SettingEvent.SetSearchResultEnum(searchResult))

        advanceUntilIdle()
        val lastSearchResultCount = settingViewModel.state.value.searchResult
        assertThat(lastSearchResultCount).isEqualTo(searchResult)
    }

}