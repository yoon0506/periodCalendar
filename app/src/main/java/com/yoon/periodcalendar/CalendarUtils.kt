package com.yoon.periodcalendar

import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarUtils {

    companion object {

        const val WEEKS_PER_MONTH = 6
        var mStartFromMonday = false

        /**
         * 선택된 날짜에 해당하는 월간 달력을 반환한다.
         */
        fun getMonthList(dateTime: DateTime): List<DateTime> {
            val list = mutableListOf<DateTime>()

            val date = dateTime.withDayOfMonth(1)

            val prev: Int = if (mStartFromMonday) {
                if (getPrevOffSet(date) - 1 <= -1) {
                    getPrevOffSet(date) + 6
                } else {
                    getPrevOffSet(date) - 1
                }
            } else {
                getPrevOffSet(date)
            }

            val cur = getCurOffset(date)
            val startValue = date.minusDays(prev)

            // 이 전 달의 개수와 현재 달의 개수 합이 29 미만이면 4주, 35 초과면 6주, 기타 5주 노출
            val totalDay: Int = if (prev + cur < 29) {
                DateTimeConstants.DAYS_PER_WEEK * (WEEKS_PER_MONTH - 2)
            } else if (prev + cur > 35) {
                DateTimeConstants.DAYS_PER_WEEK * WEEKS_PER_MONTH
            } else {
                DateTimeConstants.DAYS_PER_WEEK * (WEEKS_PER_MONTH - 1)
            }

            for (i in 0 until totalDay) {
                list.add(DateTime(startValue.plusDays(i)))
            }

            return list
        }

        /**
         * 해당 calendar 의 이전 달의 일 개수를 반환한다.
         */
        private fun getPrevOffSet(dateTime: DateTime): Int {
            var prevMonthTailOffset = dateTime.dayOfWeek
            if (prevMonthTailOffset >= 7) prevMonthTailOffset %= 7
            return prevMonthTailOffset
        }


        /**
         * 해당 calendar 의 현재 달의 일 개수를 반환한다.
         */
        fun getCurOffset(dateTime: DateTime): Int {
            return dateTime.dayOfMonth().maximumValue
        }

        /**
         * 같은 달인지 체크
         */

        fun isSameMonth(first: DateTime, second: DateTime): Boolean =
            first.year == second.year && first.monthOfYear == second.monthOfYear

        fun intToDateTime(date: Int): DateTime {
            val dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
            return DateTime(dateFormat.parse(date.toString()).time)
        }

        fun strToDateTime(str: String): DateTime {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            return DateTime(dateFormat.parse(str).time)
        }

        fun createHashKey(date: DateTime): String {
            val dateTime = date.withDayOfMonth(1)
            val dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
            return dateFormat.format(Timestamp(dateTime.millis))
        }

        /**
         *  1~7 숫자를 요일로 리턴한다.
         *  월요일시작/ 일요일 시작 분기점있음
         * */
        fun returnDayOfWeek(value: Int): String {
            var dayOfWeek = value
            if (!mStartFromMonday) {
                dayOfWeek = value - 1
            }
            when (dayOfWeek % 7) {
                0 -> {
                    return "일"
                }
                1 -> {
                    return "월"
                }
                2 -> {
                    return "화"
                }
                3 -> {
                    return "수"
                }
                4 -> {
                    return "목"
                }
                5 -> {
                    return "금"
                }
                6 -> {
                    return "토"
                }
            }
            return "월"
        }
    }
}