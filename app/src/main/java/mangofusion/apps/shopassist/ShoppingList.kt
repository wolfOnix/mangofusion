package mangofusion.apps.shopassist

import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class ShoppingList {

    companion object {
        private var userCounter: Int = 0
    }

    var listID: String private set // shopping list unique ID
    var elementArray: MutableList<ShoppingListElement> private set
    var observations: String? private set
    var issuerID: String
    var bonusSum: Int private set
    var dateCreated: Calendar private set
    var invoices: MutableList<Invoice> private set
    var totalSum: Double private set
    var providerID: String? private set
    var taken: Boolean private set
    var fulfilled: Boolean private set
    var delivered: Boolean private set

    constructor(
        elmArr: MutableList<ShoppingListElement>,
        obs: String?,
        issuerUserID: String,
        bonus: Int
    ) {
        elementArray = elmArr
        observations = obs
        issuerID = issuerUserID
        bonusSum = bonus
        dateCreated = Calendar.getInstance()
        invoices = mutableListOf()
        totalSum = 0.0
        providerID = null
        taken = false
        fulfilled = false
        delivered = false
        listID = FirebaseAuth.getInstance().currentUser.uid
        listID += "_" + userCounter.toString()
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
        FirebaseDatabase.getInstance().getReference("lists")
            .child(FirebaseAuth.getInstance().currentUser.uid)
            .child(userCounter.toString())
            .setValue(this)/*.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@SignUpActivity,
                        getString(R.string.verification_email_sent),
                        Toast.LENGTH_LONG
                    ).show()
                    FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Failed to register! Try again!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }*/
    }

    fun takeList() { // called when a provider takes the request

    }

    fun deliverList() { // called when the provider finishes the request

    }

    fun killList() { // called when the shopping bag was delivered and the provider received the money

    }

    fun addInvoice(invoice: Invoice) {
        invoices.add(invoice)
    }

}