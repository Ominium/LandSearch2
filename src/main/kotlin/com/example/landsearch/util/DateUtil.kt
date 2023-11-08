package com.example.landsearch.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtil {
    /**
     * 날짜, 시간 출력하기
     *
     * @param fm 날짜 출력 형식
     * @return date
     */
    fun getDateTime(fm: String?): String {
        val today = Date()
        val date = SimpleDateFormat(fm)
        return date.format(today)
    }

    val dateTime: String
        /**
         * 날짜, 시간 출력하기
         *
         * @return 기본값은 년.월.일
         */
        get() = getDateTime("yyyy.MM.dd")

    /**
     * Unix UTC 타입의 날짜, 시간 출력하기
     *
     * @param time 시간
     * @return date
     */
    fun getLongDateTime(time: Any): String {
        return getLongDateTime(time, "yyyy-MM-dd HH:mm:ss")
    }

    /**
     * Unix UTC 타입의 날짜, 시간 출력하기
     *
     * @param time 시간
     * @return date
     */
    fun getLongDateTime(time: Int): String {
        return getLongDateTime(time, "yyyy-MM-dd HH:mm:ss")
    }

    /**
     * Unix UTC 타입의 날짜, 시간 출력하기
     *
     * @param time 시간
     * @param fm   날짜 출력 형식
     * @return date
     */
    fun getLongDateTime(time: Any, fm: String?): String {
        return getLongDateTime(time as Int, fm)
    }

    /**
     * Unix UTC 타입의 날짜, 시간 출력하기
     *
     * @param time 시간
     * @param fm   날짜 출력 형식
     * @return date
     */
    fun getLongDateTime(time: Int, fm: String?): String {
        val instant = Instant.ofEpochSecond(time.toLong())
        return DateTimeFormatter.ofPattern(fm)
            .withZone(ZoneId.systemDefault())
            .format(instant)
    }
}