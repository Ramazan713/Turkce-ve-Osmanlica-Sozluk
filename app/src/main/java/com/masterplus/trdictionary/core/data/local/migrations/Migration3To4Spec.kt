package com.masterplus.trdictionary.core.data.local.migrations

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec


@DeleteColumn.Entries(
    DeleteColumn(
        tableName = "Histories",
        columnName = "wordId"
    )
)
class Migration3To4Spec: AutoMigrationSpec