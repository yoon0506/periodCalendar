package com.yoon.periodcalendar

import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class CalendarDetailFragment : BaseFragment<FragmentCalendarDetailBinding>() {
    private val This = this
    private var mMillis: Long = 0L
    private lateinit var mCustomCalendarAdapter: CustomCalendarAdapter
    private val mSharedMillis: SharedMillis by activityViewModels()
    private val mRecordViewModel: RecordViewModel by activityViewModels()
    private var mDataHash = HashMap<Int, Record>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mMillis = it.getLong(Key.MILLIS)
        }
        mSharedMillis.setMillis(mMillis)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedMillis.millis.observe(viewLifecycleOwner, {
            mMillis = it
        })
        mCustomCalendarAdapter =
            CustomCalendarAdapter(
                context!!, getMonthList(DateTime(mMillis))
            )

        mRecordViewModel.recordData.observe(viewLifecycleOwner,{it->
            mDataHash = it!!.clone() as HashMap<Int, Record>
            Log.d("checkCheck","mDataHash.size : "+mDataHash.size)
            mCustomCalendarAdapter.setData(mDataHash, getCurDateTime())
        })

        mBinding.calendarListView.adapter = mCustomCalendarAdapter
        mBinding.calendarListView.layoutManager = GridLayoutManager(context, 7)
        mCustomCalendarAdapter.setDateTime(DateTime(mMillis))
        mCustomCalendarAdapter.setData(mDataHash, getCurDateTime())
        mCustomCalendarAdapter.listener(object : CustomCalendarAdapter.Listener {
            override fun onClick(mMillis: Long) {
                var tempDateTime = DateTime(mMillis)
                var tempDays = tempDateTime.dayOfMonth
                var tempDataHash = HashMap<Int, Record>()
                for(i : Int in 0..6){
                    tempDateTime = tempDateTime.plusDays(i)
                    tempDataHash[tempDays++] = Record(tempDateTime)
                }
                mRecordViewModel.setData(tempDataHash)
//                findNavController().navigate(R.id.fragPopup)
            }
        })

    }
    companion object {
        fun newInstance(mMillis: Long) = CalendarDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(Key.MILLIS, mMillis)
            }
        }
    }
    
    private fun getCurDateTime(): DateTime {
        return DateTime(mMillis)
    }
    
    override fun getViewBinding(): FragmentCalendarDetailBinding {
        return FragmentCalendarDetailBinding.inflate(layoutInflater)
    }
}