package com.touchgfx.amap

import android.content.Context
import android.graphics.Color
import android.location.Location
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.amap.api.trace.LBSTraceClient
import com.amap.api.trace.TraceListener
import com.amap.api.trace.TraceLocation
import com.touchgfx.GPSUtil
import com.touchgfx.bean.LatLngPoint
import kotlin.math.roundToLong


/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/3/30 15:12
 * @desc 高德地图Model
 */
class AMapModel {

    private var locationClient: AMapLocationClient? = null

    private var locationOption: AMapLocationClientOption? = null

    fun setUpMap(aMap: AMap, locationSource: LocationSource) {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        aMap.mapTextZIndex = 2

        /**
         * 设置一些amap的属性
         */
        val uiSettings = aMap.uiSettings
        uiSettings.isZoomControlsEnabled = false //设置缩放按钮是否可见。
        uiSettings.isZoomGesturesEnabled = true
        uiSettings.isCompassEnabled = false // 设置指南针是否显示
        uiSettings.isRotateGesturesEnabled = true // 设置地图旋转是否可用
        uiSettings.isTiltGesturesEnabled = true // 设置地图倾斜是否可用
        uiSettings.isMyLocationButtonEnabled = false // 设置默认定位按钮是否显示
        /**
         * 自定义系统定位小蓝点
         */
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//定位一次，且将视角移动到地图中心点
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0))
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0))
        val myLocationIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_self)
        myLocationStyle.myLocationIcon(myLocationIcon)
        myLocationStyle.anchor(0.5f, 0.5f)
        aMap.myLocationStyle = myLocationStyle
        aMap.setLocationSource(locationSource) // 设置定位监听
        aMap.isMyLocationEnabled = true // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    /**
     * 初始化定位
     */
    @JvmOverloads
    fun initLocationClient(context: Context, locationListener: AMapLocationListener, isOnceLocation: Boolean = false): AMapLocationClient {
        if (locationClient == null) {
            //初始化定位
            locationClient = AMapLocationClient(context)
            //设置定位回调监听
            locationClient!!.setLocationListener(locationListener)
            val locationOption = getLocationClientOption(isOnceLocation)
            locationClient!!.setLocationOption(locationOption)
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//            locationClient!!.startLocation()
        }
        return locationClient!!
    }

    fun getLocationClientOption(isOnceLocation: Boolean = false): AMapLocationClientOption {
        if (locationOption == null) {
            //初始化AMapLocationClientOption对象
            locationOption = AMapLocationClientOption()
            // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
            locationOption!!.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Transport
            ////设置定位模式为设备模式Device_Sensors，Battery_Saving为低功耗模式，Hight_Accuracy高精度定位模式
            locationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            locationOption!!.interval = 2000
            //设置是否返回地址信息（默认返回地址信息）
            locationOption!!.isNeedAddress = true
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            locationOption!!.httpTimeOut = 20000
        }
        // 设置是否只定位一次，默认为false
        locationOption!!.isOnceLocation = isOnceLocation
        return locationOption!!
    }

    fun startLocation() {
        locationClient?.startLocation()
    }

    fun stopLocation() {
        locationClient?.stopLocation()
    }

    fun destroyLocation() {
        locationClient?.onDestroy()
    }

    fun queryProcessedTrace(context: Context, data: MutableList<LatLng>, traceCallback: (data: MutableList<LatLng>) -> Unit) {
        val lbsTraceClient = LBSTraceClient.getInstance(context)
        val hashCode = data.toString().hashCode()
        val traceLocations = mutableListOf<TraceLocation>()
        data.forEach {
            val traceLocation = TraceLocation()
            traceLocation.longitude = it.longitude
            traceLocation.latitude = it.latitude
            traceLocations.add(traceLocation)
        }
        lbsTraceClient.queryProcessedTrace(hashCode, traceLocations, LBSTraceClient.TYPE_AMAP, object : TraceListener {
            override fun onRequestFailed(lineID: Int, errorInfo: String?) {
                val compress = LatLngPoint.Douglas(data, 5.0).compress()
                traceCallback(compress)
            }

            override fun onTraceProcessing(lineID: Int, index: Int, segments: MutableList<LatLng>) {

            }

            override fun onFinished(lineID: Int, linepoints: MutableList<LatLng>, distance: Int, waitingtime: Int) {
                traceCallback(linepoints)
            }
        })
    }

    fun traceDrawLine(context: Context, aMap: AMap, width: Int, height: Int, data: MutableList<LatLng>?) {
        if (data == null || data.size <= 2) return
        val convertData = mutableListOf<LatLng>()
        data.forEach {
            val locationArray = GPSUtil.gps84_To_Gcj02(it.latitude, it.longitude)
            convertData.add(LatLng(locationArray[0], locationArray[1]))
        }

//        queryProcessedTrace(context, convertData, traceCallback = {
//            drawLine(context, aMap, width, height, it)
//        })

        val compress = LatLngPoint.Douglas(convertData, 5.0).compress()
        drawLine(context, aMap, width, height, compress)
    }

    fun drawLine(context: Context, aMap: AMap, width: Int, height: Int, data: MutableList<LatLng>?) {
        if (data == null || data.size < 2) return
        var lanSum = 0.0
        var lonSum = 0.0
        var starMark: MarkerOptions? = null
        var endMark: MarkerOptions? = null
        val options = PolylineOptions()

//        val argbEvaluator = ArgbEvaluator() //渐变色计算类
//        val colorStart = Color.parseColor("#FF5151")
//        val colorEnd = Color.parseColor("#10FF88")
//        val colorList: MutableList<Int> = ArrayList()

        val builder = LatLngBounds.Builder()
        for (i in data.indices) {
            val sourceLatLng = data[i]
            if (i == 0) {
                starMark = MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_path_start)).position(sourceLatLng)
            } else if (i == data.size - 1) {
                endMark = MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_path_end)).position(sourceLatLng)
            }
            options.add(sourceLatLng)
            builder.include(sourceLatLng)
            lanSum += sourceLatLng.latitude
            lonSum += sourceLatLng.longitude
            //计算渐变色
