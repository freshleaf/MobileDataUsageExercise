package com.yuman.anotherexercise.data

import android.os.Parcelable
import android.util.Log
import com.yuman.anotherexercise.util.LOG_TAG
import kotlinx.android.parcel.Parcelize

/**
 * data class of quarter data usage volume
 */
@Parcelize
data class QuarterVolumeItem(
    var year: Int = 0,
    var quarter: Int = 0,
    var volume: Float = 0.0f,
    var isDropdown: Boolean = false
) : Parcelable {
    constructor(quarterContent: QuarterContent) : this() {
        val temp = quarterContent.quarterStr.split("-Q")
        if (temp.size != 2) {
            Log.e(LOG_TAG, "parse error: $quarterContent")
            return
        }
        year = temp[0].toInt()
        quarter = temp[1].toInt()
        volume = quarterContent.volumeInPb
    }
}