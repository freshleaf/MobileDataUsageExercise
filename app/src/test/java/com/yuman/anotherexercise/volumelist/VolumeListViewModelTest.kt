package com.yuman.anotherexercise.volumelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.yuman.anotherexercise.data.FakeVolumeRepository
import com.yuman.anotherexercise.data.QuarterVolumeItem
import com.yuman.anotherexercise.data.YearVolumeItem
import com.yuman.anotherexercise.data.remote.DataUsageResponse
import com.yuman.anotherexercise.data.remote.QuarterContent
import com.yuman.anotherexercise.data.remote.ResultContent
import com.yuman.anotherexercise.util.FetchDataStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(sdk = [Config.TARGET_SDK])
@RunWith(AndroidJUnit4::class)
class VolumeListViewModelTest {

    private lateinit var volumeListViewModel: VolumeListViewModel
    private lateinit var volumeRepository: FakeVolumeRepository

    private lateinit var records: ArrayList<QuarterContent>
    private lateinit var dataUsageResponse: DataUsageResponse
    private lateinit var failedResponse: DataUsageResponse

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        volumeRepository = FakeVolumeRepository()

        val year2004Q3 = QuarterContent(1, "2004-Q3", 0.000384f)
        val year2004Q4 = QuarterContent(2, "2004-Q4", 0.000543f)
        val year2005Q1 = QuarterContent(3, "2005-Q1", 0.00062f)
        val year2005Q2 = QuarterContent(4, "2005-Q2", 0.000614f) // dropdown
        val year2005Q3 = QuarterContent(5, "2005-Q3", 0.000718f)
        val year2005Q4 = QuarterContent(6, "2005-Q4", 0.000801f)
        val year2006Q1 = QuarterContent(7, "2006-Q1", 0.00089f)
        val year2006Q2 = QuarterContent(8, "2006-Q2", 0.001189f)
        val year2006Q3 = QuarterContent(9, "2006-Q3", 0.001735f)

