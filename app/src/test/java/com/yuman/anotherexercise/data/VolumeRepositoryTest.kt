package com.yuman.anotherexercise.data

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.VolleyError
import com.google.common.truth.Truth.assertThat
import com.yuman.anotherexercise.data.local.FakeRemoteDataSource
import com.yuman.anotherexercise.data.local.FakeVolumeDao
import com.yuman.anotherexercise.data.remote.DataUsageResponse
import com.yuman.anotherexercise.data.remote.QuarterContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class VolumeRepositoryTest {

    private lateinit var application: Application
    private lateinit var volumeDao: FakeVolumeDao
    private lateinit var remoteDataSource: FakeRemoteDataSource

    private lateinit var volumeRepository: VolumeRepository

    private lateinit var records: ArrayList<QuarterContent>
    private lateinit var volumeList: ArrayList<QuarterVolumeItem>

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createRepository() {
        val resultYear2004Q3 = QuarterVolumeItem(2004, 3, 0.000384f)
        val resultYear2004Q4 = QuarterVolumeItem(2004, 4, 0.000543f)
        val resultYear2005Q1 = QuarterVolumeItem(2005, 1, 0.00062f)
        val resultYear2005Q2 = QuarterVolumeItem(2005, 2, 0.000614f, true)
        val resultYear2005Q3 = QuarterVolumeItem(2005, 3, 0.000718f)
        val resultYear2005Q4 = QuarterVolumeItem(2005, 4, 0.000801f)
        val resultYear2006Q1 = QuarterVolumeItem(2006, 1, 0.00089f)
        val resultYear2006Q2 = QuarterVolumeItem(2006, 2, 0.001189f)
        val resultYear2006Q3 = QuarterVolumeItem(2006, 3, 0.001735f)
        volumeList = ArrayList<QuarterVolumeItem>().also {
            it.add(resultYear2004Q3)
            it.add(resultYear2004Q4)
            it.add(resultYear2005Q1)
            it.add(resultYear2005Q2)
            it.add(resultYear2005Q3)
            it.add(resultYear2005Q4)
            it.add(resultYear2006Q1)
            it.add(resultYear2006Q2)
            it.add(resultYear2006Q3)
        }

        val year2004Q3 = QuarterContent(1, "2004-Q3", 0.000384f)
        val year2004Q4 = QuarterContent(2, "2004-Q4", 0.000543f)
        val year2005Q1 = QuarterContent(3, "2005-Q1", 0.00062f)
        val year2005Q2 = QuarterContent(4, "2005-Q2", 0.000614f) // dropdown
        val year2005Q3 = QuarterContent(5, "2005-Q3", 0.000718f)
        val year2005Q4 = QuarterContent(6, "2005-Q4", 0.000801f)
        val year2006Q1 = QuarterContent(7, "2006-Q1", 0.00089f)
        val year2006Q2 = QuarterContent(8, "2006-Q2", 0.001189f)
        val year2006Q3 = QuarterContent(9, "2006-Q3", 0.001735f)
        records = ArrayList<QuarterContent>().also {
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

        application = ApplicationProvider.getApplicationContext()
        volumeDao = FakeVolumeDao()
        remoteDataSource = FakeRemoteDataSource(records)

        volumeRepository = VolumeRepository(application, volumeDao, remoteDataSource, Dispatchers.Main)
    }

    @Test
    fun cacheAllVolumes() {
        runBlocking {
            volumeRepository.cacheAllVolumes(volumeList)
        }

        assertThat(volumeDao.getAllVolumes() == volumeList)
    }

    @Test
    fun storeIsCardView_isCardView() {
        assertThat(volumeRepository.isCardView()).isFalse()

        volumeRepository.storeIsCardView(true)

        assertThat(volumeRepository.isCardView()).isTrue()
    }

    @Test
    fun getCachedVolumes() {
        volumeDao.insertVolume(volumeList)
        runBlocking {
            val data = volumeRepository.getCachedVolumes()
            assertThat(data).isEqualTo(volumeList)
        }
    }

    @Test
    fun fetchRemoteVolumes_success() {
        remoteDataSource.isNetworkSuccess = true
        remoteDataSource.isSuccess = true
        var listenerCalled = 0
        var errorListenerCalled = 0
        fun networkCallback(response: DataUsageResponse) {
            listenerCalled += 1
            assertThat(response.isSuccess).isTrue()
            assertThat(listenerCalled).isEqualTo(1)
        }
        fun networkErrorCallback(error: VolleyError) {
            errorListenerCalled += 1
            assertThat(error).isNotNull()
            assertThat(errorListenerCalled).isEqualTo(0) // not run
        }

        volumeRepository.fetchRemoteVolumes(
            { response -> networkCallback(response) },
            { error -> networkErrorCallback(error) }
        )
    }

    @Test
    fun fetchRemoteVolumes_netError() {
        remoteDataSource.isNetworkSuccess = false
        remoteDataSource.isSuccess = false
        var listenerCalled = 0
        var errorListenerCalled = 0
        fun networkCallback(response: DataUsageResponse) {
            listenerCalled += 1
            assertThat(response.isSuccess).isTrue()
            assertThat(listenerCalled).isEqualTo(0) // not run
        }
        fun networkErrorCallback(error: VolleyError) {
            errorListenerCalled += 1
            assertThat(error).isNotNull()
            assertThat(errorListenerCalled).isEqualTo(1)
        }

        volumeRepository.fetchRemoteVolumes(
            { response -> networkCallback(response) },
            { error -> networkErrorCallback(error) }
        )
    }

}