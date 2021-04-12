package mangofusion.apps.shopassist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MyAccountUpdateDataActivity : Activity(), View.OnClickListener {

    private var btn_sys_back: Button? = null
    private var btn_update_data: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account_update_data)

        btn_sys_back = findViewById<View>(R.id.btn_sys_back) as Button
        btn_sys_back!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_sys_back -> finish()
            }
        }
    }

}