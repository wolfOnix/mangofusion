package mangofusion.apps.shopassist

import android.view.View
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyAccountActivityTest {
    @Rule
    @JvmField
    var myAccountActivityActivityActivityTestRule = ActivityTestRule(
        MyAccountActivity::class.java
    )
    var myAccountActivity: MyAccountActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        myAccountActivity = myAccountActivityActivityActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        val view = myAccountActivity!!.findViewById<View>(R.id.txvw_title_top)
        assertNotNull(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        myAccountActivity = null
    }
}