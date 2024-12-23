package com.touchgfx.mvvm.base.widget.segmented

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.annotation.IntDef
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.touchgfx.mvvm.base.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by song on 2016/10/12.
 * github: https://github.com/danledian/SegmentedControl
 */
class SegmentedControlView : View, ISegmentedControl {

    companion object {
        private const val VELOCITY_UNITS = 1000
        private const val ANIMATION_DURATION = 300
        private const val MOVE_ITEM_MIN_VELOCITY = 1500 //移动Item的最小速度
        private const val DEFAULT_RADIUS = 10
        private val DEFAULT_OUTER_COLOR = Color.parseColor("#12B7F5")
        private const val DEFAULT_ITEM_COLOR = Color.WHITE
        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private val DEFAULT_SELECTED_TEXT_COLOR = Color.parseColor("#00A5E0")
        private const val MIN_MOVE_X = 5f
        private const val VELOCITY_CHANGE_POSITION_THRESHOLD = 0.25f //速度改变位置阈值,范围:[0-1)

        /**
         * 圆角
         */
        const val Round = 0

        /**
         * 圆形
         */
        const val Circle = 1
    }

    @IntDef(Round, Circle)
    @Retention(RetentionPolicy.RUNTIME)
    annotation class Mode

    /**
     * 边角半径
     */
    private var cornersRadius = 0

    /**
     * 背景颜色
     */
    private var bgColor = 0

    /**
     * Item水平方向外边距
     */
    private var itemHorizontalMargin = 0

    /**
     * Item垂直方向边距
     */
    private var itemVerticalMargin = 0

    /**
     * 选中项背景颜色
     */
    private var selectedItemBackgroundColor = 0

    /**
     * 文本尺寸
     */
    private var textSize = 0

    /**
     * 默认文本颜色
     */
    private var textColor = 0

    /**
     * 选中文本颜色
     */
    private var selectedItemTextColor = 0

    /**
     * 选中位置
     */
    private var selectedItem = 0

    /**
     * 边角模式
     */
    private var cornersMode = Round

    /**
     * 滑动选择是否可用
     */
    var isScrollSelectEnabled = true
        private set
    private var itemPadding = 0
    private var bgStrokeWidth = 0
    private var offset = 0
    private var onSegItemClickListener: OnSegItemClickListener? = null
    private var mStart = 0
    private var mEnd = 0
    private var mHeight = 0
    private var mItemWidth = 0
    private var onClickDownPosition = -1 //点击事件down选中的位置
    private var mMaximumFlingVelocity = 0
    private var eventX = 0f
    private var mRectF: RectF? = null
    private var mPaint: Paint? = null
    private var mTextPaint: Paint? = null
    private var mScroller: Scroller? = null
    private var mVelocityTracker: VelocityTracker? = null
    private val mSegmentedControlItems: MutableList<SegmentedControlItem> = ArrayList()

