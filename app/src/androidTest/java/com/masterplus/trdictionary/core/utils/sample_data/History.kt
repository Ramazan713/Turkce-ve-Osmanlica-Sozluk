package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.features.search.domain.model.History
import java.util.Calendar

fun history(
    id: Int = 1,
    content: String = "content $id",
    timeStamp: Long = Calendar.getInstance().timeInMillis
): History {
    return History(
        id, content, timeStamp
    )
}