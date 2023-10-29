package com.masterplus.trdictionary.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.masterplus.trdictionary.core.data.local.entities.*
import com.masterplus.trdictionary.core.data.local.views.*
import com.masterplus.trdictionary.core.data.local.entities.HistoryEntity
import com.masterplus.trdictionary.core.data.local.entities.ListEntity
import com.masterplus.trdictionary.core.data.local.views.ListViewEntity
import com.masterplus.trdictionary.core.data.local.entities.ListWordsEntity
import com.masterplus.trdictionary.core.data.local.entities.SavePointEntity
import com.masterplus.trdictionary.core.data.local.services.*

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        AuthorEntity::class, CompoundWordsCrossRef::class, ExampleEntity::class,
        MeaningEntity::class, ProverbIdiomWordsCrossRef::class, WordEntity::class,
        HistoryEntity::class, ListWordsEntity::class, WordFtsEntity::class,
        ListEntity::class, SimilarWordsCrossRef::class, SavePointEntity::class,
        BackupMetaEntity::class
    ],
    views = [
        ExampleDetailsView::class, WordDetailView::class,
        ListViewEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    abstract fun searchDao(): SearchDao

    abstract fun wordDetailDao(): SingleWordDetailDao

    abstract fun wordListDao(): WordListDao

    abstract fun shortInfoDao(): ShortInfoDao

    abstract fun listDao(): ListDao

    abstract fun listViewDao(): ListViewDao

    abstract fun listWordsDao(): ListWordsDao

    abstract fun wordListDetailDao(): WordListDetailDao

    abstract fun savePointDao(): SavePointDao

    abstract fun backupMetaDao(): BackupMetaDao

    abstract fun localBackupDao(): LocalBackupDao

}