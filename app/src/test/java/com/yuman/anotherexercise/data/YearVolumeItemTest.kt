package com.yuman.anotherexercise.data

import com.google.common.truth.Truth.assertThat
import com.yuman.anotherexercise.data.remote.QuarterContent
import org.junit.Test

class YearVolumeItemTest {

    @Test
    fun addQuarterVolume_validQuarterNotEmpty() {
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 1.0f)
        val thisYearQ2 = QuarterVolumeItem(2019, 2, 2.0f)
        val thisYearQ3 = QuarterVolumeItem(2019, 3, 3.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val thisYear = YearVolumeItem(2019)

        thisYear.addQuarterVolume(thisYearQ1)
        thisYear.addQuarterVolume(thisYearQ2)
        thisYear.addQuarterVolume(thisYearQ3)
        thisYear.addQuarterVolume(thisYearQ4)

        assertThat(thisYear.year).isEqualTo(2019)
        assertThat(thisYear.volume).isEqualTo(10f)
        assertThat(thisYear.quarterItems[0]).isEqualTo(thisYearQ1)
        assertThat(thisYear.quarterItems[1]).isEqualTo(thisYearQ2)
        assertThat(thisYear.quarterItems[2]).isEqualTo(thisYearQ3)
        assertThat(thisYear.quarterItems[3]).isEqualTo(thisYearQ4)
    }

    @Test
    fun addQuarterVolume_validQuarterEmptyQuarter() {
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 1.0f)
        val thisYearQ2 = QuarterVolumeItem(2019, 2, 2.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val thisYear = YearVolumeItem(2019)

        thisYear.addQuarterVolume(thisYearQ1)
        thisYear.addQuarterVolume(thisYearQ2)
        thisYear.addQuarterVolume(thisYearQ4)

        assertThat(thisYear.year).isEqualTo(2019)
        assertThat(thisYear.volume).isEqualTo(7f)
        assertThat(thisYear.quarterItems[0]).isEqualTo(thisYearQ1)
        assertThat(thisYear.quarterItems[1]).isEqualTo(thisYearQ2)
        assertThat(thisYear.quarterItems[2]).isNull()
        assertThat(thisYear.quarterItems[3]).isEqualTo(thisYearQ4)
    }

    @Test
    fun addQuarterVolume_validQuarterDuplicateQuarter() {
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 1.0f)
        val thisYearQ2 = QuarterVolumeItem(2019, 2, 2.0f)
        val thisYearQ3 = QuarterVolumeItem(2019, 3, 3.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val thisYearQ4New = QuarterVolumeItem(2019, 4, 5.0f)
        val thisYear = YearVolumeItem(2019)

        thisYear.addQuarterVolume(thisYearQ1)
        thisYear.addQuarterVolume(thisYearQ2)
        thisYear.addQuarterVolume(thisYearQ3)
        thisYear.addQuarterVolume(thisYearQ3)
        thisYear.addQuarterVolume(thisYearQ4)

        assertThat(thisYear.year).isEqualTo(2019)
        assertThat(thisYear.volume).isEqualTo(10f)
        assertThat(thisYear.quarterItems[0]).isEqualTo(thisYearQ1)
        assertThat(thisYear.quarterItems[1]).isEqualTo(thisYearQ2)
        assertThat(thisYear.quarterItems[2]).isEqualTo(thisYearQ3)
        assertThat(thisYear.quarterItems[3]).isEqualTo(thisYearQ4)

        thisYear.addQuarterVolume(thisYearQ4New)
        assertThat(thisYear.volume).isEqualTo(11f)
        assertThat(thisYear.quarterItems[3]).isEqualTo(thisYearQ4New)
    }

