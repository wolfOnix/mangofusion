package mangofusion.apps.shopassist

import java.io.File

class Invoice(private var photo: File, private var sum: Double) {

    fun getSum(): Double {
        return sum
    }

}