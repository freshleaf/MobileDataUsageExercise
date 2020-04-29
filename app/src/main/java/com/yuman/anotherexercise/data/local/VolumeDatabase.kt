package com.yuman.anotherexercise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yuman.anotherexercise.data.QuarterVolumeItem

@Database(entities = [QuarterVolumeItem::class], version = 1, exportSchema = false)
abstract class VolumeDatabase : RoomDatabase() {

    abstract fun volumeDao(): VolumeDao

}