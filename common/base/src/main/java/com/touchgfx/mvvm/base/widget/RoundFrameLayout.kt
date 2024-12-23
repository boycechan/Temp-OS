package com.touchgfx.mvvm.base.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.touchgfx.mvvm.base.R


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/30 19:58
 * @desc 圆角FrameLayout
 */
class RoundFrameLayout : FrameLayout {
    private val path by lazy { Path() }
    private var radius = 20f
    var disallowIntercept = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr, defStyleAttr) {
        if (attrs != null) {
            val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout)
            radius = ta.getDimension(R.styleable.RoundFrameLayout_radius, 20f)
            ta.recycle()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        path.reset()
        path.addRoundRect(RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()), radius, radius, Path.Direction.CW)
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            canvas.clipPath(path)
        } else {
            canvas.clipPath(path, Region.Op.REPLACE)
        }
        super.dispatchDraw(canvas)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(disallowIntercept)
        return super.dispatchTouchEvent(ev)
    }
}