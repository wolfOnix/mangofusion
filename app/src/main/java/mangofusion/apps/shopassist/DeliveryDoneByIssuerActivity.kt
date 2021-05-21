package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView

class DeliveryDoneByIssuerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_done_by_issuer)

        findViewById<TextView>(R.id.txvw_congrats_for).text = getString(R.string.you_have_successfully_received_the_shopping_from, "${THE_OTHER_USER!!.firstName} ${THE_OTHER_USER!!.lastName}")

        val timer = object: CountDownTimer(5000, 5000) { // check every 5 s for delivery confirmation but not more than 60 s
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                goHome()
            }
        }
        timer.start()
    }

    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

}