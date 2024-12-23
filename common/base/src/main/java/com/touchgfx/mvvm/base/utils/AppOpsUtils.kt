package com.touchgfx.mvvm.base.utils

import android.app.AppOpsManager
import android.content.Context
import android.os.Binder
import android.os.Build
import java.lang.reflect.Modifier

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/7/28 11:58
 * @desc AppOpsUtils
 */
object AppOpsUtils {
    /**
     * 通过这个获取所有的OPS Field, 理论上第三方系统厂商都是修改这个来实现新增权限的, 这个可以解决比如miui的自启动和后台弹出界面等
     */
    fun getAllOPSField(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val f = manager.javaClass.declaredFields

            for (field in f) {
                if (field.type == Int::class.java && Modifier.isStatic(field.modifiers)) {
                    field.isAccessible = true
                    println("${field.name} = ${field.get(null)}")
                }
            }

        }
    }

    fun checkOp(context: Context, op: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val managerClass = manager.javaClass
                val method = managerClass.getDeclaredMethod(
                        "checkOp",
                        Int::class.java,
                        Int::class.java,
                        String::class.java
                ).apply {
                    isAccessible = true
                }
                val isAllowNum = method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int

                return AppOpsManager.MODE_ALLOWED == isAllowNum
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun checkAutoStart(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val managerClass = manager.javaClass
                val field = managerClass.getDeclaredField("OP_AUTO_START")
                val op = field.getInt(manager)
                val method = managerClass.getDeclaredMethod(
                        "checkOp",
                        Int::class.java,
                        Int::class.java,
                        String::class.java
                ).apply {
                    isAccessible = true
                }
                val isAllowNum = method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int

                return AppOpsManager.MODE_ALLOWED == isAllowNum
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }
}