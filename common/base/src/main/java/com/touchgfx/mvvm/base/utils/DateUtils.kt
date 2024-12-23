package com.touchgfx.mvvm.base.utils

import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author suruozhong
 * @project cb-console-service
 * @description (日期工具类)
 * @date 2017年9月5日
 */
object DateUtils {
    val zodiacArr = arrayOf("猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊")
    val constellationArr = arrayOf("水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座")
    val constellationEdgeDay = intArrayOf(20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22)

    /**
     * 分钟转小时
     *
     * @param minute
     * @return
     */
    fun minuteToHour(minute: Int): String {
        val h = minute / 60.0
        return "" + NumberFormat.formatNum(h)
    }

    /**
     * 格式化字符串 0‘00“
     *
     * @param second
     * @return
     */
    fun cal(second: Long): String {
        var d: Long = 0
        var s: Long = 0
        val temp = second % 60
        if (second >= 60) {
            d = second / 60
            if (temp % 60 != 0L) {
                s = temp % 60
            }
        } else {
            s = second
        }
        return if (s < 10) {
            "$d'0$s\""
        } else {
            "$d'$s\""
        }
    }

    /**
     * 根据日期获取生肖
     *
     * @return
     */
    fun getZodica(date: Date?): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return zodiacArr[cal[Calendar.YEAR] % 12]
    }

    /**
     * 根据日期获取星座
     *
     * @return
     */
    fun getConstellation(date: Date?): String {
        if (date == null) {
            return ""
        }
        val cal = Calendar.getInstance()
        cal.time = date
        var month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        if (day < constellationEdgeDay[month]) {
            month = month - 1
        }
        return if (month >= 0) {
            constellationArr[month]
        } else constellationArr[11]
        // default to return 魔羯
    }/*
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();*/

    /**
     * 获取当天的开始时间
     *
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    val dayBegin: Date
        get() {
            /*
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();*/
            val date = Date()
            return getDayStartTime(date)
        }/*Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();*/

    /**
     * 获取当天的结束时间
     *
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    val dayEnd: Date
        get() {
            /*Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();*/
            val date = Date()
            return getDayEndTime(date)
        }

    /**
     * 获取昨天的开始时间
     *
     * @return 默认格式 Wed May 31 14:47:18 CST 2017
     */
    val beginDayOfYesterday: Date
        get() {
            val cal: Calendar = GregorianCalendar()
            cal.time = dayBegin
            cal.add(Calendar.DAY_OF_MONTH, -1)
            return cal.time
        }

    /**
     * 获取昨天的结束时间
     *
     * @return 默认格式 Wed May 31 14:47:18 CST 2017
     */
    val endDayOfYesterDay: Date
        get() {
            val cal: Calendar = GregorianCalendar()
            cal.time = dayEnd
            cal.add(Calendar.DAY_OF_MONTH, -1)
            return cal.time
        }

    /**
     * 获取明天的开始时间
     *
     * @return 默认格式 Wed May 31 14:47:18 CST 2017
     */
    val beginDayOfTomorrow: Date
        get() {
            val cal: Calendar = GregorianCalendar()
            cal.time = dayBegin
            cal.add(Calendar.DAY_OF_MONTH, 1)
            return cal.time
        }

    /**
     * 获取明天的结束时间
     *
     * @return 默认格式 Wed May 31 14:47:18 CST 2017
     */
    val endDayOfTomorrow: Date
        get() {
            val cal: Calendar = GregorianCalendar()
            cal.time = dayEnd
            cal.add(Calendar.DAY_OF_MONTH, 1)
            return cal.time
        }

    /**
     * 获取本周的开始时间
     *
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    val beginDayOfWeek: Date
        get() {
            val date = Date()
            val cal = Calendar.getInstance()
            cal.time = date
            var dayofweek = cal[Calendar.DAY_OF_WEEK]
            if (dayofweek == 1) {
                dayofweek += 7
            }
            cal.add(Calendar.DATE, 2 - dayofweek)
            return getDayStartTime(cal.time)
        }

    /**
     * 获取本周的结束时间
     *
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    val endDayOfWeek: Date
        get() {
            val cal = Calendar.getInstance()
            cal.time = beginDayOfWeek
            cal.add(Calendar.DAY_OF_WEEK, 6)
            val weekEndSta = cal.time
            return getDayEndTime(weekEndSta)
        }

    /**
     * 获取本月的开始时间
     *
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    val beginDayOfMonth: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar[nowYear, nowMonth - 1] = 1
            return getDayStartTime(calendar.time)
        }

    /**
     * 获取本月的结束时间
     *
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    val endDayOfMonth: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar[nowYear, nowMonth - 1] = 1
            val day = calendar.getActualMaximum(5)
            calendar[nowYear, nowMonth - 1] = day
            return getDayEndTime(calendar.time)
        }// cal.set

    /**
     * 获取本年的开始时间
     *
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    val beginDayOfYear: Date
        get() {
            val cal = Calendar.getInstance()
            cal[Calendar.YEAR] = nowYear
            // cal.set
            cal[Calendar.MONTH] = Calendar.JANUARY
            cal[Calendar.DATE] = 1
            return getDayStartTime(cal.time)
        }

    /**
     * 获取本年的结束时间
     *
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    val endDayOfYear: Date
        get() {
            val cal = Calendar.getInstance()
            cal[Calendar.YEAR] = nowYear
            cal[Calendar.MONTH] = Calendar.DECEMBER
            cal[Calendar.DATE] = 31
            return getDayEndTime(cal.time)
        }

    /**
     * 获取某个日期的开始时间
     *
     * @param d
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    fun getDayStartTime(d: Date?): Timestamp {
        val calendar = Calendar.getInstance()
        if (null != d) calendar.time = d
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH], 0, 0] = 0
        calendar[Calendar.MILLISECOND] = 0
        return Timestamp(calendar.timeInMillis)
    }

    /**
     * 获取某个日期的结束时间
     *
     * @param d
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    fun getDayEndTime(d: Date?): Timestamp {
        val calendar = Calendar.getInstance()
        if (null != d) calendar.time = d
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH], 23, 59] = 59
        calendar[Calendar.MILLISECOND] = 999
        return Timestamp(calendar.timeInMillis)
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    fun getStartMonthDate(year: Int, month: Int): Date {
        val calendar = Calendar.getInstance()
        calendar[year, month - 1] = 1
        return calendar.time
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    fun getEndMonthDate(year: Int, month: Int): Date {
        val calendar = Calendar.getInstance()
        calendar[year, month - 1] = 1
        val day = calendar.getActualMaximum(5)
        calendar[year, month - 1] = day
        return calendar.time
    }

    /**
     * 获取今年是哪一年
     *
     * @return
     */
    val nowYear: Int
        get() {
            val date = Date()
            val gc = Calendar.getInstance() as GregorianCalendar
            gc.time = date
            return Integer.valueOf(gc[1])
        }

    /**
     * 获取本月是哪一月
     *
     * @return
     */
    val nowMonth: Int
        get() {
            val date = Date()
            val gc = Calendar.getInstance() as GregorianCalendar
            gc.time = date
            return gc[2] + 1
        }

    /**
     * 两个日期相减得到的天数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    fun getDiffDays(beginDate: Date?, endDate: Date?): Int {
        require(!(beginDate == null || endDate == null)) { "getDiffDays param is null!" }
        val diff = ((endDate.time - beginDate.time)
                / (1000 * 60 * 60 * 24))
        return diff.toInt()
    }

    /**
     * 两个日期相减得到的毫秒数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    fun dateDiff(beginDate: Date, endDate: Date): Long {
        val date1ms = beginDate.time
        val date2ms = endDate.time
        return date2ms - date1ms
    }

    /**
     * 获取两个日期中的最大日期
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    fun max(beginDate: Date?, endDate: Date?): Date? {
        if (beginDate == null) {
            return endDate
        }
        if (endDate == null) {
            return beginDate
        }
        return if (beginDate.after(endDate)) {
            beginDate
        } else endDate
    }

    /**
     * 获取两个日期中的最小日期
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    fun min(beginDate: Date?, endDate: Date?): Date? {
        if (beginDate == null) {
            return endDate
        }
        if (endDate == null) {
            return beginDate
        }
        return if (beginDate.after(endDate)) {
            endDate
        } else beginDate
    }

    /**
     * 返回某月该季度的第一个月
     *
     * @param date
     * @return
     */
    fun getFirstSeasonDate(date: Date?): Date {
        val SEASON = intArrayOf(1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4)
        val cal = Calendar.getInstance()
        cal.time = date
        val sean = SEASON[cal[Calendar.MONTH]]
        cal[Calendar.MONTH] = sean * 3 - 3
        return cal.time
    }

    /**
     * 返回某个日期下几天的日期
     *
     * @param date
     * @param i
     * @return
     */
    fun getNextDay(date: Date?, i: Int): Date {
        val cal: Calendar = GregorianCalendar()
        cal.time = date
        cal[Calendar.DATE] = cal[Calendar.DATE] + i
        return cal.time
    }

    /**
     * 返回某个日期前几天的日期
     *
     * @param date
     * @param i
     * @return
     */
    fun getFrontDay(date: Date?, i: Int): Date {
        val cal: Calendar = GregorianCalendar()
        cal.time = date
        cal[Calendar.DATE] = cal[Calendar.DATE] - i
        return cal.time
    }

    /**
     * 获取某年某月到某年某月按天的切片日期集合（间隔天数的日期集合）
     *
     * @param beginYear
     * @param beginMonth
     * @param endYear
     * @param endMonth
     * @param k
     * @return
     */
    fun getTimeList(beginYear: Int, beginMonth: Int, endYear: Int,
                    endMonth: Int, k: Int): List<List<Date>> {
        val list: MutableList<List<Date>> = ArrayList()
        if (beginYear == endYear) {
            for (j in beginMonth..endMonth) {
                list.add(getTimeList(beginYear, j, k))
            }
        } else {
            run {
                for (j in beginMonth..11) {
                    list.add(getTimeList(beginYear, j, k))
                }
                for (i in beginYear + 1 until endYear) {
                    for (j in 0..11) {
                        list.add(getTimeList(i, j, k))
                    }
                }
                for (j in 0..endMonth) {
                    list.add(getTimeList(endYear, j, k))
                }
            }
        }
        return list
    }

    /**
     * 获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
     *
     * @param beginYear
     * @param beginMonth
     * @param k
     * @return
     */
    fun getTimeList(beginYear: Int, beginMonth: Int, k: Int): List<Date> {
        val list: MutableList<Date> = ArrayList()
        var begincal: Calendar = GregorianCalendar(beginYear, beginMonth, 1)
        val max = begincal.getActualMaximum(Calendar.DATE)
        var i = 1
        while (i < max) {
            list.add(begincal.time)
            begincal.add(Calendar.DATE, k)
            i = i + k
        }
        begincal = GregorianCalendar(beginYear, beginMonth, max)
        list.add(begincal.getTime())
        return list
    }

    /**
     * 格式化日期
     * yyyy-MM-dd HH:mm:ss
     *
     * @param @param  date
     * @param @return
     * @Description:
     */
    private fun format(date: Date): Date {
        var date = date
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            date = sd.parse(sd.format(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val s1 = cal(800)
        val s2 = cal(60)
        val s3 = cal(121)
        val s4 = cal(10)
        println("")
    }
}