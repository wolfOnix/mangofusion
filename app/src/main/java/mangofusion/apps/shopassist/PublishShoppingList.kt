package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class PublishShoppingList : Activity(), View.OnClickListener {

    private var btn_publish_list: Button? = null
    private lateinit var shoppingList: ShoppingList
    private var elementsReadyContainer: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_shopping_list)

        elementsReadyContainer = findViewById(R.id.lnly_elements_ready)

        btn_publish_list = findViewById(R.id.btn_publish_list)
        btn_publish_list!!.setOnClickListener(this)

        shoppingList = intent.getSerializableExtra("shoppingListKeeper") as ShoppingList

        if (!fillList(shoppingList)) println("something is not ok")

        println("Bonus sum: ${shoppingList.bonusSum}")

        /*val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(R.color.purple.toString()),
                Color.parseColor(R.color.magenta.toString()))
        );
        gradientDrawable.cornerRadius = 0f;

        findViewById<View>(R.id.btn_publish_list).background = gradientDrawable;*/
    }

    @SuppressLint("SetTextI18n")
    fun fillList(shList: ShoppingList): Boolean {
        val sz: Int = shList.elementArray.size

        val txvwArtNr = findViewById<TextView>(R.id.txvw_article_number)
        txvwArtNr!!.text = resources.getQuantityString(R.plurals.number_of_articles, sz, sz)

        val txvwNotesContent = findViewById<TextView>(R.id.txvw_notes_content)
        val txvwSubtitleNotes = findViewById<TextView>(R.id.txvw_subtitle_notes)

        val txvwBonusSum = findViewById<TextView>(R.id.txvw_bonus_sum)
        txvwBonusSum.text = "${shList.bonusSum} RON"

        val txvwReason = findViewById<TextView>(R.id.txvw_reason)
        txvwReason.text = getString(ShoppingList.getReasonPos(shList.reason.toInt()))

        if (shList.observations.isEmpty()) {
            txvwSubtitleNotes.visibility = View.GONE
            txvwNotesContent.visibility = View.GONE
        } else {
            txvwNotesContent.text = shList.observations
        }

        for (i in 0 until sz) {
            val inflater: LayoutInflater = LayoutInflater.from(applicationContext)
            val v: View = layoutInflater.inflate(R.layout.shopping_list_element, elementsReadyContainer, false)

            elementsReadyContainer?.addView(v, i)

            val txvwArtName: TextView = (elementsReadyContainer?.getChildAt(i) as ViewGroup).getChildAt(1) as TextView? ?: return false
            val txvwQuantAndUnit: TextView = (elementsReadyContainer?.getChildAt(i) as ViewGroup).getChildAt(2) as TextView? ?: return false

            txvwArtName.text = shList.elementArray[i].elementName
            txvwQuantAndUnit.text = "${shList.elementArray[i].quantity} ${shList.elementArray[i].unitOfMeasure}"

        }
        return true
    }

    fun goBack(view: View) {
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_publish_list -> { shoppingList.publishList(); startActivity(Intent(this, HomeActivity::class.java)) }
            }
        }
    }
}