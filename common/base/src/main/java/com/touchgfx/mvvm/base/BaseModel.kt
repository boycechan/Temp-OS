package com.touchgfx.mvvm.base

import androidx.room.RoomDatabase
import com.touchgfx.mvvm.base.config.Constants
import com.touchgfx.mvvm.base.data.IDataRepository
import javax.inject.Inject

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 16:43
 * @desc
 * 如果您继承使用了BaseModel或其子类，你需要参照如下方式在构造函数上添加@Inject注解
 *
 * @example BaseModel
 * //-------------------------
 *    public class YourModel extends BaseModel {
 *        @Inject
 *        public YourModel(IDataRepository dataRepository){
 *            super(dataRepository);
 *        }
 *    }
 * //-------------------------
 *
 *
 * 标准MVVM模式中的M (Model)层基类
 */
open class BaseModel @Inject constructor(private var dataRepository: IDataRepository) : IModel {

    override fun onDestroy() {

    }

    /**
     * 传入Class 获得[retrofit2.Retrofit.create] 对应的Class
     * @param service
     * @param <T>
     * @return [retrofit2.Retrofit.create]
    </T> */
    fun <T> getRetrofitService(service: Class<T>): T {
        return dataRepository.getRetrofitService(service)
    }


    /**
     * 传入Class 通过[Room.databaseBuilder],[#build()][<]获得对应的Class
     * @param database
     * @param <T>
     * @return [#build()][<]</T>
     */
    fun <T : RoomDatabase> getRoomDatabase(database: Class<T>): T {
        return getRoomDatabase(database, Constants.DEFAULT_DATABASE_NAME)
    }

    /**
     * 传入Class 通过[Room.databaseBuilder],[#build()][<]获得对应的Class
     * @param database
     * @param dbName
     * @param <T>
     * @return [#build()][<]</T>
     */
    fun <T : RoomDatabase> getRoomDatabase(database: Class<T>, dbName: String?): T {
        return dataRepository.getRoomDatabase(database, dbName)
    }
}