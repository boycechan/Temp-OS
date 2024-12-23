package com.touchgfx.mvvm.base.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_7: Migration = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val createDialTableSql = "CREATE TABLE IF NOT EXISTS `DBDeviceBean` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `deviceId` INTEGER NOT NULL, `name` TEXT, `mac` TEXT NOT NULL, `deviceType` TEXT, `sdkType` TEXT, `sn` TEXT, `mode` TEXT, `config` TEXT, `imgUrl` TEXT)"
        database.execSQL(createDialTableSql)
    }
}