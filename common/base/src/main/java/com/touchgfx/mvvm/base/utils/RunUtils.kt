package com.touchgfx.mvvm.base.utils

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/4/1 15:27
 * @desc RunUtils
 */
object RunUtils {
    /**
     * 步数转公里
     */
    fun getDistanceByStep(steps: Long): Float {
        return steps * 0.6f / 1000
    }

    /**
     * 千卡路里计算公式
     */
    //String.format("%.1f", steps * 0.6f * 60 * 1.036f / 1000);
    fun getCalorieByStep(steps: Long): Float {
        return steps * 0.6f * 60 * 1.036f / 1000
    }


    /**
     * 千米转千卡路里
     * @param distance 公里
     * @param weight 体重 kg
     */
    fun getCalorieByDistance(distance: Double, weight: Double = 60.0): Double {
        return distance * weight * 1.036f //60为体重
    }

}