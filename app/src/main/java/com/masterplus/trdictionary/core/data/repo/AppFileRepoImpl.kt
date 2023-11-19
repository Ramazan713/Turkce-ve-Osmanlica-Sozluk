package com.masterplus.trdictionary.core.data.repo

import android.app.Application
import android.util.Log
import com.masterplus.trdictionary.core.domain.DispatcherProvider
import com.masterplus.trdictionary.core.domain.repo.AppFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.use
import java.io.File
import javax.inject.Inject

class AppFileRepoImpl @Inject constructor(
    private val application: Application,
    private val dispatcherProvider: DispatcherProvider
): AppFileRepo {

    private val filesDir = application.filesDir

    override suspend fun insertFile(fileName: String, data: ByteArray): File {
        return withContext(dispatcherProvider.io){
            val file = File(filesDir,fileName)

            file.parentFile?.let { parentFile->
                if(!parentFile.exists()){
                    parentFile.mkdirs()
                }
            }

            file.outputStream().use { outputStream->
                outputStream.write(data)
            }
            return@withContext file
        }
    }

    override suspend fun deleteFile(fileName: String) {
        withContext(dispatcherProvider.io){
            File(filesDir,fileName).let { file->
                if(file.exists()){
                    file.delete()
                }
            }
        }
    }

    override suspend fun getFile(fileName: String): File? {
        return withContext(dispatcherProvider.io){
            val file = File(filesDir,fileName)
            if(file.exists()) return@withContext file
            return@withContext null
        }
    }

    override suspend fun getFileData(fileName: String): ByteArray? {
        return withContext(dispatcherProvider.io){
            getFile(fileName)?.readBytes()
        }
    }
}