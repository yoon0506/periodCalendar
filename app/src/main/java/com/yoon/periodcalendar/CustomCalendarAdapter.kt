package com.yoon.periodcalendar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yoon.periodcalendar.databinding.CustomCalendarDayViewBinding
import org.joda.time.DateTime

class CustomCalendarAdapter(
    context: Context, private val dateList: List<DateTime>
) : RecyclerView.Adapter<CustomCalendarAdapter.ViewHolder>() {
    private lateinit var mDateTime: DateTime
    private val mContext = context
    private lateinit var mListener: Listener
    private var startPosition: Int = 0
    private var lastDay: Int = 0

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
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {

            if (CalendarUtils.isSameMonth(mDateTime, dateList[position])) {
                binding.date.text = dateList[position].dayOfMonth.toString()
                binding.calendarBookContainer.setOnClickListener {
                    if (mListener != null) {
                        mListener.onClick(dateList[position].millis)
                    }
                }
            } else {
                binding.date.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            }
        }
    }

    fun setDateTime(date: DateTime) {
        mDateTime = date
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    public interface Listener {
        public fun onClick(millis: Long)
    }
}