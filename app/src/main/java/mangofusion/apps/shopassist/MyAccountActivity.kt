package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MyAccountActivity : Activity(),View.OnClickListener {

    private var button_Logout: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        button_Logout = findViewById<View>(R.id.button_Logout) as Button
        button_Logout!!.setOnClickListener(this)
    }

    fun goBack(view: View) {
        finish()
    }

    fun logOut(view: View) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, MainActivity::class.java).apply { }
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@MyAccountActivity,MainActivity::class.java))
    }

}