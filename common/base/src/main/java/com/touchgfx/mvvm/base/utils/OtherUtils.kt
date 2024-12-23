package com.touchgfx.mvvm.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.media.AudioAttributes
import android.net.ConnectivityManager
import android.os.Build
import android.os.LocaleList
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.annotation.RequiresApi
import java.util.*


object OtherUtils {


    /**
     * 设置状态栏--//沉浸式
     */
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
      fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //设置状态栏字体颜色
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }


    /**
     * 短震动效果
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun vibrator(context : Activity){
        (context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(70, AudioAttributes.USAGE_NOTIFICATION));
    }


    /**
     * 判断网络状态
     */
    fun isNetConnected(mContext : Context): Boolean {
        val connectivityManager = mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val info = connectivityManager.activeNetworkInfo
            return info != null && info.isConnected && info.isAvailable
        }
        return false
    }

    fun getSystemLanguage(): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().get(0)
        } else {
            Locale.getDefault()
        }
        return locale.language
    }


    /**
     * 旋转动画
     */
    fun rotateAnimationStart( view : View ) : Animation{
        val animation: Animation = RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.setDuration(1000)
        animation.setInterpolator(LinearInterpolator())
        animation.setRepeatCount(-1)
        animation.setFillAfter(true)
        view.startAnimation(animation)
        return animation
    }


}
