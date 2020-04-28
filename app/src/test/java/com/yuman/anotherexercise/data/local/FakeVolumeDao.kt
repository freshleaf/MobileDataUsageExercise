package com.yuman.anotherexercise.data.local

import com.yuman.anotherexercise.data.QuarterVolumeItem

class FakeVolumeDao : VolumeDao {

    private val data = ArrayList<QuarterVolumeItem>()

    override fun insertVolume(volumes: List<QuarterVolumeItem>) {
        data.addAll(volumes)
    }

    override fun deleteAllVolumes(): Int {
        val size = data.size
        data.clear()
        return size
    }

    override fun getAllVolumes(): List<QuarterVolumeItem> {
        return data
    }
}