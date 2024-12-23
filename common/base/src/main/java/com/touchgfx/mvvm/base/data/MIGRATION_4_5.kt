package com.touchgfx.mvvm.base.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5: Migration = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val createDialTableSql = "CREATE TABLE IF NOT EXISTS DBDialBean (`state` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `deviceType` TEXT NOT NULL, `dialId` INTEGER NOT NULL, `name` TEXT NOT NULL, `enName` TEXT NOT NULL, `type` INTEGER NOT NULL, `coverUrl` TEXT NOT NULL, `downloadUrl` TEXT NOT NULL, `size` INTEGER NOT NULL, `desc` TEXT, `enDesc` TEXT)"
        database.execSQL(createDialTableSql)
    }
}