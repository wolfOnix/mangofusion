package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import mangofusion.apps.shopassist.ShoppingList.Companion.eraseList

class HomeActivity: Activity(), View.OnClickListener {

    private var btnHome: ImageButton? = null
    private var txvwRequestsNr: TextView? = null
    private var txvwRequestsText: TextView? = null
    private var nrNoRoleLists: Int = 0
    private var shoppingListsReadyContainer: ViewGroup? = null
    private val listOfShLists: MutableList<ShoppingList> = ArrayList()
    private val listOfIssuers: MutableList<User> = ArrayList()
    private var singleShList: ShoppingList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (CURR_USER == null) { // if the user is not allocated
            FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener {
                CURR_USER = it.getValue(User::class.java)
                greet()
            }
        } else greet()

        val refresher = findViewById<SwipeRefreshLayout>(R.id.refresher)
        refresher.setColorSchemeResources(R.color.purple)
        refresher.setOnRefreshListener { // set refresh behaviour
            refresher.isRefreshing = false
            startActivity(Intent(this, HomeActivity::class.java))
        }

        shoppingListsReadyContainer = findViewById(R.id.lnly_all_lists_container)

        txvwRequestsNr = findViewById(R.id.txvw_requests_nr)
        txvwRequestsText = findViewById(R.id.txvw_requests_text)

        btnHome = findViewById(R.id.btn_home)
        btnHome!!.setOnClickListener(this)
        findViewById<ImageButton>(R.id.btn_my_account).setOnClickListener(this)
        findViewById<ImageButton>(R.id.btn_new_request).setOnClickListener(this)

