package com.yuman.anotherexercise.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yuman.anotherexercise.data.QuarterVolumeItem
import com.yuman.anotherexercise.util.DATABASE_NAME

@Database(entities = [QuarterVolumeItem::class], version = 1, exportSchema = false)
abstract class VolumeDatabase : RoomDatabase() {
    abstract fun volumeDao(): VolumeDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: VolumeDatabase? = null

        fun getInstance(context: Context): VolumeDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    VolumeDatabase::class.java,
                    DATABASE_NAME
                ).build().also { instance = it }
            }
        }
    }
}