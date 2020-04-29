package com.yuman.anotherexercise.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.yuman.anotherexercise.data.local.VolumeDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class VolumeDaoTest {

    private lateinit var database: VolumeDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            VolumeDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertAndGet() = runBlockingTest {
        val volume = QuarterVolumeItem(2000, 1, 10.1f)
        val volumeList = ArrayList<QuarterVolumeItem>()
        volumeList.add(volume)
        val loadedVolume = QuarterVolumeItem(2000, 1, 10.1f)

        database.volumeDao().insertVolume(volumeList)
        val loadedList = database.volumeDao().getAllVolumes()

        assertThat(loadedList).isNotNull()
        assertThat(loadedList.size).isEqualTo(1)
        assertThat(loadedList[0]).isEqualTo(loadedVolume)
    }

    @Test
    fun insertAndDelete() = runBlockingTest {
        val volume = QuarterVolumeItem(2000, 1, 10.1f)
        val volumeList = ArrayList<QuarterVolumeItem>()
        volumeList.add(volume)

        database.volumeDao().insertVolume(volumeList)
        database.volumeDao().deleteAllVolumes()
        val loadedList = database.volumeDao().getAllVolumes()

        assertThat(loadedList).isNotNull()
        assertThat(loadedList).isEmpty()
    }

}