    @Test
    fun addQuarterVolume_invalidQuarter() {
        val lastYearQ1 = QuarterVolumeItem(2018, 1, 1.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val thisYear = YearVolumeItem(2019)

        thisYear.addQuarterVolume(lastYearQ1)
        thisYear.addQuarterVolume(thisYearQ4)

        assertThat(thisYear.volume).isEqualTo(4f)
        assertThat(thisYear.quarterItems[1]).isNull()
        assertThat(thisYear.quarterItems[3]).isEqualTo(thisYearQ4)
    }

    @Test
    fun checkDropdown_noDropNotEmpty_returnFalse() {
        val lastYearQ4 = QuarterVolumeItem(2018, 4, 1.0f)
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 1.0f)
        val thisYearQ2 = QuarterVolumeItem(2019, 2, 2.0f)
        val thisYearQ3 = QuarterVolumeItem(2019, 3, 3.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val lastYear = YearVolumeItem(2018, 10.0f, arrayOf(null, null, null, lastYearQ4))
        val thisYear =
            YearVolumeItem(2019, 10.0f, arrayOf(thisYearQ1, thisYearQ2, thisYearQ3, thisYearQ4))

        thisYear.checkDropdown(lastYear)

        assertThat(thisYear.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[0]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[1]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[2]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[3]!!.isDropdown).isFalse()
    }

    @Test
    fun checkDropdown_noDropThisEmpty_returnFalse() {
        val lastYearQ4 = QuarterVolumeItem(2018, 4, 1.0f)
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 2.0f)
        val thisYearQ2: QuarterVolumeItem? = null
        val thisYearQ3 = QuarterVolumeItem(2019, 3, 1.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val lastYear = YearVolumeItem(2018, 10.0f, arrayOf(null, null, null, lastYearQ4))
        val thisYear =
            YearVolumeItem(2019, 10.0f, arrayOf(thisYearQ1, thisYearQ2, thisYearQ3, thisYearQ4))

        thisYear.checkDropdown(lastYear)

        assertThat(thisYear.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[0]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[2]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[3]!!.isDropdown).isFalse()
    }

    @Test
    fun checkDropdown_noDropLastEmpty_returnFalse() {
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 1.0f)
        val thisYearQ2 = QuarterVolumeItem(2019, 2, 2.0f)
        val thisYearQ3 = QuarterVolumeItem(2019, 3, 3.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val lastYear = YearVolumeItem(2018)
        val thisYear =
            YearVolumeItem(2019, 10.0f, arrayOf(thisYearQ1, thisYearQ2, thisYearQ3, thisYearQ4))

        thisYear.checkDropdown(lastYear)

        assertThat(thisYear.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[0]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[1]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[2]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[3]!!.isDropdown).isFalse()
    }

    @Test
    fun checkDropdown_noDropLastEmpty_returnFalse2() {
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 1.0f)
        val thisYearQ2 = QuarterVolumeItem(2019, 2, 2.0f)
        val thisYearQ3 = QuarterVolumeItem(2019, 3, 3.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val thisYear =
            YearVolumeItem(2019, 10.0f, arrayOf(thisYearQ1, thisYearQ2, thisYearQ3, thisYearQ4))

        thisYear.checkDropdown(null)

        assertThat(thisYear.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[0]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[1]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[2]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[3]!!.isDropdown).isFalse()
    }

    @Test
    fun checkDropdown_dropNotEmpty_returnTrue() {
        val lastYearQ4 = QuarterVolumeItem(2018, 4, 2.0f)
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 1.0f)
        val thisYearQ2 = QuarterVolumeItem(2019, 2, 2.0f)
        val thisYearQ3 = QuarterVolumeItem(2019, 3, 3.0f)
        val thisYearQ4 = QuarterVolumeItem(2019, 4, 4.0f)
        val lastYear = YearVolumeItem(2018, 10.0f, arrayOf(null, null, null, lastYearQ4))
        val thisYear =
            YearVolumeItem(2019, 10.0f, arrayOf(thisYearQ1, thisYearQ2, thisYearQ3, thisYearQ4))

        thisYear.checkDropdown(lastYear)

        assertThat(thisYear.isDropdown).isTrue()
        assertThat(thisYear.quarterItems[0]!!.isDropdown).isTrue()
        assertThat(thisYear.quarterItems[1]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[2]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[3]!!.isDropdown).isFalse()
    }

