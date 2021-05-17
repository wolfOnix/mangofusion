package mangofusion.apps.shopassist

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ShoppingListElementTest {

    @Test
    fun `empty fields returns false` () {
        val result = ShoppingListElementUtil.validateShoppingListElementInput(
            "",
            "",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty elementName returns false` () {
        val result = ShoppingListElementUtil.validateShoppingListElementInput(
            "",
            "21",
            "kg"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty quantity returns false` () {
        val result = ShoppingListElementUtil.validateShoppingListElementInput(
            "Milk",
            "",
            "L"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty unitOfMeasure returns false` () {
        val result = ShoppingListElementUtil.validateShoppingListElementInput(
            "Tomato",
            "1",
            ""
        )
        assertThat(result).isFalse()
    }

}