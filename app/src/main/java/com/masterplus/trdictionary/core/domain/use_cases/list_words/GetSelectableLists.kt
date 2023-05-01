package com.masterplus.trdictionary.core.domain.use_cases.list_words

import com.masterplus.trdictionary.features.list.domain.model.SelectableListView
import com.masterplus.trdictionary.core.domain.repo.ListViewRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSelectableLists @Inject constructor(
    private val listViewRepo: ListViewRepo
) {
    operator fun invoke(useArchiveAsList: Boolean?, wordId: Int): Flow<List<SelectableListView>>{
        val removableLists = listViewRepo.getRemovableListViews(
            if(useArchiveAsList==true)null else false,
        )
        val selectedLists = listViewRepo.getListViewsByWordId(wordId)

        return combine(removableLists,selectedLists){lists,selecteds->
            lists.map { item->
                val isSelected = selecteds.contains(item)
                SelectableListView(item,isSelected)
            }
        }
    }

}