package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class FulfillShoppingActivity : Activity(), View.OnClickListener {

    private var shList: ShoppingList? = null
    private var otherUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fulfill_shopping)

        shList = intent.getSerializableExtra("takenShoppingList") as ShoppingList
        otherUser = intent.getSerializableExtra("issuerUser") as User

        findViewById<Button>(R.id.btn_begin_delivery).setOnClickListener(this)
    }

    private fun prepareShoppingList(): Boolean {
        val totalSum: Double
        val txvwErr: TextView = findViewById(R.id.txvw_err)
        val sum = findViewById<EditText>(R.id.edtxt_total_sum).text.toString().trim(' ')
        if (sum == "" || sum == " " || sum.isEmpty()) { txvwErr.text = getString(R.string.field_should_not_be_empty); return false}
        try {
            totalSum = sum.toDouble()
            shList?.totalSum = totalSum
        } catch (e: NumberFormatException) {
            txvwErr.text = getString(R.string.the_sum_does_not_have_a_valid_format);
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_begin_delivery -> {
                    if (prepareShoppingList()) {
                        shList?.deliverList()
                        startActivity(Intent(this, DeliveryViewActivity::class.java).putExtra("takenShoppingList", shList).putExtra("issuerUser", otherUser))
                    }
                }
            }
        }
    }

    fun goBack(view: View) {
        finish()
    }
}