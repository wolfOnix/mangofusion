package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity: Activity(), View.OnClickListener {

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

        displayRequests()
    }

    @SuppressLint("SetTextI18n")
    private fun displayRequests(): Int {

        val ref = FirebaseDatabase.getInstance().getReference("lists")
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listOfShLists: MutableList<ShoppingList> = ArrayList()
                for (ds in dataSnapshot.children) {
                    val shList: ShoppingList? = ds.getValue(ShoppingList::class.java)
                    if (shList != null && shList.issuerID != getUserID()) {
                        listOfShLists.add(shList)
                        println(shList.observations)
                    }
                }
                //val lastItem: ShoppingList = list[list.size - 1]
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        ref.addListenerForSingleValueEvent(valueEventListener)

        return 0
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.btn_new_request -> startActivity(Intent(this, CreateShoppingList::class.java))
                R.id.btn_my_account -> startActivity(Intent(this, MyAccountActivity::class.java))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
