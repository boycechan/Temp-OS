package com.touchgui.sidebar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * @company TouchGFX
 * @author boyce.chan
 * @date 2022/3/11 1:35 下午
 * @desc SideBarLayout
 */
class SideBarLayout : RelativeLayout, SideBarSortView.OnIndexChangedListener {

    companion object {
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        fun px2sp(context: Context, pxValue: Float): Int {
            val fontScale: Float = context.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }
    }

    private lateinit var mContext: Context
    private var mLayout: View? = null
    private var mTvTips: TextView? = null
    private var mSortView: SideBarSortView? = null
    private val mList: List<String>? = null
    private var selectTextColor = 0
    private var unselectTextColor = 0
    private var selectTextSize = 0f
    private var unselectTextSize = 0f
    private var wordTextColor = 0
    private var wordTextSize = 0f
    private var wordBackground: Drawable? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
        initView()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        //获取自定义属性
        if (attrs != null) {
            val ta: TypedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SideBarView)
            unselectTextColor = ta.getColor(R.styleable.SideBarView_sidebarUnSelectTextColor, Color.parseColor("#1ABDE6"))
            selectTextColor = ta.getColor(R.styleable.SideBarView_sidebarSelectTextColor, Color.parseColor("#2E56D7"))
            selectTextSize = ta.getDimension(R.styleable.SideBarView_sidebarSelectTextSize, dip2px(mContext, 12f).toFloat())
            unselectTextSize = ta.getDimension(R.styleable.SideBarView_sidebarUnSelectTextSize, dip2px(mContext, 10f).toFloat())
            wordTextSize = ta.getDimension(R.styleable.SideBarView_sidebarWordTextSize, px2sp(mContext, 45f).toFloat())
            wordTextColor = ta.getColor(R.styleable.SideBarView_sidebarWordTextColor, Color.parseColor("#FFFFFF"))
            wordBackground = ta.getDrawable(R.styleable.SideBarView_sidebarWordBackground)
            if (wordBackground == null) {
                wordBackground = context.resources.getDrawable(R.drawable.sort_text_view_hint_bg)
            }
            ta.recycle()
        }
    }

    private fun initView() {
        //引入布局
        mLayout = LayoutInflater.from(mContext).inflate(R.layout.view_sidebar_layout, null, true)
        mTvTips = mLayout?.findViewById(R.id.tvTips) as TextView
        mSortView = mLayout?.findViewById(R.id.sortView) as SideBarSortView
        mSortView?.setIndexChangedListener(this)
        mSortView?.setTextColor(unselectTextColor)
        mSortView?.setTextSize(unselectTextSize)
        mSortView?.setTextColorChoose(selectTextColor)
        mSortView?.setTextSizeChoose(selectTextSize)
        mSortView?.invalidate()
        mTvTips?.setTextColor(wordTextColor)
        mTvTips?.textSize = px2sp(mContext, wordTextSize).toFloat()
        mTvTips?.background = wordBackground
        this.addView(mLayout) //将子布局添加到父容器,才显示控件
    }

    private var mListener: OnSideBarLayoutListener? = null

    fun setSideBarLayout(listener: OnSideBarLayoutListener?) {
        mListener = listener
    }

    override fun onSideBarScrollUpdateItem(word: String?) {
        mTvTips?.visibility = View.VISIBLE
        mTvTips?.text = word
        mListener?.onSideBarScrollUpdateItem(word)
    }

    override fun onSideBarScrollEndHideText() {
        mTvTips?.visibility = View.GONE
    }

    fun OnItemScrollUpdateText(word: String) {
        if (mListener != null) {
            mSortView?.onItemScrollUpdateText(word)
        }
    }

    interface OnSideBarLayoutListener {
        fun onSideBarScrollUpdateItem(word: String?)
    }
}