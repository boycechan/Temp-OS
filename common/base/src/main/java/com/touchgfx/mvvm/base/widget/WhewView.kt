package com.touchgfx.mvvm.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.touchgfx.mvvm.base.R
import com.touchgfx.mvvm.base.extension.dp2px
import timber.log.Timber
import java.util.*
import kotlin.math.max


class WhewView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint: Paint
    private var mInitRadius = 0

    private val mCircleAlphaList: MutableList<Int> = ArrayList()
    private val mCircleRadiusList: MutableList<Int> = ArrayList()
    private var mStarting = false
    private var mScaleRadius = 1.0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 颜色：完全透明
        setBackgroundColor(Color.TRANSPARENT)

        // 依次绘制同心圆
        for (i in mCircleAlphaList.indices) {
            val alpha = mCircleAlphaList[i]
            val radius = mCircleRadiusList[i]

            mPaint.alpha = alpha
            canvas.drawCircle(width / 2f, height / 2f, radius.toFloat() * mScaleRadius, mPaint)

            if (mStarting && alpha > 0) {
                mCircleAlphaList[i] = alpha - 1
            }
            if (mStarting && radius * mScaleRadius < max(width, height) / 2) {
                mCircleRadiusList[i] = radius + 1
            }
        }

        if (mStarting && mCircleAlphaList.last() == 200) {
            mCircleAlphaList.add(255)
            mCircleRadiusList.add(mInitRadius)
        }

        // 同心圆数量达到10个，删除最外层圆
        if (mStarting && mCircleRadiusList.size == DEFAULT_CIRCLE_COUNT) {
            mCircleRadiusList.removeAt(0)
            mCircleAlphaList.removeAt(0)
        }

        // 刷新界面
        invalidate()
    }

    // 执行动画
    fun start() {
        mStarting = true
    }

    // 停止动画
    fun stop() {
        mStarting = false
    }

    fun setScaleRadius(scaleRadius: Float) {
        mScaleRadius = scaleRadius
    }

    fun getScaleRadius() = mScaleRadius

    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.WhewView)

        val color = typedArray?.getColor(R.styleable.WhewView_color, Color.BLACK)!!.toInt()

        mInitRadius = typedArray.getDimension(R.styleable.WhewView_init_radius, context.dp2px(40f).toFloat()).toInt()

        typedArray.recycle()

        mPaint = Paint()
        mPaint.color = color
        mCircleAlphaList.add(255)
        mCircleRadiusList.add(mInitRadius)
    }

    companion object {
        const val DEFAULT_CIRCLE_COUNT = 10
    }

}