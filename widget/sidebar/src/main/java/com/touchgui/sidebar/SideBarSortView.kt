package com.touchgui.sidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 * @company TouchGFX
 * @author boyce.chan
 * @date 2022/3/11 1:37 下午
 * @desc SideBarSortView
 */
class SideBarSortView : View {

    companion object {
        var mList = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z", "#")
    }

    private var mCanvas: Canvas? = null
    private var mSelectIndex = 0
    private var mTextSize = 0f
    private var mTextColor = 0
    private var mTextSizeChoose = 0f
    private var mTextColorChoose = 0
    private var paint: Paint = Paint()
    private var mClickListener: OnIndexChangedListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mCanvas = canvas
        paintText()
    }

    private fun paintText() {
        //计算每一个字母的高度,总告诉除以字母集合的高度就可以
        val height = height / mList.size
        for (i in mList.indices) {
            if (i == mSelectIndex) {
                paint.color = mTextColorChoose
                paint.textSize = mTextSizeChoose
            } else {
                paint.color = mTextColor
                paint.textSize = mTextSize
            }
            paint.isAntiAlias = true //设置抗锯齿
            paint.typeface = Typeface.DEFAULT_BOLD
            //计算每一个字母x轴
            val paintX: Float = width / 2f - paint.measureText(mList[i]) / 2
            //计算每一个字母Y轴
            val paintY = height * i + height
            //绘画出来这个TextView
            mCanvas?.drawText(mList[i], paintX, paintY.toFloat(), paint)
            //画完一个以后重置画笔
            paint.reset()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val index = (event.y / height * mList.size).toInt()
                if (index >= 0 && index < mList.size) {
                    mClickListener?.onSideBarScrollUpdateItem(mList[index])
                    mSelectIndex = index
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (mClickListener != null) {
                mClickListener?.onSideBarScrollEndHideText()
            }
        }
        return true
    }

    fun setTextSize(mTextSize: Float) {
        this.mTextSize = mTextSize
    }

    fun setTextColor(mTextColor: Int) {
        this.mTextColor = mTextColor
    }

    fun setTextSizeChoose(mTextSizeChoose: Float) {
        this.mTextSizeChoose = mTextSizeChoose
    }

    fun setTextColorChoose(mTextColorChoose: Int) {
        this.mTextColorChoose = mTextColorChoose
    }

    fun setIndexChangedListener(listener: OnIndexChangedListener?) {
        mClickListener = listener
    }

    fun onItemScrollUpdateText(word: String) {
        for (i in mList.indices) {
            if (mList[i] == word && mSelectIndex != i) {
                mSelectIndex = i
                invalidate()
            }
        }
    }

    interface OnIndexChangedListener {
        fun onSideBarScrollUpdateItem(word: String?) //滚动位置
        fun onSideBarScrollEndHideText() //隐藏提示文本
    }
}