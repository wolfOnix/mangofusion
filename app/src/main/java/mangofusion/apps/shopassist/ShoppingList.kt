package mangofusion.apps.shopassist

import android.content.Intent
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

        fun eraseList(list: ShoppingList): Boolean { // called to permanently erase the shopping list before being taken
            println("${list.listID} ->")
            if (!list.listID.contains(list.issuerID) || list.listID.isEmpty()) return false
            if (list.taken || list.fulfilled || list.delivered || !list.providerID.isEmpty()) return false
            FirebaseDatabase.getInstance().reference.child("lists").child(list.listID).setValue(null)
            println("erased")
            return true
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
        if (listID == "") {
            FirebaseDatabase.getInstance().reference.child("listIndex").get().addOnSuccessListener {
                val globalCounter: Long = if (it.value != null) it.value.toString().toLong() else 0
                FirebaseDatabase.getInstance().reference.child("listIndex").setValue(globalCounter + 1)
                listID = issuerID + "_" + globalCounter.toString()
                FirebaseDatabase.getInstance().reference.child("lists").child(this.listID).setValue(this)
            }
        } else FirebaseDatabase.getInstance().reference.child("lists").child(this.listID).setValue(this)
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

    fun claimDeliveredList(value: Boolean = true) {
        this.claimedDelivered = value;
        val ref = FirebaseDatabase.getInstance().reference.child("lists").child(this.listID).child("claimedDelivered").setValue(value)
    }

    fun closeList(value: Boolean = true) { // called when the shopping bag was delivered and the provider received the money
        this.delivered = value;
        val ref = FirebaseDatabase.getInstance().reference.child("lists").child(this.listID).child("delivered").setValue(value)
    }

    fun addInvoice(invoice: Invoice) {
        val tempList: MutableList<Invoice> = invoices as MutableList<Invoice>
        tempList.add(invoice)
        invoices = tempList
    }

}