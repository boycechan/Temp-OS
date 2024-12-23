package com.touchgfx.mvvm.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import androidx.annotation.StringDef
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date as Date

object TimeUtils {

    const val PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_DATE_TIME_HM = "yyyy-MM-dd HH:mm"
    const val PATTERN_DATE = "yyyy-MM-dd"
    const val PATTERN_TIME = "HH:mm:ss"
    const val PATTERN_TIME_HM = "HH:mm"
    const val PATTERM_POINT_DATE = "yyyy.MM.dd"
    const val PATTERM_DATE_MONTH = "yyyy-MM"
    const val PATTERM_DATE_YEAR = "yyyy"
    const val PATTERM_WEEK = "EEEE"
    const val DAY_MILLIS = 86400000
    const val PATTERN_DATE_MORE = "yyyy/MM/dd"

    fun getHours(second: Long): Long { //计算秒有多少小时
        var h: Long = 0
        if (second > 3600) {
            h = second / 3600
        }
        return h
    }

    fun getMins(second: Long): String { //计算秒有多少分
        var d: Long = 0
        val temp = second % 3600
        if (second > 3600) {
            if (temp != 0L) {
                if (temp > 60) {
                    d = temp / 60
                }
            }
        } else {
            d = second / 60
        }
        return d.toString() + ""
    }

    fun getSeconds(second: Long): String { //计算秒有多少秒
        var s: Long = 0
        val temp = second % 3600
        if (second > 3600) {
            if (temp != 0L) {
                if (temp > 60) {
                    if (temp % 60 != 0L) {
                        s = temp % 60
                    }
                } else {
                    s = temp
                }
            }
        } else {
            if (second % 60 != 0L) {
                s = second % 60
            }
        }
        return s.toString() + ""
    }

    /***
     * 获取本周的第一天和最后一天：
     */
    @get:SuppressLint("SimpleDateFormat")
    val firstDayAndLastDayOfDay: String
        get() {
            val stringBuffer = StringBuffer()
            val dateFormater = SimpleDateFormat(PATTERN_DATE)
            val cal = Calendar.getInstance()
            cal[Calendar.DAY_OF_WEEK] = 1
            cal.time
            stringBuffer.append(dateFormater.format(cal.time) + "")
            cal[Calendar.DAY_OF_WEEK] = cal.getActualMaximum(Calendar.DAY_OF_WEEK)
            stringBuffer.append("~" + dateFormater.format(cal.time))
            return stringBuffer.toString()
        }// 根据日历的规则，为给定的日历字段添加或减去指定的时间量//本周最后一天

    // 使用给定的 Date 设置此 Calendar 的时间
    // 使用给定的 Date 设置此 Calendar 的时间
    // 测试此日期是否在指定日期之后
//本周第一天//获取本周时间

    /**
     * 获取本周日期集合
     *
     * @return
     */
    @get:SuppressLint("SimpleDateFormat")
    val weekDate: List<String>
        get() {
            val dateFormater = SimpleDateFormat(PATTERN_DATE)
            val calBegin = Calendar.getInstance()
            val lDate: MutableList<String> = ArrayList()
            val yz_time = getTimeInterval(Date()) //获取本周时间
            val array = yz_time.split(",").toTypedArray()
            val start_time = array[0] //本周第一天
            val end_time = array[1] //本周最后一天
            var dBegin: Date? = null
            var dEnd: Date? = null
            try {
                dBegin = dateFormater.parse(start_time)
                dEnd = dateFormater.parse(end_time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            lDate.add(start_time)

            // 使用给定的 Date 设置此 Calendar 的时间
            calBegin.time = dBegin
            val calEnd = Calendar.getInstance()
            // 使用给定的 Date 设置此 Calendar 的时间
            calEnd.time = dEnd
            // 测试此日期是否在指定日期之后
            while (dEnd!!.after(calBegin.time)) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                calBegin.add(Calendar.DAY_OF_MONTH, 1)
                val date = dateFormater.format(calBegin.time)
                lDate.add(date)
            }
            return lDate
        }

    private fun getTimeInterval(date: Date): String {
        val cal = Calendar.getInstance()
        val dateFormater = SimpleDateFormat(PATTERN_DATE)
        cal.time = date
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        val dayWeek = cal[Calendar.DAY_OF_WEEK] // 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1)
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.firstDayOfWeek = Calendar.MONDAY
        // 获得当前日期是一个星期的第几天
        val day = cal[Calendar.DAY_OF_WEEK]
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.firstDayOfWeek - day)
        val imptimeBegin = dateFormater.format(cal.time)
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6)
        val imptimeEnd = dateFormater.format(cal.time)
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return "$imptimeBegin,$imptimeEnd"
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    fun getYearFirst(year: Int): String {
        val dateFormater = SimpleDateFormat(PATTERN_DATE)
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar[Calendar.YEAR] = year
        val currYearFirst = calendar.time
        return dateFormater.format(currYearFirst)
    }

