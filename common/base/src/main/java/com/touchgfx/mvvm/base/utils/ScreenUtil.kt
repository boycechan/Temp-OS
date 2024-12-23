package com.touchgfx.mvvm.base.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.Window
import android.view.WindowManager


/**
 * @company TouchGFX
 * @author boyce.chan
 * @date 2022/2/17 2:35 下午
 * @desc ScreenUtil
 */
object ScreenUtil {
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 不包含虚拟导航栏高度
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getScreenWidth2(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeight2(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    /**
     * 包含虚拟导航栏高度
     * @param context
     * @return
     */
    fun getScreenWidth3(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val defaultDisplay: Display = windowManager.defaultDisplay
        val outPoint = Point()
        defaultDisplay.getRealSize(outPoint)
        return outPoint.x
    }

    fun getScreenHeight3(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val defaultDisplay: Display = windowManager.defaultDisplay
        val outPoint = Point()
        defaultDisplay.getRealSize(outPoint)
        return outPoint.y
    }

    fun getNavigationBarHeight(context: Context): Int {
        var navigationBarHeight = -1
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return navigationBarHeight
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 注意：这个方法的值必须等到页面创建完成后才能获取到。推荐写在onWindowFocusChanged（）方法中
     * @param activity
     * @return
     */
    fun getStatusBarHeight2(activity: Activity): Int {
        val rectangle = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rectangle)
        return rectangle.top
    }

    /**
     * 注意：这个方法的值必须等到页面创建完成后才能获取到。推荐写在onWindowFocusChanged（）方法中
     * @param activity
     * @return
     */
    fun getContentViewHeight(activity: Activity): Int {
        val rectangle = Rect()
        activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT).getDrawingRect(rectangle)
        return rectangle.height()
    }

    /**
     * 可以利用getScreenHeight() 与 getScreenHeight3() 返回值的差异来判断是否存在虚拟导航栏
     * @param context
     * @return
     */
    fun hasNavigationBar(context: Context): Boolean {
        return getScreenHeight(context) != getScreenHeight3(context)
    }
}