    interface OnSegItemClickListener {
        fun onItemClick(item: SegmentedControlItem?, position: Int)
    }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context!!, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentedControlView)
        cornersRadius = ta.getDimensionPixelSize(R.styleable.SegmentedControlView_segCornersRadius, DEFAULT_RADIUS)
        bgColor = ta.getColor(R.styleable.SegmentedControlView_segBackgroundColor, DEFAULT_OUTER_COLOR)
        selectedItemBackgroundColor = ta.getColor(R.styleable.SegmentedControlView_segSelectedItemBackgroundColor, DEFAULT_ITEM_COLOR)
        textColor = ta.getColor(R.styleable.SegmentedControlView_segTextColor, DEFAULT_TEXT_COLOR)
        selectedItemTextColor = ta.getColor(R.styleable.SegmentedControlView_segSelectedItemTextColor, DEFAULT_SELECTED_TEXT_COLOR)
        itemHorizontalMargin = ta.getDimensionPixelSize(R.styleable.SegmentedControlView_segItemHorizontalMargin, 0)
        itemVerticalMargin = ta.getDimensionPixelSize(R.styleable.SegmentedControlView_segItemVerticalMargin, 0)
        selectedItem = ta.getInteger(R.styleable.SegmentedControlView_segSelectedItem, 0)
        textSize = ta.getDimensionPixelSize(R.styleable.SegmentedControlView_segTextSize, dp2px(16f))
        cornersMode = ta.getInt(R.styleable.SegmentedControlView_segCornersMode, Round)
        isScrollSelectEnabled = ta.getBoolean(R.styleable.SegmentedControlView_segScrollSelectEnabled, true)
        itemPadding = ta.getDimensionPixelOffset(R.styleable.SegmentedControlView_segItemPadding, dp2px(30f))
        bgStrokeWidth = ta.getDimensionPixelOffset(R.styleable.SegmentedControlView_bgStrokeWidth, 0)
        offset = ta.getDimensionPixelOffset(R.styleable.SegmentedControlView_offset, 0)
        ta.recycle()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = null
        } else {
            setBackgroundDrawable(null)
        }
        mScroller = Scroller(context, FastOutSlowInInterpolator())
        val configuration = ViewConfiguration.get(context)
        mMaximumFlingVelocity = configuration.scaledMaximumFlingVelocity
        mRectF = RectF()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mPaint!!.isAntiAlias = true
        mPaint!!.color = bgColor
        mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.color = textColor
        mTextPaint!!.textSize = textSize.toFloat()
    }

    fun setCornersMode(@Mode mode: Int) {
        cornersMode = mode
        invalidate()
    }

    fun setTextColor(color: Int) {
        textColor = color
        invalidate()
    }

    fun setSelectedItemTextColor(color: Int) {
        selectedItemTextColor = color
        invalidate()
    }

    fun setSelectedItem(position: Int) {
        require(!(position < 0 || position >= count)) { "position error" }
        selectedItem = position
        this.requestLayout()
        invalidate()
    }

    fun getSelectedItem(): Int {
        return selectedItem
    }

    fun getCornersMode(): Int {
        return cornersMode
    }

    fun setOnSegItemClickListener(onSegItemClickListener: OnSegItemClickListener?) {
        this.onSegItemClickListener = onSegItemClickListener
    }

    fun addItems(list: List<SegmentedControlItem>?) {
        requireNotNull(list) { "list is null" }
        mSegmentedControlItems.addAll(list)
        requestLayout()
        invalidate()
    }

    fun addItem(item: SegmentedControlItem?) {
        requireNotNull(item) { "item is null" }
        mSegmentedControlItems.add(item)
        requestLayout()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isItemZero) return
        drawBackgroundRect(canvas)
        drawUnselectedItemsText(canvas)
        drawSelectedItem(canvas)
        drawSelectedItemsText(canvas)
    }

    /**
     * 画选中项
     *
     * @param canvas
     */
    private fun drawSelectedItem(canvas: Canvas) {
        val r = if (cornersMode == Round) cornersRadius.toFloat() else (mHeight shr 1) - itemVerticalMargin.toFloat()
        mPaint!!.color = selectedItemBackgroundColor
        mPaint!!.style = Paint.Style.FILL
        mRectF!![mStart.toFloat(), itemVerticalMargin.toFloat(), (mStart + mItemWidth).toFloat()] = (height - itemVerticalMargin).toFloat()
        canvas.drawRoundRect(mRectF!!, r, r, mPaint!!)
    }

    /**
     * 画背景区域
     *
     * @param canvas
     */
    private fun drawBackgroundRect(canvas: Canvas) {
        val r = if (cornersMode == Round) cornersRadius.toFloat() else (mHeight shr 1.toFloat().toInt()).toFloat()
        mPaint!!.xfermode = null
        mPaint!!.color = bgColor
        if (bgStrokeWidth > 0) {
            mPaint!!.style = Paint.Style.STROKE
            mPaint!!.strokeWidth = bgStrokeWidth.toFloat()
        }
        mRectF!![(0 + offset).toFloat(), (0 + offset).toFloat(), (width - offset).toFloat()] = (height - offset).toFloat()
        canvas.drawRoundRect(mRectF!!, r, r, mPaint!!)
    }

    /**
     * 画所有未选中Item的文字
     *
     * @param canvas
     */
    private fun drawUnselectedItemsText(canvas: Canvas) {
        mTextPaint!!.color = textColor
        mTextPaint!!.xfermode = null
        for (i in 0 until count) {
            val start = itemHorizontalMargin + i * mItemWidth
            val x = start + (mItemWidth shr 1) - mTextPaint!!.measureText(getName(i)) / 2
            val y = (height shr 1) - (mTextPaint!!.ascent() + mTextPaint!!.descent()) / 2
            canvas.drawText(getName(i)!!, x, y, mTextPaint!!)
        }
    }

    /**
     * 画所有选中Item的文字
     *
     * @param canvas
     */
    private fun drawSelectedItemsText(canvas: Canvas) {
        canvas.saveLayer(mStart.toFloat(), 0f, (mStart + mItemWidth).toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        mTextPaint!!.color = selectedItemTextColor
        mTextPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        val begin = mStart / mItemWidth
        val end = if (begin + 2 < count) begin + 2 else count
        for (i in begin until end) {
            val start = itemHorizontalMargin + i * mItemWidth
            val x = start + (mItemWidth shr 1) - mTextPaint!!.measureText(getName(i)) / 2
            val y = (height shr 1) - (mTextPaint!!.ascent() + mTextPaint!!.descent()) / 2
            canvas.drawText(getName(i)!!, x, y, mTextPaint!!)
        }
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled || !isInTouchMode || count == 0) return false
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            eventX = event.x
            onClickDownPosition = -1
            val y = event.y
            if (isItemInside(eventX, y)) {
                return isScrollSelectEnabled
            } else if (isItemOutside(eventX, y)) {
                if (!mScroller!!.isFinished) {
                    mScroller!!.abortAnimation()
                }
                onClickDownPosition = ((eventX - itemHorizontalMargin) / mItemWidth).toInt()
                startScroll(positionStart(eventX))
                return true
            }
            return false
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!mScroller!!.isFinished || !isScrollSelectEnabled) {
                return true
            }
            val dx = event.x - eventX
            if (Math.abs(dx) > MIN_MOVE_X) {
                mStart = (mStart + dx).toInt()
                mStart = Math.min(Math.max(mStart, itemHorizontalMargin), mEnd)
                postInvalidate()
                eventX = event.x
            }
            return true
        } else if (action == MotionEvent.ACTION_UP) {
            var newSelectedItem: Int
            val offset = ((mStart - itemHorizontalMargin) % mItemWidth).toFloat()
            val itemStartPosition = (mStart - itemHorizontalMargin) * 1.0f / mItemWidth
            if (!mScroller!!.isFinished && onClickDownPosition != -1) {
                newSelectedItem = onClickDownPosition
            } else {
                if (offset == 0f) {
                    newSelectedItem = itemStartPosition.toInt()
                } else {
                    val velocityTracker = mVelocityTracker
                    velocityTracker!!.computeCurrentVelocity(VELOCITY_UNITS, mMaximumFlingVelocity.toFloat())
                    val initialVelocity = velocityTracker.xVelocity.toInt()
                    val itemRate = offset / mItemWidth
                    newSelectedItem = if (isXVelocityCanMoveNextItem(initialVelocity, itemRate)) {
                        if (initialVelocity > 0) itemStartPosition.toInt() + 1 else itemStartPosition.toInt()
                    } else {
                        Math.round(itemStartPosition)
                    }
                    newSelectedItem = Math.max(Math.min(newSelectedItem, count - 1), 0)
                    startScroll(getXByPosition(newSelectedItem))
                }
            }
            onStateChange(newSelectedItem)
            mVelocityTracker = null
            onClickDownPosition = -1
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun startScroll(dx: Int) {
        mScroller!!.startScroll(mStart, 0, dx - mStart, 0, ANIMATION_DURATION)
        postInvalidate()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller!!.computeScrollOffset()) {
            mStart = mScroller!!.currX
            postInvalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (isItemZero) {
            return
        }
        val measureWidth = measureWidth(widthMeasureSpec)
        val measureHeight = measureHeight(heightMeasureSpec)
        setMeasuredDimension(measureWidth, measureHeight)
        mHeight = measuredHeight
        val width = measuredWidth
        mItemWidth = (width - 2 * itemHorizontalMargin) / count
        mStart = itemHorizontalMargin + mItemWidth * selectedItem
        mEnd = width - itemHorizontalMargin - mItemWidth
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        var width = 0
        for (i in 0 until count) {
            width += mTextPaint!!.measureText(getName(i)).toInt()
        }
        width += itemPadding * count
        return if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else if (specMode == MeasureSpec.AT_MOST) {
            Math.min(width, specSize)
        } else {
            width
        }
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(heightMeasureSpec)
        val specSize = MeasureSpec.getSize(heightMeasureSpec)
        val height = (mTextPaint!!.descent() - mTextPaint!!.ascent()).toInt() + paddingTop + paddingBottom + 2 * itemVerticalMargin
        Log.i("test", "specMode:$specMode,specSize:$specSize,height:$height")
        return if (specMode == MeasureSpec.AT_MOST) {
            Math.min(height, specSize)
        } else if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            height
        }
    }

    fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.displayMetrics).toInt()
    }

    private fun onStateChange(selectedItem: Int) {
        if (this.selectedItem != selectedItem) {
            this.selectedItem = selectedItem
            onItemClick(getItem(selectedItem), selectedItem)
        }
    }

    private fun onItemClick(item: SegmentedControlItem?, position: Int) {
        if (null != onSegItemClickListener) {
            onSegItemClickListener!!.onItemClick(item, position)
        }
    }

    private fun getXByPosition(item: Int): Int {
        return item * mItemWidth + itemHorizontalMargin
    }

    private fun positionStart(x: Float): Int {
        return itemHorizontalMargin + ((x - itemHorizontalMargin) / mItemWidth).toInt() * mItemWidth
    }

    private fun isItemInside(x: Float, y: Float): Boolean {
        return x >= mStart && x <= mStart + mItemWidth && y > itemVerticalMargin && y < mHeight - itemVerticalMargin
    }

    private fun isItemOutside(x: Float, y: Float): Boolean {
        return !isItemInside(x, y) && y > itemVerticalMargin && y < mHeight - itemVerticalMargin && x < mEnd + mItemWidth
    }

    /**
     * 根据速度和位置判断是否能移动下一个item
     *
     * @return 否能移动item
     */
    private fun isXVelocityCanMoveNextItem(xVelocity: Int, dxItemRate: Float): Boolean {
        return Math.abs(xVelocity) > MOVE_ITEM_MIN_VELOCITY && (xVelocity > 0 && dxItemRate >= VELOCITY_CHANGE_POSITION_THRESHOLD ||
                xVelocity < 0 && dxItemRate < 1 - VELOCITY_CHANGE_POSITION_THRESHOLD)
    }

    private val isItemZero: Boolean
        private get() = count == 0
    override val count: Int
        get() = mSegmentedControlItems.size

    override fun getItem(position: Int): SegmentedControlItem? {
        return mSegmentedControlItems[position]
    }

    override fun getName(position: Int): String? {
        return getItem(position)!!.name
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()
        val pullToLoadState = SelectedItemState(parcelable)
        pullToLoadState.selectedItem = selectedItem
        return pullToLoadState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SelectedItemState) return
        val pullToLoadState = state
        super.onRestoreInstanceState(pullToLoadState.superState)
        selectedItem = pullToLoadState.selectedItem
        invalidate()
    }

}