package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity: Activity(), View.OnClickListener {

    private var btnHome: ImageButton? = null
    private var btnMyAccount: ImageButton? = null
    private var btnNewRequest: ImageButton? = null
    private var txvwRequestsNr: TextView? = null
    private var txvwRequestsText: TextView? = null
    private var txvwFirstname: TextView? = null
    private var nrIssuerLists: Int = 0
    private var nrNoRoleLists: Int = 0
    private var shoppingListsReadyContainer: ViewGroup? = null
    private val listOfShLists: MutableList<ShoppingList> = ArrayList()
    private val listOfIssuers: MutableList<User> = ArrayList()
    private var takenShoppingList: ShoppingList? = null
    private var takenIssuerUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (intent.hasExtra("listAndIssuerMAP")) {
            val listAndIssuer: HashMap<String, Any> = intent.getSerializableExtra("listAndIssuerMAP") as HashMap<String, Any>
            takenShoppingList = listAndIssuer["shoppingList"] as ShoppingList
            takenIssuerUser = listAndIssuer["issuerUser"] as User
        }

        if (CURR_USER == null) { // if user is not saved
            FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener {
                CURR_USER = it.getValue(User::class.java)
                greet()
            }
        } else greet()

        shoppingListsReadyContainer = findViewById(R.id.lnly_all_lists_container)

        txvwRequestsNr = findViewById(R.id.txvw_requests_nr)
        txvwRequestsText = findViewById(R.id.txvw_requests_text)

        btnHome = findViewById(R.id.btn_home)
        btnMyAccount = findViewById(R.id.btn_my_account)
        btnNewRequest = findViewById(R.id.btn_new_request)

        btnHome!!.setOnClickListener(this)
        btnMyAccount!!.setOnClickListener(this)
        btnNewRequest!!.setOnClickListener(this)

        if (takenIssuerUser == null || takenShoppingList == null)
            loadShoppingRequests()
        else
            displayRequest(takenShoppingList!!, 3, 0)
    }

    private fun greet() {
        findViewById<TextView>(R.id.txvw_firstname).text = CURR_USER!!.firstName
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
                            displayRequest(shList, 3, 0)
                            listOfShLists.clear()
                            break
                        }
                        if (shList.issuerID == getUserID()) {
                            USER_AS_ISSUER = true // the shopping list belongs to the current user
                            nrIssuerLists++
                        }
                        listOfShLists.add(shList)
                    }
                }
                if (USER_AS_PROVIDER) {
                    println("CP0")
                    USER_AS_ISSUER = false // the provider role has a greater priority, in case of conflicts caused by eventual future app design
                } else if(listOfShLists.size > 0) { // there are lists to display
                    println("CP1")
                    loopRequests(listOfShLists.toTypedArray())
                } else if (listOfShLists.size == 0) { // there is no list to display
                    println("CP2")
                    txvwRequestsNr?.text = "0"
                }
                //val lastItem: ShoppingList = list[list.size - 1]
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        ref.addListenerForSingleValueEvent(valueEventListener)

    }

    private fun loopRequests(shListArr: Array<ShoppingList>) {
        if (USER_AS_ISSUER) { // user issued at least one list
            println("CP3")
            for (i in shListArr.indices) {
                if (shListArr[i].issuerID == getUserID()) {
                    if (shListArr[i].taken)
                        displayRequest(shListArr[i], 2, i)
                    else
                        displayRequest(shListArr[i], 1, i)
                }
            }
        } else if (!USER_AS_PROVIDER) { // user has no role
            println("CP4")
            nrNoRoleLists = shListArr.size
            txvwRequestsText?.text = resources.getString(R.string.PHRASE_available_requests_nearby)
            txvwRequestsNr?.text = nrNoRoleLists.toString()

            for (i in 0 until nrNoRoleLists) {
                displayRequest(shListArr[i], 0, i)
            }
        }
    }

    private fun displayRequest(shList: ShoppingList, mode: Int, order: Int): Boolean {
        /* mode:
        0 - user - no role
        1 - user - issuer, shList - not taken
        2 - user - issuer, shList - taken
        3 - user - provider */

            // inflate with shoppingListContainer
            val v: View = layoutInflater.inflate(R.layout.shopping_list_ready_container_home, shoppingListsReadyContainer, false)
            shoppingListsReadyContainer?.addView(v, order)

            val oneListReadyContainer: ViewGroup = v as ViewGroup // this is the view which was added

            // fill the shoppingListContainer
            val sz: Int = shList.elementArray.size

            val txvwArtNr: TextView = oneListReadyContainer.getChildAt(1) as TextView
            txvwArtNr.text = resources.getQuantityString(R.plurals.number_of_articles, sz, sz)

            val elementsReadyContainer: ViewGroup = oneListReadyContainer.getChildAt(2) as ViewGroup
            val txvwSubtitleNotes: TextView = oneListReadyContainer.getChildAt(3) as TextView
            val txvwNotesContent: TextView = oneListReadyContainer.getChildAt(4) as TextView
            val txvwAddress: TextView = oneListReadyContainer.getChildAt(6) as TextView

            val btnOpenList: Button = oneListReadyContainer.getChildAt(7) as Button
            btnOpenList.setOnClickListener { openList(order) }

            if (mode == 3) // remove btnOpenList
                oneListReadyContainer.removeViewAt(7)

            if (shList.observations.isEmpty()) {
                txvwSubtitleNotes.visibility = View.GONE
                txvwNotesContent.visibility = View.GONE
            } else {
                txvwNotesContent.text = shList.observations
            }

            if (takenIssuerUser == null) { // if the issuer is not allocated -> the user is certainly not a provider
                mDatabase.child("users").child(shList.issuerID).get().addOnSuccessListener {
                    val issuerUser: User? = it.getValue(User::class.java)
                    if (issuerUser != null) txvwAddress.text = "${issuerUser.city}, ${issuerUser.streetAndNumber}"
                    if (issuerUser != null) {
                        listOfIssuers.add(issuerUser)
                    }
                }.addOnFailureListener { }
            } else if (mode == 3) {
                txvwAddress.text = "${takenIssuerUser!!.city}, ${takenIssuerUser!!.streetAndNumber}"
            }

            for (i in 0 until sz) { // fill in all the elements in the list
                val v: View = layoutInflater.inflate(R.layout.shopping_list_element, elementsReadyContainer, false)
                elementsReadyContainer.addView(v, i)

                if (mode == 3) { // make checkbox tickable
                    ((elementsReadyContainer.getChildAt(i) as ViewGroup).getChildAt(0) as CheckBox).isClickable = true
                }

                val txvwArtName: TextView = (elementsReadyContainer.getChildAt(i) as ViewGroup).getChildAt(1) as TextView? ?: return false
                val txvwQuantAndUnit: TextView = (elementsReadyContainer.getChildAt(i) as ViewGroup).getChildAt(2) as TextView? ?: return false

                txvwArtName.text = shList.elementArray[i].elementName
                txvwQuantAndUnit.text = "${shList.elementArray[i].quantity} ${shList.elementArray[i].unitOfMeasure}"
            }

        when (mode) {
            3 -> {
                findViewById<LinearLayout>(R.id.lnly_requests_nr).visibility = View.GONE
                findViewById<LinearLayout>(R.id.lnly_alternative_phrase).visibility = View.VISIBLE

            }
        }

        /*when (mode) {
            0 -> {
                // inflate with shoppingListContainer
                val v: View = layoutInflater.inflate(R.layout.shopping_list_ready_container_home, shoppingListsReadyContainer, false)
                shoppingListsReadyContainer?.addView(v, order)

                val oneListReadyContainer: ViewGroup = v as ViewGroup // this is the view which was added

                // fill the shoppingListContainer
                val sz: Int = shList.elementArray.size

                val txvwArtNr: TextView = oneListReadyContainer.getChildAt(1) as TextView
                txvwArtNr.text = resources.getQuantityString(R.plurals.number_of_articles, sz, sz)

                val elementsReadyContainer: ViewGroup = oneListReadyContainer.getChildAt(2) as ViewGroup
                val txvwSubtitleNotes: TextView = oneListReadyContainer.getChildAt(3) as TextView
                val txvwNotesContent: TextView = oneListReadyContainer.getChildAt(4) as TextView
                val txvwAddress: TextView = oneListReadyContainer.getChildAt(6) as TextView
                val btnOpenList: Button = oneListReadyContainer.getChildAt(7) as Button
                btnOpenList.setOnClickListener { openList(order) }

                if (shList.observations.isEmpty()) {
                    txvwSubtitleNotes.visibility = View.GONE
                    txvwNotesContent.visibility = View.GONE
                } else {
                    txvwNotesContent.text = shList.observations
                }

                mDatabase.child("users").child(shList.issuerID).get().addOnSuccessListener {
                    val issuerUser: User? = it.getValue(User::class.java)
                    if (issuerUser != null) txvwAddress.text = "${issuerUser.city}, ${issuerUser.streetAndNumber}"
                    if (issuerUser != null) {
                        listOfIssuers.add(issuerUser)
                    }
                }.addOnFailureListener { }

                for (i in 0 until sz) {
                    val v: View = layoutInflater.inflate(R.layout.shopping_list_element, elementsReadyContainer, false)
                    elementsReadyContainer.addView(v, i)

                    val txvwArtName: TextView = (elementsReadyContainer.getChildAt(i) as ViewGroup).getChildAt(1) as TextView? ?: return false
                    val txvwQuantAndUnit: TextView = (elementsReadyContainer.getChildAt(i) as ViewGroup).getChildAt(2) as TextView? ?: return false

                    txvwArtName.text = shList.elementArray[i].elementName
                    txvwQuantAndUnit.text = "${shList.elementArray[i].quantity} ${shList.elementArray[i].unitOfMeasure}"
                }
            }
            3 -> { // the user is the provider
                findViewById<LinearLayout>(R.id.lnly_requests_nr).visibility = View.GONE
                findViewById<LinearLayout>(R.id.lnly_alternative_phrase).visibility = View.VISIBLE
            }
        }*/
        return true
    }

    private fun openList(i: Int) {
        val listAndIssuer : HashMap<String, Any> = HashMap()
        listAndIssuer["shoppingList"] = listOfShLists[i]
        listAndIssuer["issuerUser"] = listOfIssuers[i]
        startActivity(Intent(this, TakeListActivity::class.java).putExtra("listAndIssuerMAP", listAndIssuer))
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