        records = ArrayList<QuarterContent>()
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
        failedResponse = DataUsageResponse(false, resultContent)
        volumeListViewModel = VolumeListViewModel(volumeRepository)
    }

    @Test
    fun updateIsCardView_true() {
        volumeListViewModel.updateIsCardView(true)

        assertThat(volumeListViewModel.isCardView).isTrue()
    }

    @Test
    fun updateIsCardView_false() {
        volumeListViewModel.updateIsCardView(false)

        assertThat(volumeListViewModel.isCardView).isFalse()
    }

    @Test
    fun clearLocalCache() {
        volumeListViewModel.clearLocalCache()

        val listValue = volumeListViewModel.volumeList.getOrAwaitValue()
        assertThat(listValue).isNull()
    }

    @Test
    fun resetQuery_netError() {
        volumeRepository.isNetworkSuccess = false
        volumeRepository.dataUsageResponse = dataUsageResponse

        volumeListViewModel.resetQuery()
        val status = volumeListViewModel.fetchDataStatus.getOrAwaitValue()

        assertThat(status).isEqualTo(FetchDataStatus.NETWORK_ERROR)
    }

    @Test
    fun resetQuery_notSuccess() {
        volumeRepository.dataUsageResponse = failedResponse

        volumeListViewModel.resetQuery()
        val status = volumeListViewModel.fetchDataStatus.getOrAwaitValue()

        assertThat(status).isEqualTo(FetchDataStatus.NETWORK_NOT_SUCCESS)
    }

    @Test
    fun resetQuery_success() {
        volumeRepository.dataUsageResponse = dataUsageResponse
        val resultYear2004Q3 = QuarterVolumeItem(2004, 3, 0.000384f)
        val resultYear2004Q4 = QuarterVolumeItem(2004, 4, 0.000543f)
        val resultYear2004 = YearVolumeItem(
            2004,
            0.000384f + 0.000543f,
            arrayOf(null, null, resultYear2004Q3, resultYear2004Q4)
        )
        val resultYear2005Q1 = QuarterVolumeItem(2005, 1, 0.00062f)
        val resultYear2005Q2 = QuarterVolumeItem(2005, 2, 0.000614f, true)
        val resultYear2005Q3 = QuarterVolumeItem(2005, 3, 0.000718f)
        val resultYear2005Q4 = QuarterVolumeItem(2005, 4, 0.000801f)
        val resultYear2005 = YearVolumeItem(
            2005,
            0.00062f + 0.000614f + 0.000718f + 0.000801f,
            arrayOf(resultYear2005Q1, resultYear2005Q2, resultYear2005Q3, resultYear2005Q4),
            true
        )
        val resultYear2006Q1 = QuarterVolumeItem(2006, 1, 0.00089f)
        val resultYear2006Q2 = QuarterVolumeItem(2006, 2, 0.001189f)
        val resultYear2006Q3 = QuarterVolumeItem(2006, 3, 0.001735f)
        val resultYear2006 = YearVolumeItem(
            2006,
            0.00089f + 0.001189f + 0.001735f,
            arrayOf(resultYear2006Q1, resultYear2006Q2, resultYear2006Q3, null),
            false
        )

        // fetch success
        volumeListViewModel.resetQuery()
        val listValue = volumeListViewModel.volumeList.getOrAwaitValue()
        var status = volumeListViewModel.fetchDataStatus.getOrAwaitValue()

        assertThat(status).isEqualTo(FetchDataStatus.FETCHED_FROM_REMOTE)
        assertThat(listValue.size).isEqualTo(3)
        assertThat(listValue[0]).isEqualTo(resultYear2004)
        assertThat(listValue[1]).isEqualTo(resultYear2005)
        assertThat(listValue[2]).isEqualTo(resultYear2006)
        assertThat(volumeRepository.isCacheExpired).isFalse()

        // fetch fail, but hit in cache
        volumeRepository.isNetworkSuccess = false
        volumeListViewModel.resetQuery()
        val listValue2 = volumeListViewModel.volumeList.getOrAwaitValue()
        status = volumeListViewModel.fetchDataStatus.getOrAwaitValue()

        assertThat(status).isEqualTo(FetchDataStatus.FETCHED_FROM_LOCAL)
        assertThat(listValue2.size).isEqualTo(3)
        assertThat(listValue2[0]).isEqualTo(resultYear2004)
        assertThat(listValue2[1]).isEqualTo(resultYear2005)
        assertThat(listValue2[2]).isEqualTo(resultYear2006)

        // clear cache, fetch nothing
        volumeListViewModel.clearLocalCache()
        volumeListViewModel.resetQuery()
        val listValue3 = volumeListViewModel.volumeList.getOrAwaitValue()
        status = volumeListViewModel.fetchDataStatus.getOrAwaitValue()

        assertThat(status).isEqualTo(FetchDataStatus.NETWORK_ERROR)
        assertThat(listValue3).isNull()
    }


    @Test
    fun fetchData_netError() {
        volumeRepository.isNetworkSuccess = false
        volumeRepository.dataUsageResponse = dataUsageResponse

        volumeListViewModel.resetQuery()
        val status = volumeListViewModel.fetchDataStatus.getOrAwaitValue()

        assertThat(status).isEqualTo(FetchDataStatus.NETWORK_ERROR)
    }

    @Test
    fun fetchData_notSuccess() {
        volumeRepository.dataUsageResponse = failedResponse

        volumeListViewModel.resetQuery()
        val status = volumeListViewModel.fetchDataStatus.getOrAwaitValue()

        assertThat(status).isEqualTo(FetchDataStatus.NETWORK_NOT_SUCCESS)
    }

    @Test
    fun fetchData_success() {
        volumeRepository.dataUsageResponse = dataUsageResponse
        val resultYear2004Q3 = QuarterVolumeItem(2004, 3, 0.000384f)
        val resultYear2004Q4 = QuarterVolumeItem(2004, 4, 0.000543f)
        val resultYear2004 = YearVolumeItem(
            2004,
            0.000384f + 0.000543f,
            arrayOf(null, null, resultYear2004Q3, resultYear2004Q4)
        )

        volumeListViewModel.resetQuery()
        val listValue = volumeListViewModel.volumeList.getOrAwaitValue()
        var status = volumeListViewModel.fetchDataStatus.getOrAwaitValue()
        assertThat(status).isEqualTo(FetchDataStatus.FETCHED_FROM_REMOTE)
        assertThat(listValue.size).isEqualTo(3)
        assertThat(listValue[0]).isEqualTo(resultYear2004)
    }
}