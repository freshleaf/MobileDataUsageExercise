package com.yuman.anotherexercise

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.yuman.anotherexercise.data.IVolumeRepository
import com.yuman.anotherexercise.data.VolumeRepository
import com.yuman.anotherexercise.data.local.VolumeDatabase
import com.yuman.anotherexercise.data.remote.RemoteDataSource
import com.yuman.anotherexercise.util.DATABASE_NAME

object ServiceLocator {

    private val lock = Any()
    private var database: VolumeDatabase? = null

    @Volatile
    var volumeRepository: IVolumeRepository? = null
        @VisibleForTesting set

    fun provideVolumeRepository(context: Context): IVolumeRepository {
        synchronized(this) {
            return volumeRepository ?: createVolumeRepository(context)
        }
    }

    private fun createVolumeRepository(context: Context): IVolumeRepository {
        return VolumeRepository(
            context,
            database?.volumeDao() ?: createDatabase(context).volumeDao(),
            RemoteDataSource(context)
        ).also {
            volumeRepository = it
        }
    }

    private fun createDatabase(context: Context): VolumeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            VolumeDatabase::class.java,
            DATABASE_NAME
        ).build().also { database = it }
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            volumeRepository = null

            // TODO
//            context.getSharedPreferences(SHP_STORE_NAME, Context.MODE_PRIVATE)?.edit().clear().commit()
        }
    }
}