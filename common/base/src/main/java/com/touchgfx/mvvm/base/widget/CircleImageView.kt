package com.touchgfx.mvvm.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/8/6 16:43
 * @desc CircleImageView
 */
class CircleImageView : AppCompatImageView {
    //画笔
    private var mPaint = Paint()

    //圆形图片的半径
    private var mRadius = 0

    //图片的宿放比例
    private var mScale = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //由于是圆形，宽高应保持一致
        val size = Math.min(measuredWidth, measuredHeight)
        mRadius = size / 2
        setMeasuredDimension(size, size)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (null != drawable) {
            var bitmap: Bitmap? = null
            if (drawable is BitmapDrawable) {
                bitmap = (drawable as BitmapDrawable).bitmap
            } else if (drawable is ColorDrawable) {
                bitmap = drawable.toBitmap(width, height)
            }
            if (bitmap != null) {
                //初始化BitmapShader，传入bitmap对象
                val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                //计算缩放比例
                mScale = mRadius * 2.0f / bitmap.height.coerceAtMost(bitmap.width)
                val matrix = Matrix()
                matrix.setScale(mScale, mScale)
                bitmapShader.setLocalMatrix(matrix)
                mPaint.shader = bitmapShader
                //画圆形，指定好坐标，半径，画笔
                canvas.drawCircle(mRadius.toFloat(), mRadius.toFloat(), mRadius.toFloat(), mPaint)
            }
        }
    }
}