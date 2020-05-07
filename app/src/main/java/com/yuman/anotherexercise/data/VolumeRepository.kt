package com.yuman.anotherexercise.data

import android.content.Context
import com.android.volley.VolleyError
import com.yuman.anotherexercise.data.local.VolumeDao
import com.yuman.anotherexercise.data.remote.DataUsageResponse
import com.yuman.anotherexercise.data.remote.IRemoteDataSource
import com.yuman.anotherexercise.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VolumeRepository (
    private val context: Context,
    private val volumeDao: VolumeDao,
    private val remoteDataSource: IRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IVolumeRepository {
    private val shp = context.getSharedPreferences(SHP_STORE_NAME, Context.MODE_PRIVATE)

    override fun isCardView(): Boolean =
        shp.getBoolean(SHP_KEY_IS_CARD_VIEW, false)

    override fun storeIsCardView(isCard: Boolean) =
        shp.edit().putBoolean(SHP_KEY_IS_CARD_VIEW, isCard).apply()

    override fun isCacheEmptyOrExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        val cacheTime = shp.getLong(SHP_KEY_FETCH_VOLUME_REMOTE_TIME, 0)
        return currentTime - cacheTime > CACHE_EXPIRED_TIME
    }

    override suspend fun clearLocalCache() {
        // clear volley cache
        VolleySingleton.getInstance(context).requestQueue.cache.clear()

        shp.edit().putLong(SHP_KEY_FETCH_VOLUME_REMOTE_TIME, 0).apply()
        withContext(ioDispatcher) {
            volumeDao.deleteAllVolumes()
        }
    }

    override suspend fun cacheAllVolumes(list: List<QuarterVolumeItem>) {
        shp.edit().putLong(SHP_KEY_FETCH_VOLUME_REMOTE_TIME, System.currentTimeMillis()).apply()
        withContext(ioDispatcher) {
            volumeDao.deleteAllVolumes()
            volumeDao.insertVolume(list)
        }
    }

    override suspend fun getCachedVolumes(): List<QuarterVolumeItem> {
        return withContext(ioDispatcher) {
            volumeDao.getAllVolumes()
        }
    }

    override fun fetchRemoteVolumes(
        listener: (DataUsageResponse) -> Unit,
        errorListener: (VolleyError) -> Unit
    ) {
        remoteDataSource.fetchRemoteVolumes(listener, errorListener)
    }

}