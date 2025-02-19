plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.jetbrains.kotlin.android).apply(false)
    alias(libs.plugins.google.devtools.ksp).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.google.google.services).apply(false)
    alias(libs.plugins.hilt).apply(false)
    alias(libs.plugins.firebase.crashlytics.gradle).apply(false)
    alias(libs.plugins.firebase.perf.plugin).apply(false)
}
