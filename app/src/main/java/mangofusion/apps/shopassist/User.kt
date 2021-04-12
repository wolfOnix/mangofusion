package mangofusion.apps.shopassist

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class User {

    var firstName: String private set
    var lastName: String private set
    var email: String private set
    var birthdayDate: String private set
    var telephoneNumber: String private set
    var city: String private set
    var country: String private set
    var streetAndNumber: String private set
    var dateStampOnCreate: String private set

    constructor(
        firstName: String,
        lastName: String,
        email: String,
        birthdayDate: String,
        telephoneNumber: String,
        city: String,
        streetAndNumber: String,
        country: String
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.birthdayDate = birthdayDate
        this.telephoneNumber = telephoneNumber
        this.city = city
        this.streetAndNumber = streetAndNumber
        this.country = country
        val date = Calendar.getInstance().time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy.mm.dd/hh:mm:ss")
        this.dateStampOnCreate = dateFormat.format(date) // TODO to GMT0
    }

}