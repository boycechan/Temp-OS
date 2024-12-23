package com.touchgfx.mvvm.base.bean

import com.google.gson.annotations.SerializedName

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/4/6 15:52
 * @desc BaseResponse
 */

class BaseResponse<T> : BaseBean {

    @SerializedName("resultCode")
    val code = 0

    @SerializedName("resultMsg")
    val message: String? = null

    @SerializedName("resultData")
    var data: T? = null


    fun isSuccess(): Boolean = 200 == code


}