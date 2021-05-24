package mangofusion.apps.shopassist

import android.view.View
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForgotPasswordActivityTest {
    @Rule
    @JvmField
    var createForgotPasswordActivityActivityTestRule = ActivityTestRule(
        ForgotPasswordActivity::class.java
    )
    var createForgotPasswordActivity: ForgotPasswordActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        createForgotPasswordActivity = createForgotPasswordActivityActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        val view = createForgotPasswordActivity!!.findViewById<View>(R.id.txvw_description)
        assertNotNull(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        createForgotPasswordActivity = null
    }
}