package com.yuman.anotherexercise.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yuman.anotherexercise.data.remote.QuarterContent
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * data class of quarter data usage volume
 */
@Parcelize
@Entity(tableName = "volume")
data class QuarterVolumeItem(
    var year: Int = 0,
    var quarter: Int = 0,
    var volume: Float = 0.0f,
    @Transient var isDropdown: Boolean = false,
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuarterVolumeItem

        if (year != other.year) return false
        if (quarter != other.quarter) return false
        if (volume != other.volume) return false
        if (isDropdown != other.isDropdown) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + quarter
        result = 31 * result + volume.hashCode()
        return result
    }

}