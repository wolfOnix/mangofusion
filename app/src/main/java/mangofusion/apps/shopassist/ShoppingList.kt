package mangofusion.apps.shopassist

import java.time.LocalDateTime
import java.util.*

class ShoppingList(private var elementArray: Array<ShoppingListElement>, private var observations: String?, private var issuer: User) {

    private var dateCreated: Calendar = Calendar.getInstance()
    private var invoices: Array<Invoice>? = null
    private var totalSum: Double? = 0.0

    private fun calculateTotalSum() {
        if (invoices != null) {
            for (i in invoices!!) {
                totalSum = totalSum?.plus(i.getSum())
            }
        }
    }

}