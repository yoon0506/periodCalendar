package com.yoon.periodcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yoon.periodcalendar.CalendarUtils.Companion.isSameDate
import com.yoon.periodcalendar.CalendarUtils.Companion.isSameMonth
import com.yoon.periodcalendar.databinding.CustomCalendarDayViewBinding
import org.joda.time.DateTime
import java.util.HashMap

class CustomCalendarAdapter(
    context: Context, private val dateList: List<DateTime>
) : RecyclerView.Adapter<CustomCalendarAdapter.ViewHolder>() {
    private lateinit var mDateTime: DateTime
    private val mContext = context
    private lateinit var mListener: Listener
    private var mRecordData = HashMap<Int, Record>()
    private var mStartDay: Int = 0

    public fun listener(listener: Listener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomCalendarDayViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dateList.size

    inner class ViewHolder(private val binding: CustomCalendarDayViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
//            if (CalendarUtils.isSameMonth(mDateTime, dateList[position])) {
//                binding.date.text = dateList[position].dayOfMonth.toString()
//                binding.calendarBookContainer.setOnClickListener {
//                    if (mListener != null) {
//                        mListener.onClick(dateList[position].millis)
//                    }
//                }
//            } else {
//                binding.date.setTextColor(ContextCompat.getColor(mContext, R.color.white))
//            }
            if (mRecordData.size > 0 && mRecordData.containsKey(position - mStartDay + 2)) {
                binding.date.text = dateList[position].dayOfMonth.toString()
                binding.calendarBookContainer.setBackgroundColor(ContextCompat.getColor(mContext,R.color.lightPurple))
            } else {
                if ((position - mStartDay + 2) != dateList[position].dayOfMonth) {
                    binding.date.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                } else {
                    binding.date.text = dateList[position].dayOfMonth.toString()
                    binding.date.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                    binding.calendarBookContainer.setOnClickListener {
                        if (mListener != null) {
                            mListener.onClick(dateList[position].millis)
                        }
                    }
                }
            }
        }
    }

    fun setDateTime(date: DateTime) {
        mDateTime = date
        mStartDay = mDateTime.withDayOfMonth(1).dayOfWeek
    }

    fun setData(recordData: HashMap<Int, Record>, curDateTime: DateTime) {
        if (recordData != null && isSameMonth(mDateTime, curDateTime)) {
            mRecordData.clear()
            mRecordData = recordData.clone() as HashMap<Int, Record>
            notifyDataSetChanged()
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    public interface Listener {
        public fun onClick(millis: Long)
    }
}