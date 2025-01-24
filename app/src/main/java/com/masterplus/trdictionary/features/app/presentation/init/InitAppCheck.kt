package com.masterplus.trdictionary.features.app.presentation.init

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.masterplus.trdictionary.BuildConfig


fun initAppCheck(context: Context){
    val debugTypes = listOf("debug", "staging")
    if(debugTypes.contains(BuildConfig.BUILD_TYPE)){
        setAppCheckDebugKey(context)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
    }else{
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
    }
}


private fun setAppCheckDebugKey(context: Context){
    val persistenceKey = FirebaseApp.getPersistenceKey(
        FirebaseApp.DEFAULT_APP_NAME,
        FirebaseOptions.fromResource(context)
    )
    val prefsName = String.format("com.google.firebase.appcheck.debug.store.%s", persistenceKey)
    val sharedPreferences = context.getSharedPreferences(prefsName, MODE_PRIVATE)
    val secretKey = "com.google.firebase.appcheck.debug.DEBUG_SECRET"
    sharedPreferences.edit().putString(secretKey, BuildConfig.APP_CHECK_DEBUG_TOKEN).apply()
    FirebaseApp.initializeApp(context)
}
