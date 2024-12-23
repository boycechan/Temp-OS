package com.touchgfx.mvvm.base.widget.segmented

/**
 * Created by song on 2016/10/12.
 *
 */
interface ISegmentedControl {
    val count: Int
    fun getItem(position: Int): SegmentedControlItem?
    fun getName(position: Int): String?
}