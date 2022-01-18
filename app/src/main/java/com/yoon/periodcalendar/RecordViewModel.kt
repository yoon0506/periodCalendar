package com.yoon.periodcalendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordViewModel : ViewModel() {
    val recordData = MutableLiveData<HashMap<Int, Record>?>()

    fun setData(data : HashMap<Int, Record>) {
        recordData.value = data
    }
}