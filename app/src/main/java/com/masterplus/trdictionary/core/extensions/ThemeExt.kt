package com.masterplus.trdictionary.core.extensions

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.masterplus.trdictionary.core.domain.model.ThemeModel


@Composable
fun ThemeModel.getThemeScheme(
    darkColorScheme: ColorScheme,
    lightColorScheme: ColorScheme,
    darkTheme: Boolean = isSystemInDarkTheme()
): ColorScheme {
    if(themeEnum.hasDarkTheme(darkTheme)){
        return getDarkThemeScheme(darkColorScheme = darkColorScheme)
    }
    return getLightThemeScheme(lightColorScheme = lightColorScheme)
}


@Composable
fun ThemeModel.getLightThemeScheme(
    lightColorScheme: ColorScheme,
): ColorScheme {
    val context = LocalContext.current
    return if(useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        dynamicLightColorScheme(context)
    }else{
        lightColorScheme
    }
}

@Composable
fun ThemeModel.getDarkThemeScheme(
    darkColorScheme: ColorScheme,
): ColorScheme {
    val context = LocalContext.current
    return if(useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        dynamicDarkColorScheme(context)
    }else{
        darkColorScheme
    }
}