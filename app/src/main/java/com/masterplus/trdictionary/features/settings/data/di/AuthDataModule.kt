package com.masterplus.trdictionary.features.settings.data.di

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.masterplus.trdictionary.features.settings.data.manager.AuthManagerImpl
import com.masterplus.trdictionary.features.settings.data.repo.FirebaseAuthRepo
import com.masterplus.trdictionary.features.settings.domain.manager.AuthManager
import com.masterplus.trdictionary.features.settings.domain.manager.BackupManager
import com.masterplus.trdictionary.features.settings.domain.repo.AuthRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDataModule {

    @Provides
    @Singleton
    fun provideAuthRepo(googleSignInClient: GoogleSignInClient): AuthRepo =
        FirebaseAuthRepo(
            googleSignInClient = googleSignInClient,
            firebaseAuth = FirebaseAuth.getInstance()
        )

    @Provides
    @Singleton
    fun provideAuthManager(authRepo: AuthRepo,
                           backupManager: BackupManager
    ): AuthManager =
        AuthManagerImpl(authRepo, backupManager)
}