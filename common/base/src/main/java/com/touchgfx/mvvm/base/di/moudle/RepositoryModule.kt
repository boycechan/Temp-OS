package com.touchgfx.mvvm.base.di.moudle

import com.touchgfx.mvvm.base.data.DataRepository
import com.touchgfx.mvvm.base.data.IDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/20 11:27
 * @desc
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindDataRepository(dataRepository: DataRepository?): IDataRepository?
}