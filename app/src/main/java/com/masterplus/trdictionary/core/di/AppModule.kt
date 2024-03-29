package com.masterplus.trdictionary.core.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.ExperimentalMultiProcessDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.masterplus.trdictionary.core.data.ConnectivityProviderImpl
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.data.local.TransactionProviderImpl
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.masterplus.trdictionary.core.data.DefaultDispatcherProvider
import com.masterplus.trdictionary.core.data.preferences.DefaultAppPreferencesImpl
import com.masterplus.trdictionary.core.data.preferences.SettingsPreferencesImplApp
import com.masterplus.trdictionary.core.domain.DispatcherProvider
import com.masterplus.trdictionary.core.domain.TransactionProvider
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsDataSerializer
import java.io.File

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            name = "dictDb.db"
        )
            .createFromAsset("dictDb.db")
            .addCallback(object: RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO WordsFts(WordsFts) VALUES ('rebuild')")
                }
            })
            .build()


    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("AppPreferences",MODE_PRIVATE)

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
    fun provideCoroutineScope(provider: DispatcherProvider) = CoroutineScope( provider.io + SupervisorJob())


    @OptIn(ExperimentalMultiProcessDataStore::class)
    @Provides
    @Singleton
    fun provideSettingsDataStore(application: Application) =
        MultiProcessDataStoreFactory.create(
            SettingsDataSerializer(),
            produceFile = {
                File("${application.cacheDir.path}/myapp.settingsPreferences_pb")
            }
        )

    @Provides
    @Singleton
    fun provideSettingsPreferenceRepo(datastore: DataStore<SettingsData>): SettingsPreferencesApp =
        SettingsPreferencesImplApp(datastore)

    @Provides
    @Singleton
    fun provideAppDataStore(application: Application): DataStore<Preferences> =
        application.dataStore

    @Singleton
    @Provides
    fun provideTransactionProvider(appDatabase: AppDatabase): TransactionProvider =
        TransactionProviderImpl(appDatabase)

}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("appPreferences")