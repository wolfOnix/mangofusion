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
    var claimedDelivered: Boolean = false,
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
            globalCounter = if (it.value != null) it.value.toString().toLong() else 0
            FirebaseDatabase.getInstance().reference.child("listIndex").setValue(globalCounter + 1)
            listID = issuerID + "_" + globalCounter.toString()
            FirebaseDatabase.getInstance().reference
                .child("lists")
                .child(listID)
                .setValue(this)
        }
    }

    fun takeList(provID: String) { // called when a provider takes the request
        this.providerID = provID
        this.taken = true
        val ref = FirebaseDatabase.getInstance().reference.child("lists").child(this.listID) // point to the list in the database
        ref.child("providerID").setValue(provID)
        ref.child("taken").setValue(true)
    }

    fun deliverList() { // called when the provider finishes the request
        this.fulfilled = true
        val ref = FirebaseDatabase.getInstance().reference.child("lists").child(this.listID) // point to the list in the database
        ref.child("fulfilled").setValue(true)
        ref.child("totalSum").setValue(this.totalSum)
    }

    fun closeList() { // called when the shopping bag was delivered and the provider received the money

    }

    fun eraseList() { // called to permanently erase the shopping list before being taken
        if (this.taken || this.fulfilled || this.delivered || !providerID.isEmpty()) return
        FirebaseDatabase.getInstance().reference.child("lists").child(this.listID).removeValue()
    }

    fun addInvoice(invoice: Invoice) {
        val tempList: MutableList<Invoice> = invoices as MutableList<Invoice>
        tempList.add(invoice)
        invoices = tempList
    }

}