package com.touchgfx.mvvm.base.utils

object SPKey {

    //权限管理---是否点击【不再提示已关闭权限】,默认状态是false
    var IS_CLICK_NO_PROMPT = "isClickNoPrompt"

    //启动页弹框使用
    var IS_AGREE_LAUCH = "isAgreelauch"

    //webview 的 url
    var PRIVARY_AND_AGREEMENT = "privaryAndAgreement"

    //引导界面使用
    var IS_AGREE_GUIDE = "isAgreeGuide"

    //选择的国家
    var IS_CHINA = "isChina"

    //保存用户名
    var USERNAME = "username"

    //保存密码
    var PASSWORD = "password"

    //保存用户access_token
    var ACCESS_TOKEN = "access_token"

    //保存用户信息
    var USER_INFO = "user_info"

    //第三方登录---IS_BINDING
    var IS_BINDING = "is_binding"

    //第三方登录---source
    var SOURCE = "source"

    //第三方登录---uuid
    var UUID = "uuid"

    //跳转界面，根据不同界面显示不同效果
    const val LAST_ACTIVITY  = "last_activity"

    //微信接口返回数据，传参到登陆界面
    @JvmField var USER_INFO_WECHAT: String = "user_info_wechat"

    //保存用户user_id
    var USER_ID = "user_id"

    //昵称界面下一步传输的昵称数据
    const val NICKNAME = "nickname"

    //昵称界面下一步传输的性别数据
    const val SEX = "sex"

    //生日界面传到下一个界面的生日数据
    const val BIRTHDAY = "birthday"
    // 单位类型
    const val UNIT_TYPE = "unit_type"

//    //身高界面传到下一个界面的英制数据
//    var HEIGHT_YINGCHI_YINGCUN = "height_yingchi_yingcun"



    //身高界面传到下一个界面的公制数据
    const val HEIGHT_DATA = "height_data"

    //身高单位
    const val HEIGHT_UNIT = "height_unit"


    //体重数据
    const val WEIGHT_DATA = "weight_data"

    //体重单位
    const val WEIGHT_UNIT = "weight_unit"

    //设置设备
    const val SET_DEVICE = "set_device"


    //保存当前选择的国家区域
    var CURRENT_AREA = "current_area"

    //保存当前选择的国家区域编码
    var CURRENT_AREA_CODE = "current_area_code"

    //选择区域返回界面使用
    var RESULT_CODE_AND_AREA = "result_code_and_area"

    //当前区域跳转到区域选择界面
    var CURRENT_AREA_INTENT = "current_area_intent"

    //跳转到管理设置界面传蓝牙状态
    var MANAGE_DEVICE_INTENT = "manage_device_intent"




    const val MESSAGE_APPS = "message_apps"


    const val CURRENT_DEVICE_ID = "current_device_id"

}