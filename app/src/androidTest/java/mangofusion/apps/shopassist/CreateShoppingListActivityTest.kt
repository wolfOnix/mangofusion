package mangofusion.apps.shopassist

import android.view.View
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateShoppingListActivityTest {
    @Rule @JvmField
    var createShoppingListActivityActivityTestRule = ActivityTestRule(
        CreateShoppingListActivity::class.java
    )
    var createShoppingListActivity: CreateShoppingListActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        createShoppingListActivity = createShoppingListActivityActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        val view = createShoppingListActivity!!.findViewById<View>(R.id.txvw_signup)
        assertNotNull(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        createShoppingListActivity = null
    }
}