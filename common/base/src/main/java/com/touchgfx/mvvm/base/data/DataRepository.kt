package com.touchgfx.mvvm.base.data

import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.collection.LruCache
import androidx.room.Room
import androidx.room.RoomDatabase
import com.touchgfx.mvvm.base.config.AppliesOptions.RoomDatabaseOptions
import com.touchgfx.mvvm.base.config.Constants
import dagger.Lazy
import dagger.hilt.internal.Preconditions
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 17:59
 * @desc统一管理数据业务层
 */
@Singleton
class DataRepository @Inject constructor() : IDataRepository {
    @JvmField
    @Inject
    var mRetrofit: Lazy<Retrofit>? = null

    @JvmField
    @Inject
    var mApplication: Application? = null

    @JvmField
    @Inject
    var mRoomDatabaseOptions: RoomDatabaseOptions<RoomDatabase>? = null

    /**
     * 缓存 Retrofit Service
     */
    private var mRetrofitServiceCache: LruCache<String, Any?>? = null

    /**
     * 缓存 RoomDatabase
     */
    private var mRoomDatabaseCache: LruCache<String?, Any?>? = null

    /**
     * 提供上下文[Context]
     * @return [.mApplication]
     */
    override val context: Context?
        get() = mApplication

    /**
     * 传入Class 通过[Retrofit.create] 获得对应的Class
     * @param service
     * @param <T>
     * @return [Retrofit.create]
    </T> */
    override fun <T> getRetrofitService(service: Class<T>): T {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = LruCache(Constants.DEFAULT_RETROFIT_SERVICE_MAX_SIZE)
        }
        Preconditions.checkNotNull<LruCache<String, Any?>>(mRetrofitServiceCache)
        var retrofitService = mRetrofitServiceCache!![service.canonicalName] as T
        if (retrofitService == null) {
            synchronized(mRetrofitServiceCache!!) {
                if (retrofitService == null) {
                    retrofitService = mRetrofit!!.get().create(service)
                    //缓存
                    mRetrofitServiceCache!!.put(service.canonicalName, retrofitService!!)
                }
            }
        }
        return retrofitService
    }

    /**
     * 传入Class 通过[Room.databaseBuilder],[#build()][<]获得对应的Class
     * @param database
     * @param dbName 为`null`时，默认为[Constants.DEFAULT_DATABASE_NAME]
     * @param <T>
     * @return [#build()][<]
    </T> */
    override fun <T : RoomDatabase> getRoomDatabase(database: Class<T>, dbName: String?): T {
        if (mRoomDatabaseCache == null) {
            mRoomDatabaseCache = LruCache(Constants.DEFAULT_ROOM_DATABASE_MAX_SIZE)
        }
        Preconditions.checkNotNull<LruCache<String?, Any?>>(mRoomDatabaseCache)
        var roomDatabase = mRoomDatabaseCache!![database.canonicalName] as T?
        if (roomDatabase == null) {
            synchronized(mRoomDatabaseCache!!) {
                if (roomDatabase == null) {
                    val builder = Room.databaseBuilder(context!!.applicationContext,
                            database,
                            if (TextUtils.isEmpty(dbName)) Constants.DEFAULT_DATABASE_NAME else dbName!!
                    )
                    if (mRoomDatabaseOptions != null) {
                        mRoomDatabaseOptions!!.applyOptions(builder as RoomDatabase.Builder<RoomDatabase>)
                    }
                    builder.addMigrations(MIGRATION_4_5)
                    builder.addMigrations(MIGRATION_5_6)
                    builder.addMigrations(MIGRATION_6_7)
                    builder.addMigrations(MIGRATION_7_8)
                    builder.addMigrations(MIGRATION_8_9)
                    roomDatabase = builder.build()
                    //缓存
                    mRoomDatabaseCache!!.put(database.canonicalName, roomDatabase!!)
                }
            }
        }
        return roomDatabase!!
    }
}