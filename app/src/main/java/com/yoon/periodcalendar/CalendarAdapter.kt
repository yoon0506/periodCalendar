package com.yoon.periodcalendar

import androidx.viewpager2.adapter.FragmentStateAdapter
import org.joda.time.DateTime


class CalendarAdapter(millis: Long, fm: CalendarFragment) : FragmentStateAdapter(fm) {

    /* 달의 첫 번째 Day timeInMillis*/
    private var start: Long = DateTime(millis).withTimeAtStartOfDay().millis
    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): CalendarDetailFragment {
        val millis = getItemId(position)
        return CalendarDetailFragment.newInstance(millis)
    }

    override fun getItemId(position: Int): Long = DateTime(start).plusMonths(position - START_POSITION).millis

    override fun containsItem(itemId: Long): Boolean {
        val date = DateTime(itemId)
        return date.dayOfMonth == 1 && date.millisOfDay == 0
    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}