package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DeliveryViewByIssuerActivity : Activity(), View.OnClickListener {

    lateinit var shList: ShoppingList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_view_by_issuer)

        findViewById<ImageButton>(R.id.btn_home).setOnClickListener(this)
        findViewById<ImageButton>(R.id.btn_my_account).setOnClickListener(this)

        shList = intent.getSerializableExtra("takenShoppingList") as ShoppingList

        val refresher = findViewById<SwipeRefreshLayout>(R.id.refresher)
        refresher.setColorSchemeResources(R.color.purple)
        refresher.setOnRefreshListener { // set refresh behaviour
            refresher.isRefreshing = false
            startActivity(Intent(this, HomeActivity::class.java))
        }

        if (intent.hasExtra("providerUser")) {
            THE_OTHER_USER = intent.getSerializableExtra("providerUser") as User
            goFurther()
        }
        else {
            mDatabase.child("users").child(shList.providerID).get().addOnSuccessListener {
                val issuerUser: User? = it.getValue(User::class.java)
                THE_OTHER_USER = issuerUser
                goFurther()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun goFurther() {
        if (CURR_USER == null) { // if the user is not allocated
            FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener {
                CURR_USER = it.getValue(User::class.java)
                greet()
            }
        } else greet()

        if (shList.claimedDelivered) {
            val deliveryConfirmationContainer: ViewGroup = findViewById<LinearLayout>(R.id.lnly_delivery_confirmation_container)
            val v: View = layoutInflater.inflate(R.layout.delivery_confirm_card, deliveryConfirmationContainer, false)
            deliveryConfirmationContainer.addView(v, 0)

            findViewById<TextView>(R.id.txvw_confirm_by_issuer_info).text = getString(R.string.PHRASE_confirm_delivery_by_issuer, "${THE_OTHER_USER!!.firstName} ${THE_OTHER_USER!!.lastName}")
            findViewById<TextView>(R.id.txvw_complete_sum_small).text = "${shList.totalSum + shList.bonusSum} RON"

            findViewById<Button>(R.id.btn_confirm_delivery).setOnClickListener(this)
        }

        findViewById<TextView>(R.id.txvw_other_user_name).text = "${THE_OTHER_USER!!.firstName} ${THE_OTHER_USER!!.lastName}"

        findViewById<LinearLayout>(R.id.lnly_status_container).visibility = View.GONE

        findViewById<TextView>(R.id.txvw_complete_sum).text = "${shList.totalSum + (shList.bonusSum.toDouble())} RON"
        findViewById<TextView>(R.id.txvw_shopping_sum_and_bonus).text = "${shList.totalSum} RON\n${shList.bonusSum} RON"

        findViewById<TextView>(R.id.txvw_price_card_by_issuer_info).text = getString(R.string.PHRASE_delivery_info_by_issuer, "${THE_OTHER_USER!!.firstName} ${THE_OTHER_USER!!.lastName}")

        (findViewById<Button>(R.id.btn_call)).setOnClickListener(this)
    }

    private fun greet() {
        findViewById<TextView>(R.id.txvw_greeting_firstname).text = CURR_USER!!.firstName
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.btn_my_account -> startActivity(Intent(this, MyAccountActivity::class.java))
                R.id.btn_call -> { startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:${THE_OTHER_USER?.telephoneNumber}"))) }
                R.id.btn_confirm_delivery -> { shList.closeList(); startActivity(Intent(this, DeliveryDoneByIssuerActivity::class.java)) }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}