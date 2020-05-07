package com.yuman.anotherexercise.data.remote

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteDataSourceTest {

    private lateinit var application: Application
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        application = ApplicationProvider.getApplicationContext()
        remoteDataSource = RemoteDataSource(application)
    }

    @Test
    fun getQueryUrl_withParameters() {
        val resourceId = "myressource"
        val params = LinkedHashMap<String, String>()
        params["param1"] = "value1"
        params["param2"] = "value2"
        val targetUrl =
            "https://data.gov.sg/api/action/datastore_search?resource_id=myressource&param1=value1&param2=value2"

        val url = remoteDataSource.getQueryUrl(resourceId, params)

        assertThat(url).isEqualTo(targetUrl)
    }

    @Test
    fun getQueryUrl_withoutParameters() {
        val resourceId = "myressource"
        val targetUrl =
            "https://data.gov.sg/api/action/datastore_search?resource_id=myressource"

        val url = remoteDataSource.getQueryUrl(resourceId)

        assertThat(url).isEqualTo(targetUrl)
    }
}