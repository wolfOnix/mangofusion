package mangofusion.apps.shopassist

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private var emailEditText: EditText? = null
    private var resetPasswordButton: Button? = null
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        emailEditText = findViewById<View>(R.id.edtxt_email) as EditText
        resetPasswordButton = findViewById<View>(R.id.button_reset_password) as Button
        auth = FirebaseAuth.getInstance()
        resetPasswordButton!!.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {
        val email = emailEditText!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            emailEditText!!.error = "Email is required!"
            emailEditText!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText!!.error = "Please provide a valid email!"
            emailEditText!!.requestFocus()
            return
        }
        auth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Check your email to reset your password!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Try again! Something wrong happend!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}