package com.masterplus.trdictionary.core.domain.constants

object K {
    const val backupMetaRefreshMilliSeconds = 60 * 1000
    const val backupMetaSizeInDb = 3
    const val maxDownloadSizeBytes = 1024*1024*8L

    const val wordsDetailPagerPageSize = 75//5
    const val wordsDetailPagerJumpThreshold = 200//10

    const val wordListPageSize = 50
    const val wordListJumpThreshold = 100

    const val searchDelayMilliSeconds = 300L


    const val defaultCategoryAlphaChar = "*"

    object ReviewApi{
        const val reviewApiDestinationThreshold = 5
    }

    object Ad{
        const val thresholdOpeningCount = 19

        const val consumeIntervalSeconds = 5
        const val thresholdConsumeSeconds = 120
    }

    object DeepLink{
        const val baseUrl = "https://www.tr-osm-dictionary.masterplus.com"
        const val categoryBaseUrl = "$baseUrl/category"
        const val categoryDetailBaseUrl = "$baseUrl/categoryDetail"
        const val singleWordBaseUrl = "$baseUrl/detail"

        const val numberZerosLength = 6
    }
}