package com.touchgfx.mvvm.base.widget

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.touchgfx.mvvm.base.R
import com.touchgfx.mvvm.base.extension.getScreenHeight
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/24 15:51
 * @desc LoadingDialog
 */
class LoadingDialog @Inject constructor(@ActivityContext context: Context) : BaseProgressDialog(context) {

    private lateinit var loadingView: ImageView
    private lateinit var loadingHint: TextView
    private lateinit var loadAnimation: Animation

    override fun initUI() {
        super.initUI()
        val contentView = layoutInflater.inflate(R.layout.dialog_loading, null)
        setContentView(contentView)
        window!!.attributes.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        val lp: WindowManager.LayoutParams? = window?.attributes
        val px = context.resources.getDimensionPixelSize(R.dimen.dp_200)
        lp?.y = (context.getScreenHeight() * 0.27).toInt()
        lp?.width = px // 宽度
        lp?.height = px // 高度
        window?.attributes = lp // WindowManager m = getWindowManager();

        loadingView = contentView.findViewById(R.id.dialog_loading_view)
        loadingHint = contentView.findViewById(R.id.dialog_loading_hint)
        loadAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim)
        val linearInterpolator = LinearInterpolator()
        loadAnimation.interpolator = linearInterpolator
    }

    fun setLoadingHint(text: String = context.getString(R.string.processing)) {
        loadingHint.text = text
    }

    fun show(cancelable: Boolean = true) {
        setCancelable(cancelable)
        super.show()
        loadingView.startAnimation(loadAnimation)
    }

    override fun dismiss() {
        super.dismiss()
        loadingView.clearAnimation()
    }
}