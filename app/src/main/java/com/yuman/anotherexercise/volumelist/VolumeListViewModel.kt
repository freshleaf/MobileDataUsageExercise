package com.yuman.anotherexercise.volumelist

import android.util.Log
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.yuman.anotherexercise.data.remote.DataUsageResponse
import com.yuman.anotherexercise.data.IVolumeRepository
import com.yuman.anotherexercise.data.QuarterVolumeItem
import com.yuman.anotherexercise.data.YearVolumeItem
import com.yuman.anotherexercise.util.FetchDataStatus
import com.yuman.anotherexercise.util.LOG_TAG
import kotlinx.coroutines.launch

/**
 * view model of "list screen", holds list data
 * also handle the job of fetching data
 */
class VolumeListViewModel(
    private val volumeRepository: IVolumeRepository
) : ViewModel() {

    // list screen card view or normal list view
    var isCardView: Boolean = false

    // Network flag, do one job one time
    private var isLoading = false

    // contain list screen data
    private var _volumeList = MutableLiveData<ArrayList<YearVolumeItem>>()
    val volumeList: LiveData<ArrayList<YearVolumeItem>>
        get() = _volumeList

    private val _fetchDataStatus: MutableLiveData<FetchDataStatus> = MutableLiveData()
    val fetchDataStatus: LiveData<FetchDataStatus>
        get() = _fetchDataStatus

    init {
        isCardView = volumeRepository.isCardView()
    }

    fun updateIsCardView(isCard: Boolean) {
        isCardView = isCard
        volumeRepository.storeIsCardView(isCard)
    }

    /**
     * start a new fetching action no matter whether it's working
     */
    fun resetQuery() {
        isLoading = false
        fetchData()
    }

    /**
     * fetch data, use simple rule:
     * first try to load local data in DB, the cache will be expired in some time
     * if not hit try to load from remote
     *
     * NOTE: do local caching is for demonstrating, as Volley already supports caching
     */
    fun fetchData() {
        if (isLoading) return
        isLoading = true

        if (volumeRepository.isCacheEmptyOrExpired()) {
            // fetch data from remote
            volumeRepository.fetchRemoteVolumes(
                { response -> networkCallback(response) },
                { error -> networkErrorCallback(error) }
            )
        } else {
            // fetch data from local
            viewModelScope.launch {
                val quarterVolumeList = volumeRepository.getCachedVolumes()
                _volumeList.value = YearVolumeItem.getYearVolumeItemList(quarterVolumeList)
                _fetchDataStatus.value = FetchDataStatus.FETCHED_FROM_LOCAL
                isLoading = false
            }
        }
    }

    /**
     * fetch from remote success callback
     * if success, will store data in cache
     */
    private fun networkCallback(response: DataUsageResponse) {
        if (response.isSuccess) {
            _volumeList.value =
                YearVolumeItem.getYearVolumeItemListFromRaw(response.result.records)
            _fetchDataStatus.value = FetchDataStatus.FETCHED_FROM_REMOTE

            // store cache
            val volumeList = ArrayList<QuarterVolumeItem>()
            for (quarterContent in response.result.records) {
                volumeList.add(QuarterVolumeItem(quarterContent))
            }
            viewModelScope.launch {
                volumeRepository.cacheAllVolumes(volumeList)
            }
        } else {
            // network finished, but server told it is not success
            _fetchDataStatus.value = FetchDataStatus.NETWORK_NOT_SUCCESS
        }
        isLoading = false
    }

    /**
     * fetch from remote error callback
     */
    private fun networkErrorCallback(error: VolleyError) {
        Log.e(LOG_TAG, error.toString())

        _fetchDataStatus.value = FetchDataStatus.NETWORK_ERROR
        isLoading = false
    }

    fun clearLocalCache() {
        _volumeList.value = null

        viewModelScope.launch {
            volumeRepository.clearLocalCache()
        }
    }
}

@Suppress("UNCHECKED_CAST")
class VolumeViewModelFactory(
    private val volumeRepository: IVolumeRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (VolumeListViewModel(volumeRepository) as T)
}