package com.touchgfx.mvvm.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.touchgfx.mvvm.base.R

/**
 * @author boyce.chan
 * @company TouchGFX
 * @date 2022/6/10 12:08
 * @desc RadiusProgressView
 */
/**
 * 圆角进度
 */
class RadiusProgressView : View {
    private var paint: Paint? = null

    //圆角
    private var radius = 90

    //进度值
    private var progress = 0

    //进度最大值
    private var max = 100

    //背景颜色
    private var bgColor = Color.parseColor("#EBEBEB")

    //进度颜色
    private var progressColor = Color.parseColor("#0347FE")

    //0是水平，1是垂直
    private var progressStyle = 0

    constructor(context: Context) : super(context) {
        initAttributeSet(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributeSet(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributeSet(context, attrs)
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.RadiusProgressView)
            radius = array.getDimensionPixelSize(R.styleable.RadiusProgressView_android_radius, radius)
            progress = array.getInt(R.styleable.RadiusProgressView_android_progress, progress)
            bgColor = array.getColor(R.styleable.RadiusProgressView_progressBgColor, bgColor)
            progressColor = array.getColor(R.styleable.RadiusProgressView_progressColor, progressColor)
            progressStyle = array.getColor(R.styleable.RadiusProgressView_progressStyle, progressStyle)
            array.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.color = bgColor
        val bgRect = RectF(0F, 0F, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(bgRect, radius.toFloat(), radius.toFloat(), paint!!)
        //进度
        if (progress > 0) {
            paint!!.color = progressColor
            val psRect = if (progressStyle == 0) {
                RectF(0F, 0F, (width * progress / max).toFloat(), height.toFloat())
            } else {
                RectF(0F, (height - height * progress / max).toFloat(), width.toFloat(), height.toFloat())
            }
            canvas.drawRoundRect(psRect, radius.toFloat(), radius.toFloat(), paint!!)
        }
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    fun setMax(max: Int) {
        this.max = max
        invalidate()
    }

    /**
     * 设置进度值
     *
     * @param progress
     */
    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    /**
     * 设置进度颜色
     *
     * @param progressColor
     */
    fun setProgressColor(progressColor: Int) {
        this.progressColor = progressColor
        invalidate()
    }

    /**
     * 设置背景颜色
     *
     * @param solidColor
     */
    fun setSolidColor(solidColor: Int) {
        this.bgColor = solidColor
        invalidate()
    }
}