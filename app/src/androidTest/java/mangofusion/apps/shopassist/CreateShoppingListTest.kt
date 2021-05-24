package mangofusion.apps.shopassist

import android.view.View
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateShoppingListTest {
    @Rule @JvmField
    var createShoppingListActivityActivityTestRule = ActivityTestRule(
        CreateShoppingList::class.java
    )
    var createShoppingList: CreateShoppingList? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        createShoppingList = createShoppingListActivityActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        val view = createShoppingList!!.findViewById<View>(R.id.txvw_signup)
        assertNotNull(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        createShoppingList = null
    }
}