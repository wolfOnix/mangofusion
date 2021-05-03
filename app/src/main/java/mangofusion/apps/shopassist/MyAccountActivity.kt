package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MyAccountActivity : Activity(), View.OnClickListener {

    private var btn_logout: Button? = null
    private var btn_update_data: Button? = null
    private var btn_sys_back: Button? = null
    private var txvw_firstname_lastname: TextView? = null
    private var txvw_user_email_add_telnr: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.post {

            setContentView(R.layout.activity_my_account)

            btn_logout = findViewById<View>(R.id.btn_logout) as Button
            btn_logout!!.setOnClickListener(this)

            btn_update_data = findViewById<View>(R.id.btn_update_data) as Button
            btn_update_data!!.setOnClickListener(this)

            btn_sys_back = findViewById<View>(R.id.btn_sys_back) as Button
            btn_sys_back!!.setOnClickListener(this)

            txvw_firstname_lastname = findViewById<View>(R.id.txvw_firstname_lastname) as TextView
            mDatabase.child("users").child(getUserID()).get().addOnSuccessListener {
                txvw_firstname_lastname!!.text =
                    "${it.child("firstName").value} ${it.child("lastName").value}"
            }.addOnFailureListener { // sign out if error has occurred
                signOut(this)
            }

            txvw_user_email_add_telnr = findViewById<View>(R.id.txvw_user_email_add_telnr) as TextView
            mDatabase.child("users").child(getUserID()).get().addOnSuccessListener {
                txvw_user_email_add_telnr!!.text =
                    "${it.child("email").value}\n${it.child("city").value}, ${it.child("streetAndNumber").value}\n${
                        it.child("telephoneNumber").value
                    }"
            }.addOnFailureListener { // sign out if error has occurred
                signOut(this)
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_logout -> signOut(this)
                R.id.btn_update_data -> startActivity(Intent(this, MyAccountUpdateDataActivity::class.java))
                R.id.btn_sys_back -> finish()
            }
        }
    }

}