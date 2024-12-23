package com.touchgfx.amap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.GpsStatus
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.*
import com.touchgfx.GPSUtil
import kotlin.math.roundToLong

/**
 * @company TouchGFX
 * @author chenxiangbo
 * @date 2021/4/8 15:43
 * @desc 谷歌Model
 */
@SuppressLint("MissingPermission")
class GMapModel {

    private var locationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var onLocationListener: OnGLocationListener? = null

    private var locationManager: LocationManager? = null
    private var onGpsStatusListener: OnGpsStatusListener? = null


    fun setUpMap(gMap: GoogleMap, locationSource: LocationSource) {
        gMap.isMyLocationEnabled = true
        gMap.uiSettings?.isMyLocationButtonEnabled = false
        gMap.uiSettings?.isZoomControlsEnabled = false
        gMap.uiSettings?.isZoomGesturesEnabled = true
        gMap.setLocationSource(locationSource)

//        gMap.isBuildingsEnabled = true
//        gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//        gMap.isTrafficEnabled = true
    }

    fun initGoogleLocation(context: Context) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager?.addGpsStatusListener(gpsStatusListener)
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest().apply {
            interval = 5000  //请求时间间隔
            fastestInterval = 2000 //最快时间间隔
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val gMapLocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                onLocationListener?.onGLocationChanged(locationResult.lastLocation)
            }
        }
    }

    private val gpsStatusListener by lazy {
        GpsStatus.Listener {
            when (it) {
                //卫星状态改变
                GpsStatus.GPS_EVENT_SATELLITE_STATUS -> {
                    //获取当前状态
                    val gpsStatus = locationManager?.getGpsStatus(null)
                    //获取卫星颗数的默认最大值
                    val maxSatellites = gpsStatus?.maxSatellites ?: -1
                    val iterator = gpsStatus?.satellites?.iterator()
                    var count = 0
                    while (iterator?.hasNext() == true && count <= maxSatellites) {
                        val s = iterator.next()
                        if (s?.snr != 0f) {//只有信躁比不为0的时候才算搜到了星
                            count++
                        }
                    }
                    val status = if (count > 1) 1 else if (count == 1) 0 else -1
                    onGpsStatusListener?.onGpsStatusChanged(status)
                }
            }
        }
    }


    fun setOnLocationListener(locationCallback: OnGLocationListener?) {
        this.onLocationListener = locationCallback
    }

    fun setOnGpsStatusListener(onGpsStatusListener: OnGpsStatusListener) {
        this.onGpsStatusListener = onGpsStatusListener
    }

    fun startLocation(context: Context,
                      requestLocationPermission: () -> Unit = {}) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }
        locationClient?.requestLocationUpdates(locationRequest, gMapLocationCallback, Looper.myLooper())
    }

    /**
     * 停止定位
     */
    fun stopLocation() {
        locationClient?.removeLocationUpdates(gMapLocationCallback)
    }

    fun distance(latLngs: MutableList<LatLng>): Long {
        var distance = 0.0
        if (latLngs.size < 2) return distance.toLong()
        val convertData = mutableListOf<LatLng>()
        latLngs.forEach {
            val locationArray = GPSUtil.transform(it.latitude, it.longitude)
            convertData.add(LatLng(locationArray[0], locationArray[1]))
        }
//        val data = PolyUtil.simplify(convertData, 5.0)
        var pre = convertData[0]
        for (index in 1..convertData.lastIndex) {
            val latLng = convertData[index]
            val results = FloatArray(3)
            Location.distanceBetween(pre.latitude, pre.longitude, latLng.latitude, latLng.longitude, results)
            distance += results[0]
            pre = latLng
        }
        return distance.roundToLong()
    }

    fun drawLine(context: Context, googleMap: GoogleMap, width: Int, height: Int, data: ArrayList<LatLng>?) {
        if (data == null || data.size < 2) return
        val convertData = mutableListOf<LatLng>()
        data.forEach {
            val locationArray = GPSUtil.transform(it.latitude, it.longitude)
            convertData.add(LatLng(locationArray[0], locationArray[1]))
        }

//        val data = PolyUtil.simplify(convertData, 5.0)
        val data = convertData

        var lanSum = 0.0
        var lonSum = 0.0
        val rectOptions = PolylineOptions()
        val builder = LatLngBounds.Builder()
        var starMark: MarkerOptions? = null
        var endMark: MarkerOptions? = null
        for (i in data.indices) {
            val sourceLatLng = data[i]
            if (i == 0) {
                starMark = MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_path_start)).position(sourceLatLng)
            } else if (i == data.size - 1) {
                endMark = MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_path_end)).position(sourceLatLng)
            }
            builder.include(sourceLatLng)
            rectOptions.add(sourceLatLng)
            lanSum += sourceLatLng.latitude
            lonSum += sourceLatLng.longitude
        }
        //移动中心位置 不然加载可能显示非洲0,0
        val target = LatLng(lanSum / data.size, lonSum / data.size)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(target))
        //将整个轨迹放入可见区域
        val width: Int = if (width > 0) width else context.resources.displayMetrics.widthPixels
        val height: Int = if (height > 0) height else context.resources.displayMetrics.heightPixels
        val padding = (width * 0.10).toInt()
        val bounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        googleMap.animateCamera(cu)
        val polylineColor = Color.parseColor("#FF5151")
        rectOptions.width(dp2px(context, 6f)).color(polylineColor).geodesic(true)
        //轨迹绘制
        googleMap.addPolyline(rectOptions)
        //添加起点
        googleMap.addMarker(starMark!!)
        //添加结束点
        googleMap.addMarker(endMark!!)
    }

    fun destroy() {
        locationManager?.removeGpsStatusListener(gpsStatusListener)
    }


    fun move2MyLocation(gMap: GoogleMap?, curLocation: Location?) {
        val map = gMap ?: return
        val location = curLocation ?: map.myLocation ?: return
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f))
    }

    private fun dp2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale
    }

    interface OnGLocationListener {
        fun onGLocationChanged(location: Location?)
    }

    interface OnGpsStatusListener {
        fun onGpsStatusChanged(status: Int)
    }
}