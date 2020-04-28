package com.yuman.anotherexercise.data

import com.android.volley.VolleyError
import com.yuman.anotherexercise.data.remote.DataUsageResponse
import com.yuman.anotherexercise.data.remote.QuarterContent
import com.yuman.anotherexercise.data.remote.ResultContent

class FakeVolumeRepository : IVolumeRepository {

    private var _isCardView = false

    private var records = ArrayList<QuarterContent>()
    private var resultContent = ResultContent(0, 0, 0, records)
    var dataUsageResponse = DataUsageResponse(true, resultContent)
    var isNetworkSuccess = true

    var localVolumeData = ArrayList<QuarterVolumeItem>()
    var isCacheExpired = true

    override fun isCardView(): Boolean {
        return _isCardView
    }

    override fun storeIsCardView(isCard: Boolean) {
        _isCardView = isCard
    }

    override fun isCacheEmptyOrExpired(): Boolean {
        return isCacheExpired || localVolumeData.isNullOrEmpty()
    }

    override suspend fun clearLocalCache() {
        isCacheExpired = true
        localVolumeData.clear()
    }

    override suspend fun cacheAllVolumes(list: List<QuarterVolumeItem>) {
        localVolumeData.clear()
        for (item in list) {
            localVolumeData.add(item)
        }
        isCacheExpired = false
    }

    override suspend fun getCachedVolumes(): List<QuarterVolumeItem> {
        return localVolumeData
    }

    override fun fetchRemoteVolumes(
        listener: (DataUsageResponse) -> Unit,
        errorListener: (VolleyError) -> Unit
    ) {
        if (isNetworkSuccess) {
            listener(dataUsageResponse)
        } else {
            errorListener(VolleyError())
        }
    }
}