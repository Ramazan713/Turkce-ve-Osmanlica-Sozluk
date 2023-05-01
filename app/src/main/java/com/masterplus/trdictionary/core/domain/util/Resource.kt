package com.masterplus.trdictionary.core.domain.util

sealed class Resource<T>{

    data class Success<T>(val data: T): Resource<T>()

    data class Error<T>(val error: UiText, val data: T? = null): Resource<T>()
}
