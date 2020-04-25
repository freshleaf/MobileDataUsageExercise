package com.yuman.anotherexercise.volumelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yuman.anotherexercise.data.YearVolumeItem

class VolumeListViewModel(application: Application) : AndroidViewModel(application) {

    // contain list screen data
    private val mVolumeList = MutableLiveData<List<YearVolumeItem>>()
    val volumeList: LiveData<List<YearVolumeItem>>
        get() = mVolumeList

    fun fetchData() {
        // TODO mock data for now
        val tempList = ArrayList<YearVolumeItem>()
        for (i in 1..10) {
            val temp = YearVolumeItem(2000 + i)
            temp.volume = 10.123456f
            if (i % 3 == 0) {
                temp.isDropdown = true
            }
            tempList.add(temp)
        }
        mVolumeList.value = tempList
    }

}