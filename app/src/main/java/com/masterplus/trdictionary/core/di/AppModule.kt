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
import com.masterplus.trdictionary.core.data.GsonParser
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.data.local.TransactionProvider
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.JsonParser
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.masterplus.trdictionary.core.data.preferences.DefaultAppPreferencesImpl
import com.masterplus.trdictionary.core.data.preferences.SettingsPreferencesImplApp
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
    @Singleton
    fun provideJsonParser(): JsonParser = GsonParser()

    @Provides
    @Singleton
    fun provideTransactionProvider(db: AppDatabase) = TransactionProvider(db)

    @Provides
    fun provideConnectivityProvider(application: Application): ConnectivityProvider =
        ConnectivityProviderImpl(application)


    @Provides
    @Singleton
    fun provideCoroutineScope(ioDispatcher: CoroutineDispatcher) = CoroutineScope( ioDispatcher + SupervisorJob())

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO



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

}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("appPreferences")