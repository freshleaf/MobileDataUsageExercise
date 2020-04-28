package com.yuman.anotherexercise.data.local

import com.android.volley.VolleyError
import com.yuman.anotherexercise.data.remote.DataUsageResponse
import com.yuman.anotherexercise.data.remote.IRemoteDataSource
import com.yuman.anotherexercise.data.remote.QuarterContent
import com.yuman.anotherexercise.data.remote.ResultContent

class FakeRemoteDataSource(dataList: ArrayList<QuarterContent>) : IRemoteDataSource {

    var isSuccess = true
    var isNetworkSuccess = true
    var dataList = dataList

    private val resultContent = ResultContent(0, 0, 0, dataList)
    private val dataUsageResponse = DataUsageResponse(true, resultContent)
    var failedResponse = DataUsageResponse(false, resultContent)


    override fun fetchRemoteVolumes(
        listener: (DataUsageResponse) -> Unit,
        errorListener: (VolleyError) -> Unit
    ) {
        if (isNetworkSuccess) {
            if (isSuccess) {
                listener(dataUsageResponse)
            } else {
                listener(failedResponse)
            }
        } else {
            errorListener(VolleyError())
        }
    }

}