package com.touchgfx.mvvm.base

import android.app.Application
import android.os.Message
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.touchgfx.mvvm.base.http.ApiException
import com.touchgfx.mvvm.base.livedata.MessageEvent
import com.touchgfx.mvvm.base.livedata.SingleLiveEvent
import com.touchgfx.mvvm.base.livedata.StatusEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/19 16:40
 * @desc
 * TouchGfxFrame 框架基于Google官方的 JetPack 构建，TouchGfxFrame，需遵循一些规范：
 *
 * 如果您继承使用了BaseViewModel或其子类，你需要参照如下方式在类上和构造函数上添加@HiltViewModel和@Inject注解
 *
 * @example BaseViewModel
 * //-------------------------
 *    @HiltViewModel
 *    public class YourViewModel extends BaseViewModel<YourModel> {
 *        @Inject
 *        public YourViewModel(@NonNull Application application, YourModel model) {
 *            super(application, model);
 *        }
 *    }
 * //-------------------------
 *
 * 如果您继承使用了BaseModel或其子类，你需要参照如下方式在构造函数上添加@Inject注解
 *
 * @example BaseModel
 * //-------------------------
 *    public class YourModel extends BaseModel {
 *        @Inject
 *        public YourModel(IDataRepository dataRepository){
 *            super(dataRepository);
 *        }
 *    }
 * //-------------------------
 *
 *
 * 标准MVVM模式中的VM (ViewModel)层基类
 */
@HiltViewModel
open class BaseViewModel<out M : BaseModel> : AndroidViewModel, IViewModel {
    /**
     * 请通过 [.getModel] 获取，后续版本 [.mModel]可能会私有化
     */
    private var mModel: M? = null

    /**
     * 消息事件
     */
    private val mMessageEvent: MessageEvent = MessageEvent()

    /**
     * 状态事件
     */
    private val mStatusEvent: StatusEvent = StatusEvent()

    /**
     * 加载状态
     */
    private val mLoadingEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    /**
     * 提供自定义单一消息事件
     */
    private val mSingleLiveEvent: SingleLiveEvent<Message> = SingleLiveEvent()

    /**
     * 继承者都将使用此构造
     * @param application
     * @param model
     */
    constructor(application: Application, model: M) : super(application) {
        mModel = model
    }

    /**
     * 特殊构造，仅供内部使用
     * 增加@Inject注解
     * @param application
     */
    @Inject
    constructor(application: Application) : super(application)

