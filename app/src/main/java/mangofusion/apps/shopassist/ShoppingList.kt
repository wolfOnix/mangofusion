package mangofusion.apps.shopassist

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*
import java.io.Serializable

@IgnoreExtraProperties
data class ShoppingList (
    var issuerID: String,
    var listID: String? = null, // shopping list unique ID
    var elementArray: List<ShoppingListElement>,
    var observations: String?,
    var reason: String,
    var bonusSum: Long,
    var dateCreated: String = SimpleDateFormat("yyyy.mm.dd/HH:mm:ss").format(Date()),
    var invoices: List<Invoice> = listOf(),
    var totalSum: Double = 0.0,
    var providerID: String? = null,
    var taken: Boolean = false,
    var fulfilled: Boolean = false,
    var delivered: Boolean = false
): Serializable {

    companion object {
        private var userCounter: Int = 0
    }

    private fun calculateTotalSum() {
        if (invoices.size > 0) {
            for (i in invoices) {
                totalSum = totalSum.plus(i.getSum())
            }
        } else {
            totalSum = 0.0
        }
    }

    fun publishList() {
        //var database: DatabaseReference = Firebase.database.reference
        listID = issuerID + "_" + userCounter
        println("Fang")
        FirebaseDatabase.getInstance().reference
            .child("lists")
            .child(issuerID)
            .child(userCounter.toString())
            .setValue(this)
        userCounter++
        println("Gesandt")
    }

    fun takeList() { // called when a provider takes the request

    }

    fun deliverList() { // called when the provider finishes the request

    }

    fun killList() { // called when the shopping bag was delivered and the provider received the money

    }

    fun addInvoice(invoice: Invoice) {
        val tempList: MutableList<Invoice> = invoices as MutableList<Invoice>
        tempList.add(invoice)
        invoices = tempList
    }

}