package mangofusion.apps.shopassist

import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
data class ShoppingList(
    var issuerID: String = "",
    var listID: String = "", // shopping list unique ID
    var elementArray: List<ShoppingListElement> = listOf(),
    var observations: String = "",
    var reason: Long = 0,
    var bonusSum: Long = 0,
    var dateCreated: String = SimpleDateFormat("yyyy.MM.dd/HH:mm:ss").format(Date()),
    var invoices: List<Invoice> = listOf(),
    var totalSum: Double = 0.0,
    var providerID: String = "",
    var taken: Boolean = false,
    var fulfilled: Boolean = false,
    var delivered: Boolean = false
): Serializable {

    companion object {
        private val reasonsArr = arrayOf<Int>(
            R.string.home_isolation,
            R.string.elderly_person,
            R.string.time_shortage,
            R.string.locomotive_deficiency,
            R.string.unspecified
        )

        fun getReasonPos(pos: Int): Int {
            return reasonsArr[pos]
        }
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "listID" to listID,
            "issuerID" to issuerID
        )
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

        // TODO DO NOT PUBLISH MORE THAN ONE LIST !!!

        FirebaseDatabase.getInstance().reference.child("listIndex").get().addOnSuccessListener {
            val globalCounter: Long
            if (it.value != null)
                globalCounter = it.value.toString().toLong()
            else
                globalCounter = 0
            FirebaseDatabase.getInstance().reference.child("listIndex").setValue(globalCounter + 1)
            listID = issuerID + "_" + globalCounter.toString()
            FirebaseDatabase.getInstance().reference
                .child("lists")
                .child(listID)
                .setValue(this)
        }/*.addOnFailureListener { // listIndex is not set // VERY SUSPICIOUS UNDETECTION OF MISSING listIndex :.(
            val globalCounter: Long = 0
            FirebaseDatabase.getInstance().reference.child("listIndex").setValue(globalCounter + 1)
            listID = issuerID + "_" + globalCounter.toString()
            FirebaseDatabase.getInstance().reference
                .child("lists")
                .child(issuerID)
                .child(globalCounter.toString())
                .setValue(this)
        }*/
        println("Sent")
    }

    fun takeList(providerID: String) { // called when a provider takes the request
        val ref = FirebaseDatabase.getInstance().reference.child("lists").child(this.listID) // point to the list in the database
        ref.child("providerID").setValue(providerID)
        ref.child("taken").setValue(true)
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