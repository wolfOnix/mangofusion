package mangofusion.apps.shopassist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class DeliveryViewActivity : Activity(), View.OnClickListener {

    lateinit var shList: ShoppingList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_view)

        findViewById<ImageButton>(R.id.btn_home).setOnClickListener(this)
        findViewById<ImageButton>(R.id.btn_my_account).setOnClickListener(this)

        shList = intent.getSerializableExtra("takenShoppingList") as ShoppingList

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

    private fun goFurther() {

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.btn_my_account -> startActivity(Intent(this, MyAccountActivity::class.java))
                // R.id.btn_call -> { startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:${THE_OTHER_USER?.telephoneNumber}"))) }
            }
        }
    }
}