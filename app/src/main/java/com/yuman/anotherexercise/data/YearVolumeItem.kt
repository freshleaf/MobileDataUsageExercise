package com.yuman.anotherexercise.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * data class of year data usage volume
 * It contains 4 quarters volume and total volume
 */
@Parcelize
data class YearVolumeItem(
    var year: Int,
    var volume: Float = 0.0f,
    val quarterItems: Array<QuarterVolumeItem?> = arrayOfNulls(4),
    var isDropdown: Boolean = false
) : Parcelable {

    companion object {
        fun getYearVolumeItemListFromRaw(quarterContentList: ArrayList<QuarterContent>): ArrayList<YearVolumeItem> {
            val quarterVolumeItemList = ArrayList<QuarterVolumeItem>()
            quarterContentList.forEach {
                val quarterVolume = QuarterVolumeItem(it)
                quarterVolumeItemList.add(quarterVolume)
            }
            return getYearVolumeItemList(quarterVolumeItemList)
        }

        private fun getYearVolumeItemList(quarterVolumeItemList: ArrayList<QuarterVolumeItem>): ArrayList<YearVolumeItem> {
            val map = HashMap<Int, YearVolumeItem>()
            var minYear = Int.MAX_VALUE
            var maxYear = 0
            for (item in quarterVolumeItemList) {
                if (item.year < minYear) {
                    minYear = item.year
                }
                if (item.year > maxYear) {
                    maxYear = item.year
                }
                if (map.containsKey(item.year)) {
                    map[item.year]!!.addQuarterVolume(item)
                } else {
                    val yearItem = YearVolumeItem(item.year)
                    yearItem.addQuarterVolume(item)
                    map[item.year] = yearItem
                }
            }
            val result = ArrayList<YearVolumeItem>()
            for (i in minYear..maxYear) {
                if (map.containsKey(i)) {
                    val temp = map[i]!!
                    result.add(temp)
                    if (map.containsKey(i - 1)) {
                        temp.checkDropdown(map[i - 1])
                    }
                }
            }
            return result
        }
    }

    fun addQuarterVolume(quarterVolumeItem: QuarterVolumeItem) {
        val quarter = quarterVolumeItem.quarter
        val year = quarterVolumeItem.year
        if (this.year != year) {
            return // not valid
        }
        if (quarter > 4 || quarter < 1) {
            return // not valid
        }

        // remove original quarter if it exists
        volume -= quarterItems[quarter - 1]?.volume ?: 0.0f
        volume += quarterVolumeItem.volume
        quarterItems[quarter - 1] = quarterVolumeItem
    }

    fun checkDropdown(lastYear: YearVolumeItem?): Boolean {
        val volumeArray = floatArrayOf(0f, 0f, 0f, 0f, 0f)
        volumeArray[0] = lastYear?.quarterItems?.get(3)?.volume!!
        volumeArray[1] = this.quarterItems[0]?.volume ?: 0.0f
        volumeArray[2] = this.quarterItems[1]?.volume ?: 0.0f
        volumeArray[3] = this.quarterItems[2]?.volume ?: 0.0f
        volumeArray[4] = this.quarterItems[3]?.volume ?: 0.0f
        for (i in 1..4) {
            if (volumeArray[i] < volumeArray[i - 1] && quarterItems[i - 1] != null) {
                this.quarterItems[i - 1]?.isDropdown = true
                this.isDropdown = true
            }
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as YearVolumeItem

        if (year != other.year) return false
        if (volume != other.volume) return false
        if (!quarterItems.contentEquals(other.quarterItems)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + volume.hashCode()
        return result
    }
}