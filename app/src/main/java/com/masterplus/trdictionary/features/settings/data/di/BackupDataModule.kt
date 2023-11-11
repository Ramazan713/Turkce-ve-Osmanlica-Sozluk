package com.masterplus.trdictionary.features.settings.data.di

import com.google.firebase.storage.FirebaseStorage
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.data.local.TransactionProvider
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.features.settings.data.local.repo.BackupMetaImpl
import com.masterplus.trdictionary.features.settings.data.local.repo.LocalBackupRepoImpl
import com.masterplus.trdictionary.features.settings.data.manager.BackupManagerImpl
import com.masterplus.trdictionary.features.settings.data.repo.StorageServiceImpl
import com.masterplus.trdictionary.core.domain.JsonParser
import com.masterplus.trdictionary.features.settings.domain.manager.BackupManager
import com.masterplus.trdictionary.features.settings.domain.repo.BackupMetaRepo
import com.masterplus.trdictionary.features.settings.domain.repo.LocalBackupRepo
import com.masterplus.trdictionary.features.settings.domain.repo.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupDataModule {

    @Provides
    @Singleton
    fun provideStorageService(): StorageService =
        StorageServiceImpl(
            storage = FirebaseStorage.getInstance()
        )

    @Provides
    @Singleton
    fun provideBackupMetaRepo(db: AppDatabase): BackupMetaRepo =
        BackupMetaImpl(db.backupMetaDao())

    @Provides
    @Singleton
    fun provideLocalBackupRepo(db: AppDatabase,
                               jsonParser: JsonParser,
                               appPreferences: AppPreferences,
                               transactionProvider: TransactionProvider
    ): LocalBackupRepo =
        LocalBackupRepoImpl(
            backupDao = db.localBackupDao(),
            jsonParser = jsonParser,
            appPreferences = appPreferences,
            transactionProvider = transactionProvider
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