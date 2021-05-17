package mangofusion.apps.shopassist

import android.view.View
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpActivityTest {
    @Rule
    @JvmField
    var signUpActivityActivityTestRule = ActivityTestRule(
        SignUpActivity::class.java
    )
    var signUpActivity: SignUpActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        signUpActivity = signUpActivityActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        val view = signUpActivity!!.findViewById<View>(R.id.txvw_signup)
        assertNotNull(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        signUpActivity = null
    }
}