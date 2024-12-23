package com.touchgfx.mvvm.base.glide

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/23 9:52
 * @desc 图片加载器
 */
object ImageLoader {

    fun displayImage(iv: ImageView, url: String?, defaultImage: Int) {
        url?.let {
            Glide.with(iv).load(url).placeholder(defaultImage).error(defaultImage).into(iv)
        } ?: run {
            iv.setImageResource(defaultImage)
        }
    }

    fun displayImageNoCache(iv: ImageView, url: String?, defaultImage: Int) {
        url?.let {
            Glide.with(iv).load(url)
                    .placeholder(defaultImage)
                    .error(defaultImage)
                    .skipMemoryCache(true)  // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .into(iv)
        } ?: run {
            iv.setImageResource(defaultImage)
        }
    }

    fun displayImage(iv: ImageView, url: String?, defaultImage: Int, roundedCorners: Int) {
        url?.let {
            Glide.with(iv).load(url).placeholder(defaultImage).error(defaultImage)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(roundedCorners))).into(iv)
        } ?: run {
            iv.setImageResource(defaultImage)
        }
    }

    fun displayImage(iv: ImageView, uri: Uri?, defaultImage: Int, roundedCorners: Int) {
        uri?.let {
            Glide.with(iv).load(uri).placeholder(defaultImage).error(defaultImage)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(roundedCorners))).into(iv)
        } ?: run {
            iv.setImageResource(defaultImage)
        }
    }

    fun displayImage(context: Context, iv: ImageView, url: String?, defaultImage: Int) {
        url?.let {
            Glide.with(context).load(url).placeholder(defaultImage).error(defaultImage).into(iv)
        } ?: run {
            iv.setImageResource(defaultImage)
        }
    }

    fun displayImage(fragment: Fragment, iv: ImageView, url: String?, defaultImage: Int) {
        url?.let {
            Glide.with(fragment).load(url).placeholder(defaultImage).error(defaultImage).into(iv)
        } ?: run {
            iv.setImageResource(defaultImage)
        }
    }

    fun displayImage(activity: Activity, iv: ImageView, url: String?, defaultImage: Int) {
        url?.let {
            Glide.with(activity).load(url).placeholder(defaultImage).error(defaultImage).into(iv)
        } ?: run {
            iv.setImageResource(defaultImage)
        }
    }

}