package com.touchgfx.mvvm.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.NumberPicker
import android.widget.Scroller
import com.touchgfx.mvvm.base.R
import com.touchgfx.mvvm.base.extension.dp2px
import com.touchgfx.mvvm.base.extension.sp2px
import kotlin.math.abs
import kotlin.math.max


class RulerView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mMinVelocity = 0
    private var mScroller: Scroller
    private var mVelocityTracker: VelocityTracker? = null
    private var mValue = 50
    private var mMaxValue = 100
    private var mMinValue = 0

    private var mSpanValue = 1
    private var mRadix = 10

    private var mLineSpacing = 0
    private var mLineMaxHeight = 0
    private var mLineMiddleHeight = 0
    private var mLineMinHeight = 0
    private var mLineWidth = 0
    private var mTextMarginTop = 0
    private var mTextHeight = 0f
    private var mTextPaint: Paint
    private var mLinePaint: Paint

    private var mMaxOffset = 0
    private var mOffset = 0
    private var mLastX = 0

    private var mMove = 0

    private var mListener: OnValueChangeListener? = null
    private var mFormatter: NumberPicker.Formatter? = null

    interface OnValueChangeListener {
        fun onValueChange(rulerView: RulerView, value: Int, fromUser: Boolean)
    }

    interface Formatter {
        fun format(value: Int): String
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = (mLineMaxHeight + mTextMarginTop + mTextHeight).toInt()
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
                getDefaultSize(max(suggestedMinimumHeight, height), heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var left: Float
        var height: Float
        var value: String
        var alpha: Int
        var scale: Float
        val srcPointX = width / 2
        for (i in mMinValue until mMaxValue + 1) {
            if (i % mSpanValue != 0) continue

            left = srcPointX + mOffset + ((i - mMinValue) / mSpanValue * mLineSpacing).toFloat()
            if (left < 0 || left > width) {
                continue
            }
            height = when {
                i % (mSpanValue * mRadix) == 0 -> {
                    mLineMaxHeight.toFloat()
                }
                i % (mSpanValue * mRadix / 2) == 0 -> {
                    mLineMiddleHeight.toFloat()
                }
                else -> {
                    mLineMinHeight.toFloat()
                }
            }
            scale = 1 - abs(left - srcPointX) / srcPointX
            alpha = (255 * scale * scale).toInt()
            mLinePaint.alpha = alpha
            if (i == mValue) {
                mLinePaint.strokeWidth = mLineWidth.toFloat() + 10
                canvas.drawLine(left, 0f, left, height, mLinePaint)
            } else {
                mLinePaint.strokeWidth = mLineWidth.toFloat()
                canvas.drawLine(left, 0f, left, height, mLinePaint)
            }
            if (i % (mSpanValue * mRadix) == 0) {
                value = formatNumber(i)
                mTextPaint.alpha = alpha
                canvas.drawText(value, left - mTextPaint.measureText(value) / 2,
                        height + mTextMarginTop + mTextHeight - context.dp2px(3f), mTextPaint)
            }
        }
    }

    fun setValueChangeListener(listener: OnValueChangeListener?) {
        mListener = listener
    }

    fun setFormatter(formatter: NumberPicker.Formatter) {
        if (formatter === mFormatter) {
            return
        }
        mFormatter = formatter
        invalidate()
    }

    private fun formatNumber(value: Int): String {
        return if (mFormatter != null) mFormatter!!.format(value) else "%d".format(value)
    }

    fun initParam(defaultValue: Int, minValue: Int, maxValue: Int, spanValue: Int) {
        mValue = defaultValue
        mMaxValue = maxValue
        mMinValue = minValue
        mSpanValue = spanValue
        mMaxOffset = (mMinValue - mMaxValue) / mSpanValue * mLineSpacing
        mOffset = (mMinValue - defaultValue) / mSpanValue * mLineSpacing
        notifyValueChange(false)
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val xPosition = event.x.toInt()
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mScroller.forceFinished(true)
                mLastX = xPosition
                mMove = 0
            }
            MotionEvent.ACTION_MOVE -> {
                mMove = mLastX - xPosition
                changeMoveAndValue()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                countMoveEnd()
                countVelocityTracker()
                return false
            }
            else -> {
            }
        }
        mLastX = xPosition
        return true
    }

    private fun countVelocityTracker() {
        mVelocityTracker!!.computeCurrentVelocity(1000)
        val xVelocity = mVelocityTracker!!.xVelocity
        if (abs(xVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, xVelocity.toInt(), 0, Int.MIN_VALUE, Int.MAX_VALUE, 0, 0)
        }
    }

    private fun countMoveEnd() {
        mOffset -= mMove
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset
        } else if (mOffset >= 0) {
            mOffset = 0
        }
        mLastX = 0
        mMove = 0
        mValue = mMinValue - mOffset / mLineSpacing * mSpanValue
        // 矫正位置,保证不会停留在两个相邻刻度之间
        mOffset = (mMinValue - mValue) / mSpanValue * mLineSpacing
        notifyValueChange(true)
        postInvalidate()
    }

    private fun changeMoveAndValue() {
        mOffset -= mMove
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset
            mMove = 0
            mScroller.forceFinished(true)
        } else if (mOffset >= 0) {
            mOffset = 0
            mMove = 0
            mScroller.forceFinished(true)
        }
        mValue = mMinValue - mOffset / mLineSpacing * mSpanValue
        notifyValueChange(true)
        postInvalidate()
    }

    private fun notifyValueChange(fromUser: Boolean) {
        mListener?.onValueChange(this, mValue, fromUser)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            if (mScroller.currX == mScroller.finalX) { // over
                countMoveEnd()
            } else {
                val xPosition = mScroller.currX
                mMove = mLastX - xPosition
                changeMoveAndValue()
                mLastX = xPosition
            }
        }
    }

    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.RulerView)

        mLineSpacing = typedArray?.getDimension(R.styleable.RulerView_line_spacing, context.dp2px(8f).toFloat())!!.toInt()
        mLineWidth = typedArray.getDimension(R.styleable.RulerView_line_width, context.dp2px(1f).toFloat()).toInt()
        mLineMaxHeight = typedArray.getDimension(R.styleable.RulerView_line_max_height, context.dp2px(42f).toFloat()).toInt()
        mLineMiddleHeight = typedArray.getDimension(R.styleable.RulerView_line_middle_height, context.dp2px(31f).toFloat()).toInt()
        mLineMinHeight = typedArray.getDimension(R.styleable.RulerView_line_min_height, context.dp2px(17f).toFloat()).toInt()
        mTextMarginTop = typedArray.getDimension(R.styleable.RulerView_text_margin_top, context.dp2px(11f).toFloat()).toInt()
        val textSize = typedArray.getDimension(R.styleable.RulerView_text_size, context.sp2px(16f).toFloat())

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.textSize = textSize
        mTextPaint.color = typedArray.getColor(R.styleable.RulerView_text_color, Color.BLACK)

        mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLinePaint.strokeWidth = mLineWidth.toFloat()
        mLinePaint.color = typedArray.getColor(R.styleable.RulerView_line_color, Color.BLACK)

        typedArray.recycle()

        mTextHeight = getFontHeight(mTextPaint)

        mScroller = Scroller(context)
        mMinVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    }

    /**
     * @return 返回指定笔的文字高度
     */
    private fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }

}