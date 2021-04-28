package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SignUpActivity : Activity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    // private var locationPermissionGranted = false
    private var mAuth: FirebaseAuth? = null

    private var signUp: Button? = null
    private var btn_sys_back: Button? = null
    private var country: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        signUp = findViewById<View>(R.id.button_create_account) as Button
        signUp!!.setOnClickListener(this)
        btn_sys_back = findViewById<View>(R.id.btn_sys_back) as Button
        btn_sys_back!!.setOnClickListener(this)

        val spinner: Spinner = findViewById(R.id.spn_country)
        ArrayAdapter.createFromResource(
            this,
            R.array.available_countries,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Specify the layout to use when the list of choices appears
            spinner.adapter = adapter // Apply the adapter to the spinner
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_create_account -> registerUser()
            R.id.btn_sys_back -> finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        country = parent.getItemAtPosition(pos).toString()
        when (country) {
            R.string.Romania.toString() -> country = "RO"
            R.string.Germany.toString() -> country = "DE"
            R.string.The_United_Kingdom.toString() -> country = "UK"
        }
        println("Hey")
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    private fun registerUser() {

        var noError: Boolean = true

        val edtxt_firstname: EditText = findViewById<View>(R.id.edtxt_firstname) as EditText
        val edtxt_lastname: EditText = findViewById<View>(R.id.edtxt_lastname) as EditText
        val edtxt_email: EditText = findViewById<View>(R.id.edtxt_email) as EditText
        val edtxt_password: EditText = findViewById<View>(R.id.edtxt_password) as EditText
        val edtxt_confirmpassword: EditText = findViewById<View>(R.id.edtxt_confirmpassword) as EditText
        val edtxt_birthdaydate: EditText = findViewById<View>(R.id.edtxt_birthdaydate) as EditText
        val edtxt_telephonenumber: EditText = findViewById<View>(R.id.edtxt_telephonenumber) as EditText
        val edtxt_city: EditText = findViewById<View>(R.id.edtxt_city) as EditText
        val edtxt_street_and_number: EditText = findViewById<View>(R.id.edtxt_street_and_number) as EditText

        val firstName = edtxt_firstname.text.toString().trim { it <= ' ' }.filter { it.isLetter() || it == ' '}
        val lastName = edtxt_lastname.text.toString().trim { it <= ' ' }.filter { it.isLetter() || it == ' ' }
        val email = edtxt_email.text.toString().trim { it <= ' ' }
        val password: String = edtxt_password.text.toString()
        val confirmPassword: String = edtxt_confirmpassword.text.toString()
        val birthdayDate = edtxt_birthdaydate.text.toString().filter { it.isDigit() || it == '.' || it == '/' || it == '-' }
        val telephoneNumber = edtxt_telephonenumber.text.toString().filter { it.isDigit() || it == '+' || it == '.' || it == '-' }
        val city = edtxt_city.text.toString().trim { it <= ' ' }.filter { it.isLetter() || it == ',' || it == '-' }
        val streetAndNumber = edtxt_street_and_number.text.toString().trim { it <= ' ' }

        val err_edtxt_firstname: TextView = findViewById<View>(R.id.txvw_error_firstname) as TextView
        val err_edtxt_lastname: TextView = findViewById<View>(R.id.txvw_error_lastname) as TextView
        val err_edtxt_email: TextView = findViewById<View>(R.id.txvw_error_email) as TextView
        val err_edtxt_password: TextView = findViewById<View>(R.id.txvw_error_password) as TextView
        val err_edtxt_birthdaydate: TextView = findViewById<View>(R.id.txvw_error_birthdaydate) as TextView
        val err_edtxt_telephonenumber: TextView = findViewById<View>(R.id.txvw_error_telephonenumber) as TextView
        val err_edtxt_city: TextView = findViewById<View>(R.id.txvw_error_city) as TextView
        val err_edtxt_street_and_number: TextView = findViewById<View>(R.id.txvw_error_streetandnumber) as TextView
        val err_edtxt_country: TextView = findViewById<View>(R.id.txvw_error_country) as TextView

        if (firstName.isEmpty()) {
            err_edtxt_firstname.text = (getString(R.string.field_should_not_be_empty))
            err_edtxt_firstname.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_firstname.visibility = View.GONE
        }
        if (lastName.isEmpty()) {
            err_edtxt_lastname.text = (getString(R.string.field_should_not_be_empty))
            err_edtxt_lastname.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_lastname.visibility = View.GONE
        }
        if (email.isEmpty()) {
            err_edtxt_email.text = (getString(R.string.field_should_not_be_empty))
            err_edtxt_email.visibility = View.VISIBLE
            noError = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            err_edtxt_email.text = (getString(R.string.email_not_valid))
            err_edtxt_email.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_email.visibility = View.GONE
        }
        
        if (password.isEmpty()) {
            err_edtxt_password.text = getString(R.string.field_should_not_be_empty)
            err_edtxt_password.visibility = View.VISIBLE
            noError = false
        } else if (!confirmPassword.contentEquals(password)) {
            err_edtxt_password.text = getString(R.string.passwords_do_not_match)
            err_edtxt_password.visibility = View.VISIBLE
            noError = false
        } else if (password.length < 6) {
            err_edtxt_password.text = getString(R.string.password_is_too_weak)
            err_edtxt_password.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_password.visibility = View.GONE
        }

        if (birthdayDate.isEmpty()) {
            err_edtxt_birthdaydate.text = getString(R.string.field_should_not_be_empty)
            err_edtxt_birthdaydate.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_birthdaydate.visibility = View.GONE
        } /*else if (birthdayDate.length != 10) { // TODO regex check
            errorTextView = findViewById<View>(R.id.txvw_error_birthdaydate) as TextView
            errorTextView.text = getString(R.string.this_field_does_not_respect_the_standard_format)
            errorTextView.visibility = View.VISIBLE
        }*/

        if (telephoneNumber.isEmpty()) {
            err_edtxt_telephonenumber.text = getString(R.string.field_should_not_be_empty)
            err_edtxt_telephonenumber.visibility = View.VISIBLE
            noError = false
        } else if (telephoneNumber.length != 10) { // TODO regex check
            err_edtxt_telephonenumber.text = getString(R.string.this_field_does_not_respect_the_standard_format)
            err_edtxt_telephonenumber.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_telephonenumber.visibility = View.GONE
        }

        if (city.isEmpty()) {
            err_edtxt_city.text = getString(R.string.field_should_not_be_empty)
            err_edtxt_city.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_city.visibility = View.GONE
        }

        if (streetAndNumber.isEmpty()) {
            err_edtxt_street_and_number.text = getString(R.string.field_should_not_be_empty)
            err_edtxt_street_and_number.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_street_and_number.visibility = View.GONE
        }

        /*if (country == "") { // TODO
            err_edtxt_country.text = getString(R.string.field_should_not_be_empty)
            err_edtxt_country.visibility = View.VISIBLE
            noError = false
        } else {
            err_edtxt_country.visibility = View.GONE
        }*/

        if (!noError) { // At least one error appeared
            edtxt_password.setText("")
            edtxt_confirmpassword.setText("")
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user =
                        User(firstName, lastName, email, birthdayDate, telephoneNumber, city, streetAndNumber, "RO") // TODO RO
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().currentUser.uid)
                        .setValue(user).addOnCompleteListener { task ->
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
                        }
                } else {
                    // Toast.makeText(this@SignUpActivity, "", Toast.LENGTH_SHORT).show()
                }
            }
    }

}