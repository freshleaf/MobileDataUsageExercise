package com.yuman.anotherexercise.volumedetails

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.yuman.anotherexercise.R
import com.yuman.anotherexercise.data.QuarterVolumeItem
import com.yuman.anotherexercise.data.YearVolumeItem
import com.yuman.anotherexercise.util.VOLUME_ITEM_KEY
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class VolumeDetailsFragmentTest {

    @Test
    fun volumeDetails_DisplayedInUi() {
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
        val bundle = Bundle().apply { putParcelable(VOLUME_ITEM_KEY, resultYear2005) }

        launchFragmentInContainer<VolumeDetailsFragment>(bundle, R.style.AppTheme)

        onView(withId(R.id.tvYear)).check(matches(withText("2005")))
        onView(withId(R.id.tvYearVolume)).check(matches(withText("0.002753")))
        onView(withId(R.id.tvQ1)).check(matches((withText("0.00062"))))
        onView(withId(R.id.tvQ2)).check(matches((withText("0.000614"))))
        onView(withId(R.id.tvQ3)).check(matches((withText("0.000718"))))
        onView(withId(R.id.tvQ4)).check(matches((withText("0.000801"))))

    }

}