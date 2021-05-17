package mangofusion.apps.shopassist

object ShoppingListElementUtil {

    /***
     * the input is not valid if...
     * ...the fields are empty
     * ...the element name is empty
     * ...the quantity is empty
     * ...the unit of measure is empty
     *
     */

    fun validateShoppingListElementInput(
        elementName: String,
        quantity: String,
        unitOfMeasure: String
    ): Boolean {
        if(elementName.isEmpty() || quantity.isEmpty() || unitOfMeasure.isEmpty()) {
            return false
        }

        return true
    }
}