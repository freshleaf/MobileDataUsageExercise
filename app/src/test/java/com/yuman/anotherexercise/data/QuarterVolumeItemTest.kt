package com.yuman.anotherexercise.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class QuarterVolumeItemTest {

    @Test
    fun constructor_quarter_returnQuarter() {
        val quarterContent = QuarterContent(1, "2009-Q1", 1.066517f)

        val quarterVolumeItem = QuarterVolumeItem(quarterContent)

        assertThat(quarterVolumeItem.year).isEqualTo(2009)
        assertThat(quarterVolumeItem.quarter).isEqualTo(1)
        assertThat(quarterVolumeItem.volume).isEqualTo(1.066517f)
    }

    @Test
    fun constructor_empty_returnZero() {
        val quarterContent = QuarterContent(12, "", 1.066517f)

        val quarterVolumeItem = QuarterVolumeItem(quarterContent)

        assertThat(quarterVolumeItem.year).isEqualTo(0)
        assertThat(quarterVolumeItem.quarter).isEqualTo(0)
        assertThat(quarterVolumeItem.volume).isEqualTo(0f)
    }

    @Test
    fun constructor_invalidQuarter_returnZero() {
        val quarterContent = QuarterContent(12, "2009-QA", 1.066517f)
        val quarterContent2 = QuarterContent(12, "2009-Q6", 1.066517f)

        val quarterVolumeItem = QuarterVolumeItem(quarterContent)
        val quarterVolumeItem2 = QuarterVolumeItem(quarterContent2)

        assertThat(quarterVolumeItem.year).isEqualTo(0)
        assertThat(quarterVolumeItem.quarter).isEqualTo(0)
        assertThat(quarterVolumeItem.volume).isEqualTo(0f)
        assertThat(quarterVolumeItem2.year).isEqualTo(0)
        assertThat(quarterVolumeItem2.quarter).isEqualTo(0)
        assertThat(quarterVolumeItem2.volume).isEqualTo(0f)
    }

    @Test
    fun constructor_invalidYear_returnZero() {
        val quarterContent = QuarterContent(12, "A-Q1", 1.066517f)

        val quarterVolumeItem = QuarterVolumeItem(quarterContent)

        assertThat(quarterVolumeItem.year).isEqualTo(0)
        assertThat(quarterVolumeItem.quarter).isEqualTo(0)
        assertThat(quarterVolumeItem.volume).isEqualTo(0f)
    }

}