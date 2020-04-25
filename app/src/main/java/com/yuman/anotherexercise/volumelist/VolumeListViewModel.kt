package com.yuman.anotherexercise.volumelist

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yuman.anotherexercise.data.QuarterVolumeItem
import com.yuman.anotherexercise.data.YearVolumeItem
import com.yuman.anotherexercise.util.SHP_KEY_IS_CARD_VIEW
import com.yuman.anotherexercise.util.SHP_STORE_NAME

class VolumeListViewModel(application: Application) : AndroidViewModel(application) {

    // list screen card view or normal list view
    var isCardView: Boolean = false

    // contain list screen data
    private val mVolumeList = MutableLiveData<List<YearVolumeItem>>()
    val volumeList: LiveData<List<YearVolumeItem>>
        get() = mVolumeList

    init {
        val shp =
            getApplication<Application>().getSharedPreferences(SHP_STORE_NAME, Context.MODE_PRIVATE)
        isCardView = shp.getBoolean(SHP_KEY_IS_CARD_VIEW, false)
    }

    fun storeIsCardView(isCard: Boolean) {
        val shp =
            getApplication<Application>().getSharedPreferences(SHP_STORE_NAME, Context.MODE_PRIVATE)
        shp.edit().putBoolean(SHP_KEY_IS_CARD_VIEW, isCard).apply()
        isCardView = isCard
    }

    fun fetchData() {
        // TODO mock data for now
        val tempList = ArrayList<YearVolumeItem>()
        for (year in 1..10) {
            val temp = YearVolumeItem(2000 + year)
            temp.volume = 10.123456f
            if (year % 3 == 0) {
                temp.isDropdown = true
            }
            for (quarter in 1..4) {
                val tempQuarter = QuarterVolumeItem(2000 + year, quarter,1.321654f)
                if (quarter%2 == 0) tempQuarter.isDropdown = true
                temp.addQuarterVolume(tempQuarter)
            }
            tempList.add(temp)
        }
        mVolumeList.value = tempList
    }

}