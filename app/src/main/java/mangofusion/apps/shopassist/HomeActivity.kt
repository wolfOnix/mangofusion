package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : Activity(), View.OnClickListener {

    private var btn_home: ImageButton? = null
    private var btn_my_account: ImageButton? = null
    private var btn_new_request: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_home = findViewById<View>(R.id.btn_home) as ImageButton
        btn_my_account = findViewById<View>(R.id.btn_my_account) as ImageButton
        btn_new_request = findViewById<View>(R.id.btn_new_request) as ImageButton

        btn_home!!.setOnClickListener(this)
        btn_my_account!!.setOnClickListener(this)
        btn_new_request!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.btn_my_account -> startActivity(Intent(this, MyAccountActivity::class.java))
            }
        }
    }

}