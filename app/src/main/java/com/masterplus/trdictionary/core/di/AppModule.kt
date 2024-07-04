package com.masterplus.trdictionary.core.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.masterplus.trdictionary.BuildConfig
import com.masterplus.trdictionary.core.data.ConnectivityProviderImpl
import com.masterplus.trdictionary.core.data.DefaultDispatcherProvider
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.data.local.TransactionProviderImpl
import com.masterplus.trdictionary.core.data.local.utils.SQLCipherUtils
import com.masterplus.trdictionary.core.data.preferences.DefaultAppPreferencesImpl
import com.masterplus.trdictionary.core.data.preferences.SettingsPreferencesImplApp
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.DispatcherProvider
import com.masterplus.trdictionary.core.domain.TransactionProvider
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsDataSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase{
        val dbName = "dictDb.db"

        val passphrase: ByteArray = SQLiteDatabase.getBytes(BuildConfig.DATABASE_PASSPHRASE.toCharArray())
        val factory = SupportFactory(passphrase)
        val state = SQLCipherUtils.getDatabaseState(application, dbName)

        // Migrate the database to an encrypted one if it is currently unencrypted
        if (state == SQLCipherUtils.State.UNENCRYPTED) {
            SQLCipherUtils.migrateToEncryptedDatabase(dbName, application, BuildConfig.DATABASE_PASSPHRASE)
        }

        return Room
            .databaseBuilder(
                application,
                AppDatabase::class.java,
                name = dbName
            )
            .createFromAsset(dbName)
            .openHelperFactory(factory)
            .addCallback(object: RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO WordsFts(WordsFts) VALUES ('rebuild')")
                }
            })
            .build()
    }


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