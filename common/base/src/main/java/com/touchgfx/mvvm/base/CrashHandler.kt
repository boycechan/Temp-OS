package com.touchgfx.mvvm.base

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Process
import com.orhanobut.logger.Logger
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 *
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    companion object {
        val TAG = CrashHandler::class.java.simpleName

        private var ROOT_PATH = "/sdcard"
        private const val FILE_NAME_PREFIX = "crash"
        private const val FILE_NAME_SUFFIX = ".trace"

        private var instance = CrashHandler()

        /**
         * CrashHandler实例
         */
        fun getInstance(): CrashHandler = instance
    }

    /**
     * 系统默认的UncaughtException处理类
     */
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 程序的Context对象
     */
    private var mApplication: Context? = null

    /**
     * App的相关信息集合
     */
    private val mInfos: MutableMap<String, String> = HashMap()

    /**
     * 用于格式化日期,作为日志文件名的一部分
     */
    private val mFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

    /**
     * 初始化
     */
    fun init(application: Application?) {
        mApplication = application
        ROOT_PATH = mApplication?.externalMediaDirs?.firstOrNull()?.let {
            File(it, "log").apply { mkdirs() }
        }?.absolutePath ?: "/sdcard"
        /**
         * 获取系统默认的UncaughtException处理器
         */
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        /**
         * 设置该CrashHandler为程序的默认处理器
         */
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            //退出程序
            Process.killProcess(Process.myPid())
            System.exit(1)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        //收集设备参数信息
        collectDeviceInfo(mApplication)

        //保存日志文件
        saveCatchInfo2File(ex)
        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private fun collectDeviceInfo(ctx: Context?) {
        try {
            val pm = ctx!!.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                mInfos["versionName"] = versionName
                mInfos["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.e(TAG, "an error occured when collect package info")
        }
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                mInfos[field.name] = field[null].toString()
                Logger.d(TAG, field.name + " : " + field[null])
            } catch (e: Exception) {
                Logger.d(e.toString())
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCatchInfo2File(ex: Throwable): String? {
        val sb = StringBuffer()
        for ((key, value) in mInfos) {
            sb.append("$key=$value\n")
        }
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        var fos: FileOutputStream? = null
        try {
            val time = mFormatter.format(Date())
            val fileName = "$FILE_NAME_PREFIX-$time$FILE_NAME_SUFFIX"
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val filePath = ROOT_PATH + File.separator + fileName
                fos = FileOutputStream(filePath)
                fos.write(sb.toString().toByteArray())
                //通知刷新
                val uri = Uri.fromFile(File(filePath))
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
                mApplication!!.sendBroadcast(intent)
                //发送给开发人员
//                sendCrashLogOut2PM(filePath)
            }
            Logger.e(sb.toString())
            return fileName
        } catch (e: Exception) {
            Logger.d(e.toString())
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                Logger.d(e.toString())
            }
        }
        return null
    }

    /**
     * 将捕获的导致崩溃的错误信息发送给开发人员
     *
     *
     * 目前只将log日志保存在sdcard 和输出到LogOutCat中，并未发送给后台。
     */
    private fun sendCrashLogOut2PM(fileName: String) {
        if (!File(fileName).exists()) {
//            Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return
        }
        var fis: FileInputStream? = null
        var reader: BufferedReader? = null
        var s: String? = null
        try {
            fis = FileInputStream(fileName)
            reader = BufferedReader(InputStreamReader(fis, "GBK"))
            while (true) {
                s = reader.readLine()
                if (s == null) {
                    break
                }
                //由于目前尚未确定以何种方式发送，所以先打出log日志。
                Logger.e("CrashLog", s.toString())
            }
        } catch (e: FileNotFoundException) {
            Logger.e(e.toString())
        } catch (e: IOException) {
            Logger.e(e.toString())
        } finally {   // 关闭流
            try {
                reader?.close()
                fis?.close()
            } catch (e: IOException) {
                Logger.e(e.toString())
            }
        }
    }

}