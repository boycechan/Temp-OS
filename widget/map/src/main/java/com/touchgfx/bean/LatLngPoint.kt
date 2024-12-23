package com.touchgfx.bean

import android.graphics.Point
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import java.util.ArrayList
import kotlin.math.abs

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/30 15:52
 * @desc 存储得到的坐标点，并进行抽稀算法，对坐标进行压缩
 * @param id 用于记录每一个点的序号
 * @param latLng 每一个点的经纬度
 */
class LatLngPoint(var id: Int, var latLng: LatLng) : Point(), Comparable<LatLngPoint> {

    override operator fun compareTo(o: LatLngPoint): Int {
        if (id < o.id) {
            return -1
        } else if (id > o.id) return 1
        return 0
    }

    /**
     * 道格拉斯算法
     */
    class Douglas {

        private var dMax = 0.0
        private var start = 0
        private var end = 0
        private val mLineInit: MutableList<LatLngPoint> = ArrayList()

        constructor(mLineInit: MutableList<LatLng>, dmax: Double) {
            dMax = dmax
            start = 0
            end = mLineInit.size - 1
            for (i in mLineInit.indices) {
                this.mLineInit.add(LatLngPoint(i, mLineInit[i]!!))
            }
        }

        /**
         * 压缩经纬度点
         *
         * @return
         */
        fun compress(): ArrayList<LatLng> {
            val size = mLineInit.size
            val latLngPoints = compressLine(mLineInit.toTypedArray(), mLineInit as ArrayList<LatLngPoint>, start, end, dMax)
            latLngPoints.add(mLineInit[0])
            latLngPoints.add(mLineInit[size - 1])
            //对抽稀之后的点进行排序
            latLngPoints.sortWith(Comparator { o1, o2 -> o1.compareTo(o2) })
            val latLngs = ArrayList<LatLng>()
            for (point in latLngPoints) {
                latLngs.add(point.latLng)
            }
            return latLngs
        }

        /**
         * 根据最大距离限制，采用DP方法递归的对原始轨迹进行采样，得到压缩后的轨迹
         * x
         *
         * @param originalLatLngs 原始经纬度坐标点数组
         * @param endLatLngs      保持过滤后的点坐标数组
         * @param start           起始下标
         * @param end             结束下标
         * @param dMax            预先指定好的最大距离误差
         */
        private fun compressLine(originalLatLngs: Array<LatLngPoint>, endLatLngs: ArrayList<LatLngPoint>, start: Int, end: Int, dMax: Double): ArrayList<LatLngPoint> {
            if (start < end) {
                //递归进行调教筛选
                var maxDist = 0.0
                var currentIndex = 0
                for (i in start + 1 until end) {
                    val currentDist = distToSegment(originalLatLngs[start], originalLatLngs[end], originalLatLngs[i])
                    if (currentDist > maxDist) {
                        maxDist = currentDist
                        currentIndex = i
                    }
                }
                //若当前最大距离大于最大距离误差
                if (maxDist >= dMax) {
                    //将当前点加入到过滤数组中
                    endLatLngs.add(originalLatLngs[currentIndex])
                    //将原来的线段以当前点为中心拆成两段，分别进行递归处理
                    compressLine(originalLatLngs, endLatLngs, start, currentIndex, dMax)
                    compressLine(originalLatLngs, endLatLngs, currentIndex, end, dMax)
                }
            }
            return endLatLngs
        }


        /**
         * 使用三角形面积（使用海伦公式求得）相等方法计算点pX到点pA和pB所确定的直线的距离
         * @param start  起始经纬度
         * @param end    结束经纬度
         * @param center 前两个点之间的中心点
         * @return 中心点到 start和end所在直线的距离
         */
        private fun distToSegment(start: LatLngPoint, end: LatLngPoint, center: LatLngPoint): Double {
            val a = abs(AMapUtils.calculateLineDistance(start.latLng, end.latLng)).toDouble()
            val b = abs(AMapUtils.calculateLineDistance(start.latLng, center.latLng)).toDouble()
            val c = abs(AMapUtils.calculateLineDistance(end.latLng, center.latLng)).toDouble()
            val p = (a + b + c) / 2.0
            val s = Math.sqrt(Math.abs(p * (p - a) * (p - b) * (p - c)))
            return s * 2.0 / a
        }

    }
}