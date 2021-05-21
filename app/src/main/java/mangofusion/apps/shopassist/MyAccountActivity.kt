package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MyAccountActivity : Activity(), View.OnClickListener {

    private var btn_logout: Button? = null
    private var btn_update_data: Button? = null
    private var btn_sys_back: Button? = null
    private var txvw_firstname_lastname: TextView? = null
    private var txvw_user_email_add_telnr: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        btn_logout = findViewById<View>(R.id.btn_logout) as Button
        btn_logout!!.setOnClickListener(this)

        btn_update_data = findViewById<View>(R.id.btn_update_data) as Button
        btn_update_data!!.setOnClickListener(this)

        btn_sys_back = findViewById<View>(R.id.btn_sys_back) as Button
        btn_sys_back!!.setOnClickListener(this)

        txvw_firstname_lastname = findViewById<View>(R.id.txvw_firstname_lastname) as TextView
        txvw_user_email_add_telnr = findViewById<View>(R.id.txvw_user_email_add_telnr) as TextView

        if (CURR_USER == null) {
            mDatabase.child("users").child(getUserID()).get().addOnSuccessListener {
                CURR_USER = it.getValue(User::class.java)
                txvw_firstname_lastname!!.text = "${CURR_USER!!.firstName} ${CURR_USER!!.lastName}"
                txvw_user_email_add_telnr!!.text = "${CURR_USER!!.email}\n${CURR_USER!!.city}, ${CURR_USER!!.streetAndNumber}\n${CURR_USER!!.telephoneNumber}"
            }.addOnFailureListener { // sign out if error has occurred
                signOut(this)
            }
        } else {
            txvw_firstname_lastname!!.text = "${CURR_USER!!.firstName} ${CURR_USER!!.lastName}"
            txvw_user_email_add_telnr!!.text = "${CURR_USER!!.email}\n${CURR_USER!!.city}, ${CURR_USER!!.streetAndNumber}\n${CURR_USER!!.telephoneNumber}"
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_logout -> signOut(this)
                R.id.btn_update_data -> if (CURR_USER != null) startActivity(Intent(this, MyAccountUpdateDataActivity::class.java))
                R.id.btn_sys_back -> startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

}