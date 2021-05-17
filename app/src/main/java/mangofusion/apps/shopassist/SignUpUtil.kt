package mangofusion.apps.shopassist

import android.util.Patterns


object SignUpUtil {

    /***
     * the input is not valid if...
     * ...the fields are empty
     * ...the fist name is empty
     * ...the last name is empty
     * ...the email is empty
     * ...the email is not valid
     * ...the password is empty
     * ...the password is less than 6 characters
     * ...the confirmed password is not the same as the real password
     * ...the birthday is empty
     * ...the telephone number is empty
     * ...the telephone number is different than 10 characters
     * ...the city is empty
     * ...the street and number is empty
     */

    fun validateRegistrationInput(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmedPassword: String,
        birthdayDate: String,
        telephoneNumber: String,
        city: String,
        streetAndNumber: String
    ): Boolean {
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
            confirmedPassword.isEmpty() || birthdayDate.isEmpty() || telephoneNumber.isEmpty() ||
            city.isEmpty() || streetAndNumber.isEmpty()) {
            return false
        }

//        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            return false
//        }

        if(password.count{ it.isLetterOrDigit() } < 6) {
            return false
        }

        if(password != confirmedPassword) {
            return false
        }

        return true
    }
}