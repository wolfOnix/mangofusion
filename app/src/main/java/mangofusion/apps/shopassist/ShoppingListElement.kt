package mangofusion.apps.shopassist

class ShoppingListElement(
    private var elementName: String, // TODO Check avoiding the \ sign
    private var quantity: Double,
    private var unitOfMeasure: String
) {

    override fun toString(): String {
        return "$elementName\\$quantity\\$unitOfMeasure"
    }

}