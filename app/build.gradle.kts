import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.firebase.crashlytics.gradle)
    alias(libs.plugins.firebase.perf.plugin)
    alias(libs.plugins.android.junit5)
    id("kotlin-parcelize")
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
        versionCode = 5
        versionName = "1.0"

        testInstrumentationRunner = "com.masterplus.trdictionary.shared_test.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["adMobAppId"] = "${keystoreProperties["adMobAppId"]}"
        buildConfigField("String","TTS_CLIENT_ID","\"${keystoreProperties["ttsClientId"]}\"")
        buildConfigField("String","TTS_KEY","\"${keystoreProperties["ttsKey"]}\"")
        buildConfigField("String","INTERSTITIAL_AD_ID","\"${keystoreProperties["interstitialTestAdId"]}\"")


        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )

            }
        }
    }

    ksp {
        arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
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
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders += mapOf("appNameSuffix" to "")

            buildConfigField("String","AUTH_CLIENT_ID","\"${keystoreProperties["releaseAuthClientId"]}\"")
            buildConfigField("String","INTERSTITIAL_AD_ID","\"${keystoreProperties["interstitialAdId"]}\"")
        }

        debug {
            applicationIdSuffix = ".debug"
            manifestPlaceholders += mapOf("appNameSuffix" to "_debug")
            buildConfigField("String","AUTH_CLIENT_ID","\"${keystoreProperties["debugAuthClientId"]}\"")
        }

        create("staging"){
            signingConfig = signingConfigs.getByName("staging")
            applicationIdSuffix = ".staging"
            manifestPlaceholders += mapOf("appNameSuffix" to "_staging")
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.navigation.testing)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    kspAndroidTest(libs.dagger.hilt.android.compiler)


    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.room.testing)
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)


    implementation(libs.androidx.navigation.compose)
    implementation(libs.gson)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.perf)

    implementation(libs.coil.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    implementation(libs.play.services.ads)
    implementation(libs.billing.ktx)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.review)
    implementation(libs.review.ktx)

    implementation(libs.accompanist.adaptive)
    implementation(libs.androidx.material3.window.size)

    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.core.android)


    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)

    testImplementation(libs.assertk)
    testImplementation(libs.mockk)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.assertk)
    androidTestImplementation(libs.mockk.android)
}



class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> {
        return listOf(
            "room.schemaLocation=${schemaDir.path}",
            "room.incremental=true"
        )
    }
}