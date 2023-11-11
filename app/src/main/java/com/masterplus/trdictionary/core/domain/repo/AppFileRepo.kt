package com.masterplus.trdictionary.core.domain.repo

import java.io.File

interface AppFileRepo {

    suspend fun insertFile(fileName: String, data: ByteArray): File

    suspend fun deleteFile(fileName: String)

    suspend fun getFile(fileName: String): File?

    suspend fun getFileData(fileName: String): ByteArray?
}