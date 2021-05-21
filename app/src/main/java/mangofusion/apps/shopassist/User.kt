package mangofusion.apps.shopassist

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
data class User (
    var userID: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var birthdayDate: String = "",
    var telephoneNumber: String = "",
    var city: String = "",
    var country: String = "",
    var streetAndNumber: String = "",
    var dateStampOnCreate: String = SimpleDateFormat("yyyy.mm.dd/HH:mm:ss").format(Calendar.getInstance().time)
): Serializable {

    /*@Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "telephoneNumber" to telephoneNumber,
            "city" to city,
            "streetAndNumber" to streetAndNumber
        )
    }*/

    private val countriesArr = arrayOf<Int>(
        R.string.Romania,
        R.string.Germany,
        R.string.The_United_Kingdom
    )

    fun getCountryPos(pos: Int): Int {
        return countriesArr[pos]
    }

}