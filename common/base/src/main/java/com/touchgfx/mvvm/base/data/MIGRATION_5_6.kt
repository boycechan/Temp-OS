package com.touchgfx.mvvm.base.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6: Migration = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val createDialTableSql = "CREATE TABLE IF NOT EXISTS `DBStressBean` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `device_id` INTEGER NOT NULL, `year` INTEGER NOT NULL, `month` INTEGER NOT NULL, `day` INTEGER NOT NULL, `records` TEXT, `updateTag` INTEGER NOT NULL)"
        database.execSQL(createDialTableSql)
    }
}