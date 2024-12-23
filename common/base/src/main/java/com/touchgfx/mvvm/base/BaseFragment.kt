package com.touchgfx.mvvm.base

import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.touchgfx.mvvm.base.livedata.MessageEvent
import com.touchgfx.mvvm.base.livedata.StatusEvent
import com.touchgfx.mvvm.base.widget.LoadingDialog
import java.lang.reflect.ParameterizedType

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/20 15:33
 * @desc 如果您继承使用了BaseFragment或其子类，你需要参照如下方式添加@AndroidEntryPoint注解
 *
 * @example Fragment
 * //-------------------------
 *    @AndroidEntryPoint
 *    public class YourFragment extends BaseFragment {
 *
 *    }
 * //-------------------------
 */
abstract class BaseFragment<VM : BaseViewModel<BaseModel>, VB : ViewBinding> : Fragment(), IView<VM, VB>, ILoading {

    /**
     * viewBinding
     */
    lateinit var viewBinding: VB

    /**
     * 请通过 [.getViewModel]获取，后续版本 [.mViewModel]可能会私有化
     */
    private var mViewModel: VM? = null

    lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        preInit()
        viewBinding = initViewBinding()
        initViewModel()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireContext())
        initData(savedInstanceState)
        initView()
    }

    /**
     * 页面初始化之前需要做的操作
     */
    override fun preInit() {

    }

    /**
     * 初始化 [.mViewModel]
     */
    private fun initViewModel() {
        mViewModel = createViewModel()
        if (mViewModel != null) {
            lifecycle.addObserver(mViewModel!!)
            registerLoadingEvent()
        }
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
    }

    /**
     * 注册状态监听
     */
    protected fun registerLoadingEvent() {
        mViewModel?.getLoadingEvent()?.observe(
                viewLifecycleOwner,
                Observer<Boolean?> { isLoading ->
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
        mViewModel?.getMessageEvent()?.observe(viewLifecycleOwner, observer)
    }

    /**
     * 注册单个消息事件，消息对象:[Message]
     * @param observer
     */
    protected fun registerSingleLiveEvent(observer: Observer<Message?>) {
        mViewModel?.getSingleLiveEvent()?.observe(viewLifecycleOwner, observer)
    }

    /**
     * 注册状态事件
     * @param observer
     */
    protected fun registerStatusEvent(observer: StatusEvent.StatusObserver) {
        mViewModel?.getStatusEvent()?.observe(viewLifecycleOwner, observer)
    }

    /**
     * 创建ViewModel
     * @return 默认为null，为null时，[.mViewModel]会默认根据当前Activity泛型 [VM]获得ViewModel
     */
    override fun createViewModel(): VM {
        return obtainViewModel(getVMClass())
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
     */
    fun <T : ViewModel?> obtainViewModel(modelClass: Class<T>): T {
        return createViewModelProvider(this)[modelClass]
    }

    /**
     * 通过 [.createViewModelProvider]获得 ViewModel
     * @param modelClass
     * @param <T>
     * @return
     */
    fun <T : ViewModel?> obtainViewModel(owner: ViewModelStoreOwner, modelClass: Class<T>): T {
        return createViewModelProvider(owner)[modelClass]
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