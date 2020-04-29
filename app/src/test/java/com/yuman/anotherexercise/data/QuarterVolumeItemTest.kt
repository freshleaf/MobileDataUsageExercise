package com.yuman.anotherexercise.data

import com.google.common.truth.Truth.assertThat
import com.yuman.anotherexercise.data.remote.QuarterContent
import org.junit.Test

class QuarterVolumeItemTest {

    @Test
    fun constructor_quarter_returnQuarter() {
        val quarterContent =
            QuarterContent(
                1,
                "2009-Q1",
                1.066517f
            )

        val quarterVolumeItem = QuarterVolumeItem(quarterContent)

        assertThat(quarterVolumeItem.year).isEqualTo(2009)
        assertThat(quarterVolumeItem.quarter).isEqualTo(1)
        assertThat(quarterVolumeItem.volume).isEqualTo(1.066517f)
    }

    @Test
    fun constructor_empty_returnZero() {
        val quarterContent =
            QuarterContent(
                12,
                "",
                1.066517f
            )

        val quarterVolumeItem = QuarterVolumeItem(quarterContent)

        assertThat(quarterVolumeItem.year).isEqualTo(0)
        assertThat(quarterVolumeItem.quarter).isEqualTo(0)
        assertThat(quarterVolumeItem.volume).isEqualTo(0f)
    }

    @Test
    fun constructor_invalidQuarter_returnZero() {
        val quarterContent =
            QuarterContent(
                12,
                "2009-QA",
                1.066517f
            )
        val quarterContent2 =
            QuarterContent(
                12,
                "2009-Q6",
                1.066517f
            )

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
        val quarterContent =
            QuarterContent(
                12,
                "A-Q1",
                1.066517f
            )

        val quarterVolumeItem = QuarterVolumeItem(quarterContent)

        assertThat(quarterVolumeItem.year).isEqualTo(0)
        assertThat(quarterVolumeItem.quarter).isEqualTo(0)
        assertThat(quarterVolumeItem.volume).isEqualTo(0f)
    }

    @Test
    fun equals_true() {
        val one = QuarterVolumeItem(2000, 1, 10.0f, true)
        val other = QuarterVolumeItem(2000, 1, 10.0f, true)
        val other2 = QuarterVolumeItem(2000, 1, 10.0f, true, "new_id")

        assertThat(one).isEqualTo(other)
        assertThat(one).isEqualTo(other2)
    }

    @Test
    fun equals_false() {
        val one = QuarterVolumeItem(2000, 1, 10.0f)
        val other = QuarterVolumeItem(2000, 1, 10.0f, true)
        val other2 = QuarterVolumeItem(2000, 1, 10.1f)
        val other3 = QuarterVolumeItem(2000, 2, 10.0f)
        val other4 = QuarterVolumeItem(2001, 1, 10.0f)

        assertThat(one).isNotEqualTo(other)
        assertThat(one).isNotEqualTo(other2)
        assertThat(one).isNotEqualTo(other3)
        assertThat(one).isNotEqualTo(other4)
    }

}