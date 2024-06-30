package com.masterplus.trdictionary.core.di

import com.google.firebase.auth.FirebaseAuth
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager.AuthManagerImpl
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.FirebaseAuthRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.AuthManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.use_cases.ValidateEmailUseCase
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.use_cases.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepo(): AuthRepo =
        FirebaseAuthRepo(
            firebaseAuth = FirebaseAuth.getInstance()
        )

    @Provides
    @Singleton
    fun provideAuthManager(authRepo: AuthRepo,
                           backupManager: BackupManager
    ): AuthManager =
        AuthManagerImpl(authRepo, backupManager)


    @Provides
    fun provideValidateEmailUseCase() = ValidateEmailUseCase()

    @Provides
    fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()

}