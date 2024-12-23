package com.touchgfx.mvvm.base.http

import com.touchgfx.mvvm.base.config.Constants

/**
 * @author chenxiangbo
 * @company TouchGFX
 * @date 2021/4/27 10:01
 * @desc ApiException
 */
class ApiException(val errorCode: Int, errorMessage: String?) : RuntimeException(errorMessage) {
    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    val isTokenExpried: Boolean
        get() = errorCode == Constants.ErrorCode.TOKEN_INVALID
                || errorCode == Constants.ErrorCode.TOKEN_AUTH_FAILURE
                || errorCode == Constants.ErrorCode.TOKEN_UNAUTH
}