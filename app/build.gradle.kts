import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.firebase.crashlytics.gradle)
    alias(libs.plugins.firebase.perf.plugin)
    alias(libs.plugins.android.junit5)
    id("kotlin-parcelize")
    alias(libs.plugins.room)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.masterplus.trdictionary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.masterplus.trdictionary"
        minSdk = 24
        targetSdk = 34
        versionCode = 6
        versionName = "1.1.0"

        testInstrumentationRunner = "com.masterplus.trdictionary.shared_test.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["adMobAppId"] = "${keystoreProperties["adMobAppId"]}"
        buildConfigField("String","TTS_CLIENT_ID","\"${keystoreProperties["ttsClientId"]}\"")
        buildConfigField("String","TTS_KEY","\"${keystoreProperties["ttsKey"]}\"")
        buildConfigField("String","INTERSTITIAL_AD_ID","\"${keystoreProperties["interstitialTestAdId"]}\"")
    }
    signingConfigs {

        create("release"){
            keyAlias = "${keystoreProperties["releaseKeyAlias"]}"
            keyPassword = "${keystoreProperties["releaseKeyPassword"]}"
            storeFile = file(keystoreProperties["releaseStoreFile"]!!)
            storePassword = "${keystoreProperties["releaseStorePassword"]}"
        }

        create("staging"){
            keyAlias = "${keystoreProperties["stagingKeyAlias"]}"
            keyPassword = "${keystoreProperties["stagingKeyPassword"]}"
            storeFile = file(keystoreProperties["stagingStoreFile"]!!)
            storePassword = "${keystoreProperties["stagingStorePassword"]}"
        }
    }

    buildTypes {
        release {
            manifestPlaceholders += mapOf("appNameSuffix" to "")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")

            buildConfigField("String","AUTH_CLIENT_ID","\"${keystoreProperties["releaseAuthClientId"]}\"")
            buildConfigField("String","INTERSTITIAL_AD_ID","\"${keystoreProperties["interstitialAdId"]}\"")
        }

        debug {
            manifestPlaceholders += mapOf("appNameSuffix" to "_debug")
            applicationIdSuffix = ".debug"
            buildConfigField("String","AUTH_CLIENT_ID","\"${keystoreProperties["debugAuthClientId"]}\"")
        }

        create("staging"){
            manifestPlaceholders += mapOf("appNameSuffix" to "_staging")
            signingConfig = signingConfigs.getByName("staging")
            applicationIdSuffix = ".staging"
            buildConfigField("String","AUTH_CLIENT_ID","\"${keystoreProperties["stagingAuthClientId"]}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.assertk)
    androidTestImplementation(libs.mockk.android)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)

    testImplementation(libs.junit)
    testImplementation(libs.assertk)
    testImplementation(libs.mockk)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)


    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)

    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)

    implementation(libs.bundles.lifecycle)
    ksp(libs.lifecycle.compiler)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.gson)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.coil.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.bundles.media3.exoplayer)
    implementation(libs.bundles.glance)
    implementation(libs.bundles.paging)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.credentials)

    implementation(libs.play.services.ads)
    implementation(libs.billing.ktx)
    implementation(libs.bundles.play.review)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.accompanist.adaptive)
    implementation(libs.androidx.material3.window.size)

    implementation(libs.kotlinx.serialization.json)
}