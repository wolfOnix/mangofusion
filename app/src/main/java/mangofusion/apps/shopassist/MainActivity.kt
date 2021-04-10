package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class MainActivity : Activity(), View.OnClickListener {

    private var button_sign_up: Button? = null
    private var button_forgot_password: Button? = null
    private var button_Login: Button? = null
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.getCurrentUser() != null) { // if user is already logged in, jump to HomeActivity
            goHome(null)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_sign_up = findViewById<View>(R.id.button_sign_up) as Button
        button_sign_up!!.setOnClickListener(this)
        button_forgot_password = findViewById<View>(R.id.button_forgot_password) as Button
        button_forgot_password!!.setOnClickListener(this)
        button_Login = findViewById<View>(R.id.button_Login) as Button
        button_Login!!.setOnClickListener(this)
        editTextEmail = findViewById<View>(R.id.edtxt_email) as EditText
        editTextPassword = findViewById<View>(R.id.edtxt_password) as EditText
    }

    fun goHome(view: View?) {
        val intent = Intent(this, HomeActivity::class.java).apply { }
        startActivity(intent)
    }

    fun goToSignUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java).apply { }
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.button_sign_up -> startActivity(Intent(this, SignUpActivity::class.java))
                R.id.button_Login -> userLogin()
                R.id.button_forgot_password -> startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }
        }
    }

    private fun userLogin() {
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            editTextEmail!!.error = getString(R.string.required_field)
            editTextEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail!!.error = getString(R.string.email_not_valid)
            editTextEmail!!.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editTextPassword!!.error = getString(R.string.required_field)
            editTextPassword!!.requestFocus()
            return
        }
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user.isEmailVerified) {
                        // redirect to user profile
                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    } else {
                        user.sendEmailVerification()
                        Toast.makeText(
                            this@MainActivity,
                            "Check your email to verify your account!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.login_not_successful), // @string resource. TO be changed to xml labels
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}
