package com.yoon.periodcalendar

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yoon.periodcalendar.CalendarUtils.Companion.getMonthList
import com.yoon.periodcalendar.databinding.FragmentCalendarBinding
import org.joda.time.DateTime

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {
    private val This = this
    private var millis: Long = 0L
    private lateinit var mCalendarAdapter: CalendarAdapter
    private val mSharedMillis: SharedMillis by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        millis = DateTime().millis
        /**
         * @param mStartFromMonday  true:월요일 시작 / false:일요일 시작
         */
        CalendarUtils.mStartFromMonday = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedMillis.millis.observe(viewLifecycleOwner, {
            millis = it
            mBinding.millis.text = DateTime(millis).toString("yyyy년 MM월")
        })
        setWeekOfDayLayout()
        mCalendarAdapter = CalendarAdapter(millis, This)
        mBinding.calendarViewPager.apply {
            adapter = mCalendarAdapter
            setCurrentItem(CalendarAdapter.START_POSITION, false)
            registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    mSharedMillis.setMillis(mCalendarAdapter.getItemId(position))
                }
            })

        }
    }

    // 상단 '요일' 레이아웃 생성
    private fun setWeekOfDayLayout() {
        val parent = mBinding.calendarWeekContainer
        for (i: Int in 1..7) {
            val tv = TextView(context)
            tv.apply{
                text = getDayOfWeek(i)
                setTextColor(ContextCompat.getColor(context!!, R.color.gray))
                gravity = Gravity.CENTER
                layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            }
            parent.addView(tv)
        }
    }

    private fun getDayOfWeek(value: Int): String {
        return CalendarUtils.returnDayOfWeek(value)
    }

    override fun getViewBinding(): FragmentCalendarBinding {
        return FragmentCalendarBinding.inflate(layoutInflater)
    }

}