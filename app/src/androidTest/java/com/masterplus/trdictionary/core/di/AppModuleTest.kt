package com.masterplus.trdictionary.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.ExperimentalMultiProcessDataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.masterplus.trdictionary.core.data.ConnectivityProviderImpl
import com.masterplus.trdictionary.core.data.DefaultDispatcherProvider
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.data.local.TransactionProviderImpl
import com.masterplus.trdictionary.core.data.preferences.DefaultAppPreferencesImpl
import com.masterplus.trdictionary.core.data.preferences.SettingsPreferencesImplApp
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.DispatcherProvider
import com.masterplus.trdictionary.core.domain.TransactionProvider
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsDataSerializer
import com.masterplus.trdictionary.core.preferences.SettingsPreferencesFake
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.*
import java.io.File
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [
        AppModule::class
    ]
)
object AppModuleTest {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) =
        Room.inMemoryDatabaseBuilder(
                application, AppDatabase::class.java)
            .build()


    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("AppPreferencesTesting", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideAppPreferences(datastore: DataStore<Preferences>): AppPreferences =
        DefaultAppPreferencesImpl(datastore)


    @Provides
    fun provideConnectivityProvider(application: Application): ConnectivityProvider =
        ConnectivityProviderImpl(application)


    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideCoroutineScope(provider: DispatcherProvider): CoroutineScope = CoroutineScope(provider.io + SupervisorJob())


    @Provides
    @Singleton
    fun provideSettingsPreferenceRepo(): SettingsPreferencesApp =
        SettingsPreferencesFake()

    @Provides
    @Singleton
    fun provideAppDataStore(application: Application): DataStore<Preferences> =
        application.dataStore

    @Singleton
    @Provides
    fun provideTransactionProvider(appDatabase: AppDatabase): TransactionProvider =
        TransactionProviderImpl(appDatabase)

}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("appPreferencesTesting")