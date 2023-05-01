package com.masterplus.trdictionary.features.settings.domain.di

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.masterplus.trdictionary.BuildConfig
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
    fun provideGoogleSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(BuildConfig.AUTH_CLIENT_ID)
            .build()

    @Provides
    @Singleton
    fun provideGoogleSignInClient(application: Application,gso: GoogleSignInOptions):
            GoogleSignInClient = GoogleSignIn.getClient(application, gso)




}