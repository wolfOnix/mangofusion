package mangofusion.apps.shopassist

class User {
    var firstName: String? = null
        private set
    var lastName: String? = null
        private set
    var email: String? = null
        private set
    var password: String? = null
        private set
    var birthdayDate: String? = null
        private set
    var telephoneNumber: String? = null
        private set

    constructor() {}
    constructor(
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?,
        birthdayDate: String?,
        telephoneNumber: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.password = password
        this.birthdayDate = birthdayDate
        this.telephoneNumber = telephoneNumber
    }
}