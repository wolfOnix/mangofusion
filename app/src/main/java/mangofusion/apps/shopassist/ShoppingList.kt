package mangofusion.apps.shopassist

import com.google.firebase.database.FirebaseDatabase
import androidx.appcompat.app.AppCompatActivity
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
    var reason: Long,
    var bonusSum: Long,
    var dateCreated: String = SimpleDateFormat("yyyy.MM.dd/HH:mm:ss").format(Date()),
    var invoices: List<Invoice> = listOf(),
    var totalSum: Double = 0.0,
    var providerID: String = "",
    var taken: Boolean = false,
    var fulfilled: Boolean = false,
    var delivered: Boolean = false
): Serializable {

    companion object {
        private val reasonsArr = arrayOf<Int>(R.string.home_isolation, R.string.elderly_person, R.string.time_shortage, R.string.locomotive_deficiency, R.string.unspecified)

        fun getReasonPos(pos: Int): Int {
            return reasonsArr[pos]
        }
    }

    private fun calculateTotalSum() {
        if (invoices.isNotEmpty()) {
            for (i in invoices) {
                totalSum = totalSum.plus(i.getSum())
            }
        } else {
            totalSum = 0.0
        }
    }

    fun publishList() {
        FirebaseDatabase.getInstance().reference.child("listIndex").get().addOnSuccessListener {
            val globalCounter = it.value.toString().toLong()
            FirebaseDatabase.getInstance().reference.child("listIndex").setValue(globalCounter + 1)
            listID = issuerID + "_" + globalCounter.toString()
            FirebaseDatabase.getInstance().reference
                .child("lists")
                .child(issuerID)
                .child(globalCounter.toString())
                .setValue(this)
        }/*.addOnFailureListener { // listIndex is not set
            val globalCounter: Long = 0
            FirebaseDatabase.getInstance().reference.child("listIndex").setValue(globalCounter + 1)
            listID = issuerID + "_" + globalCounter.toString()
            FirebaseDatabase.getInstance().reference
                .child("lists")
                .child(issuerID)
                .child(globalCounter.toString())
                .setValue(this)
        }*/
        // to update a list, overwrite the new shoppinglist on that ID
        println("Sent")
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