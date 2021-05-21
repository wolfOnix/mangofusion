package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class TakeListActivity : Activity(), View.OnClickListener {

    private lateinit var shoppingList: ShoppingList
    private lateinit var issuerUser: User
    private var elementsReadyContainer: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_list)

        elementsReadyContainer = findViewById(R.id.lnly_elements_ready)

        (findViewById<Button>(R.id.btn_take_request)).setOnClickListener(this)
        (findViewById<Button>(R.id.btn_call)).setOnClickListener(this)

        val listAndIssuer : HashMap<String, Any> = intent.getSerializableExtra("listAndIssuerMAP") as HashMap<String, Any>
        shoppingList = listAndIssuer["shoppingList"] as ShoppingList
        issuerUser = listAndIssuer["issuerUser"] as User

        (findViewById<View>(R.id.txvw_other_user_name) as TextView).text = "${issuerUser.firstName} ${issuerUser.lastName}"
        (findViewById<View>(R.id.txvw_delivery_address) as TextView).text = "${issuerUser.city}, ${issuerUser.streetAndNumber}"

        /*mDatabase.child("users").child(getUserID()).get().addOnSuccessListener {
            txvw_firstname_lastname = findViewById<View>(R.id.txvw_firstname_lastname) as TextView
            txvw_user_email_add_telnr = findViewById<View>(R.id.txvw_user_email_add_telnr) as TextView

            txvw_firstname_lastname!!.text = "${it.child("firstName").value} ${it.child("lastName").value}"
            txvw_user_email_add_telnr!!.text = "${it.child("email").value}\n${it.child("city").value}, ${it.child("streetAndNumber").value}\n${it.child("telephoneNumber").value}"
        }*/

        fillList(shoppingList)
    }

    @SuppressLint("SetTextI18n")
    fun fillList(shList: ShoppingList) {
        val sz: Int = shList.elementArray.size

        val txvwArtNr = findViewById<TextView>(R.id.txvw_article_number)
        txvwArtNr!!.text = resources.getQuantityString(R.plurals.number_of_articles, sz, sz)

        val txvwNotesContent = findViewById<TextView>(R.id.txvw_notes_content)
        val txvwSubtitleNotes = findViewById<TextView>(R.id.txvw_subtitle_notes)

        (findViewById<TextView>(R.id.txvw_bonus_sum)).text = "${shList.bonusSum} RON"
        (findViewById<TextView>(R.id.txvw_reason)).text = getString(ShoppingList.getReasonPos(shList.reason.toInt()))

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
                R.id.btn_take_request -> { shoppingList.takeList(getUserID()); startActivity(Intent(this, HomeActivity::class.java)) }
                R.id.btn_call -> { startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:${issuerUser.telephoneNumber}"))) }
            }
        }
    }
}