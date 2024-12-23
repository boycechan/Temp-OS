package com.touchgfx.mvvm.base.widget.toolbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.view.updateLayoutParams
import com.touchgfx.mvvm.base.R
import com.touchgfx.mvvm.base.extension.getScreenWidth
import kotlin.math.ceil


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/26 15:57
 * @desc TouchElxToolbar
 */
class TouchelxToolbar : LinearLayout {

    companion object {

        private const val DRAWABLE_LEFT = 0 //TextView 的左边
        private const val DRAWABLE_RIGHT = 2 //TextView 的右边
        private const val DRAWABLE_TOP = 1 //TextView 的上边
        private const val DRAWABLE_BOTTOM = 3 //TextView 的下边

    }

    private lateinit var mTvToolbarIcon: TextView
    private lateinit var mTvToolbarTitle: TextView
    private lateinit var mTvToolbarRight: TextView
    private lateinit var mViewGroup: ViewGroup
    private lateinit var mWebClose: ImageButton
    private lateinit var mToolbarLeft: LinearLayout

    @LayoutRes
    private var mCustomContentLayoutId = -1
    private var mSplitLineView: View? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        orientation = VERTICAL
        val at: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TouchelxToolbar)
        mCustomContentLayoutId = attrs?.getAttributeResourceValue(null, "layout", 0) ?: 0

        //默认布局ID
        if (mCustomContentLayoutId == 0) {
            mCustomContentLayoutId = resources.getIdentifier("default_toolbar_layout", "layout", context.packageName)
        }

        val defaultBg = resources.getColor(R.color.touchelx_toobar_bg)
        val bgColor = at.getColor(R.styleable.TouchelxToolbar_tb_background_color, defaultBg)
        val titleColor = at.getColor(R.styleable.TouchelxToolbar_tb_title_color, Color.TRANSPARENT)
        val leftTitleColor = at.getColor(R.styleable.TouchelxToolbar_tb_left_title_color, Color.TRANSPARENT)
        val rightTitleColor = at.getColor(R.styleable.TouchelxToolbar_tb_right_title_color, Color.TRANSPARENT)
        val title = at.getString(R.styleable.TouchelxToolbar_tb_title)
        val leftTitle = at.getString(R.styleable.TouchelxToolbar_tb_left_title)
        val rightTitle = at.getString(R.styleable.TouchelxToolbar_tb_right_title)
        val rightTitleSize = at.getDimensionPixelSize(R.styleable.TouchelxToolbar_tb_right_text_size, -1)
        val titleSize = at.getDimensionPixelSize(R.styleable.TouchelxToolbar_tb_title_text_size, -1)
        val titleStyle = at.getInt(R.styleable.TouchelxToolbar_tb_title_text_style, 1)//默认加粗
        val titleGravity = at.getInt(R.styleable.TouchelxToolbar_tb_title_gravity, -1)//默认加粗
        val rightTitleWidth = at.getDimensionPixelSize(R.styleable.TouchelxToolbar_tb_right_text_width, -1)
        val rightTitleHeight = at.getDimensionPixelSize(R.styleable.TouchelxToolbar_tb_right_text_height, -1)
        val bgTvRight = at.getResourceId(R.styleable.TouchelxToolbar_tb_right_bg, -1)
        val iconResIdLeft = at.getResourceId(R.styleable.TouchelxToolbar_tb_icon, -1)
        val iconResIdRight = at.getResourceId(R.styleable.TouchelxToolbar_tb_icon_right, -1)
        val isShowSplitLine = at.getBoolean(R.styleable.TouchelxToolbar_tb_enable_split_line, true)
        at.recycle()

        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_tool_bar_layout, this, true)
        mViewGroup = findViewById<View>(R.id.fl_toolbar_content) as ViewGroup
        mSplitLineView = findViewById(R.id.view_top_split_line)

        mViewGroup.setBackgroundColor(bgColor)
        if (mCustomContentLayoutId != -1) {
            val viewChild = LayoutInflater.from(context).inflate(mCustomContentLayoutId, this, false)
            mViewGroup.addView(viewChild)
        }
        mSplitLineView?.visibility = if (isShowSplitLine) View.VISIBLE else View.GONE

        //处理默认布局
        if (mCustomContentLayoutId == R.layout.default_toolbar_layout) {
            mTvToolbarIcon = view.findViewById(R.id.tv_toolbar_icon) as TextView
            mTvToolbarTitle = view.findViewById(R.id.tv_toolbar_title) as TextView
            mTvToolbarRight = view.findViewById(R.id.tv_toolbar_opt) as TextView
            mTvToolbarRight = view.findViewById(R.id.tv_toolbar_opt) as TextView
            mWebClose = view.findViewById(R.id.ib_toolbar_close)
            mToolbarLeft = view.findViewById(R.id.ll_toolbar_left)
            if (rightTitleColor != 0) {
                mTvToolbarRight.setTextColor(rightTitleColor)
            }
            mTvToolbarTitle.visibility = View.VISIBLE
            mTvToolbarTitle.text = title
            if (titleColor != 0) {
                mTvToolbarTitle.setTextColor(titleColor)
            }
            if (titleSize != -1) {
                mTvToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
            }
            mTvToolbarTitle.setTypeface(mTvToolbarTitle.typeface, titleStyle)

            setTitleGravity(titleGravity)

            if (rightTitleSize != -1) {
                mTvToolbarRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTitleSize.toFloat())
            }
            if (iconResIdLeft != -1) {
                ViewUtil.setDrawablePadding(mTvToolbarIcon, iconResIdLeft, 0)
            } else {
                ViewUtil.setDrawablePadding(mTvToolbarIcon, R.drawable.ic_back, 0)
            }
            if (!TextUtils.isEmpty(rightTitle)) {
                mTvToolbarRight.visibility = View.VISIBLE
                mTvToolbarRight.text = rightTitle
            }
            if (bgTvRight != -1) {
                mTvToolbarRight.setBackgroundResource(bgTvRight)
            }
            if (iconResIdRight != -1) {
                mTvToolbarRight.visibility = View.VISIBLE
                ViewUtil.setDrawablePadding(mTvToolbarRight, iconResIdRight, 0)
            }
            if (rightTitleHeight != -1) {
                val lp = mTvToolbarRight.layoutParams
                lp.height = rightTitleHeight
                lp.width = rightTitleWidth
                mTvToolbarRight.layoutParams = lp
            }
            mTvToolbarIcon.text = leftTitle
            if (leftTitleColor != 0) {
                mTvToolbarIcon.setTextColor(leftTitleColor)
            }

            mTvToolbarRight.viewTreeObserver.addOnGlobalLayoutListener {
                val listener = this

            }
        }
    }

    /**
     * 设置文本居中
     */
    fun setTitleGravity(gravity: Int) {
        if (gravity != -1) {
            mTvToolbarTitle.gravity = gravity
            if (gravity == Gravity.CENTER || gravity == Gravity.CENTER_HORIZONTAL) {
                mTvToolbarTitle.setPadding(0, 0, 0, 0)
            }
        }
    }

    fun setTitleSize(textSize: Float) {
        mTvToolbarTitle.setTextSize(textSize)
    }

    fun setRootBackgroundColor(resColorId: Int) {
        mViewGroup.setBackgroundColor(resources.getColor(resColorId))
    }

    private fun isDefaultLayout(): Boolean {
        return mCustomContentLayoutId == R.layout.default_toolbar_layout
    }

    /**
     * 返回箭头的回调处理
     *
     * @param clickListener：
     */
    fun setBackAction(clickListener: OnClickListener?) {
        if (clickListener != null && isDefaultLayout()) {
            mTvToolbarIcon.setOnClickListener(clickListener)
        }
    }

    /**
     * 左边按钮的回调处理
     *
     * @param clickListener：
     */
    fun setRigthAction(clickListener: OnClickListener?) {
        if (clickListener != null && isDefaultLayout()) {
            mTvToolbarRight.setOnClickListener(clickListener)
        }
    }

    /**
     * 左边按钮的回调处理
     *
     * @param clickListener：
     */
    fun setWebCloseAction(clickListener: OnClickListener?) {
        if (clickListener != null && isDefaultLayout()) {
            mWebClose.setOnClickListener(clickListener)
        }
    }

    /**
     * 代码中更换返回图标
     *
     * @param resId：
     */
    fun setToolbarIcon(resId: Int) {
        if (isDefaultLayout()) {
            ViewUtil.setDrawablePadding(mTvToolbarIcon, resId, DRAWABLE_RIGHT)
        }
    }

    /**
     * 代码中更换右边图标
     *
     * @param resId：
     */
    fun setToolbarRightIcon(@DrawableRes resId: Int) {
        if (isDefaultLayout()) {
            mTvToolbarRight.visibility = View.VISIBLE
            ViewUtil.setDrawablePadding(mTvToolbarRight, resId, DRAWABLE_RIGHT)
        }
    }

    /**
     * 代码中设置标题
     *
     * @param resId：
     */
    fun setToolbarTitle(resId: Int) {
        if (isDefaultLayout()) {
            if (!mTvToolbarTitle.isShown) {
                mTvToolbarTitle.visibility = View.VISIBLE
            }
            mTvToolbarTitle.setText(resId)
        }
    }

    /**
     * 代码中设置标题
     *
     * @param resContent：
     */
    fun setToolbarTitle(resContent: CharSequence?) {
        if (isDefaultLayout()) {
            if (!mTvToolbarTitle.isShown) {
                mTvToolbarTitle.visibility = View.VISIBLE
            }
            mTvToolbarTitle.text = resContent
        }
    }

    /**
     * 代码中设置标题
     *
     * @param resContent：
     */
    fun setTitleColor(color: Int) {
        if (isDefaultLayout()) {
            if (!mTvToolbarTitle.isShown) {
                mTvToolbarTitle.visibility = View.VISIBLE
            }
            mTvToolbarTitle.setTextColor(color)
        }
    }

    /**
     * 代码中设置左边标题
     *
     * @param resId：
     */
    fun setToolbarLeftTitle(resId: Int) {
        if (isDefaultLayout()) {
            if (!mTvToolbarIcon.isShown) {
                mTvToolbarIcon.visibility = View.VISIBLE
            }
            mTvToolbarIcon.setText(resId)
        }
    }

    /**
     * 代码中设置左边标题
     *
     * @param resContent：
     */
    fun setToolbarLeftTitle(resContent: CharSequence?) {
        if (isDefaultLayout()) {
            if (!mTvToolbarIcon.isShown) {
                mTvToolbarIcon.visibility = View.VISIBLE
            }
            mTvToolbarIcon.text = resContent
        }
    }

    /**
     * 代码中设置右边标题
     *
     * @param resId：
     */
    fun setToolbarRightTitle(resId: Int) {
        if (isDefaultLayout()) {
            if (!mTvToolbarRight.isShown) {
                mTvToolbarRight.visibility = View.VISIBLE
            }
            mTvToolbarRight.setText(resId)
        }
    }

    /**
     * 代码中设置右边标题
     *
     * @param resContent：
     */
    fun setToolbarRightTitle(resContent: CharSequence?) {
        if (isDefaultLayout()) {
            if (!mTvToolbarRight.isShown) {
                mTvToolbarRight.visibility = View.VISIBLE
            }
            mTvToolbarRight.text = resContent
        }
    }

    /**
     * 代码中设置右边标题颜色
     *
     * @param color：
     */
    fun setToolbarRightColor(color: String?) {
        if (isDefaultLayout()) {
            if (!mTvToolbarRight.isShown) {
                mTvToolbarRight.visibility = View.VISIBLE
            }
            mTvToolbarRight.setTextColor(Color.parseColor(color))
        }
    }

    /**
     * 代码中设置右边标题bg
     *
     * @param resId：
     */
    fun setToolbarRightBg(resId: Int) {
        if (isDefaultLayout()) {
            if (!mTvToolbarRight.isShown) {
                mTvToolbarRight.visibility = View.VISIBLE
            }
            mTvToolbarRight.setBackgroundResource(resId)
        }
    }

    /**
     * 代码中设置右边标题是否可点击
     *
     * @param isEnable：
     */
    fun setToolbarRightEnable(isEnable: Boolean) {
        if (isDefaultLayout()) {
            mTvToolbarRight.isEnabled = isEnable
        }
    }

    /**
     * 代码中获取标题名
     */
    val title: CharSequence?
        get() = if (isDefaultLayout()) {
            mTvToolbarTitle.text
        } else null

    /**
     * 代码中获取右边标题名
     */
    val rightTitle: CharSequence?
        get() = if (isDefaultLayout()) {
            mTvToolbarRight.text
        } else null

    /**
     * 代码中获取右边标题TextView
     */
    val tvToolbarRight: TextView?
        get() = if (isDefaultLayout()) {
            mTvToolbarRight
        } else null

    /**
     * 代码中获取返回控件
     */
    val tvToolbarIcon: TextView?
        get() = if (isDefaultLayout()) {
            mTvToolbarIcon
        } else null

    /**
     * 头部分割线是否显示
     */
    fun showSplitLine(isShow: Boolean) {
        if (mSplitLineView != null) {
            mSplitLineView?.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    /**
     * 代码中获取关闭控件
     */
    fun webCloseVisible(visible: Boolean) {
        if (isDefaultLayout()) {
            mTvToolbarTitle.updateLayoutParams {
                if (this is FrameLayout.LayoutParams) {
                    var backIcoWidth = mTvToolbarIcon.width
                    if (backIcoWidth == 0) {
                        backIcoWidth = context.resources.getDimension(R.dimen.dp_44).toInt()
                    }
                    if (visible) {
                        mWebClose.visibility = View.VISIBLE
                        val textWidth = getTextWidth(mTvToolbarTitle.paint, mTvToolbarTitle.text.toString())
                        val screenWidth = context.getScreenWidth()
                        val offset = backIcoWidth * 2
                        leftMargin = offset
                        if (screenWidth - textWidth - offset > offset) {
                            rightMargin = offset
                        } else {
                            gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                        }
                    } else {
                        mWebClose.visibility = View.GONE
                        leftMargin = backIcoWidth
                        rightMargin = backIcoWidth
                    }
                }
            }

        }
    }

    fun getTextWidth(paint: Paint, str: String?): Int { //calculate text width
        var iRet = 0
        if (str != null && str.isNotEmpty()) {
            val len = str.length
            val widths = FloatArray(len)
            paint.getTextWidths(str, widths)
            for (j in 0 until len) {
                iRet += ceil(widths[j].toDouble()).toInt()
            }
        }
        return iRet
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (isDefaultLayout()) {
            if (mTvToolbarRight.text.isNotEmpty() && mTvToolbarRight.visibility == View.VISIBLE) {
                mTvToolbarTitle.updateLayoutParams {
                    if (this is FrameLayout.LayoutParams) {
                        val margin = resources.getDimension(R.dimen.dp_44).toInt()
                        val offset = resources.getDimension(R.dimen.dp_5).toInt()
                        val leftWidth = mToolbarLeft.width
                        val rightWidth = mTvToolbarRight.width
                        val max = Math.max(leftWidth, rightWidth)
                        val textWidth = getTextWidth(mTvToolbarTitle.paint, mTvToolbarTitle.text.toString())
                        val screenWidth = context.getScreenWidth()
                        when (mTvToolbarTitle.gravity) {
                            Gravity.CENTER -> {
                                if (screenWidth - textWidth - max * 2 - margin - offset > margin) {
                                    leftMargin = margin + max + offset
                                    rightMargin = leftMargin
                                } else {
                                    mTvToolbarTitle.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                                }
                            }
                            Gravity.LEFT, Gravity.LEFT or Gravity.CENTER_VERTICAL -> {
                                leftMargin = margin + leftWidth
                                rightMargin = margin + rightWidth + offset
                            }
                        }

                    }
                }
            }
        }
    }
}