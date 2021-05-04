package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.*
import mangofusion.apps.shopassist.ShoppingList.Companion.getReasonPos


class CreateShoppingList : Activity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private var btn_create_list_next: Button? = null
    private var btns_bonus = mutableListOf<Button>()
    private var txvw_art_nr: TextView? = null
    private lateinit var edtx_observations: EditText
    private var art_nr: Int = 0
    private var elementContainer: ViewGroup? = null
    private var bonusSum: Long = 0
    private var reasonPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shopping_list)

        elementContainer = findViewById(R.id.lnly_element_container)

        btn_create_list_next = findViewById(R.id.btn_create_list_next)
        btn_create_list_next!!.setOnClickListener(this)

        btns_bonus.add(findViewById(R.id.btn_bonus_0))
        btns_bonus.add(findViewById(R.id.btn_bonus_5))
        btns_bonus.add(findViewById(R.id.btn_bonus_10))
        btns_bonus.add(findViewById(R.id.btn_bonus_15))
        btns_bonus.add(findViewById(R.id.btn_bonus_20))
        for (i in btns_bonus) i.setOnClickListener(this)

        edtx_observations = findViewById(R.id.edtx_observations)

        addShoppingListElementField(null)

        val spinner: Spinner = findViewById(R.id.spn_reason)
        ArrayAdapter.createFromResource(
            this,
            R.array.available_reasons,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Specify the layout to use when the list of choices appears
            spinner.adapter = adapter // Apply the adapter to the spinner
        }

        with(spinner)
        {
            adapter = adapter
            setSelection(0, false)
            onItemSelectedListener = this@CreateShoppingList
        }
    }

    fun goBack(view: View) {
        finish()
    }

    fun updateDisplay() {
        txvw_art_nr = findViewById<TextView>(R.id.txvw_article_number)
        txvw_art_nr!!.text = resources.getQuantityString(
            R.plurals.number_of_articles,
            art_nr,
            art_nr
        ) // pass art_nr twice: 1st for selecting the plural state and the other for the actual replacement
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_create_list_next -> if (prepareShoppingList()) {
                    println("Check OK")
                    findViewById<TextView>(R.id.txvw_error_invalid_shopping_list).visibility = View.GONE
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    findViewById<TextView>(R.id.txvw_error_invalid_shopping_list).visibility = View.VISIBLE
                }
                R.id.btn_bonus_0 -> { recolour(0); bonusSum = 0 }
                R.id.btn_bonus_5 -> { recolour(1); bonusSum = 5 }
                R.id.btn_bonus_10 -> { recolour(2); bonusSum = 10 }
                R.id.btn_bonus_15 -> { recolour(3); bonusSum = 15 }
                R.id.btn_bonus_20 -> { recolour(4); bonusSum = 20 }
            }
        }
    }

    private fun recolour(pos: Int) {
        for (i in btns_bonus) {
            i.setBackgroundColor(ContextCompat.getColor(this, R.color.carbon10P))
            i.setTextColor(ContextCompat.getColor(this, R.color.carbon))
        }
        btns_bonus[pos].setBackgroundColor(ContextCompat.getColor(this, R.color.purple))
        btns_bonus[pos].setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    fun addShoppingListElementField(v: View?) {
        val inflater: LayoutInflater = LayoutInflater.from(applicationContext)
        val v: View = layoutInflater.inflate(
            R.layout.shopping_list_container,
            elementContainer,
            false
        )
        elementContainer?.addView(v, art_nr)
        art_nr++
        println(elementContainer!!.childCount)
        updateDisplay()
    }

    private fun prepareShoppingList(): Boolean {
        val shoppingElements = mutableListOf<ShoppingListElement>()

        for (i in 0 until elementContainer!!.childCount) {
            val lnlyContainer: ViewGroup = elementContainer!!.getChildAt(i) as ViewGroup? ?: return false // eject from function if lnlyContainer is null
            val edtxArtName: EditText = lnlyContainer.getChildAt(0) as EditText? ?: return false
            val edtxQuantity: EditText = (lnlyContainer.getChildAt(1) as ViewGroup).getChildAt(0) as EditText? ?: return false
            val edtxUnit: EditText = (lnlyContainer.getChildAt(1) as ViewGroup).getChildAt(1) as EditText? ?: return false

            val artName = edtxArtName.text.toString().trim(' ')
            if (artName == "" || artName == " " || artName.isEmpty()) return false

            val tempStr: String = edtxQuantity.text.toString().trim(' ')
            if (tempStr.isEmpty() || tempStr == " ") return false
            if(!noLetter(tempStr)) return false

            val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault()) // support number with comma decimal separation
            val number: Number? = format.parse(tempStr)
            if (number == null || number.toDouble() == 0.0) return false
            val quant: Double = number.toDouble()

            var unit = edtxUnit.text.toString().trim(' ').toLowerCase(Locale.ROOT)
            if (unit.toIntOrNull() != null) return false // numbers are not expected in the unit of measure
            if (unit == "" || unit == " " || unit.isEmpty()) unit = "-"

            shoppingElements.add(ShoppingListElement(artName, quant, unit))

            println("$i: $artName, $quant, $unit")
        }
        val observations: String = edtx_observations.text.toString().trim(' ')
        val shoppingList = ShoppingList(getUserID(), null, shoppingElements, observations, reasonPos.toLong(), bonusSum)
        shoppingList.publishList() // !! TEMPORARILY
        return true
    }

    private fun noLetter(str: String): Boolean {
        for (c in str) {
            if (c !in '0'..'9' && c != '.' && c != ',')
                return false
        }
        return true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        reasonPos = position
        println("Spinner: $position und ${getString(getReasonPos(position))}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }
}
