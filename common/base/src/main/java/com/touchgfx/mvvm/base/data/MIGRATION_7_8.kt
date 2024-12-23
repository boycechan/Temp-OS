package com.touchgfx.mvvm.base.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_7_8: Migration = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        var createDialTableSql = "ALTER TABLE `DBDeviceBean` ADD COLUMN `version` TEXT"
        database.execSQL(createDialTableSql)
        createDialTableSql = "ALTER TABLE `DBDeviceBean` ADD COLUMN `globalConfig` TEXT"
        database.execSQL(createDialTableSql)
    }
}