package com.touchgfx.mvvm.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.widget.Button
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LanguageUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.touchgfx.mvvm.base.livedata.MessageEvent
import com.touchgfx.mvvm.base.livedata.StatusEvent
import com.touchgfx.mvvm.base.widget.LoadingDialog
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 15:30
 * @desc
 * TouchGfxFrame 框架基于Google官方的 JetPack 构建，TouchGfxFrame，需遵循一些规范：
 *
 * 如果您继承使用了BaseActivity或其子类，你需要参照如下方式添加@AndroidEntryPoint注解
 */
abstract class BaseActivity<VM : BaseViewModel<BaseModel>, VB : ViewBinding> : AppCompatActivity(),
        IView<VM, VB>, ILoading {

    lateinit var viewBinding: VB

    /**
     * 请通过 [.getViewModel]获取，后续版本 [.mViewModel]可能会私有化
     */
    private var mViewModel: VM? = null

    lateinit var loadingDialog: LoadingDialog

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LanguageUtils.attachBaseContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //透明状态栏transparency star
//        OtherUtils.setAndroidNativeLightStatusBar(this,false)
        Timber.i("current Activity is ${getContext().javaClass.simpleName}")

        preInit()

        immersionBar {
            fitsSystemWindows(!isUseFullScreenMode())
            if (!isUseFullScreenMode()) {
                setStatusBarColor(R.color.status_bar_color)
                autoStatusBarDarkModeEnable(true)
            } else {
                statusBarDarkFont(isStatusBarDarkFont())
            }
        }

        viewBinding = initViewBinding()
        setContentView(viewBinding.root)
        loadingDialog = LoadingDialog(this)
        initViewModel()
        initData(savedInstanceState)
        initView()
    }

    /**
     * 页面初始化之前需要做的操作
     */
    override fun preInit() {

    }

    /**
     *  全屏模式布局穿透系统状态栏
     */
    protected open fun isUseFullScreenMode(): Boolean {
        return false
    }

    /**
     * 系统状态栏深色字体
     */
    protected open fun isStatusBarDarkFont(): Boolean {
        return false
    }

    /**
     * 设置状态栏颜色
     */
    protected fun setStatusBarColor(@ColorRes statusBarColor: Int) {
        immersionBar {
            statusBarColor(statusBarColor)
        }
    }

    /**
     * 初始化 [.mViewModel]
     */
    private fun initViewModel() {
        mViewModel = createViewModel()
        mViewModel?.let {
            lifecycle.addObserver(it)
        }
        registerLoadingEvent()
    }

    private fun getVMClass(): Class<VM> {
        var cls: Class<*>? = javaClass
        var vmClass: Class<VM>? = null
        while (vmClass == null && cls != null) {
            vmClass = getVMClass(cls) as Class<VM>?
            cls = cls.superclass
        }
        if (vmClass == null) {
            vmClass = BaseViewModel::class.java as Class<VM>
        }
        return vmClass
    }

    private fun getVMClass(cls: Class<*>): Class<*>? {
        val type = cls.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (t in types) {
                if (t is Class<*>) {
                    if (BaseViewModel::class.java.isAssignableFrom(t)) {
                        return t
                    }
                } else if (t is ParameterizedType) {
                    val rawType = t.rawType
                    if (rawType is Class<*>) {
                        if (BaseViewModel::class.java.isAssignableFrom(rawType)) {
                            return rawType
                        }
                    }
                }
            }
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel?.let {
            lifecycle.removeObserver(it)
            mViewModel = null
        }
        hideLoading()
    }

    /**
     * 注册状态加载事件
     */
    protected fun registerLoadingEvent() {
        mViewModel?.getLoadingEvent()
                ?.observe(this, Observer { isLoading ->
                    if (isLoading!!) {
                        showLoading()
                    } else {
                        hideLoading()
                    }
                })
    }

    override fun showLoading(cancelable: Boolean) {
        if (!loadingDialog.isShowing) {
            loadingDialog.show(cancelable)
        }
    }

    override fun hideLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    /**
     * 注册消息事件
     */
    protected fun registerMessageEvent(observer: MessageEvent.MessageObserver) {
        getViewModel()?.getMessageEvent()?.observe(this, observer)
    }

    /**
     * 注册单个消息事件，消息对象:[Message]
     * @param observer
     */
    protected fun registerSingleLiveEvent(observer: Observer<Message?>) {
        getViewModel()?.getSingleLiveEvent()?.observe(this, observer)
    }

    /**
     * 注册状态事件
     * @param observer
     */
    protected fun registerStatusEvent(observer: StatusEvent.StatusObserver) {
        getViewModel()?.getStatusEvent()?.observe(this, observer)
    }

    fun getContext(): Context {
        return this
    }

    /**
     * 创建ViewModel
     * @return [.mViewModel]会默认根据当前Activity泛型 [VM]获得ViewModel
     */
    override fun createViewModel(): VM {
        return obtainViewModel(getVMClass()!!)
    }

    /**
     * 获取 ViewModel
     * @return [.mViewModel]
     */
    fun getViewModel(): VM? {
        return mViewModel
    }


    /**
     * 通过 [.createViewModelProvider]获得 ViewModel
     * @param modelClass
     * @param <T>
     * @return
    </T> */
    fun <T : ViewModel?> obtainViewModel(modelClass: Class<T>): T {
        return createViewModelProvider(this)[modelClass]
    }

    /**
     * 创建 [ViewModelProvider]
     * @param owner
     * @return
     */
    private fun createViewModelProvider(owner: ViewModelStoreOwner): ViewModelProvider {
        return ViewModelProvider(owner)
    }
}
