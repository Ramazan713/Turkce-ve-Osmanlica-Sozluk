package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.model.Meaning


fun meaning(
    id: Int? = 1,
    wordId: Int = 1,
    orderItem: Int = 1,
    meaning: String = "meaning $id",
    feature: String? = null
): Meaning{
    return Meaning(id, wordId, orderItem, meaning, feature)
}