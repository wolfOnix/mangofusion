package mangofusion.apps.shopassist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /* Called when the user taps the Send button */
    fun goToSignUp(view: View) {
        val intent = Intent(this, SignUp::class.java).apply { }
        startActivity(intent)
    }
}