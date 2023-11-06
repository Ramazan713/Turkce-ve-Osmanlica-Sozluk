package com.masterplus.trdictionary.core.domain.use_cases

import com.masterplus.trdictionary.core.domain.repo.ListRepo
import javax.inject.Inject

class ListInFavoriteControlForDeletionUseCase @Inject constructor(
    private val listRepo: ListRepo
) {
    suspend operator fun invoke(listId: Int?, isInFavorite: Boolean?): Boolean{
        if(isInFavorite == false) return false
        if(listId == null) return false
        return listRepo.isFavoriteList(listId)
    }
}