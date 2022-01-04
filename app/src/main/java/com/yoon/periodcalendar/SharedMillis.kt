package com.yoon.periodcalendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedMillis : ViewModel() {
    val millis = MutableLiveData<Long>()

    fun setMillis(data : Long) {
        millis.value = data
    }
}