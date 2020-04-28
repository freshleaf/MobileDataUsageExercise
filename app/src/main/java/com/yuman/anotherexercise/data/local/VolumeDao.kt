package com.yuman.anotherexercise.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yuman.anotherexercise.data.QuarterVolumeItem

@Dao
interface VolumeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVolume(volumes: List<QuarterVolumeItem>)

    @Query("DELETE FROM volume")
    fun deleteAllVolumes(): Int

    @Query("SELECT * FROM volume")
    fun getAllVolumes(): List<QuarterVolumeItem>
}