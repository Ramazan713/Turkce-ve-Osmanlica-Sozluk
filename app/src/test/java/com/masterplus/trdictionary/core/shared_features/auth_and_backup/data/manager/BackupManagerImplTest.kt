package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.data.ConnectivityProviderFake
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.BackupMetaRepoFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.LocalBackupRepoFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.StorageServiceFake
import com.masterplus.trdictionary.core.utils.sample_data.backupMeta
import com.masterplus.trdictionary.core.utils.sample_data.user
import io.mockk.coVerify
import io.mockk.coVerifyAll
import io.mockk.coVerifyOrder
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BackupManagerImplTest {

    private lateinit var localBackupRepo: LocalBackupRepoFake
    private lateinit var storageService: StorageServiceFake
    private lateinit var backupMetaRepo: BackupMetaRepoFake
    private lateinit var connectivityProvider: ConnectivityProviderFake

    private lateinit var backupManager: BackupManagerImpl

    private val errorText = UiText.Text("Error")

    @BeforeEach
    fun setUp() {
        localBackupRepo = LocalBackupRepoFake()
        storageService = StorageServiceFake()
        backupMetaRepo = BackupMetaRepoFake()
        connectivityProvider = ConnectivityProviderFake()

        backupManager = BackupManagerImpl(localBackupRepo, storageService, backupMetaRepo, connectivityProvider)

    }

    @Test
    fun uploadBackup_IfNotInternetConnection_returnsError() = runTest {
        connectivityProvider.returnedHasConnection = false

        val response = backupManager.uploadBackup(user())

        assertThat(response is Resource.Error).isTrue()
        assertThat((response as Resource.Error).error).isEqualTo(UiText.Resource(R.string.check_your_internet_connection))
    }

    @Test
    fun uploadBackup_IfLocalBackupJsonNull_returnsError() = runTest {
        localBackupRepo.returnedJsonData = null
        val response = backupManager.uploadBackup(user())
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun uploadBackup_IfUploadDataSuccess_insertBackupMeta() = runTest {
        backupMetaRepo.items.test {
            val firstBackupMetaItems = awaitItem()
            val response = backupManager.uploadBackup(user())
            val lastBackupMetaItems = awaitItem()

            assertThat(response is Resource.Success).isTrue()
            assertThat(firstBackupMetaItems).isEmpty()
            assertThat(lastBackupMetaItems.size).isEqualTo(1)
        }
    }

    @Test
    fun uploadBackup_IfUploadDataFailed_returnsError() = runTest {
        storageService.uploadDataResponse = Resource.Error(errorText)
        val response = backupManager.uploadBackup(user())
        assertThat(response is Resource.Error).isTrue()
    }


    @Test
    fun downloadBackup_IfNotInternetConnection_returnsError() = runTest {
        connectivityProvider.returnedHasConnection = false
        val response = backupManager.downloadBackup(user(),"",false,true)
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun downloadBackup_IfGetFileDataFailed_returnsError() = runTest {
        storageService.getFileDataResponse = Resource.Error(errorText)
        val response = backupManager.downloadBackup(user(),"",false,true)
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun downloadBackup_IfGetFileDataSuccess_shouldFromJsonDataCalled() = runTest {
        mockkObject(localBackupRepo)

        val response = backupManager.downloadBackup(user(),"myFile", removeAllData = true, addOnLocalData = true)

        assertThat(response is Resource.Success).isTrue()
        coVerify {
            localBackupRepo.fromJsonData(any(),removeAllData = true, addOnLocalData = true)
        }
    }



    @Test
    fun refreshBackupMetas_IfNotInternetConnection_returnsError() = runTest {
        connectivityProvider.returnedHasConnection = false
        val response = backupManager.refreshBackupMetas(user())
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun refreshBackupMetas_IfGetFilesFailed_returnsError() = runTest {
        storageService.getFilesResponse = Resource.Error(errorText)
        val response = backupManager.refreshBackupMetas(user())
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun refreshBackupMetas_IfGetFilesSuccess_refreshLocalBackupMetas() = runTest {
        backupMetaRepo.items.test {
            awaitItem()
            backupMetaRepo.insertBackupMetas(listOf(backupMeta(), backupMeta()))
            val firstItems = awaitItem()

            val response = backupManager.refreshBackupMetas(user())
            awaitItem()
            val lastItems = awaitItem()

            assertThat(response is Resource.Success).isTrue()
            assertThat(firstItems.size).isEqualTo(2)
            assertThat(lastItems.size).isEqualTo(1)
        }
    }

    @Test
    fun refreshBackupMetas_IfGetFilesSuccess_LocalBackupMetaRepoCalled() = runTest {
        mockkObject(backupMetaRepo)
        backupMetaRepo.insertBackupMetas(listOf(backupMeta(), backupMeta()))

        backupManager.refreshBackupMetas(user())

        coVerifyAll {
            backupMetaRepo.deleteBackupMetas()
            backupMetaRepo.insertBackupMetas(any())
        }
    }

    @Test
    fun deleteAllLocalUserData_IfDeleteBackupMetaIsFalse_deleteOnlyUserData() = runTest {
        mockkObject(localBackupRepo)
        mockkObject(backupMetaRepo)

        backupManager.deleteAllLocalUserData(false)

        coVerify {
            localBackupRepo.deleteAllLocalUserData()
        }
        coVerify(exactly = 0) {
            backupMetaRepo.deleteBackupMetas()
        }
    }

    @Test
    fun deleteAllLocalUserData_IfDeleteBackupMetaIsTrue_deleteAllData() = runTest {
        mockkObject(localBackupRepo)
        mockkObject(backupMetaRepo)

        backupManager.deleteAllLocalUserData(true)

        coVerifyOrder {
            backupMetaRepo.deleteBackupMetas()
            localBackupRepo.deleteAllLocalUserData()
        }
    }


    @Test
    fun downloadLastBackup_IfNotInternetConnection_returnsError() = runTest {
        connectivityProvider.returnedHasConnection = false
        val response = backupManager.downloadLastBackup(user())
        assertThat(response is Resource.Error).isTrue()
    }


    @Test
    fun downloadLastBackup_IfBackupNotExists_returnsError() = runTest {
        val response = backupManager.downloadLastBackup(user())
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun downloadLastBackup_IfBackupExists_callDownloadBackupFunc() = runTest {
        mockkObject(backupManager)
        val fileName = "fileName"
        val user = user()

        backupMetaRepo.insertBackupMeta(backupMeta(fileName = fileName))
        val response = backupManager.downloadLastBackup(user)

        assertThat(response is Resource.Success).isTrue()

        coVerify {
            backupManager.downloadBackup(
                user = user,
                fileName = fileName,
                removeAllData = true,
                addOnLocalData = false
            )
        }
    }

    @Test
    fun hasBackupMetas_IfBackupMetaExists_returnsTrue() = runTest {
        backupMetaRepo.insertBackupMeta(backupMeta())

        val result = backupManager.hasBackupMetas()

        assertThat(result).isTrue()
    }


}