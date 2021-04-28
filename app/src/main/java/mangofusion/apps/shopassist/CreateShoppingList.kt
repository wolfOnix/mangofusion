package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class CreateShoppingList : Activity(), View.OnClickListener {

    private var btn_create_list_next: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shopping_list)

        btn_create_list_next = findViewById<View>(R.id.btn_create_list_next) as Button
        btn_create_list_next!!.setOnClickListener(this)
    }

    fun goBack(view: View) {
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_create_list_next -> startActivity(Intent(this, PublishShoppingList::class.java))
            }
        }
    }
}