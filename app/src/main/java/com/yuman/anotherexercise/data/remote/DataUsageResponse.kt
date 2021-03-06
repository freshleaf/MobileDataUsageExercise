package com.yuman.anotherexercise.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Network response serialize class
 */
data class DataUsageResponse(
    @SerializedName("success") val isSuccess: Boolean,
    val result: ResultContent
)

data class ResultContent(
    val limit: Int = 0,
    val offset: Int = 0,
    val total: Int = 0,
    val records: ArrayList<QuarterContent>
)

data class QuarterContent(
    @SerializedName("_id") val itemId: Int,
    @SerializedName("quarter") val quarterStr: String,
    @SerializedName("volume_of_mobile_data") val volumeInPb: Float
)