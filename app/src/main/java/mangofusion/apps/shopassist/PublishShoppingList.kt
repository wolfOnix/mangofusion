package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase

class PublishShoppingList : Activity(), View.OnClickListener {

    private var btnPublishList: Button? = null
    private lateinit var shoppingList: ShoppingList
    private var elementsReadyContainer: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_shopping_list)

        elementsReadyContainer = findViewById(R.id.lnly_elements_ready)

        btnPublishList = findViewById(R.id.btn_publish_list)
        btnPublishList!!.setOnClickListener(this)

        shoppingList = intent.getSerializableExtra("shoppingListKeeper") as ShoppingList

        fillList(shoppingList)
    }

    @SuppressLint("SetTextI18n")
    fun fillList(shList: ShoppingList) {
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

            val txvwArtName: TextView = (elementsReadyContainer?.getChildAt(i) as ViewGroup).getChildAt(1) as TextView
            val txvwQuantAndUnit: TextView = (elementsReadyContainer?.getChildAt(i) as ViewGroup).getChildAt(2) as TextView

            txvwArtName.text = shList.elementArray[i].elementName
            txvwQuantAndUnit.text = "${shList.elementArray[i].quantity} ${shList.elementArray[i].unitOfMeasure}"
        }
    }

    fun goBack(view: View) {
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_publish_list -> {
                    FirebaseDatabase.getInstance().reference.child("listIndex").get().addOnSuccessListener {
                        val globalCounter: Long = if (it.value != null) it.value.toString().toLong() else 0
                        FirebaseDatabase.getInstance().reference.child("listIndex").setValue(globalCounter + 1)
                        shoppingList.listID = shoppingList.issuerID + "_" + globalCounter.toString()
                        shoppingList.publishList()
                        startActivity(Intent(this, HomeActivity::class.java).putExtra("publishedList", shoppingList))
                    }
                }
            }
        }
    }
}