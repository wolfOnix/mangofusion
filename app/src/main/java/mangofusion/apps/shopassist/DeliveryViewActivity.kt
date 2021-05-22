package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeliveryViewActivity : Activity(), View.OnClickListener {

    lateinit var shList: ShoppingList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_view)

        findViewById<ImageButton>(R.id.btn_home).setOnClickListener(this)
        findViewById<ImageButton>(R.id.btn_my_account).setOnClickListener(this)

        shList = intent.getSerializableExtra("takenShoppingList") as ShoppingList

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.refresher)
        swipeRefreshLayout.setOnRefreshListener { // set refresh behaviour
            swipeRefreshLayout.isRefreshing = false
            startActivity(Intent(this, this::class.java).putExtra("takenShoppingList", shList))
        }

        if (intent.hasExtra("issuerUser")) {
            THE_OTHER_USER = intent.getSerializableExtra("issuerUser") as User
            goFurther()
        }
        else {
            mDatabase.child("users").child(shList.issuerID).get().addOnSuccessListener {
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

        findViewById<TextView>(R.id.txvw_other_user_name).text = "${THE_OTHER_USER!!.firstName} ${THE_OTHER_USER!!.lastName}"
        findViewById<TextView>(R.id.txvw_delivery_address).text = "${THE_OTHER_USER!!.city}, ${THE_OTHER_USER!!.streetAndNumber}"

        findViewById<TextView>(R.id.txvw_current_status).text = getString(R.string.on_delivery)

        findViewById<TextView>(R.id.txvw_complete_sum).text = "${shList.totalSum + (shList.bonusSum.toDouble())} RON"
        findViewById<TextView>(R.id.txvw_shopping_sum_and_bonus).text = "${shList.totalSum} RON\n${shList.bonusSum} RON"

        (findViewById<Button>(R.id.btn_call)).setOnClickListener(this)
        (findViewById<Button>(R.id.btn_route)).setOnClickListener(this)
        findViewById<Button>(R.id.btn_finish_delivery).setOnClickListener(this)

        if (shList.claimedDelivered) waitUntilConfirmed()
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
                R.id.btn_route -> { startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://maps.google.com/maps?saddr=${THE_OTHER_USER?.city}, ${THE_OTHER_USER?.streetAndNumber}"))) }
                R.id.btn_finish_delivery -> { shList.claimDeliveredList(); waitUntilConfirmed() }
            }
        }
    }

    private fun waitUntilConfirmed() {
        /*val deliveryConfirmationContainer: ViewGroup = findViewById<LinearLayout>(R.id.lnly_delivery_confirmation_container)
        deliveryConfirmationContainer.removeViewAt(1)*/

        findViewById<Button>(R.id.btn_finish_delivery).visibility = View.GONE

        val txvwWaitForDelivery: TextView = findViewById(R.id.txvw_wait_for_delivery)
        txvwWaitForDelivery.visibility = View.VISIBLE
        txvwWaitForDelivery.text = getString(R.string.the_issuer_has_to_confirm_the_delivery, "60")

        findViewById<ProgressBar>(R.id.prbr_linear).visibility = View.VISIBLE

        val timer = object: CountDownTimer(60000, 1000) { // check every 5 s for delivery confirmation but not more than 60 s
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished / 1000 % 5 == 0L)
                    mDatabase.child("lists").child(shList.listID).child("delivered").get().addOnSuccessListener {
                        if (it.value == true) {
                            this.cancel() // stop countdown
                            goIllustration()
                        }
                    }
                txvwWaitForDelivery.text = getString(R.string.the_issuer_has_to_confirm_the_delivery, (millisUntilFinished / 1000 + 1).toString())
            }
            override fun onFinish() {
                shList.claimDeliveredList(false) // cancel delivered claim
                shList.closeList(false) // cancel eventual delivered confirmation
                findViewById<Button>(R.id.btn_finish_delivery).visibility = View.VISIBLE
                txvwWaitForDelivery.visibility = View.INVISIBLE
                findViewById<ProgressBar>(R.id.prbr_linear).visibility = View.INVISIBLE
            }
        }
        timer.start()
    }

    private fun goIllustration() {
        startActivity(Intent(this, DeliveryDoneActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}