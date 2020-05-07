package com.yuman.anotherexercise.data

import com.android.volley.VolleyError
import com.yuman.anotherexercise.data.remote.DataUsageResponse
import com.yuman.anotherexercise.data.remote.QuarterContent
import com.yuman.anotherexercise.data.remote.ResultContent

class FakeAndroidTestRepository : IVolumeRepository {

    private var _isCardView = false

    var records: ArrayList<QuarterContent>
    var dataUsageResponse: DataUsageResponse

    var isNetworkSuccess = true

    var localVolumeData = ArrayList<QuarterVolumeItem>()
    var isCacheExpired = true

    init {
        val year2004Q3 = QuarterContent(1, "2004-Q3", 0.000384f)
        val year2004Q4 = QuarterContent(2, "2004-Q4", 0.000543f)
        val year2005Q1 = QuarterContent(3, "2005-Q1", 0.00062f)
        val year2005Q2 = QuarterContent(4, "2005-Q2", 0.000614f) // dropdown
        val year2005Q3 = QuarterContent(5, "2005-Q3", 0.000718f)
        val year2005Q4 = QuarterContent(6, "2005-Q4", 0.000801f)
        val year2006Q1 = QuarterContent(7, "2006-Q1", 0.00089f)
        val year2006Q2 = QuarterContent(8, "2006-Q2", 0.001189f)
        val year2006Q3 = QuarterContent(9, "2006-Q3", 0.001735f)

        records = ArrayList()
        records.let {
            it.add(year2004Q3)
            it.add(year2004Q4)
            it.add(year2005Q1)
            it.add(year2005Q2)
            it.add(year2005Q3)
            it.add(year2005Q4)
            it.add(year2006Q1)
            it.add(year2006Q2)
            it.add(year2006Q3)
        }
        val resultContent = ResultContent(0, 0, 0, records)
        dataUsageResponse = DataUsageResponse(true, resultContent)
    }

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