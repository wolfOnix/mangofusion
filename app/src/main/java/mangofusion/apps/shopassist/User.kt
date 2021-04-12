package mangofusion.apps.shopassist

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class User {
    var firstName: String? = null
        private set
    var lastName: String? = null
        private set
    var email: String? = null
        private set
    var birthdayDate: String? = null
        private set
    var telephoneNumber: String? = null
        private set
    var address: String? = null
        private set
    var dateStampOnCreate: String? = null
        private set

    constructor(
        firstName: String?,
        lastName: String?,
        email: String?,
        birthdayDate: String?,
        telephoneNumber: String?,
        address: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.birthdayDate = birthdayDate
        this.telephoneNumber = telephoneNumber
        this.address = address
        val date = Calendar.getInstance().time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy.mm.dd/hh:mm:ss")
        this.dateStampOnCreate = dateFormat.format(date) // TODO to GMT0
    }
}