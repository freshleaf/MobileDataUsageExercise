package com.yuman.anotherexercise.volumelist

import android.app.Application
import androidx.lifecycle.*
import com.android.volley.VolleyError
import com.yuman.anotherexercise.data.DataUsageResponse
import com.yuman.anotherexercise.data.QuarterVolumeItem
import com.yuman.anotherexercise.data.VolumeRepository
import com.yuman.anotherexercise.data.YearVolumeItem
import com.yuman.anotherexercise.util.FetchDataStatus
import com.yuman.anotherexercise.util.VOLUME_LIST_KEY
import kotlinx.coroutines.launch

/**
 * view model of "list screen", holds list data
 * also handle the job of fetching data
 */
class VolumeListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    var volumeRepository = VolumeRepository.getInstance(getApplication())

    // list screen card view or normal list view
    var isCardView: Boolean = false
    // Network flag, do one job one time
    private var isLoading = false

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
                savedStateHandle.set(VOLUME_LIST_KEY, _volumeList.value)
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
            savedStateHandle.set(VOLUME_LIST_KEY, _volumeList.value)

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
        _fetchDataStatus.value = FetchDataStatus.NETWORK_ERROR
        isLoading = false
    }

    fun clearLocalCache() {
        _volumeList.value = null
        savedStateHandle.set(VOLUME_LIST_KEY, _volumeList.value)

        viewModelScope.launch {
            volumeRepository.clearLocalCache()
        }
    }

}