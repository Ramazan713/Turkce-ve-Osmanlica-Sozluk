package com.masterplus.trdictionary.core.domain

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

abstract class BaseAppSerializer<T>: Serializer<T> {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    protected abstract val serializer: KSerializer<T>


    override suspend fun readFrom(input: InputStream): T {
        try {
            return withContext(Dispatchers.IO){
                json.decodeFromString(serializer,input.readBytes().decodeToString())
            }
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                json.encodeToString(serializer, t).encodeToByteArray()
            )
        }
    }
}