package com.touchgfx.mvvm.base

import android.app.Application
import androidx.room.RoomDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/20 15:00
 * @desc 如果您继承使用了DataViewModel或其子类，你需要参照如下方式在构造函数上添加@Inject注解
 *
 * @example DataViewModel
 * //-------------------------
 *    @HiltViewModel
 *    public class YourViewModel extends DataViewModel {
 *        @Inject
 *        public DataViewModel(@NonNull Application application, BaseModel model) {
 *            super(application, model);
 *        }
 *    }
 * //-------------------------
 *
 *
 * 默认提供{@link BaseModel#getRetrofitService}的功能，当ViewModel和Model数据比较简单时可使用本类，弱化Model层。
 * 如果ViewModel或Model层里面逻辑比较复杂请尽量使用继承{@link BaseViewModel} 和{@link BaseModel}进行分层。
 */
@HiltViewModel
open class DataViewModel @Inject constructor(application: Application, model: BaseModel) :
    BaseViewModel<BaseModel>(application, model) {
    /**
     * 传入Class 获得[retrofit2.Retrofit.create] 对应的Class
     * @param service
     * @param <T>
     * @return [retrofit2.Retrofit.create]
     */
    fun <T> getRetrofitService(service: Class<T>?): T? {
        return service?.let { getModel()?.getRetrofitService(it) }
    }

    /**
     * 传入Class 通过[Room.databaseBuilder],[#build()][<]获得对应的Class
     * @param database
     * @param <T>
     * @return [#build()]
     */
    fun <T : RoomDatabase> getRoomDatabase(database: Class<T>): T? {
        return getRoomDatabase(database, null)
    }

    /**
     * 传入Class 通过[Room.databaseBuilder],[#build()][<]获得对应的Class
     * @param database
     * @param dbName
     * @param <T>
     * @return [#build()]
     */
    fun <T : RoomDatabase> getRoomDatabase(
        database: Class<T>,
        dbName: String?
    ): T? {
        return getModel()?.getRoomDatabase(database, dbName)
    }
}