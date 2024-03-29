package mangofusion.apps.shopassist

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ShoppingListElement(
    var elementName: String = "",
    var quantity: String = "",
    var unitOfMeasure: String = "",
    var ticked: Boolean = false
): Serializable {

    override fun toString(): String {
        return "$elementName\\$quantity\\$unitOfMeasure"
    }

}