package com.masterplus.trdictionary.core.data.local.migrations

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn.Entries(
    DeleteColumn(
        tableName = "ProverbIdiomWords",
        columnName = "type"
    )
)
class Migration2To3Spec: AutoMigrationSpec