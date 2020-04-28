package com.yuman.anotherexercise.data

import android.app.Application
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.yuman.anotherexercise.data.local.VolumeDao
import com.yuman.anotherexercise.data.local.VolumeDatabase
import com.yuman.anotherexercise.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VolumeRepository private constructor(application: Application) {

    private var application: Application = application
    private val volumeDao: VolumeDao = VolumeDatabase.getInstance(application).volumeDao()
    private val shp = application.getSharedPreferences(SHP_STORE_NAME, Context.MODE_PRIVATE)

    companion object {
        private var INSTANCE: VolumeRepository? = null
        fun getInstance(application: Application) = INSTANCE ?: synchronized(this) {
            VolumeRepository(application).also { INSTANCE = it }
        }
    }

    fun isCardView(): Boolean =
        shp.getBoolean(SHP_KEY_IS_CARD_VIEW, false)

    fun storeIsCardView(isCard: Boolean) =
        shp.edit().putBoolean(SHP_KEY_IS_CARD_VIEW, isCard).apply()

    fun isCacheEmptyOrExpired() : Boolean {
        val currentTime = System.currentTimeMillis()
        val cacheTime = shp.getLong(SHP_KEY_FETCH_VOLUME_REMOTE_TIME, 0)
        return currentTime - cacheTime > CACHE_EXPIRED_TIME
    }

    suspend fun clearLocalCache() {
        // clear volley cache
        VolleySingleton.getInstance(application).requestQueue.cache.clear()

        shp.edit().putLong(SHP_KEY_FETCH_VOLUME_REMOTE_TIME, 0).apply()
        withContext(Dispatchers.IO) {
            volumeDao.deleteAllVolumes()
        }
    }

    suspend fun cacheAllVolumes(list: List<QuarterVolumeItem>) {
        shp.edit().putLong(SHP_KEY_FETCH_VOLUME_REMOTE_TIME, System.currentTimeMillis()).apply()
        withContext(Dispatchers.IO) {
            volumeDao.deleteAllVolumes()
            volumeDao.insertVolume(list)
        }
    }

    suspend fun getCachedVolumes(): List<QuarterVolumeItem> {
        return withContext(Dispatchers.IO) {
            volumeDao.getAllVolumes()
        }
    }

    fun fetchRemoteVolumes(
        listener: (DataUsageResponse) -> Unit,
        errorListener: (VolleyError) -> Unit
    ) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            Response.Listener {
                val response = Gson().fromJson(it, DataUsageResponse::class.java)
                listener(response)
            },
            Response.ErrorListener {
                errorListener(it)
            }
        )
        VolleySingleton.getInstance(application).requestQueue.add(stringRequest)
    }

    private fun getUrl(): String {
        return "https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
    }

}