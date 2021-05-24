package mangofusion.apps.shopassist

import android.view.View
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {
    @Rule
    @JvmField
    var createHomeActivityActivityTestRule = ActivityTestRule(
        HomeActivity::class.java
    )
    var createHomeActivity: HomeActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        createHomeActivity = createHomeActivityActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        val view = createHomeActivity!!.findViewById<View>(R.id.txvw_greeting)
        assertNotNull(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        createHomeActivity = null
    }
}