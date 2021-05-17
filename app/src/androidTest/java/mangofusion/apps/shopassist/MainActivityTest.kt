package mangofusion.apps.shopassist

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.*
import org.junit.Assert.*

class MainActivityTest {
    @Rule @JvmField
    var mainActivityActivityTestRule = ActivityTestRule(
        MainActivity::class.java
    )
    var mainActivity: MainActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        mainActivity = mainActivityActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        val view = mainActivity!!.findViewById<View>(R.id.button_sign_up)
        assertNotNull(view)
        //onView(withId(R.id.button_sign_up)).perform(click())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mainActivity = null
    }
}