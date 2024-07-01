package com.masterplus.trdictionary.core.domain.utils

sealed class Resource<T>{

    data class Success<T>(val data: T): Resource<T>()

    data class Error<T>(val error: UiText, val data: T? = null): Resource<T>()



    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val getSuccessData: T?
        get() = if (this is Success) this.data else null

    val getError: UiText?
        get() = if (this is Error) this.error else null

}

inline fun <T, R> Resource<T>.map(map: (T) -> R): Resource<R>{
    return when(this){
        is Resource.Error -> Resource.Error(error)
        is Resource.Success -> Resource.Success(map(data))
    }
}

fun <T> Resource<T>.asEmptyResult(): EmptyResult{
    return map {  }
}

typealias EmptyResult = Resource<Unit>
