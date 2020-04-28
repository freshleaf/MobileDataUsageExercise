package com.yuman.anotherexercise.data.remote

import android.app.Application
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.yuman.anotherexercise.util.VolleySingleton

interface IRemoteDataSource {
    fun fetchRemoteVolumes(
        listener: (DataUsageResponse) -> Unit,
        errorListener: (VolleyError) -> Unit
    )
}

class RemoteDataSource(application: Application) : IRemoteDataSource {

    private val application: Application = application

    private val serverAddress = "https://data.gov.sg/api/action/datastore_search"
    private val mobileDataUsageResourceId = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
    private val resourceIdKey = "resource_id"

    private val mobileVolumeQueryUrl = getQueryUrl(mobileDataUsageResourceId)

    override fun fetchRemoteVolumes(
        listener: (DataUsageResponse) -> Unit,
        errorListener: (VolleyError) -> Unit
    ) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            mobileVolumeQueryUrl,
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

    private fun getQueryUrl(
        resourceId: String,
        parameters: LinkedHashMap<String, String>? = null
    ): String {
        var buff = StringBuffer().append(serverAddress).append('?').append(resourceIdKey)
            .append('=').append(resourceId)
        if (!parameters.isNullOrEmpty()) {
            for (pair in parameters) {
                buff.append('&').append(pair.key).append('=').append(pair.value)
            }
        }
        return buff.toString()
    }

}