    /**
     * 获取两个日期之间的日期，包括开始结束日期
     *
     * @return 日期集合
     */
    fun getBetweenDates(startTime: String, endTime: String): List<String> {
        val dateFormater = SimpleDateFormat(PATTERN_DATE)
        var startDate: Date? = null
        var endDate: Date? = null
        try {
            startDate = dateFormater.parse(startTime)
            endDate = dateFormater.parse(endTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val result: MutableList<String> = ArrayList()
        val tempStart = Calendar.getInstance()
        tempStart.time = startDate
        tempStart.add(Calendar.DAY_OF_YEAR, 1)
        val tempEnd = Calendar.getInstance()
        tempEnd.time = endDate
        result.add(startTime)
        while (tempStart.before(tempEnd)) {
            result.add(dateFormater.format(tempStart.time))
            tempStart.add(Calendar.DAY_OF_YEAR, 1)
        }
        result.add(endTime)
        return result
    }// 获得当前日期对象
    // 清除信息
    // 1月从0开始
// 所有月份从1号开始

    /**
     * 获取本月的时间组
     *
     * @return
     */
    val monthFullDay: List<String>
        get() {
            val dateFormater = SimpleDateFormat(PATTERN_DATE)
            val date = dateFormater.format(Date())
            val fullDayList: MutableList<String> = ArrayList()
            val year = date.substring(0, 4).toInt()
            val month = date.substring(5, 7).toInt()
            val day = 1 // 所有月份从1号开始
            val cal = Calendar.getInstance() // 获得当前日期对象
            cal.clear() // 清除信息
            cal[Calendar.YEAR] = year
            cal[Calendar.MONTH] = month - 1 // 1月从0开始
            cal[Calendar.DAY_OF_MONTH] = day
            val count = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            var j = 0
            while (j <= count - 1) {
                if (dateFormater.format(cal.time) == getLastDay(year, month)) break
                cal.add(Calendar.DAY_OF_MONTH, if (j == 0) +0 else +1)
                j++
                fullDayList.add(dateFormater.format(cal.time))
            }
            return fullDayList
        }

    private fun getLastDay(year: Int, month: Int): String {
        val dateFormater = SimpleDateFormat(PATTERN_DATE)
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = 0
        return dateFormater.format(cal.time)
    }

    /***
     * 获取本周的第一天和最后一天：
     */
    val firstDayAndLastDayOfDayLong: LongArray
        get() {
            val time = LongArray(2)
            time[0] = DateUtils.beginDayOfWeek.getTime()
            time[1] = DateUtils.endDayOfWeek.getTime()
            return time
        }

    /**
     * 获取本月的第一天和最后一天：
     *
     * @return
     */
    val firstDayAndLastDayOfMonth: String
        get() {
            val stringBuffer = StringBuffer()
            val dateFormater = SimpleDateFormat(PATTERN_DATE)
            val cal = Calendar.getInstance()
            cal[Calendar.DAY_OF_MONTH] = 1
            cal.time
            stringBuffer.append(dateFormater.format(cal.time) + "")
            cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            stringBuffer.append("~" + dateFormater.format(cal.time))
            return stringBuffer.toString()
        }

    /**
     * 获取本月的第一天和最后一天：
     *
     * @return
     */
    val firstDayAndLastDayOfMonthLong: LongArray
        get() {
            val time = LongArray(2)
            time[0] = DateUtils.beginDayOfMonth.getTime()
            time[1] = DateUtils.endDayOfMonth.getTime()
            return time
        }

    /**
     * 将String格式时间转成时间戳
     *
     * @param time
     * @return
     */
    fun setStringToDate(time: String?): Long {
        var longTime: Long = 0
        val format = SimpleDateFormat(PATTERN_DATE_TIME)
        try {
            val dateStart = format.parse(time)
            longTime = (dateStart.time / 1000) as Long
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return longTime
    }

    /**
     * 将String格式时间转成时间戳
     *
     * @param time
     * @return
     */
    fun setStringToTimeMillis(time: String?): Long {
        var longTime: Long = 0
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_TIME)
        try {
            val date = simpleDateFormat.parse(time)
            longTime = date.time //获取时间的时间戳
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return longTime
    }

    /**
     * 获取今天
     *
     * @return String
     */
    val today: String
        get() = SimpleDateFormat(PATTERN_DATE).format(Date())

    /**
     * 获取今天的point
     */
    val todayPoint: String get() = SimpleDateFormat(PATTERM_POINT_DATE).format(Date())

    /**
     * 获取当天的日期
     *
     * @param tag
     * @return
     */
    fun getTodayDate(tag: Int): String {
        var date = ""
        when (tag) {
            0 -> date = SimpleDateFormat("MM月dd日", Locale.CHINA).format(Date())
            1 -> date = SimpleDateFormat("MMM d", Locale.ENGLISH).format(Date())
        }
        return date
    }

    /**
     * 格式化时间
     *
     * @param millTime
     * @param format
     * @return
     */
    fun getFormatTime(millTime: Long, format: String?): String {
        var formatTime = ""
        if (millTime > 0) {
            val sdf = SimpleDateFormat(format)
            val date = Date()
            date.time = millTime
            formatTime = sdf.format(date)
        }
        return formatTime
    }

    /**
     * 格式化时间
     *
     * @param dateTime
     * @param format
     * @return
     */
    fun getFormatTime(dateTime: String?, format: String?): String {
        var date: Date? = null
        val sdf = SimpleDateFormat(format)
        try {
            date = sdf.parse(dateTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return sdf.format(date)
    }

    /**
     * 判断是不是今天
     *
     * @param millis yyyy-MM-dd HH:mm:ss
     * @return 是今天返回 上午下午，否则直接返回
     */
    fun isToday(millis: String?): String? {
        return try {
            val todayVal = today
            val temp = setStringToTimeMillis(millis)
            val timeVal = getFormatTime(temp, PATTERN_DATE)
            if (todayVal == timeVal) {
                val resVal = getAMorPM(temp)
                resVal ?: getFormatTime(temp, "HH:mm:ss")
            } else {
                getFormatTime(temp, PATTERN_TIME)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            millis
        }
    }

    /**
     * 工具年月日判断是不是今天
     *
     * @param year
     * @param mouth
     * @param day
     * @return
     */
    fun isToday(year: Int, mouth: Int, day: Int): Boolean {
        try {
            val now = Date()
            val calendar = Calendar.getInstance()
            calendar.time = now
            if (calendar[Calendar.YEAR] == year) {
                if (calendar[Calendar.MONTH] + 1 == mouth) {
                    if (calendar[Calendar.DAY_OF_MONTH] == day) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return isToday(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1,
                calendar[Calendar.DAY_OF_MONTH])
    }

    /**
     * 获取开始时间
     *
     * @param totalMinute 总时长
     * @param endtime (year,month,day,hour,minute)
     * @return
     */
    fun getStartTime(totalMinute: Int,
                     year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
        return getStartTime(totalMinute, year, month, day, hour, minute, PATTERN_TIME_HM)
    }

    /**
     * 获取睡眠开始时间带日期
     *
     * @param totalMinute 总时长
     * @param endtime (year,month,day,hour,minute)
     * @return
     */
    fun getSleepStartTime(totalMinute: Int,
                          year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
        return getStartTime(totalMinute, year, month, day, hour, minute, PATTERN_DATE_TIME)
    }

    /**
     * 获取心率开始时间
     */
    @SuppressLint("SimpleDateFormat")
    fun getHeartRateStartTestTime(year: Int, month: Int, day: Int, offsetDateTime: Int): String {
        val hour = offsetDateTime / 60
        val minute = offsetDateTime % 60
        var monthString = "$month"
        if (month < 10) {
            monthString = "0$month"
        }
        var dayString = "$day"
        if (day < 10) {
            dayString = "0$day"
        }
        var hourString = "$hour"
        if (hour < 10) {
            hourString = "0$hour"
        }
        var minuteString = "$minute"
        if (minute < 10) {
            minuteString = "0$minute"
        }

        return "$year-$monthString-$dayString $hourString:$minuteString:00"
    }

    /**
     * 获取开始时间
     *
     * @param totalMinute 总时长
     * @param endtime (year,month,day,hour,minute)
     * @param format 时间格式
     * @return
     */
    fun getStartTime(totalMinute: Int,
                     year: Int, month: Int, day: Int, hour: Int, minute: Int,
                     format: String): String {
        val time = "$year-$month-$day $hour:$minute"
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_TIME_HM)
        try {
            val date = simpleDateFormat.parse(time)
            val dateC = Calendar.getInstance()
            dateC.time = date
            dateC.add(Calendar.MINUTE, -totalMinute)
            return getFormatTime(dateC.timeInMillis, format)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 判断上午还是下午
     *
     * @param time
     * @return
     */
    fun getAMorPM(time: Long): String {
        return getFormatTime(time, "a KK:mm")
    }

    /**
     * 修改时间的样式
     *
     * @param date
     * @return
     */
    fun formatDate(date: String): String {
        var date = date
        val dateStr = date.split("-").toTypedArray()
        //        date = dateStr[1] + "." + Integer.parseInt(dateStr[2]);
        date = dateStr[1] + "." + dateStr[2]
        return date
    }

    fun formateDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): String {
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_TIME)
        val dateStr = "$year-$month-$day $hour:$minute:$second"
        var date: Date? = null
        try {
            date = simpleDateFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return simpleDateFormat.format(date)
    }

    fun formateDate(year: Int, month: Int, day: Int): String {
        val c = Calendar.getInstance()
        c.set(year, month, day)
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE)
        return simpleDateFormat.format(c.time)
    }

    fun parseDate(year: Int, month: Int, day: Int): Date {
        val c = Calendar.getInstance()
        c.set(year, month, day)
        return c.time
    }

    fun parseDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date {
        val c = Calendar.getInstance()
        c.set(year, month, day, hour, minute, second)
        return c.time
    }

    /**
     * 格式化时间
     *
     * @param dateString
     * @return
     */
    fun formateDate1(dateString: String): String {
        var dateString = dateString
        dateString = dateString.replace("Z", " UTC") //UTC是本地时间
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z")
        var d: Date? = null
        d = try {
            format.parse(dateString)
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return ""
        }
        //此处是将date类型装换为字符串类型，比如：Sat Nov 18 15:12:06 CST 2017转换为2017-11-18 15:12:06
        val sf = SimpleDateFormat(PATTERN_DATE_TIME)
        return sf.format(d)
    }

    /**
     * 格式化时间，yyyy-MM-dd HH:mm:dd
     *
     * @param millTime 秒时间
     * @return
     */
    fun format(millTime: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millTime * 1000
        val date = cal.time
        val dateFormat = SimpleDateFormat(PATTERN_DATE_TIME)
        return dateFormat.format(date)
    }

    /**
     * 格式化时间，yyyy-MM-dd
     *
     * @param millTime 秒时间
     * @return
     */
    fun formatDate(millTime: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millTime * 1000
        val date = cal.time
        val dateFormat = SimpleDateFormat(PATTERN_DATE)
        return dateFormat.format(date)
    }

    /**
     * 格式化时间 HH:mm:dd
     *
     * @param time 秒时间
     * @return
     */
    fun formatHMS(time: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time * 1000
        val date = cal.time
        val dateFormat = SimpleDateFormat(PATTERN_TIME)
        return dateFormat.format(date)
    }

    /**
     * 通过当前的秒钟数获取从1970年开始的秒数
     *
     * @param dayTime 当天的分钟值
     * @return
     */
    fun getSpOTodayTime(dayTime: Long): Long {
        var resultTime: Long = 0
        val current = System.currentTimeMillis() //当前时间毫秒数
        val zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().rawOffset //今天零点零分零秒的毫秒数
        resultTime = (zero + dayTime * 60 * 1000) / 1000
        return resultTime
    }

    /**
     * 比较输入的日期与当天的时间进行对比是否大于3岁
     * @param dateStr
     * @return
     */
    fun checkAdult(dateStr: String?): Boolean {
        val format = SimpleDateFormat(PATTERN_DATE)
        var date: Date? = null
        try {
            date = format.parse(dateStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val current = Calendar.getInstance()
        val birthDay = Calendar.getInstance()
        birthDay.time = date
        val year = current[Calendar.YEAR] - birthDay[Calendar.YEAR]
        if (year > 16) {//修改个人信息必须年满16岁 modify by Colin
            return true
        } else if (year < 16) {//修改个人信息必须年满16岁 modify by Colin
            return false
        }
        // 如果年相等，就比较月份
        val month = current[Calendar.MONTH] - birthDay[Calendar.MONTH]
        if (month > 0) {
            return true
        } else if (month < 0) {
            return false
        }
        // 如果月也相等，就比较天
        val day = current[Calendar.DAY_OF_MONTH] - birthDay[Calendar.DAY_OF_MONTH]
        return day >= 0
    }

    /**
     * 格式化时间 mm'ss"
     *
     * @param time 秒时间
     * @return
     */
    fun formatMmssText(time: Long): String {
        return zerofill(time / 60) + "\'" + zerofill(time % 60) + "\""
    }

    /**
     * 格式化时间 mm分ss秒
     *
     * @param time 秒时间
     * @return
     */
    fun formatMmssText(format: String, time: Long): String {
        return String.format(format, zerofill(time / 60), zerofill(time % 60))
    }

    /**
     * 格式化计时时间 HH:mm:ss
     *
     * @param time 秒时间
     * @return
     */
    fun formatHHmmssText(sec: Long): String {
        if (sec < 3600 * 24) {//时
            return zerofill(sec / 60 / 60) + ":" + zerofill(sec / 60 % 60) + ":" + zerofill(sec % 60)
        }
        return "00:00:00"
    }

    fun zerofill(num: Int): String {
        return zerofill(num.toLong())
    }

    fun zerofill(num: Long): String {
        return if (num.toString().length < 2) {
            "0$num"
        } else {
            num.toString()
        }
    }

    /**
     * 当前年月日
     */
    fun curYMD(): Triple<Int, Int, Int> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        return Triple(year, month, day)
    }

    /**
     * 获取年月日
     */
    fun getYMD(date: Date): Triple<Int, Int, Int> {
        val instance = Calendar.getInstance()
        instance.time = date
        val year = instance.get(Calendar.YEAR)
        val month = instance.get(Calendar.MONTH) + 1
        val day = instance.get(Calendar.DAY_OF_MONTH)
        return Triple(year, month, day)
    }

    fun getYMD(date: String): Triple<Int, Int, Int> {
        return getYMD(toDate(date))
    }

    fun getHMS(date: Date): Triple<Int, Int, Int> {
        val instance = Calendar.getInstance()
        instance.time = date
        val hour = instance.get(Calendar.HOUR_OF_DAY)
        val minute = instance.get(Calendar.MINUTE)
        val second = instance.get(Calendar.SECOND)
        return Triple(hour, minute, second)
    }

    fun toDate(it: String): Date {
        return SimpleDateFormat(PATTERN_DATE, Locale.getDefault()).parse(it)!!
    }

    fun toDate(dateTime: String, format: String): Date? {
        try {
            return SimpleDateFormat(format, Locale.getDefault()).parse(dateTime)!!
        } catch (e: Exception) {
        }
        return null
    }

    fun formatYMD(it: Date): String {
        return SimpleDateFormat(PATTERN_DATE, Locale.getDefault()).format(it)
    }

    fun lastRangeTime(date: Date, @Range range: String): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        when (range) {
            RANGE_DAY -> cal.add(Calendar.DAY_OF_YEAR, -1)
            RANGE_WEEK -> cal.add(Calendar.WEEK_OF_YEAR, -1)
            RANGE_MONTH -> cal.add(Calendar.MONTH, -1)
            RANGE_YEAR -> cal.add(Calendar.YEAR, -1)
        }
        return cal.time
    }

    fun nextRangeTime(date: Date, @Range range: String): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        when (range) {
            RANGE_DAY -> cal.add(Calendar.DAY_OF_YEAR, 1)
            RANGE_WEEK -> cal.add(Calendar.WEEK_OF_YEAR, 1)
            RANGE_MONTH -> cal.add(Calendar.MONTH, 1)
            RANGE_YEAR -> cal.add(Calendar.YEAR, 1)
        }
        return cal.time
    }

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @StringDef(RANGE_DAY, RANGE_WEEK, RANGE_MONTH, RANGE_YEAR)
    annotation class Range

    const val RANGE_DAY = "day"
    const val RANGE_WEEK = "week"
    const val RANGE_MONTH = "month"
    const val RANGE_YEAR = "year"

    fun formatHHmm(time: String, timeFormat: String): String {
        val format = SimpleDateFormat(timeFormat)
        return try {
            val date = format.parse(time)
            format.applyPattern(PATTERN_TIME_HM)
            format.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun formatDateTime(date: Date, timeFormat: String): String {
        val format = SimpleDateFormat(timeFormat)
        return try {
            format.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun formatDateTime(timeMillis: Long, timeFormat: String): String {
        val format = SimpleDateFormat(timeFormat)
        return try {
            format.format(timeMillis)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取当前周的第一天
     * @param date
     * @return yyyy-MM-dd
     */
    fun getFirstDayOfWeek(date: Date): String {
        return getFirstDayOfWeek(date, Calendar.MONDAY, PATTERM_POINT_DATE)
    }

    fun getFirstDayOfWeek(date: Date, firstDayOfWeek: Int): String {
        return getFirstDayOfWeek(date, firstDayOfWeek, PATTERN_DATE)
    }

    fun getFirstDayOfWeek(date: Date, firstDayOfWeek: Int, pattern: String): String {
        val cal = Calendar.getInstance()
        cal.time = date
        // 设置该周第一天为星期一
        cal.firstDayOfWeek = firstDayOfWeek
        // 设置该周第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        val format = SimpleDateFormat(pattern)
        return format.format(cal.time)
    }

    /**
     * 获取当前周的最后一天
     * @param date
     * @return yyyy-MM-dd
     */
    fun getLastDayOfWeek(date: Date): String {
        return getLastDayOfWeek(date, Calendar.MONDAY, PATTERM_POINT_DATE)
    }

    fun getLastDayOfWeek(date: Date, firstDayOfWeek: Int): String {
        return getLastDayOfWeek(date, firstDayOfWeek, PATTERN_DATE)
    }

    fun getLastDayOfWeek(date: Date, firstDayOfWeek: Int, pattern: String): String {
        val cal = Calendar.getInstance()
        cal.time = date
        // 设置该周第一天为星期一
        cal.firstDayOfWeek = firstDayOfWeek
        // 设置最后一天是星期日
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek + 6)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val format = SimpleDateFormat(pattern)
        return format.format(cal.time)
    }

    /**
     * 获取当前月第一天：
     * @param date
     * @return yyyy-MM-dd
     */
    fun getFirstDayOfMonth(date: Date): String {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.MONTH, 0)
        c.set(Calendar.DAY_OF_MONTH, 1) //设置为1号,当前日期既为本月第一天
        val format = SimpleDateFormat(PATTERM_POINT_DATE)
        return format.format(c.time)
    }

    /**
     * 获取当前月最后一天
     * @param date
     * @return yyyy-MM-dd
     */
    fun getLastDayOfMonth(date: Date): String {
        val c = Calendar.getInstance()
        c.time = date
        //获取当前月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH))
        val format = SimpleDateFormat(PATTERM_POINT_DATE)
        return format.format(c.time)
    }

    /**
     * 获取当前年第一天：
     * @param date
     * @return yyyy-MM-dd
     */
    fun getFirstDayOfYear(date: Date): String {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.MONTH, 0)
        c.set(Calendar.DAY_OF_YEAR, 1) //设置为1号,当前日期既为本月第一天
        val format = SimpleDateFormat(PATTERM_POINT_DATE)
        return format.format(c.time)
    }

    /**
     * 获取当前年最后一天
     * @param date
     * @return yyyy-MM-dd
     */
    fun getLastDayOfYear(date: Date): String {
        val c = Calendar.getInstance()
        c.time = date
        //获取当前月最后一天
        c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR))
        val format = SimpleDateFormat(PATTERM_POINT_DATE)
        return format.format(c.time)
    }

    fun is24HourFormat(context: Context): Boolean {
        return DateFormat.is24HourFormat(context)
    }

    /**
     * 解析时分秒
     * @param "07:15:00"
     */
    fun parseHHmmss(time: String): Triple<Int, Int, Int> {
        var hour = 0
        var minute = 0
        var second = 0
        if (time.contains(":")) {
            val split = time.split(":")
            if (split.size == 3) {
                hour = split[0].toInt()
                minute = split[1].toInt()
                second = split[2].toInt()
            }
        }
        return Triple(hour, minute, second)
    }

    /**
     * 解析时分秒
     * @param "07:15:00"
     */
    fun parseDate2HHmmss(datetime: String): Triple<Int, Int, Int> {
        var hour = 0
        var minute = 0
        var second = 0
        val format = SimpleDateFormat(PATTERN_DATE_TIME)
        try {
            val date = format.parse(datetime)
            val cal = Calendar.getInstance()
            cal.time = date
            hour = cal.get(Calendar.HOUR_OF_DAY)
            minute = cal.get(Calendar.MINUTE)
            second = cal.get(Calendar.SECOND)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Triple(hour, minute, second)
    }

    /**
     *
     */
    fun compareTime(startTime: String?, stopTime: String?): Int {
        return if (startTime?.contains(":") == true && stopTime?.contains(":") == true) {
            val startTimeSplit = startTime.split(":")
            val stopTimeSplit = stopTime.split(":")
            if (startTimeSplit[0] == stopTimeSplit[0] && startTimeSplit[1] == stopTimeSplit[1]) {
                0
            } else if (startTimeSplit[0] < stopTimeSplit[0] ||
                    (startTimeSplit[0] == stopTimeSplit[0] && startTimeSplit[1] < stopTimeSplit[1])) {
                -1
            } else 1
        } else {
            2
        }
    }

    /**
     *
     */
    fun timeDuration(startTime: String?, stopTime: String?): Int {
        if (startTime?.contains(":") == true && stopTime?.contains(":") == true) {
            val startTimeSplit = startTime.split(":")
            val stopTimeSplit = stopTime.split(":")
            return if (startTimeSplit[0] == stopTimeSplit[0] && startTimeSplit[1] == stopTimeSplit[1]) {
                0
            } else if (startTimeSplit[0] < stopTimeSplit[0] ||
                    (startTimeSplit[0] == stopTimeSplit[0] && startTimeSplit[1] < stopTimeSplit[1])) {
                return (stopTimeSplit[0].toInt() - startTimeSplit[0].toInt()) * 60 + stopTimeSplit[1].toInt() - startTimeSplit[1].toInt()
            } else {
                return (24 - startTimeSplit[0].toInt()) * 60 - startTimeSplit[1].toInt() + stopTimeSplit[0].toInt() * 60 + stopTimeSplit[1].toInt()
            }
        } else {
            return 0
        }
    }

    fun getCurrentDay(): Long {
        var day = System.currentTimeMillis() / DAY_MILLIS
        val y = System.currentTimeMillis() % DAY_MILLIS
        if (y > 0) {
            day += 1
        }
        return day
    }

    fun getNextDay(): Long {
        return getCurrentDay() + 1
    }

    fun utcSeconds2DayofHour(utcSeconds: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = utcSeconds * 1000
        return cal.get(Calendar.HOUR_OF_DAY)
    }

    fun utcSeconds2DayofMinute(utcSeconds: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = utcSeconds * 1000
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        return hour * 60 + minute
    }

    fun dayOfMinute2UtcSeconds(year: Int, month: Int, day: Int, minute: Int): Long {
        val c = Calendar.getInstance()
        c.set(year, month, day)
        val h = minute / 60
        val m = minute % 60
        c.set(Calendar.HOUR_OF_DAY, h)
        c.set(Calendar.MINUTE, m)
        return c.timeInMillis / 1000
    }

    fun getAge(birthday: Date): Int {
        val born = Calendar.getInstance()
        val now = Calendar.getInstance()
        now.time = Date()
        born.time = birthday
        if (born.after(now)) {
            return 0
        }
        return now[Calendar.YEAR] - born[Calendar.YEAR]
    }

    /**
     * 某日的前后日期
     *
     * @param curDate 当前日期
     * @param days 负数为当前日期的前几日，正数为后几日
     */
    fun getDate(curDate: Date, days: Int): Date {
        val c = Calendar.getInstance()
        c.time = curDate
        c.add(Calendar.DAY_OF_YEAR, days)
        return c.time
    }

    /**
     * 比较两个日期的大小
     */
    fun compareToTowDate(oneTime: String, twoTime: String, format: String): Boolean {
        val df = SimpleDateFormat(format)
        val sd1 = df.parse(oneTime)
        val sd2 = df.parse(twoTime)

        return sd1.after(sd2)
    }

    /**
     * 判断闹钟时间是否过了
     * @param startTime 闹钟设定时间戳
     * @param hour 闹钟（时）
     * @param minute 闹钟（分）
     */
    fun isExpireAlarm(startTime: String, hour: Int, minute: Int): Boolean {
        val date = Date(setStringToTimeMillis(startTime))
        val (year, month, day) = getYMD(date)
        val (h, m, _) = getHMS(date)

        val alarmDate = toDate(formateDate(year, month, day, hour, minute, 0), PATTERN_DATE_TIME_HM)
        if (hour > h || (hour == h && minute > m)) {
            return alarmDate?.after(Date()) ?: false
        }

        val c = Calendar.getInstance()
        c.time = alarmDate
        c.add(Calendar.DAY_OF_YEAR, 1)
        Timber.i("c.time = ${c.time},data() = ${Date()}")
        return c.time.after(Date())
    }

    /**
     * 判断事件提醒时间是否过了
     * @param startTime 事件提醒设定时间戳
     * @param hour 事件提醒（时）
     * @param minute 事件提醒（分）
     */
    fun isExpireEvent(startTime: String, hour: Int, minute: Int): Boolean {
        val date = Date(setStringToTimeMillis(startTime))
        val (year, month, day) = getYMD(date)

        val alarmDate = toDate(formateDate(year, month, day, hour, minute, 0), PATTERN_DATE_TIME_HM)
        return alarmDate?.after(Date()) ?: false
    }

    fun getHms(utcSeconds: Long): Triple<Int, Int, Int> {
        val millis = utcSeconds * 1000
        return getHMS(Date(millis))
    }

    fun getWeekDayList(date: Date, pattern: String = PATTERN_DATE): List<String> {
        val dayList = mutableListOf<String>()
        val cal = Calendar.getInstance()
        cal.time = date
        // 设置该周第一天为星期一
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        for (i in 0..6) {
            cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek + i)
            val format = SimpleDateFormat(pattern)
            var day = format.format(cal.time)
            dayList.add(day)
        }
        return dayList
    }

    fun getMonthDayList(date: Date, pattern: String = PATTERN_DATE): List<String> {
        val dayList = mutableListOf<String>()
        val c = Calendar.getInstance()
        c.time = date
        for (i in 1..c.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            c.set(Calendar.DAY_OF_MONTH, i)
            val format = SimpleDateFormat(pattern)
            val day = format.format(c.time)
            dayList.add(day)
        }
        return dayList
    }

    fun formatHHmm(date: String, hour: Int, minute: Int, pattern: String = PATTERN_DATE): String {
        val d = SimpleDateFormat(pattern, Locale.getDefault()).parse(date)
        val cal = Calendar.getInstance()
        cal.time = d
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        val format = SimpleDateFormat(PATTERN_TIME_HM)
        return format.format(cal.time)
    }

    /**
     * 计算两个时间点间隔多少月
     */
    fun calDiffMonth(startDate: Date, endDate: Date): Int {

        val startCal = Calendar.getInstance()
        startCal.time = startDate
        val startYear = startCal.get(Calendar.YEAR)
        val startMonth = startCal.get(Calendar.MONTH) + 1
        val startDay = startCal.get(Calendar.DATE)
        val maxDay = startCal.getActualMaximum(Calendar.DAY_OF_MONTH)//获取起始日期所在月的最后一天

        val endCal = Calendar.getInstance()
        endCal.time = endDate
        val endYear = endCal.get(Calendar.YEAR)
        val endMonth = endCal.get(Calendar.MONTH) + 1
        val endDay = endCal.get(Calendar.DATE)
        val maxEndDay = endCal.getActualMaximum(Calendar.DAY_OF_MONTH)//获取结束日期所在月的最后一天

        val result = if (startDay == maxDay) {//起始日期是在月末
            if (maxEndDay == endDay) {
                (endYear - startYear) * 12 + endMonth - startMonth
            } else {
                (endYear - startYear) * 12 + endMonth - startMonth - 1
            }
        } else if (endDay == maxEndDay) {//结束日期是在月末
            (endYear - startYear) * 12 + endMonth - startMonth
        } else {
            if (endDay >= startDay) {
                (endYear - startYear) * 12 + endMonth - startMonth
            } else {
                (endYear - startYear) * 12 + endMonth - startMonth - 1
            }
        }
        return result
    }

    /**
     * 计算两个时间点间隔多少天
     */
    fun calDiffDay(startDate: Date, endDate: Date): Long {
        val day = (endDate.time - startDate.time) / (1000 * 60 * 60 * 24)
        val r = (endDate.time - startDate.time) % (1000 * 60 * 60 * 24)
        return if (r > 0) day + 1 else day
    }

    /**
     * 获取指定日期是周几
     */
    fun getWeekOfDate(dateStr: String): String {
        val date = toDate(dateStr, PATTERN_DATE_MORE)
        val dateFormat = SimpleDateFormat(PATTERM_WEEK)
        return dateFormat.format(date)
    }
}