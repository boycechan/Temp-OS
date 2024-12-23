package com.touchgfx.mvvm.base.utils

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import timber.log.Timber
import java.lang.reflect.Method
import java.util.*

/**
 * 日期、时间选择器工具类
 */
object PickerUtils {

    fun setDivider(datePicker: DatePicker?, divider: ColorDrawable?) {
        if (datePicker == null || divider == null) {
            return
        }
        val numberPickerList = findNumberPicker(datePicker)
        for (numberPicker in numberPickerList) {
            setDivider(numberPicker, divider)
        }
    }

    /**
     * am/pm的分割没变化
     *
     * @param timePicker
     * @param divider
     */
    fun setDivider(timePicker: TimePicker?, divider: ColorDrawable?) {
        if (timePicker == null || divider == null) {
            return
        }
        try {
            val numberPickerList = findNumberPicker(timePicker)
            for (numberPicker in numberPickerList) {
                setDivider(numberPicker, divider)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setDivider(numberPicker: NumberPicker?, divider: Drawable?) {
        if (numberPicker == null || divider == null) {
            return
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            numberPicker.selectionDividerHeight = 0
        } else {
            val pickerFields = NumberPicker::class.java.declaredFields
            for (pf in pickerFields) {
                if (pf.name == "mSelectionDivider") {
                    pf.isAccessible = true
                    try {
                        pf[numberPicker] = divider
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        }
    }

    private fun findNumberPicker(viewGroup: ViewGroup?): List<NumberPicker> {
        val npList: MutableList<NumberPicker> = ArrayList()
        var child: View?
        if (null != viewGroup) {
            for (i in 0 until viewGroup.childCount) {
                child = viewGroup.getChildAt(i)
                if (child is NumberPicker) {
                    npList.add(child)
                } else if (child is LinearLayout) {
                    val result = findNumberPicker(child as ViewGroup?)
                    if (result.isNotEmpty()) {
                        return result
                    }
                }
            }
        }
        return npList
    }

    /**
     * setFormatter 对第一个显示的 Item 没有效果
     */
    @SuppressLint("DiscouragedPrivateApi")
    fun fixBug(picker: NumberPicker) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            try {
                val method: Method = picker.javaClass.getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
//                method.invoke(picker, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setTextSize(picker: NumberPicker, textSize: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            picker.textSize = textSize
        } else {
            try {
                val inputTextField = picker.javaClass.getDeclaredField("mInputText")
                val selectorWheelPaintField = picker.javaClass.getDeclaredField("mSelectorWheelPaint")
                inputTextField.isAccessible = true
                selectorWheelPaintField.isAccessible = true
                val inputText = inputTextField.get(picker)
                val selectorWheelPaint = selectorWheelPaintField.get(picker)
                if (inputText is EditText && selectorWheelPaint is Paint) {
                    selectorWheelPaint.textSize = textSize
                    inputText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                    picker.invalidate()
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}