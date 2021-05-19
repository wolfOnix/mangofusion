package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MyAccountUpdateDataActivity : Activity(), View.OnClickListener {

    private var btnSysBack: Button? = null
    private var btnUpdateUser: Button? = null
    private lateinit var edtxtFirstname: EditText
    private lateinit var edtxtLastname: EditText
    private lateinit var edtxtTelephonenumber: EditText
    private lateinit var edtxtCity: EditText
    private lateinit var edtxtAddress: EditText
    private lateinit var initFirstname: String
    private lateinit var initLastname: String
    private lateinit var initTelephonenumber: String
    private lateinit var initCity: String
    private lateinit var initAddress: String
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account_update_data)

        edtxtFirstname = findViewById(R.id.edtxt_firstname)
        edtxtLastname = findViewById(R.id.edtxt_lastname)
        edtxtTelephonenumber = findViewById(R.id.edtxt_telephonenumber)
        edtxtCity = findViewById(R.id.edtxt_city)
        edtxtAddress = findViewById(R.id.edtxt_address)

        btnSysBack = findViewById<View>(R.id.btn_sys_back) as Button
        btnSysBack!!.setOnClickListener(this)

        btnUpdateUser = findViewById<View>(R.id.btn_update_data) as Button
        btnUpdateUser?.setOnClickListener(this)

        fillFields()
    }

    private fun fillFields() {
        mDatabase.child("users").child(getUserID()).get().addOnSuccessListener {
            initFirstname = "${it.child("firstName").value}"
            edtxtFirstname.setText(initFirstname)

            initLastname = "${it.child("lastName").value}"
            edtxtLastname .setText(initLastname)

            initTelephonenumber = "${it.child("telephoneNumber").value}"
            edtxtTelephonenumber.setText(initTelephonenumber)

            initCity = "${it.child("city").value}"
            edtxtCity.setText(initCity)

            initAddress = "${it.child("streetAndNumber").value}"
            edtxtAddress .setText(initAddress)
        }
    }

    private fun updateUser() {

        var noError: Boolean = true
        var somethingChanged: Boolean = false

        val firstName = edtxtFirstname.text.toString().trim { it <= ' ' }.filter { it.isLetter() || it == ' '}
        val lastName = edtxtLastname.text.toString().trim { it <= ' ' }.filter { it.isLetter() || it == ' ' }
        val telephoneNumber = edtxtTelephonenumber.text.toString().filter { it.isDigit() || it == '+' || it == '.' || it == '-' }
        val city = edtxtCity.text.toString().trim { it <= ' ' }.filter { it.isLetter() || it == ',' || it == '-' }
        val streetAndNumber = edtxtAddress.text.toString().trim { it <= ' ' }

        if (firstName.isEmpty()) noError = false
        if (firstName != initFirstname) somethingChanged = true

        if (lastName.isEmpty()) noError = false
        if (lastName != initLastname) somethingChanged = true

        if (city.isEmpty()) noError = false
        if (city != initCity) somethingChanged = true

        if (streetAndNumber.isEmpty()) noError = false
        if (streetAndNumber != initAddress) somethingChanged = true

        if (telephoneNumber.length < 8 || telephoneNumber.length > 18) noError = false
        if (telephoneNumber != initTelephonenumber) somethingChanged = true

        if (!noError) {
            val txvwError: TextView = findViewById(R.id.txvw_error)
            txvwError.visibility = View.VISIBLE
            return
        }

        if (somethingChanged) {
            val ref = mDatabase.child("users").child(getUserID())

            ref.child("firstName").setValue(firstName)
            ref.child("lastName").setValue(lastName)
            ref.child("telephoneNumber").setValue(telephoneNumber)
            ref.child("city").setValue(city)
            ref.child("streetAndNumber").setValue(streetAndNumber)

            Toast.makeText(this, getString(R.string.account_updated), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.there_was_nothing_to_update), Toast.LENGTH_LONG).show()
        }

        startActivity(Intent(this, MyAccountActivity::class.java))
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_sys_back -> finish()
                R.id.btn_update_data -> updateUser()
            }
        }
    }

}