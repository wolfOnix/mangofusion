package mangofusion.apps.shopassist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity: Activity(), View.OnClickListener {

    private var btn_home: ImageButton? = null
    private var btn_my_account: ImageButton? = null
    private var btn_new_request: ImageButton? = null
    private var txvwRequestsNr: TextView? = null
    private var txvwRequestsText: TextView? = null
    private var txvwFirstname: TextView? = null
    private var nrIssuerLists: Int = 0
    private var nrNoRoleLists: Int = 0
    private var shoppingListsReadyContainer: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        shoppingListsReadyContainer = findViewById(R.id.lnly_all_lists_container)

        txvwFirstname = findViewById(R.id.txvw_firstname)
        mDatabase.child("users").child(getUserID()).get().addOnSuccessListener {
            txvwFirstname!!.text = "${it.child("firstName").value}"
        }.addOnFailureListener { }

        txvwRequestsNr = findViewById(R.id.txvw_requests_nr)
        txvwRequestsText = findViewById(R.id.txvw_requests_text)

        btn_home = findViewById(R.id.btn_home)
        btn_my_account = findViewById(R.id.btn_my_account)
        btn_new_request = findViewById(R.id.btn_new_request)

        btn_home!!.setOnClickListener(this)
        btn_my_account!!.setOnClickListener(this)
        btn_new_request!!.setOnClickListener(this)

        loadShoppingRequests()
    }

    @SuppressLint("SetTextI18n")
    private fun loadShoppingRequests() {

        val ref = FirebaseDatabase.getInstance().getReference("lists")
        val listOfShLists: MutableList<ShoppingList> = ArrayList()

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
                } else if(listOfShLists.size > 0) {
                    println("CP1")
                    loopRequests(listOfShLists.toTypedArray())
                } else if (listOfShLists.size == 0) {
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
        if (USER_AS_ISSUER) {
            println("CP3")
            for (i in shListArr.indices) {
                if (shListArr[i].issuerID == getUserID()) {
                    if (shListArr[i].taken)
                        displayRequest(shListArr[i], 2, i)
                    else
                        displayRequest(shListArr[i], 1, i)
                }
            }
        } else if (!USER_AS_PROVIDER) {
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
        when (mode) {
            0 -> {
                // inflate with shoppingListContainer
                val v: View = layoutInflater.inflate(R.layout.shopping_list_ready_container_home, shoppingListsReadyContainer, false)
                shoppingListsReadyContainer?.addView(v, order)

                val oneListReadyContainer: ViewGroup = v as ViewGroup

                // fill the shoppingListContainer
                val sz: Int = shList.elementArray.size

                val txvwArtNr: TextView = oneListReadyContainer.getChildAt(1) as TextView
                txvwArtNr.text = resources.getQuantityString(R.plurals.number_of_articles, sz, sz)

                val elementsReadyContainer: ViewGroup = oneListReadyContainer.getChildAt(2) as ViewGroup
                val txvwSubtitleNotes: TextView = oneListReadyContainer.getChildAt(3) as TextView
                val txvwNotesContent: TextView = oneListReadyContainer.getChildAt(4) as TextView
                val txvwAddress: TextView = oneListReadyContainer.getChildAt(6) as TextView

                if (shList.observations.isEmpty()) {
                    txvwSubtitleNotes.visibility = View.GONE
                    txvwNotesContent.visibility = View.GONE
                } else {
                    txvwNotesContent.text = shList.observations
                }

                mDatabase.child("users").child(shList.issuerID).get().addOnSuccessListener {
                    txvwAddress.text = "${it.child("city").value}, ${it.child("streetAndNumber").value}"
                }.addOnFailureListener { }

                for (i in 0 until sz) {
                    val v: View = layoutInflater.inflate(R.layout.shopping_list_element, elementsReadyContainer, false)
                    elementsReadyContainer?.addView(v, i)

                    val txvwArtName: TextView = (elementsReadyContainer?.getChildAt(i) as ViewGroup).getChildAt(1) as TextView? ?: return false
                    val txvwQuantAndUnit: TextView = (elementsReadyContainer?.getChildAt(i) as ViewGroup).getChildAt(2) as TextView? ?: return false

                    txvwArtName.text = shList.elementArray[i].elementName
                    txvwQuantAndUnit.text = "${shList.elementArray[i].quantity} ${shList.elementArray[i].unitOfMeasure}"
                }
            }
        }
        return true
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
