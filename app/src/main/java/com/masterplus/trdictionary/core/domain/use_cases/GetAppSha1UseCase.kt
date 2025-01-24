package com.masterplus.trdictionary.core.domain.use_cases

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import com.google.common.io.BaseEncoding
import java.security.MessageDigest
import javax.inject.Inject

class GetAppSha1UseCase @Inject constructor(
    private val application: Application
) {

    operator fun invoke(): String?{
        val packageName = application.packageName
        val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            application.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            application.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        }
        val firstSignature = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.signingInfo?.apkContentsSigners?.firstOrNull()
        } else {
            info.signatures?.firstOrNull()
        }

        val md = MessageDigest.getInstance("SHA1")
        return firstSignature?.let { signature->
            val result = md.digest(signature.toByteArray())
            BaseEncoding.base16().lowerCase().encode(result)
        }
    }
}