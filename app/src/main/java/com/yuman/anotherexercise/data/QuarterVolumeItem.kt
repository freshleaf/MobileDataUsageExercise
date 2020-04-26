package com.yuman.anotherexercise.data

import android.os.Parcelable
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
    /**
     * convert [QuarterContent] to [QuarterVolumeItem]
     * convert server response to data object, which is used in view model
     */
    constructor(quarterContent: QuarterContent) : this() {
        val temp = quarterContent.quarterStr.split("-Q")
        if (temp.size != 2) {
            return
        }
        try {
            year = temp[0].toInt()
            quarter = temp[1].toInt()
            if (quarter < 1 || quarter > 4) {
                year = 0
                quarter = 0
                return
            }
        } catch (e: NumberFormatException) {
            year = 0
            quarter = 0
            return
        }
        volume = quarterContent.volumeInPb
    }
}