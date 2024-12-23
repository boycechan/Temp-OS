package com.touchgfx.mvvm.base.multitype

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/25 14:39
 * @desc BaseItemViewBinder
 */
abstract class BaseItemViewBinder<T, VH : BaseItemViewBinder.BaseViewHolder<T>> : ItemViewBinder<T, VH>() {

    protected var mItemClickListener: OnItemClickListener<T>? = null //item点击事件

    fun setOnItemClickListener(itemClickListener: OnItemClickListener<T>?) {
        mItemClickListener = itemClickListener
    }

    override fun onBindViewHolder(holder: VH, item: T) {
        holder.model = item
        holder.bindData(item)
    }

    /**
     * ViewHolder基类
     * @param <T> 实体bean，必须继承BaseModel
    </T> */
    abstract class BaseViewHolder<T>(itemView: View,
                                     private var mItemClickListener: OnItemClickListener<T>?) : RecyclerView.ViewHolder(itemView) {

        var model: T? = null

        init {
            itemView.setOnClickListener {
                model?.let { m -> mItemClickListener?.onItemClick(m) }
            }
        }

        /**
         * 绑定View
         * @param model
         */
        abstract fun bindData(model: T)
    }
}