        if (intent.hasExtra("publishedList")) { // if activity is returned after shopping list publish, load it immediately, without firebase consulting
            USER_AS_PROVIDER = false
            USER_AS_ISSUER = true
            val shList = intent.getSerializableExtra("publishedList") as ShoppingList
            singleShList = shList
            displayCard(shList, (if (shList.taken) 2 else 1), 0)
        } else loadShoppingRequests()
    }

    private fun greet() {
        findViewById<TextView>(R.id.txvw_greeting_firstname).text = CURR_USER!!.firstName
    }

    @SuppressLint("SetTextI18n")
    private fun loadShoppingRequests() {

        val ref = FirebaseDatabase.getInstance().getReference("lists")

        val valueEventListener: ValueEventListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    val shList: ShoppingList? = ds.getValue(ShoppingList::class.java)
                    if (shList != null && !shList.delivered) { // the shopping list is still in request
                        if (shList.providerID == getUserID()) { // the current user is the provider of the shopping list -> no other shopping list will be displayed
                            USER_AS_PROVIDER = true
                            USER_AS_ISSUER = false
                            singleShList = shList
                            if (!shList.fulfilled) // is still not in the delivery process
                                displayCard(shList, 3, 0)
                            else { // is in the delivery process
                                startActivity(Intent(this@HomeActivity, DeliveryViewActivity::class.java).putExtra("takenShoppingList", shList))
                            }
                            //adaptLayout(3)
                            listOfShLists.clear()
                            break
                        } else if (shList.issuerID == getUserID()) { // the current user is the issuer
                            if (!USER_AS_PROVIDER) USER_AS_ISSUER = true // the shopping list belongs to the current user
                            if (shList.fulfilled) // is on delivery
                                startActivity(Intent(this@HomeActivity, DeliveryViewByIssuerActivity::class.java).putExtra("takenShoppingList", shList))
                            else {
                                singleShList = shList
                                displayCard(shList, (if (shList.taken) 2 else 1), 0)
                            }
                            // adaptLayout(2 || 1)
                            listOfShLists.clear()
                            break
                        } else if(!shList.taken && !shList.fulfilled) listOfShLists.add(shList)
                    }
                }

                if (!USER_AS_ISSUER && !USER_AS_PROVIDER) {
                    if (listOfShLists.size > 0) { // there are lists to display
                        println("CP1")
                        loopRequests(listOfShLists.toTypedArray())
                    } else if (listOfShLists.size == 0) { // there is no list to display
                        println("CP2")
                        txvwRequestsNr?.text = "0"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun loopRequests(shListArr: Array<ShoppingList>) { // user has no role
        nrNoRoleLists = shListArr.size
        txvwRequestsText?.text = resources.getString(R.string.PHRASE_available_requests_nearby)
        txvwRequestsNr?.text = nrNoRoleLists.toString()

        for (i in 0 until nrNoRoleLists) {
            displayCard(shListArr[i], 0, i)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun adaptLayout(mode: Int, reason: Long = -1, bonus: Long = -1) {

        // disable requests counter and enable info according to the active mode
        findViewById<LinearLayout>(R.id.lnly_requests_nr).visibility = View.GONE
        val infoContainer: ViewGroup = findViewById(R.id.lnly_status_and_contact)
        infoContainer.visibility = View.VISIBLE

        when (mode) {
            1 -> {
                findViewById<TextView>(R.id.txvw_current_status).text = getString(R.string.waiting_to_be_taken)
                findViewById<TextView>(R.id.txvw_alternative_phrase).text = getString(R.string.PHRASE_the_list_is_waiting_to_be_taken)

                findViewById<ImageButton>(R.id.btn_new_request).setImageResource(R.drawable.icon_cancel) // change navigation center icon

                infoContainer.removeViewAt(2) // hide contact details
            }
            2, 3 -> {

                if (mode == 2) {
                    findViewById<TextView>(R.id.txvw_current_status).text = getString(R.string.the_request_is_taken)
                    findViewById<TextView>(R.id.txvw_alternative_phrase).text = getString(R.string.PHRASE_issuer_taken_info_home, "${THE_OTHER_USER!!.firstName} ${THE_OTHER_USER!!.lastName}")

                    findViewById<ImageButton>(R.id.btn_new_request).visibility = View.GONE // hide '+' button
                } else {
                    findViewById<TextView>(R.id.txvw_current_status).text = getString(R.string.shopping)
                    findViewById<TextView>(R.id.txvw_alternative_phrase).text = getString(R.string.PHRASE_provider_info_home)

                    findViewById<ImageButton>(R.id.btn_new_request).setImageResource(R.drawable.icon_shopping_finished)
                }

                /*val v: View = layoutInflater.inflate(R.layout.the_other_user_contact, infoContainer, false)
                infoContainer.addView(v, 2)*/

                if (mode == 2) { // hide delivery address
                    val contactLayout: ViewGroup = infoContainer.getChildAt(2) as ViewGroup
                    contactLayout.getChildAt(2).visibility = View.GONE
                } else { // show the delivery address
                    findViewById<TextView>(R.id.txvw_delivery_address).text = "${THE_OTHER_USER!!.city}, ${THE_OTHER_USER!!.streetAndNumber}"
                }
                findViewById<TextView>(R.id.txvw_other_user_name).text = "${THE_OTHER_USER!!.firstName} ${THE_OTHER_USER!!.lastName}"

                (findViewById<Button>(R.id.btn_call)).setOnClickListener(this)
            }
        }
        (findViewById<TextView>(R.id.txvw_bonus_sum)).text = "$bonus RON"
        (findViewById<TextView>(R.id.txvw_reason)).text = getString(ShoppingList.getReasonPos(reason.toInt()))
    }

    @SuppressLint("SetTextI18n")
    private fun displayCard(shList: ShoppingList, mode: Int, order: Int) {
        /* mode:
        0 - user - no role
        1 - user - issuer, shList - not taken
        2 - user - issuer, shList - taken
        3 - user - provider */

        // inflate the shoppingListReadyContainer with a card
        val v: View = if (mode == 0) layoutInflater.inflate(R.layout.shopping_list_card_openable, shoppingListsReadyContainer, false) // add card with 'open' button
            else layoutInflater.inflate(R.layout.shopping_list_ready_container, shoppingListsReadyContainer, false) // add card without button, but with extra details
        shoppingListsReadyContainer?.addView(v, order)

        val thisCardContainer: ViewGroup = v as ViewGroup // the card added through inflation

        if (mode == 0) { // link the action to the 'open' button
            val btnOpenList: Button = (thisCardContainer.getChildAt(0) as ViewGroup).getChildAt(7) as Button
            btnOpenList.setOnClickListener { openList(order) }
        }

        // fill the card ->

        val sz: Int = shList.elementArray.size

        ((thisCardContainer.getChildAt(0) as ViewGroup).getChildAt(1) as TextView).text = resources.getQuantityString(R.plurals.number_of_articles, sz, sz) // write the number of articles in the list

        val elementsReadyContainer: ViewGroup = (thisCardContainer.getChildAt(0) as ViewGroup).getChildAt(2) as ViewGroup // the container which contains all the articles

        val txvwSubtitleNotes: TextView = (thisCardContainer.getChildAt(0) as ViewGroup).getChildAt(3) as TextView // the title for observations
        val txvwNotesContent: TextView = (thisCardContainer.getChildAt(0) as ViewGroup).getChildAt(4) as TextView // the observation content
        if (shList.observations.isEmpty()) {
            txvwSubtitleNotes.visibility = View.GONE
            txvwNotesContent.visibility = View.GONE
        } else {
            txvwNotesContent.text = shList.observations
        }

        if (mode == 0 || mode == 3) { // get the issuer of the list
            mDatabase.child("users").child(shList.issuerID).get().addOnSuccessListener {
                val issuerUser: User? = it.getValue(User::class.java)
                if (issuerUser != null) {
                    if (mode == 0) { listOfIssuers.add(issuerUser); ((thisCardContainer.getChildAt(0) as ViewGroup).getChildAt(6) as TextView).text = "${issuerUser.city}, ${issuerUser.streetAndNumber}" }
                    if (mode == 3) { THE_OTHER_USER = issuerUser; adaptLayout(3, shList.reason, shList.bonusSum) }
                }
            }
        }

        if (mode == 2) { // get the provider of the list
            mDatabase.child("users").child(shList.providerID).get().addOnSuccessListener {
                val providerUser: User? = it.getValue(User::class.java)
                if (providerUser != null) {
                    THE_OTHER_USER = providerUser
                    adaptLayout(2, shList.reason, shList.bonusSum)
                }
            }
        }

        if (mode == 1) {
            adaptLayout(1, shList.reason, shList.bonusSum)
        }

        // fill in all the elements inside the list ->

        for (i in 0 until sz) {
            val v: View = layoutInflater.inflate(R.layout.shopping_list_element, elementsReadyContainer, false)
            elementsReadyContainer.addView(v, i)

            if (mode == 3) { // make checkbox tickable
                ((elementsReadyContainer.getChildAt(i) as ViewGroup).getChildAt(0) as CheckBox).isClickable = true
            }

            val txvwArtName: TextView = (elementsReadyContainer.getChildAt(i) as ViewGroup).getChildAt(1) as TextView? ?: return
            val txvwQuantAndUnit: TextView = (elementsReadyContainer.getChildAt(i) as ViewGroup).getChildAt(2) as TextView? ?: return

            txvwArtName.text = shList.elementArray[i].elementName
            txvwQuantAndUnit.text = "${shList.elementArray[i].quantity} ${shList.elementArray[i].unitOfMeasure}"
        }
    }

    private fun openList(i: Int) {
        val listAndIssuer: HashMap<String, Any> = HashMap()
        listAndIssuer["shoppingList"] = listOfShLists[i]
        listAndIssuer["issuerUser"] = listOfIssuers[i]
        startActivity(Intent(this, TakeListActivity::class.java).putExtra("listAndIssuerMAP", listAndIssuer))
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.btn_new_request -> {
                    if (!USER_AS_PROVIDER && !USER_AS_ISSUER) {
                        startActivity(Intent(this, CreateShoppingList::class.java))
                    } else if (USER_AS_PROVIDER) {
                        startActivity(Intent(this, FulfillShoppingActivity::class.java).putExtra("takenShoppingList", singleShList).putExtra("issuerUser", THE_OTHER_USER))
                    } else if (USER_AS_ISSUER) {
                        openEraseDialog()
                    }
                }
                R.id.btn_my_account -> startActivity(Intent(this, MyAccountActivity::class.java))
                R.id.btn_call -> { startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:${THE_OTHER_USER?.telephoneNumber}"))) }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun openEraseDialog() {
        if (singleShList != null) {
            //ViewDialog().showEraseDialog(this, getString(R.string.do_you_want_to_delete_the_request), takenShoppingList!!)
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(true)

            dialog.setContentView(R.layout.alert_box)
            (dialog.findViewById(R.id.txvw_alert_msg) as TextView).text = getString(R.string.do_you_want_to_delete_the_request)

            (dialog.findViewById<Button>(R.id.btn_cancel)).setOnClickListener { dialog.dismiss() }
            (dialog.findViewById<Button>(R.id.btn_proceed)).setOnClickListener {
                if (eraseList(singleShList!!)) {
                    singleShList = null
                    Toast.makeText(this, getString(R.string.the_request_was_deleted), Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    btnHome?.performClick()
                } else {
                    Toast.makeText(this, getString(R.string.request_could_not_be_deleted), Toast.LENGTH_LONG).show()
                }
            }

            dialog.show()
        }
    }

}
