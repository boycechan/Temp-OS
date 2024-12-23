package com.touchgfx.mvvm.base.extension

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.touchgfx.mvvm.base.utils.ScreenUtil
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/23 19:37
 * @desc ContextExt
 */
//----------toast----------
private var toast: Toast? = null

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    if (toast != null) {
        toast?.cancel()
        toast = null
    }
    toast = Toast.makeText(this, text, duration)
    toast?.show()
}

fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(this.resources.getText(resId), duration)
}

fun Context.centerToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    var t = Toast.makeText(this, resId, duration)
    t.setGravity(Gravity.CENTER, 0, 0)
    t.show()
}

//----------尺寸转换----------

fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

//----------屏幕尺寸----------

fun Context.getScreenWidth(): Int {
    return ScreenUtil.getScreenWidth(this)
}

fun Context.getScreenHeight(): Int {
    return ScreenUtil.getScreenHeight(this)
}

fun Context.getScreenDp(): Point {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    wm.defaultDisplay.getMetrics(dm)
    val width = dm.widthPixels// 屏幕宽度（像素）
    val height = dm.heightPixels // 屏幕高度（像素）
    val density = dm.density//屏幕密度（0.75 / 1.0 / 1.5）
    val densityDpi = dm.densityDpi//屏幕密度dpi（120 / 160 / 240）
    //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
    val screenWidth = (width / density).toInt()//屏幕宽度(dp)
    val screenHeight = (height / density).toInt()//屏幕高度(dp)
    return Point(screenWidth, screenHeight)
}


//----------NetWork----------

/**
 * 打开网络设置
 */
fun Context.openWirelessSettings() {
    startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

/**
 * 网络是否连接
 */
fun Context.isConnected(): Boolean {
    var info = this.getActiveNetworkInfo()
    return info?.isConnected ?: false
}

/**
 * 判断网络是否是移动数据
 */
fun Context.isMobileData(): Boolean {
    var info = this.getActiveNetworkInfo()
    return (null != info
            && info.isAvailable
            && info.type == ConnectivityManager.TYPE_MOBILE)
}

/**
 * 退回到桌面
 */
fun Context.startHomeActivity() {
    val homeIntent = Intent(Intent.ACTION_MAIN)
    homeIntent.addCategory(Intent.CATEGORY_HOME)
    startActivity(homeIntent)
}


@SuppressLint("MissingPermission")
private fun Context.getActiveNetworkInfo(): NetworkInfo? {
    var manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo
}

fun Context.getLocationManager(): LocationManager {
    return applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
}

/**
 * 判断GPS定位是否打开
 */
fun Context.isGpsEnabled(): Boolean {
    val locationManager = getLocationManager()
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        return false
    }
    return true
}

/**
 * 跳转到定位设置页面
 */
fun Context.toGpsSetting() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.isLocationGranted(): Boolean {
    return Permissions.isLocationGranted(this)
}

fun Context.isPermissionGranted(permissions: Array<String>): Boolean {
    return Permissions.isPermissionGranted(this, permissions)
}

fun Context.getKeyguardManager(): KeyguardManager {
    return getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
}

fun Context.getPowerManager(): PowerManager {
    return getSystemService(Context.POWER_SERVICE) as PowerManager
}

/**
 * 读取本地文件中字符串
 * @param fileName
 * @return
 */
fun Context.getAssetsString(fileName: String): String {
    val stringBuilder = StringBuilder()
    try {
        val bf = BufferedReader(InputStreamReader(assets.open(fileName), "UTF-8"))
        var line: String?
        while (bf.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return stringBuilder.toString()
}

@SuppressLint("SimpleDateFormat")
fun Context.formatCurTime(): String {
    val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return sdf.format(Calendar.getInstance())
}

@RequiresApi(api = Build.VERSION_CODES.M)
fun Context.isIgnoringBatteryOptimizations(): Boolean {
    var isIgnoring = false
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager?
    if (powerManager != null) {
        isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName)
    }
    return isIgnoring
}

@RequiresApi(api = Build.VERSION_CODES.M)
fun Context.requestIgnoreBatteryOptimizations() {
    try {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.getDrawableCompat(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