//            colorList.add(argbEvaluator.evaluate((i / data.lastIndex).toFloat(), colorStart, colorEnd) as Int)
        }
        //移动中心位置 不然加载可能显示非洲0,0
        val target = LatLng(lanSum / data.size, lonSum / data.size)
        aMap.moveCamera(CameraUpdateFactory.newLatLng(target))
        //将整个轨迹放入可见区域
        val width: Int = if (width > 0) width else context.resources.displayMetrics.widthPixels
        val height: Int = if (height > 0) height else context.resources.displayMetrics.heightPixels
        val padding = (width * 0.10).toInt()
        val bounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        aMap.animateCamera(cu)
        val polylineColor = Color.parseColor("#FF5151")
        options.width(dp2px(context, 6f)).geodesic(true)
                .color(polylineColor)
                .zIndex(999f)
//                .colorValues(colorList).useGradient(true)
        //轨迹绘制
        aMap.addPolyline(options)
        //添加起点
        aMap.addMarker(starMark)
        //添加结束点
        aMap.addMarker(endMark)
    }

    fun distance(latLngs: MutableList<LatLng>): Long {
        var distance = 0.0
        if (latLngs.size < 2) return distance.toLong()
        val convertData = mutableListOf<LatLng>()
        latLngs.forEach {
            val locationArray = GPSUtil.transform(it.latitude, it.longitude)
            convertData.add(LatLng(locationArray[0], locationArray[1]))
        }
        var pre = convertData[0]
        for (index in 1..convertData.lastIndex) {
            val latLng = convertData[index]
            distance += AMapUtils.calculateLineDistance(pre, latLng).toDouble()
            pre = latLng
        }
        return distance.roundToLong()
    }

    fun move2MyLocation(aMap: AMap?, curLocation: Location?) {
        var location = curLocation ?: aMap?.myLocation ?: return
        if (aMap == null) {
            return
        }
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(location.latitude, location.longitude)))
    }

    private fun dp2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale
    }

}