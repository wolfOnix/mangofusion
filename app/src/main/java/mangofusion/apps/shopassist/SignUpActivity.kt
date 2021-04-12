package mangofusion.apps.shopassist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.security.MessageDigest
import java.util.*

class SignUpActivity : Activity(), View.OnClickListener {

    private var locationPermissionGranted = false

    private var banner: TextView? = null
    private var signUp: Button? = null
    private var btn_sys_back: Button? = null
    private var editTextFirstName: EditText? = null
    private var editTextLastName: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var editTextConfirmPassword: EditText? = null
    private var editTextBirthdayDate: EditText? = null
    private var editTextTelephoneNumber: EditText? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        banner = findViewById<View>(R.id.txtvw_signup) as TextView
        banner!!.setOnClickListener(this)
        signUp = findViewById<View>(R.id.button_create_account) as Button
        btn_sys_back = findViewById<View>(R.id.btn_sys_back) as Button
        signUp!!.setOnClickListener(this)
        btn_sys_back!!.setOnClickListener(this)
        editTextFirstName = findViewById<View>(R.id.edtxt_firstname) as EditText
        editTextLastName = findViewById<View>(R.id.edtxt_lastname) as EditText
        editTextEmail = findViewById<View>(R.id.edtxt_email) as EditText
        editTextPassword = findViewById<View>(R.id.edtxt_password) as EditText
        editTextConfirmPassword = findViewById<View>(R.id.edtxt_confirmpassword) as EditText
        editTextBirthdayDate = findViewById<View>(R.id.edtxt_birthdaydate) as EditText
        editTextTelephoneNumber = findViewById<View>(R.id.edtxt_telephonenumber) as EditText
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.txtvw_signup -> startActivity(Intent(this, MainActivity::class.java))
            R.id.button_create_account -> registerUser()
            R.id.btn_sys_back -> finish()
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        private val TAG = MapsActivityCurrentPlace::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }

    private fun registerUser() {
        val firstName = editTextFirstName!!.text.toString().trim { it <= ' ' }
        val lastName = editTextLastName!!.text.toString().trim { it <= ' ' }
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password: String = editTextPassword!!.text.toString()
        val confirmPassword: String = editTextConfirmPassword!!.text.toString()
        val birthdayDate = editTextBirthdayDate!!.text.toString().trim { it <= ' ' }
        val telephoneNumber = editTextTelephoneNumber!!.text.toString().trim { it <= ' ' }
        if (firstName.isEmpty()) {
            editTextFirstName!!.error = "First name is required!"
            editTextFirstName!!.requestFocus()
            return
        }
        if (lastName.isEmpty()) {
            editTextLastName!!.error = "Last name is required!"
            editTextLastName!!.requestFocus()
            return
        }
        if (email.isEmpty()) {
            editTextEmail!!.error = "Email is required!"
            editTextEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail!!.error = "Please provide a valid email"
            editTextEmail!!.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editTextPassword!!.error = "Password is required!"
            editTextPassword!!.requestFocus()
            return
        }
        if (password.length < 6) {
            editTextPassword!!.error = "Minimum password should be 6 characters"
            editTextPassword!!.requestFocus()
            return
        }
        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword!!.error = "Confirm password is required!"
            editTextConfirmPassword!!.requestFocus()
            return
        }
        if (!confirmPassword.contentEquals(password)) {
            editTextConfirmPassword!!.error = "Passwords don't match"
            editTextConfirmPassword!!.requestFocus()
            return
        }
        if (birthdayDate.isEmpty()) {
            return
        }
        if (telephoneNumber.isEmpty()) {
            editTextTelephoneNumber!!.error = "Telephone number is required!"
            editTextTelephoneNumber!!.requestFocus()
            return
        }
        if (telephoneNumber.length != 10) {
            editTextTelephoneNumber!!.error = "Telephone number should be 10 digits"
            editTextTelephoneNumber!!.requestFocus()
        }
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user =
                        User(firstName, lastName, email, birthdayDate, telephoneNumber, "NO_ADDRESS_ADDED")
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
                    Toast.makeText(this@SignUpActivity, "", Toast.LENGTH_SHORT).show()
                }
            }
    }

}