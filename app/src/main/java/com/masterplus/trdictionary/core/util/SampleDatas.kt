package com.masterplus.trdictionary.core.util

import com.masterplus.trdictionary.core.domain.enums.AutoType
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.model.Meaning
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.domain.model.Word
import com.masterplus.trdictionary.features.list.domain.model.SelectableListView
import java.util.Calendar

object SampleDatas {

    fun generateWord(
        id: Int = 1,
        prefix: String? = null,
        word: String = "word $id",
        suffix: String? = null,
        countWord: Int = 1,
        dictType: DictType = DictType.TR,
        showTTS: Boolean = true,
        randomOrder: Int = 13,
        wordType: WordType = WordType.Proverb
    ): Word{
        return Word(id, prefix, word, suffix, countWord, dictType, showTTS, randomOrder, wordType)
    }

    fun generateMeaning(
        id: Int? = 1,
        wordId: Int = 1,
        orderItem: Int = 2,
        meaning: String = "meaning $id",
        feature: String? = null
    ): Meaning{
        return Meaning(id, wordId, orderItem, meaning, feature)
    }

    fun generateSimpleWordResult(
        wordId: Int = 1,
        word: Word = generateWord(id = wordId),
        meanings: List<Meaning> = listOf(generateMeaning(wordId = wordId), generateMeaning(id = 2, wordId = wordId))
    ): SimpleWordResult{
        return SimpleWordResult(
            word = word,
            meanings = meanings
        )
    }

    fun generateSavePoint(
        id: Int = 1,
        title: String = "Title $id",
        itemPosIndex: Int = 5,
        savePointDestination: SavePointDestination = SavePointDestination.List(1),
        modifiedDate: Calendar = Calendar.getInstance(),
        autoType: AutoType = AutoType.Auto
    ): SavePoint{
        return SavePoint(id, title, itemPosIndex, savePointDestination, modifiedDate, autoType)
    }

    fun generateListView(
        id: Int = 1,
        name: String = "list item $id",
        isRemovable: Boolean = false,
        isArchive: Boolean = false,
        listPos: Int = 1,
        contentMaxPos: Int = 1,
        itemCounts: Int = 2
    ): ListView{
        return ListView(id, name, isRemovable, isArchive, listPos, contentMaxPos, itemCounts)
    }

    fun generateSelectableListView(
        listView: ListView = generateListView(),
        isSelected: Boolean = false
    ): SelectableListView{
        return SelectableListView(listView, isSelected)
    }


    val selectableListViewArr = listOf(
        generateSelectableListView(generateListView(id = 1)),
        generateSelectableListView(generateListView(id = 2)),
        generateSelectableListView(generateListView(id = 3))
    )

    val savePoints = listOf(
        generateSavePoint(id = 1),
        generateSavePoint(id = 2),
        generateSavePoint(id = 3)
    )

}