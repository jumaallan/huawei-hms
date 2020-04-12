package com.androidstudy.huaweihms.views.activities

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.androidstudy.huaweihms.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MapActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.INTERNET",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    @Test
    fun mapActivityTest() {
        val imageView = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView.perform(click())

        val imageView2 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        imageView2.perform(click())

        val ak = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        ak.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.recyclerView),
                childAtPosition(
                    withId(R.id.linearLayout),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
