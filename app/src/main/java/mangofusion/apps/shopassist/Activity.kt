package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class Activity: AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    protected var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    protected var USER_AS_ISSUER: Boolean = false
    protected var USER_AS_PROVIDER: Boolean = false

    companion object {

        var CURR_USER: User? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser == null) { // if user is not signed in, close app
            finishAffinity()
        }
    }

    protected fun getUserID(): String {
        val currUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return currUser?.uid ?: ""
    }

    fun signOut(currActivity: AppCompatActivity) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(currActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK) // prevent going to logged in activities, and exit the app
        startActivity(intent)
        finish()
    }
}