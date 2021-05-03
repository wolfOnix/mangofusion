package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class Activity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser == null) { // if user is not signed in, go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun getUserID(): String {
        val currUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return currUser?.uid ?: ""
    }

    fun signOut(currActivity: AppCompatActivity) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(currActivity, MainActivity::class.java))
    }
}