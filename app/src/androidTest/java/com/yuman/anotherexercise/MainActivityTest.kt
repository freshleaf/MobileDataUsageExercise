package com.yuman.anotherexercise

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.yuman.anotherexercise.data.IVolumeRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CompletableFuture.anyOf

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private lateinit var repository: IVolumeRepository

    @Before
    fun setUp() {
        repository = ServiceLocator.provideVolumeRepository(getApplicationContext())
        runBlocking {
            repository.clearLocalCache()
        }
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun showList() = runBlocking {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.switchCardView)).perform((swipeLeft()))
        Thread.sleep(1000)
        onView(withId(R.id.switchCardView)).perform((swipeRight()))
        Thread.sleep(1000)
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.menuitem_clear_cache)).perform(click())
        Thread.sleep(1000)

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.menuitem_swipe)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.switchCardView)).perform((swipeLeft()))
        Thread.sleep(1000)
        onView(withId(R.id.switchCardView)).perform((swipeRight()))
        Thread.sleep(1000)

        // Make sure the activity is closed before resetting the db.
        activityScenario.close()
    }

    @Test
    fun showDetails() = runBlocking {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(2000)
        onView(withId(R.id.recycleView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        pressBack()

        activityScenario.close()
    }
}