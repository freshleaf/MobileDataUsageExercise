package com.yuman.anotherexercise.data

import com.android.volley.VolleyError
import com.yuman.anotherexercise.data.remote.DataUsageResponse

interface IVolumeRepository {

    fun isCardView(): Boolean

    fun storeIsCardView(isCard: Boolean)

    fun isCacheEmptyOrExpired(): Boolean

    suspend fun clearLocalCache()

    suspend fun cacheAllVolumes(list: List<QuarterVolumeItem>)

    suspend fun getCachedVolumes(): List<QuarterVolumeItem>

    fun fetchRemoteVolumes(
        listener: (DataUsageResponse) -> Unit,
        errorListener: (VolleyError) -> Unit
    )
}