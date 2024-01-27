package com.masterplus.trdictionary.core.di

import com.google.firebase.storage.FirebaseStorage
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.TransactionProvider
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.BackupMetaImpl
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.LocalBackupRepoImpl
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager.BackupManagerImpl
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.StorageServiceImpl
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.BackupParserRepoImpl
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.BackupMetaRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.BackupParserRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.StorageServiceFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.LocalBackupRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [
        SingletonComponent::class
    ],
    replaces = [
        BackupModule::class
    ]
)
object BackupModuleTest {

    @Provides
    @Singleton
    fun provideStorageService(): StorageService = StorageServiceFake()

    @Provides
    @Singleton
    fun provideBackupMetaRepo(db: AppDatabase): BackupMetaRepo =
        BackupMetaImpl(db.backupMetaDao())

    @Provides
    @Singleton
    fun provideBackupParser(): BackupParserRepo =
        BackupParserRepoImpl()


    @Provides
    @Singleton
    fun provideLocalBackupRepo(
        db: AppDatabase,
        transactionProvider: TransactionProvider,
        backupParserRepo: BackupParserRepo,
        settingsPreferences: SettingsPreferencesApp
    ): LocalBackupRepo =
        LocalBackupRepoImpl(
            backupDao = db.localBackupDao(),
            transactionProvider = transactionProvider,
            backupParserRepo = backupParserRepo,
            settingsPreferences = settingsPreferences
        )

    @Provides
    @Singleton
    fun provideBackupManager(localBackupRepo: LocalBackupRepo,
                             backupMetaRepo: BackupMetaRepo,
                             storageService: StorageService,
                             connectivityProvider: ConnectivityProvider
    ): BackupManager =
        BackupManagerImpl(
            localBackupRepo = localBackupRepo,
            backupMetaRepo = backupMetaRepo,
            storageService = storageService,
            connectivityProvider = connectivityProvider
        )
}