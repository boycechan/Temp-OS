package com.touchgfx.mvvm.base.extension

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/6/11 12:10
 * @desc Permissions
 */
object Permissions {

    val storagePermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    val callPermissions = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
    ).run {
        var permissions = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissions = this.plus(Manifest.permission.ANSWER_PHONE_CALLS)
        }
        permissions
    }

    val smsPermissions = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
    )

    fun isLocationGranted(context: Context): Boolean {
        return locationPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun shouldShowRequestPermissionRationale(context: Activity, permissions: Array<String>): Boolean {
        return permissions.any() {
            ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[0])
        }
    }
}