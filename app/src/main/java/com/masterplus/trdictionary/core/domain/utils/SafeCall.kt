package com.masterplus.trdictionary.core.domain.utils

import com.masterplus.trdictionary.R
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> safeCall(crossinline execute: suspend () -> T): Resource<T>{
    try {
        val result = execute()
        return Resource.Success(result)
    }
    catch (e: CancellationException){
        throw e
    }
    catch (e: Exception){
        val error = e.localizedMessage?.let { UiText.Text(it) } ?: UiText.Resource(R.string.something_went_wrong)
        return Resource.Error(error)
    }
}