package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View

class HomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
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