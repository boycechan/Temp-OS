package com.touchgfx.mvvm.base.config

/**
 * @author chenxiangbo
 * @company TouchGFX
 * @date 2021/3/19 16:43
 * @desc
 */
object Constants {
    const val TAG = "TouchGFX"
    const val DEFAULT_RETROFIT_SERVICE_MAX_SIZE = 60
    const val DEFAULT_ROOM_DATABASE_MAX_SIZE = 60
    const val DEFAULT_DATABASE_NAME = "room.db"

    object ErrorCode {
        /**
         * 授权认证失败
         */
        const val TOKEN_AUTH_FAILURE = 4000

        /**
         * 未授权的访问
         */
        const val TOKEN_UNAUTH = 4001

        /**
         * 令牌失效,请重新登录
         */
        const val TOKEN_INVALID = 4002
    }

}