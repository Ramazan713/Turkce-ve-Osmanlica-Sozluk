package com.masterplus.trdictionary.core.data.local.migrations

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase

@RenameColumn.Entries(
    RenameColumn(
        tableName = "Words",
        fromColumnName = "dictType",
        toColumnName = "dictTypeId"
    )
)
class Migration1To2Spec: AutoMigrationSpec {

    override fun onPostMigrate(database: SupportSQLiteDatabase) {
        super.onPostMigrate(database)
        try {
            database.beginTransaction()
            database.execSQL("""update words set wordTypeId  = 2 where id in (select wordId from ProverbIdiomWords where type = 1)""")
            database.execSQL("""update words set wordTypeId  = 3 where id in (select wordId from ProverbIdiomWords where type = 2)""")
            database.execSQL("""update words set wordTypeId  = 4 where id in (select wordId from ProverbIdiomWords where type = 3)""")
            database.execSQL("""update words set wordTypeId  = 5 where id in (select wordId from ProverbIdiomWords where type = 4)""")
            database.setTransactionSuccessful()
        }finally {
            database.endTransaction()
        }
    }
}