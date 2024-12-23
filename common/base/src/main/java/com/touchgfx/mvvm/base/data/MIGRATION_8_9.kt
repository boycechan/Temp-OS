package com.touchgfx.mvvm.base.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_8_9: Migration = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        var sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `relax` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `minHr` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `avgSpeed` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `maxSpeed` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `minSpeed` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `maxPace` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `minPace` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `pacesPerKm` TEXT "
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `num` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `frq` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `avgStrideFrequency` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "ALTER TABLE `DBSportRecordBean` ADD COLUMN `avgStrideLength` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)

        sql = "ALTER TABLE `DBStepsBean` ADD COLUMN `standNum` INTEGER NOT NULL DEFAULT 0"
        database.execSQL(sql)
        sql = "CREATE TABLE IF NOT EXISTS `DBWomenHealth` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `menstrualStartDate` TEXT, `menstrualEndDate` TEXT, `ovulationDay` TEXT, `cycle` INTEGER , `duration` INTEGER)"
        database.execSQL(sql)
    }
}