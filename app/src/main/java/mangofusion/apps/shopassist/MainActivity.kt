package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("test")
        myRef.setValue("Hello, World!")
    }

    /* Called when the user taps the Send button */
    fun goToSignUp(view: View) {
        val intent = Intent(this, SignUp::class.java).apply { }
        startActivity(intent)
    }
}