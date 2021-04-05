package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View

class MyAccountActivity : Activity() {

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

    fun logOut(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply { }
        startActivity(intent)
    }

}