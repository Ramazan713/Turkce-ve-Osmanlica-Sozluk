package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.model.ListWords


fun listWords(
    listId: Int = 1,
    wordId: Int = 1,
    pos: Int = 1
): ListWords{
    return ListWords(
        listId, wordId, pos
    )
}