    override fun onCreate() {}
    override fun onStart() {}
    override fun onResume() {}
    override fun onPause() {}
    override fun onStop() {}
    override fun onDestroy() {
        if (mModel != null) {
            mModel?.onDestroy()
            mModel = null
        }
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {

    }

    /**
     * [M]
     * @return [.mModel]
     */
    fun getModel(): M? {
        return mModel
    }

    /**
     * 暴露给观察者提供加载事件，[BaseActivity] 或 [BaseFragment] 已默认注册加载事件，
     * 只需调用[.showLoading] 或 [.hideLoading]即可在[BaseActivity]
     * 或 [BaseFragment] 中收到订阅事件
     * @return [.mLoadingEvent]
     */
    fun getLoadingEvent(): SingleLiveEvent<Boolean> {
        return mLoadingEvent
    }

    /**
     * 暴露给观察者提供消息事件，通过注册[BaseActivity.registerMessageEvent]或
     * [BaseFragment.registerMessageEvent] 或
     * [BaseDialogFragment.registerMessageEvent]接收消息事件
     * @return [.mMessageEvent]
     */
    fun getMessageEvent(): MessageEvent {
        return mMessageEvent
    }

    /**
     * 暴露给观察者提供状态变化事件，通过注册[BaseActivity.registerStatusEvent]或
     * [BaseFragment.registerStatusEvent] 或
     * [BaseDialogFragment.registerStatusEvent]接收消息事件
     * @return [.mStatusEvent]
     */
    fun getStatusEvent(): StatusEvent {
        return mStatusEvent
    }

    /**
     * 暴露给观察者提供接收单个消息事件，通过注册[BaseActivity.registerSingleLiveEvent]或
     * [BaseFragment.registerSingleLiveEvent] 或
     * [BaseDialogFragment.registerSingleLiveEvent]接收消息事件
     * @return [.mSingleLiveEvent]
     */
    fun getSingleLiveEvent(): SingleLiveEvent<Message> {
        return mSingleLiveEvent
    }

    /**
     * 发送消息，通过注册[BaseActivity.registerMessageEvent]或
     * [BaseFragment.registerMessageEvent] 或
     * [BaseDialogFragment.registerMessageEvent]接收消息事件，
     * 也可通过观察[.getMessageEvent]接收消息事件
     * @param msgId 资源文件id
     */
    @MainThread
    fun sendMessage(@StringRes msgId: Int) {
        sendMessage(msgId, false)
    }

    /**
     * 发送消息，通过注册[BaseActivity.registerMessageEvent]或
     * [BaseFragment.registerMessageEvent] 或
     * [BaseDialogFragment.registerMessageEvent]接收消息事件，
     * 也可通过观察[.getMessageEvent]接收消息事件
     * @param msgId 资源文件id
     * @param post 如果为`true`则可以在子线程调用，相当于调用[MutableLiveData.postValue]，
     * 如果为`false` 相当于调用[MutableLiveData.setValue]
     */
    fun sendMessage(@StringRes msgId: Int, post: Boolean) {
        sendMessage(getApplication<Application>().getString(msgId), post)
    }

    /**
     * 发送消息，通过注册[BaseActivity.registerMessageEvent]或
     * [BaseFragment.registerMessageEvent] 或
     * [BaseDialogFragment.registerMessageEvent]接收消息事件，
     * 也可通过观察[.getMessageEvent]接收消息事件
     * @param message 消息内容
     */
    @MainThread
    fun sendMessage(message: String?) {
        mMessageEvent.setValue(message)
    }

    /**
     * 发送消息，通过注册[BaseActivity.registerMessageEvent]或
     * [BaseFragment.registerMessageEvent] 或
     * [BaseDialogFragment.registerMessageEvent]接收消息事件，
     * 也可通过观察[.getMessageEvent]接收消息事件
     * @param message 消息内容
     * @param post 如果为`true`则可以在子线程调用，相当于调用[MutableLiveData.postValue]，
     * 如果为`false` 相当于调用[MutableLiveData.setValue]
     */
    fun sendMessage(message: String?, post: Boolean) {
        if (post) {
            mMessageEvent.postValue(message)
        } else {
            mMessageEvent.setValue(message)
        }
    }

    /**
     * 更新状态，通过注册[BaseActivity.registerStatusEvent]或
     * [BaseFragment.registerStatusEvent] 或
     * [BaseDialogFragment.registerStatusEvent]接收消息事件，
     * 也可通过观察[.getStatusEvent]接收消息事件
     * @param status
     */
    @MainThread
    fun updateStatus(@StatusEvent.Status status: Int) {
        updateStatus(status, false)
    }

    /**
     * 更新状态，通过注册[BaseActivity.registerStatusEvent]或
     * [BaseFragment.registerStatusEvent] 或
     * [BaseDialogFragment.registerStatusEvent]接收消息事件，
     * 也可通过观察[.getStatusEvent]接收消息事件
     * @param status
     * @param post 如果为`true`则可以在子线程调用，相当于调用[MutableLiveData.postValue]，
     * 如果为`false` 相当于调用[MutableLiveData.setValue]
     */
    fun updateStatus(@StatusEvent.Status status: Int, post: Boolean) {
        if (post) {
            mStatusEvent.postValue(status)
        } else {
            mStatusEvent.setValue(status)
        }
    }

    /**
     * 发送单个消息事件，消息为[Message]对象，可通过[Message.what]区分消息类型，用法与[Message]一致，
     * 通过注册[BaseActivity.registerSingleLiveEvent]或
     * [BaseFragment.registerSingleLiveEvent] 或
     * [BaseDialogFragment.registerSingleLiveEvent]接收消息事件，
     * 也可通过观察[.getSingleLiveEvent]接收消息事件
     * @param what
     */
    @MainThread
    fun sendSingleLiveEvent(what: Int) {
        sendSingleLiveEvent(what, false)
    }

    /**
     * 发送单个消息事件，消息为[Message]对象，可通过[Message.what]区分消息类型，用法与[Message]一致，
     * 通过注册[BaseActivity.registerSingleLiveEvent]或
     * [BaseFragment.registerSingleLiveEvent] 或
     * [BaseDialogFragment.registerSingleLiveEvent]接收消息事件，
     * 也可通过观察[.getSingleLiveEvent]接收消息事件
     * @param what
     * @param post 如果为`true`则可以在子线程调用，相当于调用[MutableLiveData.postValue]，
     * 如果为`false` 相当于调用[MutableLiveData.setValue]
     */
    fun sendSingleLiveEvent(what: Int, post: Boolean) {
        val message = Message.obtain()
        message.what = what
        sendSingleLiveEvent(message, post)
    }

    /**
     * 发送单个消息事件，消息为[Message]对象，可通过[Message.what]区分消息类型，用法与[Message]一致，
     * 通过注册[BaseActivity.registerSingleLiveEvent]或
     * [BaseFragment.registerSingleLiveEvent] 或
     * [BaseDialogFragment.registerSingleLiveEvent]接收消息事件，
     * 也可通过观察[.getSingleLiveEvent]接收消息事件
     * @param message
     */
    @MainThread
    fun sendSingleLiveEvent(message: Message?) {
        sendSingleLiveEvent(message, false)
    }

    /**
     * 发送单个消息事件，消息为[Message]对象，可通过[Message.what]区分消息类型，用法与[Message]一致，
     * 通过注册[BaseActivity.registerSingleLiveEvent]或
     * [BaseFragment.registerSingleLiveEvent] 或
     * [BaseDialogFragment.registerSingleLiveEvent]接收消息事件，
     * 也可通过观察[.getSingleLiveEvent]接收消息事件
     * @param message
     * @param post 如果为`true`则可以在子线程调用，相当于调用[MutableLiveData.postValue]，
     * 如果为`false` 相当于调用[MutableLiveData.setValue]
     */
    fun sendSingleLiveEvent(message: Message?, post: Boolean) {
        if (post) {
            mSingleLiveEvent.postValue(message)
        } else {
            mSingleLiveEvent.setValue(message)
        }
    }

    @MainThread
    fun sendLiveMessageEvent(message: Message?) {
        sendSingleLiveEvent(message, false)
    }

    /**
     * 调用此类会同步通知执行[BaseActivity.showLoading]或[BaseFragment.showLoading]或
     * [BaseDialogFragment.showLoading]
     */
    @MainThread
    fun showLoading() {
        showLoading(false)
    }

    /**
     * 调用此类会同步通知执行[BaseActivity.showLoading]或[BaseFragment.showLoading]或
     * [BaseDialogFragment.showLoading]
     */
    fun showLoading(post: Boolean) {
        if (post) {
            mLoadingEvent.postValue(true)
        } else {
            mLoadingEvent.setValue(true)
        }
    }

    /**
     * 调用此类会同步通知执行[BaseActivity.hideLoading]或[BaseFragment.hideLoading]或
     * [BaseDialogFragment.hideLoading]
     */
    @MainThread
    fun hideLoading() {
        hideLoading(false)
    }

    /**
     * 调用此类会同步通知执行[BaseActivity.hideLoading]或[BaseFragment.hideLoading]或
     * [BaseDialogFragment.hideLoading]
     * @param post 如果为`true`则可以在子线程调用，相当于调用[MutableLiveData.postValue]，
     * 如果为`false` 相当于调用[MutableLiveData.setValue]
     */
    fun hideLoading(post: Boolean) {
        if (post) {
            mLoadingEvent.postValue(false)
        } else {
            mLoadingEvent.setValue(false)
        }
    }

    fun launch(showLoading: Boolean = true, block: suspend () -> Unit) {
        launch(showLoading, block, {
            Timber.w(it)
            sendMessage(it.message)
        })
    }

    fun launch(showLoading: Boolean, block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            if (showLoading) {
                showLoading()
            }
            block()
        } catch (e: Throwable) {
            error(e)
            tokenExpried(e)
        }
        if (showLoading) {
            hideLoading()
        }

    }

    private fun tokenExpried(e: Throwable) {
        if (e is ApiException && e.isTokenExpried) {
            EventBus.getDefault().post(e)
        }
    }
}
