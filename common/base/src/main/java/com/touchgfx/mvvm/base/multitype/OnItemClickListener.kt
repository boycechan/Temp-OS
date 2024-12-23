package com.touchgfx.mvvm.base.multitype

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/25 14:46
 * @desc 列表项点击监听
 */
interface OnItemClickListener<T> {
    /**
     * 点击事件
     * @param model    实体类
     */
    fun onItemClick(model: T)
}