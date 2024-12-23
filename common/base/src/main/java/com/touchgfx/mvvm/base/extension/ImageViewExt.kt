package com.touchgfx.mvvm.base.extension

import android.net.Uri
import android.widget.ImageView
import com.touchgfx.mvvm.base.glide.ImageLoader

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/23 18:12
 * @desc ImageViewExt
 */

/**
 * 加载图片
 */
fun ImageView.loadImage(url: String, placeholder: Int = 0) {
    ImageLoader.displayImage(this, url, placeholder)
}

fun ImageView.loadImageNoCache(url: String, placeholder: Int = 0) {
    ImageLoader.displayImageNoCache(this, url, placeholder)
}

fun ImageView.loadImage(url: String, roundedCorners: Int, placeholder: Int = 0) {
    ImageLoader.displayImage(this, url, placeholder, roundedCorners)
}

fun ImageView.loadImage(uri: Uri?, roundedCorners: Int, placeholder: Int = 0) {
    ImageLoader.displayImage(this, uri, placeholder, roundedCorners)
}