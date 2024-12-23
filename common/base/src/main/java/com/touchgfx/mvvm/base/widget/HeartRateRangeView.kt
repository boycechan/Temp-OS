package com.touchgfx.mvvm.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.touchgfx.mvvm.base.R


class HeartRateRangeView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var totalPercent: Float = 0f
    private var items = ArrayList<ItemData>()

    private var mPaint: Paint

    private val rectF = RectF()
    private val textPaint = Paint()
    private val textPaint1 = Paint()


    fun setItems(items: ArrayList<ItemData>) {
        this.items = items
        this.totalPercent = 0f
        this.items.forEach { totalPercent += it.percent }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (totalPercent <= 0f) return

        var offset = 0f
        for (i in items.indices) {
            val itemData = items[i]
            val range = itemData.percent / totalPercent * width.toFloat()

            rectF.left = offset
            rectF.top = 0f
            rectF.right = offset + range
            rectF.bottom = height / 3f

            canvas.drawText(itemData.text, rectF.centerX(),
                    rectF.centerY() + (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top) / 2 - textPaint.fontMetrics.bottom,
                    textPaint)

            mPaint.color = itemData.color
            canvas.drawRect(offset, height / 3f, offset + range, height * 2 / 3f, mPaint)

            canvas.drawText(itemData.min.toInt().toString(), offset, height.toFloat(), textPaint1)

            offset += range
        }
    }

    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.HeartRateRangeView)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = Color.BLACK

        textPaint.textSize = 50f
        textPaint.textAlign = Paint.Align.CENTER

        textPaint1.textSize = 50f

        typedArray?.recycle()
    }

    data class ItemData(val text: String, val color: Int, val percent: Float, val min: Float, val max: Float)

}