    @Test
    fun checkDropdown_dropLastEmpty_returnTrue() {
        val thisYearQ1 = QuarterVolumeItem(2019, 1, 1.0f)
        val thisYearQ2 = QuarterVolumeItem(2019, 2, 3.0f)
        val thisYearQ3 = QuarterVolumeItem(2019, 3, 2.0f)
        val thisYearQ4: QuarterVolumeItem? = null
        val thisYear =
            YearVolumeItem(2019, 10.0f, arrayOf(thisYearQ1, thisYearQ2, thisYearQ3, thisYearQ4))

        thisYear.checkDropdown(null)

        assertThat(thisYear.isDropdown).isTrue()
        assertThat(thisYear.quarterItems[0]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[1]!!.isDropdown).isFalse()
        assertThat(thisYear.quarterItems[2]!!.isDropdown).isTrue()
    }

    @Test
    fun getYearVolumeItemListFromRaw_valid() {
        val year2004Q3 = QuarterContent(1, "2004-Q3", 0.000384f)
        val year2004Q4 = QuarterContent(2, "2004-Q4", 0.000543f)
        val year2005Q1 = QuarterContent(3, "2005-Q1", 0.00062f)
        val year2005Q2 = QuarterContent(4, "2005-Q2", 0.000614f) // dropdown
        val year2005Q3 = QuarterContent(5, "2005-Q3", 0.000718f)
        val year2005Q4 = QuarterContent(6, "2005-Q4", 0.000801f)
        val year2006Q1 = QuarterContent(7, "2006-Q1", 0.00089f)
        val year2006Q2 = QuarterContent(8, "2006-Q2", 0.001189f)
        val year2006Q3 = QuarterContent(9, "2006-Q3", 0.001735f)
        val rawList = ArrayList<QuarterContent>().also {
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

        val yearVolumeList = YearVolumeItem.getYearVolumeItemListFromRaw(rawList)

        assertThat(yearVolumeList.size).isEqualTo(3)
        assertThat(yearVolumeList[0]).isEqualTo(resultYear2004)
        assertThat(yearVolumeList[1]).isEqualTo(resultYear2005)
        assertThat(yearVolumeList[2]).isEqualTo(resultYear2006)
    }

    @Test
    fun getYearVolumeItemListFromRaw_invalid() {
        val year2002Q4 = QuarterContent(2, "2002-Q4", 0.000543f)
        val year2004Q3 = QuarterContent(1, "2004-Q3", 0.000384f)
        val year2005Q1 = QuarterContent(3, "2005-Q1", 0.0f)
        val year2005Q2 = QuarterContent(4, "2005-Q2", 0.000614f)
        val year2005Q3 = QuarterContent(5, "2005-Q3", 0.000718f)
        val year2005Q4 = QuarterContent(6, "2005-Q4", 0.000801f)
        val year2006Q1 = QuarterContent(7, "2006-Q1", 0.00079f) // dropdown
        val year2006Q3 = QuarterContent(9, "2006-Q3", 0.001735f)
        val year2007Q2 = QuarterContent(8, "2007-Q2", 0.001189f)
        val yearInvalid = QuarterContent(10, "", 0.001189f)
        val rawList = ArrayList<QuarterContent>().also {
            // not in order
            it.add(year2004Q3)
            it.add(year2002Q4)
            it.add(year2005Q3)
            it.add(year2007Q2)
            it.add(year2006Q3)
            it.add(year2006Q1)
            it.add(year2005Q4)
            it.add(year2005Q1)
            it.add(year2005Q2)
            it.add(year2006Q1) // duplicate
            it.add(yearInvalid) // invalid
        }
        val resultYear2002Q4 = QuarterVolumeItem(2002, 4, 0.000543f)
        val resultYear2002 = YearVolumeItem(
            2002,
            0.000543f,
            arrayOf(null, null, null, resultYear2002Q4),
            false
        )
        val resultYear2004Q3 = QuarterVolumeItem(2004, 3, 0.000384f)
        val resultYear2004 = YearVolumeItem(
            2004,
            0.000384f,
            arrayOf(null, null, resultYear2004Q3, null),
            false
        )
        val resultYear2005Q1 = QuarterVolumeItem(2005, 1, 0.0f)
        val resultYear2005Q2 = QuarterVolumeItem(2005, 2, 0.000614f)
        val resultYear2005Q3 = QuarterVolumeItem(2005, 3, 0.000718f)
        val resultYear2005Q4 = QuarterVolumeItem(2005, 4, 0.000801f)
        val resultYear2005 = YearVolumeItem(
            2005,
            0.0f + 0.000614f + 0.000718f + 0.000801f,
            arrayOf(resultYear2005Q1, resultYear2005Q2, resultYear2005Q3, resultYear2005Q4),
            false
        )
        val resultYear2006Q1 = QuarterVolumeItem(2006, 1, 0.00079f, true) // dropdown
        val resultYear2006Q3 = QuarterVolumeItem(2006, 3, 0.001735f)
        val resultYear2006 = YearVolumeItem(
            2006,
            0.00079f + 0.001735f,
            arrayOf(resultYear2006Q1, null, resultYear2006Q3, null),
            true
        )
        val resultYear2007Q2 = QuarterVolumeItem(2007, 2, 0.001189f)
        val resultYear2007 = YearVolumeItem(
            2007,
            0.001189f,
            arrayOf(null, resultYear2007Q2, null, null),
            false
        )

        val yearVolumeList = YearVolumeItem.getYearVolumeItemListFromRaw(rawList)

        assertThat(yearVolumeList.size).isEqualTo(5)
        assertThat(yearVolumeList[0]).isEqualTo(resultYear2002)
        assertThat(yearVolumeList[1]).isEqualTo(resultYear2004)
        assertThat(yearVolumeList[2]).isEqualTo(resultYear2005)
        assertThat(yearVolumeList[3]).isEqualTo(resultYear2006)
        assertThat(yearVolumeList[4]).isEqualTo(resultYear2007)
    }

    @Test
    fun getYearVolumeItemList_valid() {
        val year2004Q3 = QuarterVolumeItem(2004, 3, 0.000384f)
        val year2004Q4 = QuarterVolumeItem(2004, 4, 0.000543f)
        val year2005Q1 = QuarterVolumeItem(2005, 1, 0.00062f)
        val year2005Q2 = QuarterVolumeItem(2005, 2, 0.000614f) // dropdown
        val year2005Q3 = QuarterVolumeItem(2005, 3, 0.000718f)
        val year2005Q4 = QuarterVolumeItem(2005, 4, 0.000801f)
        val year2006Q1 = QuarterVolumeItem(2006, 1, 0.00089f)
        val year2006Q2 = QuarterVolumeItem(2006, 2, 0.001189f)
        val year2006Q3 = QuarterVolumeItem(2006, 3, 0.001735f)
        val quarterList = ArrayList<QuarterVolumeItem>().also {
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

        val yearVolumeList = YearVolumeItem.getYearVolumeItemList(quarterList)

        assertThat(yearVolumeList.size).isEqualTo(3)
        assertThat(yearVolumeList[0]).isEqualTo(resultYear2004)
        assertThat(yearVolumeList[1]).isEqualTo(resultYear2005)
        assertThat(yearVolumeList[2]).isEqualTo(resultYear2006)
    }

    @Test
    fun getYearVolumeItemList_invalid() {
        val year2002Q4 = QuarterVolumeItem(2002, 4, 0.000543f)
        val year2004Q3 = QuarterVolumeItem(2004, 3, 0.000384f)
        val year2005Q1 = QuarterVolumeItem(2005, 1, 0.0f)
        val year2005Q2 = QuarterVolumeItem(2005, 2, 0.000614f)
        val year2005Q3 = QuarterVolumeItem(2005, 3, 0.000718f)
        val year2005Q4 = QuarterVolumeItem(2005, 4, 0.000801f)
        val year2006Q1 = QuarterVolumeItem(2006, 1, 0.00079f) // dropdown
        val year2006Q3 = QuarterVolumeItem(2006, 3, 0.001735f)
        val year2007Q2 = QuarterVolumeItem(2007, 2, 0.001189f)
        val yearInvalid = QuarterVolumeItem(2007, 5, 0.001189f) // invalid
        val quarterList = ArrayList<QuarterVolumeItem>().also {
            // not in order
            it.add(year2004Q3)
            it.add(year2002Q4)
            it.add(year2005Q3)
            it.add(year2007Q2)
            it.add(year2006Q3)
            it.add(year2006Q1)
            it.add(year2005Q4)
            it.add(year2005Q1)
            it.add(year2005Q2)
            it.add(year2006Q1) // duplicate
            it.add(yearInvalid) // invalid
        }
        val resultYear2002Q4 = QuarterVolumeItem(2002, 4, 0.000543f)
        val resultYear2002 = YearVolumeItem(
            2002,
            0.000543f,
            arrayOf(null, null, null, resultYear2002Q4),
            false
        )
        val resultYear2004Q3 = QuarterVolumeItem(2004, 3, 0.000384f)
        val resultYear2004 = YearVolumeItem(
            2004,
            0.000384f,
            arrayOf(null, null, resultYear2004Q3, null),
            false
        )
        val resultYear2005Q1 = QuarterVolumeItem(2005, 1, 0.0f)
        val resultYear2005Q2 = QuarterVolumeItem(2005, 2, 0.000614f)
        val resultYear2005Q3 = QuarterVolumeItem(2005, 3, 0.000718f)
        val resultYear2005Q4 = QuarterVolumeItem(2005, 4, 0.000801f)
        val resultYear2005 = YearVolumeItem(
            2005,
            0.0f + 0.000614f + 0.000718f + 0.000801f,
            arrayOf(resultYear2005Q1, resultYear2005Q2, resultYear2005Q3, resultYear2005Q4),
            false
        )
        val resultYear2006Q1 = QuarterVolumeItem(2006, 1, 0.00079f, true) // dropdown
        val resultYear2006Q3 = QuarterVolumeItem(2006, 3, 0.001735f)
        val resultYear2006 = YearVolumeItem(
            2006,
            0.00079f + 0.001735f,
            arrayOf(resultYear2006Q1, null, resultYear2006Q3, null),
            true
        )
        val resultYear2007Q2 = QuarterVolumeItem(2007, 2, 0.001189f)
        val resultYear2007 = YearVolumeItem(
            2007,
            0.001189f,
            arrayOf(null, resultYear2007Q2, null, null),
            false
        )

        val yearVolumeList = YearVolumeItem.getYearVolumeItemList(quarterList)

        assertThat(yearVolumeList.size).isEqualTo(5)
        assertThat(yearVolumeList[0]).isEqualTo(resultYear2002)
        assertThat(yearVolumeList[1]).isEqualTo(resultYear2004)
        assertThat(yearVolumeList[2]).isEqualTo(resultYear2005)
        assertThat(yearVolumeList[3]).isEqualTo(resultYear2006)
        assertThat(yearVolumeList[4]).isEqualTo(resultYear2007)
    }

    @Test
    fun equals_true() {
        val oneQuarter4 = QuarterVolumeItem(2002, 4, 0.000543f)
        val oneYear = YearVolumeItem(
            2002,
            0.000543f,
            arrayOf(null, null, null, oneQuarter4),
            false
        )
        val otherQuarter4 = QuarterVolumeItem(2002, 4, 0.000543f)
        val otherYear = YearVolumeItem(
            2002,
            0.000543f,
            arrayOf(null, null, null, otherQuarter4),
            false
        )

        assertThat(oneYear).isEqualTo(otherYear)
    }

    @Test
    fun equals_false() {
        val oneQuarter4 = QuarterVolumeItem(2002, 4, 0.000543f)
        val oneYear = YearVolumeItem(
            2002,
            0.000543f,
            arrayOf(null, null, null, oneQuarter4),
            false
        )
        val otherQuarter4 = QuarterVolumeItem(2002, 4, 0.000543f)
        val otherYear1 = YearVolumeItem(
            2003,
            0.000543f,
            arrayOf(null, null, null, otherQuarter4),
            false
        )
        val otherYear2 = YearVolumeItem(
            2002,
            0.000542f,
            arrayOf(null, null, null, otherQuarter4),
            false
        )
        val otherYear3 = YearVolumeItem(
            2002,
            0.000543f,
            arrayOf(null, null, null, null),
            false
        )
        val otherYear4 = YearVolumeItem(
            2002,
            0.000543f,
            arrayOf(null, null, null, otherQuarter4),
            true
        )

        assertThat(oneYear).isNotEqualTo(otherYear1)
        assertThat(oneYear).isNotEqualTo(otherYear2)
        assertThat(oneYear).isNotEqualTo(otherYear3)
        assertThat(oneYear).isNotEqualTo(otherYear4)
    }
}