package com.touchgfx.mvvm.base.config

import android.content.Context
import android.content.pm.PackageManager
import timber.log.Timber
import java.util.*

/**
 * @see [Glide](https://github.com/bumptech/glide/blob/f7d860412f061e059aa84a42f2563a01ac8c303b/library/src/main/java/com/bumptech/glide/module/ManifestParser.java)
 */
class ManifestParser(private val context: Context) {
    fun parse(): List<FrameConfigModule> {
        Timber.d("Loading MVVMFrame modules")
        val modules: MutableList<FrameConfigModule> =
            ArrayList()
        try {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData == null) {
                Timber.d("Got null app info metadata")
                return modules
            }
            Timber.v("Got app info metadata: " + appInfo.metaData)
            for (key in appInfo.metaData.keySet()) {
                if (CONFIG_MODULE_VALUE == appInfo.metaData[key]
                ) {
                    modules.add(
                        parseModule(
                            key
                        )
                    )
                    Timber.d("Loaded MVVMFrame module: $key")
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException(
                "Unable to find metadata to parse FrameConfigModules",
                e
            )
        }
        Timber.d("Finished loading MVVMFrame modules")
        return modules
    }

    companion object {
        private const val CONFIG_MODULE_VALUE = "FrameConfigModule"
        private fun parseModule(className: String): FrameConfigModule {
            val clazz: Class<*>
            clazz = try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException(
                    "Unable to find FrameConfigModule implementation",
                    e
                )
            }
            val module: Any
            module = try {
                clazz.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException(
                    "Unable to instantiate FrameConfigModule implementation for $clazz",
                    e
                )
                // These can't be combined until API minimum is 19.
            } catch (e: IllegalAccessException) {
                throw RuntimeException(
                    "Unable to instantiate FrameConfigModule implementation for $clazz",
                    e
                )
            }
            if (module !is FrameConfigModule) {
                throw RuntimeException("Expected instanceof FrameConfigModule, but found: $module")
            }
            return module
        }
    }

}