package com.yoon.periodcalendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yoon.periodcalendar.CalendarUtils.Companion.getMonthList
import com.yoon.periodcalendar.databinding.FragmentCalendarBinding
import com.yoon.periodcalendar.databinding.FragmentCalendarDetailBinding
import org.joda.time.DateTime

class CalendarDetailFragment : BaseFragment<FragmentCalendarDetailBinding>() {
    private val This = this
    private var millis: Long = 0L
    private lateinit var mCustomCalendarAdapter: CustomCalendarAdapter
    private val mSharedMillis: SharedMillis by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            millis = it.getLong(Key.MILLIS)
        }
        mSharedMillis.setMillis(millis)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedMillis.millis.observe(viewLifecycleOwner, {
            millis = it
        })
        mCustomCalendarAdapter =
            CustomCalendarAdapter(
                context!!,
                getMonthList(DateTime(millis))
            )

        mBinding.calendarListView.adapter = mCustomCalendarAdapter
        mBinding.calendarListView.layoutManager = GridLayoutManager(context, 7)
        mCustomCalendarAdapter.setDateTime(DateTime(millis))
        mCustomCalendarAdapter.listener(object : CustomCalendarAdapter.Listener {
            override fun onClick(millis: Long) {
                var tempDateTime = DateTime(millis)
//                findNavController().navigate(R.id.fragPopup)
            }
        })

    }

    companion object {
        fun newInstance(millis: Long) = CalendarDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(Key.MILLIS, millis)
            }
        }
    }

    override fun getViewBinding(): FragmentCalendarDetailBinding {
        return FragmentCalendarDetailBinding.inflate(layoutInflater)
    }
}