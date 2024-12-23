package com.touchgfx.mvvm.base.data

import android.content.Context
import androidx.room.RoomDatabase

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 17:59
 * @desc 统一管理数据业务层，实现类见[com.touchgfx.mvvm.base.data.DataRepository]
 */
interface IDataRepository {
    /**
     * 提供上下文[Context]
     * @return {@lik Context}
     */
    val context: Context?

    /**
     * 传入Class 通过[retrofit2.Retrofit.create] 获得对应的Class
     * @param service
     * @param <T>
     * @return [retrofit2.Retrofit.create]</T>
     */
    fun <T> getRetrofitService(service: Class<T>): T

    /**
     * 传入Class 通过[Room.databaseBuilder],[#build()][<]获得对应的Class
     * @param database
     * @param dbName
     * @param <T>
     * @return [#build()][<]</T>
     */
    fun <T : RoomDatabase> getRoomDatabase(database: Class<T>, dbName: String?): T
}