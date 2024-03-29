import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
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




private val roomVersion = "2.6.0"
private val navVersion = "2.7.5"
private val billingVersion = "6.0.1"

dependencies {
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-alpha10")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.navigation:navigation-testing:2.7.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:hilt-compiler:2.48.1")
    testImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    kspTest("com.google.dagger:hilt-android-compiler:2.48.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")


    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")

    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")


    implementation("androidx.navigation:navigation-compose:$navVersion")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-perf")

    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.media3:media3-exoplayer:1.1.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.1.1")
    implementation("androidx.media3:media3-ui:1.1.1")

    implementation("com.google.android.gms:play-services-ads:22.5.0")
    implementation("com.android.billingclient:billing-ktx:$billingVersion")

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")

    implementation("com.google.accompanist:accompanist-adaptive:0.27.0")
    implementation("androidx.compose.material3:material3-window-size-class")

    implementation("androidx.glance:glance-appwidget:1.0.0")
    implementation("androidx.glance:glance-material3:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.datastore:datastore-core-android:1.1.0-alpha06")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")

    testImplementation("com.willowtreeapps.assertk:assertk:0.26.1")
    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("app.cash.turbine:turbine:0.7.0")
    androidTestImplementation("com.willowtreeapps.assertk:assertk:0.26.1")
    androidTestImplementation("io.mockk:mockk-android:1.12.5")
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