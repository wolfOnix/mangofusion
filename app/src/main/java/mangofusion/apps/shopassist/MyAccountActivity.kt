package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MyAccountActivity : Activity(), View.OnClickListener {

    private var btn_logout: Button? = null
    private var btn_update_data: Button? = null
    private var btn_sys_back: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        btn_logout = findViewById<View>(R.id.btn_logout) as Button
        btn_update_data = findViewById<View>(R.id.btn_update_data) as Button
        btn_sys_back = findViewById<View>(R.id.btn_sys_back) as Button

        btn_logout!!.setOnClickListener(this)
        btn_update_data!!.setOnClickListener(this)
        btn_sys_back!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.btn_update_data -> startActivity(Intent(this, MyAccountUpdateDataActivity::class.java))
                R.id.btn_sys_back -> finish()
            }
        }
    }

}