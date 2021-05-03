package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


class CreateShoppingList : Activity(), View.OnClickListener {

    private var btn_create_list_next: Button? = null
    private var txvw_art_nr: TextView? = null
    private var art_nr: Int = 0
    private var lnlyElement: String = "lnly_art_"
    private var elementContainer: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shopping_list)

        elementContainer = findViewById(R.id.lnly_element_container)

        btn_create_list_next = findViewById<Button>(R.id.btn_create_list_next)
        btn_create_list_next!!.setOnClickListener(this)

        addShoppingListElementField(null)
    }

    fun goBack(view: View) {
        finish()
    }

    fun updateDisplay() {
        txvw_art_nr = findViewById<TextView>(R.id.txvw_article_number)
        txvw_art_nr!!.text = resources.getQuantityString(R.plurals.number_of_articles, art_nr, art_nr) // pass art_nr twice: 1st for selecting the plural state and the other for the actual replacement
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_create_list_next -> startActivity(Intent(this, PublishShoppingList::class.java))
            }
        }
    }

    fun addShoppingListElementField(v: View?) {
        val inflater: LayoutInflater = LayoutInflater.from(applicationContext)
        val v: View = layoutInflater.inflate(R.layout.shopping_list_container, elementContainer as ViewGroup?, false)
        elementContainer?.addView(v, art_nr)
        art_nr++
        updateDisplay()
    }

    fun prepareShoppingList() {

    }
}
