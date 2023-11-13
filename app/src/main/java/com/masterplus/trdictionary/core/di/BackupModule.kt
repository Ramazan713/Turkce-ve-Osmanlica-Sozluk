package com.masterplus.trdictionary.core.di

import com.google.firebase.storage.FirebaseStorage
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.data.local.TransactionProvider
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.BackupMetaImpl
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.LocalBackupRepoImpl
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager.BackupManagerImpl
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.StorageServiceImpl
import com.masterplus.trdictionary.core.domain.JsonParser
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.BackupMetaRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.LocalBackupRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupModule {

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