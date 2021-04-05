package mangofusion.apps.shopassist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MyAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)
    }

    fun goHome(view: View) {
        val intent = Intent(this, HomeActivity::class.java).apply { }
        startActivity(intent)
    }

    fun goToMyAccount(view: View) {
        val intent = Intent(this, MyAccountActivity::class.java).apply { }
        startActivity(intent)
    }

}