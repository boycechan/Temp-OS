package com.touchgfx.mvvm.base.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * @company Touch
 * @author chenxiangbo
 * @date 2021/3/23 9:54
 * @desc GlideModule
 */
@GlideModule
class GlideModule : AppGlideModule(){

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDefaultRequestOptions(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
    }

}