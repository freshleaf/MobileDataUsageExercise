package com.yuman.anotherexercise.volumelist

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.yuman.anotherexercise.util.VolleySingleton
import com.yuman.anotherexercise.data.DataUsageResponse
import com.yuman.anotherexercise.data.YearVolumeItem
import com.yuman.anotherexercise.util.FetchDataStatus
import com.yuman.anotherexercise.util.SHP_KEY_IS_CARD_VIEW
import com.yuman.anotherexercise.util.SHP_STORE_NAME
import com.yuman.anotherexercise.util.VOLUME_LIST_KEY

/**
 * view model of "list screen", holds list data
 * also handle the job of fetching data
 */
class VolumeListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    // Network flag, do one job one time
    private var isLoading = false

    // list screen card view or normal list view
    var isCardView: Boolean = false

    // contain list screen data
    private val _volumeList = MutableLiveData<List<YearVolumeItem>>().also {
        if (!savedStateHandle.contains(VOLUME_LIST_KEY)) {
            savedStateHandle.set(VOLUME_LIST_KEY, ArrayList<YearVolumeItem>())
        }
        it.value = savedStateHandle.get(VOLUME_LIST_KEY)
    }
    val volumeList: LiveData<List<YearVolumeItem>>
        get() = _volumeList

    private val _fetchDataStatus: MutableLiveData<FetchDataStatus> = MutableLiveData()
    val fetchDataUsageResponse: LiveData<FetchDataStatus>
        get() = _fetchDataStatus

    init {
        val shp =
            getApplication<Application>().getSharedPreferences(SHP_STORE_NAME, Context.MODE_PRIVATE)
        isCardView = shp.getBoolean(SHP_KEY_IS_CARD_VIEW, false)
    }

    fun storeIsCardView(isCard: Boolean) {
        val shp =
            getApplication<Application>().getSharedPreferences(SHP_STORE_NAME, Context.MODE_PRIVATE)
        shp.edit().putBoolean(SHP_KEY_IS_CARD_VIEW, isCard).apply()
        isCardView = isCard
    }

    fun resetQuery() {
        isLoading = false
        fetchData()
    }

    private fun fetchData() {
        if (isLoading) return
        isLoading = true

        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            Response.Listener {
                with(Gson().fromJson(it, DataUsageResponse::class.java)) {
                    _volumeList.value =
                        YearVolumeItem.getYearVolumeItemListFromRaw(this.result.records)
                }
                savedStateHandle.set(VOLUME_LIST_KEY, _volumeList.value)
                isLoading = false
                _fetchDataStatus.value = FetchDataStatus.COMPLETE
            },
            Response.ErrorListener {
                _fetchDataStatus.value = FetchDataStatus.NETWORK_ERROR
                isLoading = false
            }
        )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    private fun getUrl(): String {
        // TODO get whole data in one time for now, better to support paging
        return "https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
    }
}