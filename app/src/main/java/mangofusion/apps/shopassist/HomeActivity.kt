package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : Activity(), View.OnClickListener {

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

        /* // TEST
        val els = mutableListOf<ShoppingListElement>()
        val elMilk = ShoppingListElement("lapte", 1.0, "l")
        val elTomato = ShoppingListElement("roșii", 1.5, "kg")
        val elCoke = ShoppingListElement("Pop Cola", 500.0, "ml")
        els.add(elMilk)
        els.add(elTomato)
        els.add(elCoke)

        val uID: String = FirebaseAuth.getInstance().currentUser.uid

        val shlist1 = ShoppingList(uID, null, els, "Dacă nu este Pop Cola, nu cumpărați altceva", 0)

        val elApple = ShoppingListElement("mere verzi", 1.5, "kg")
        els.add(elApple)

        val shlist2 = ShoppingList(uID, null, els, null, 15)

        shlist1.publishList()
        shlist2.publishList()*/
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

}