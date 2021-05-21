package mangofusion.apps.shopassist

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
data class Handshake(
    var handshakeID: String = "",
    var emitor: User? = null,
    var receptor: User? = null,
    var eMsg: String = "",
    var rMsg: String = "",
    var eTime: String = SimpleDateFormat("yyyy.MM.dd/HH:mm:ss").format(Date()),
    var rTime: String = "",
    var closed: Boolean = false
): Serializable {

    fun respond() {

    }

}