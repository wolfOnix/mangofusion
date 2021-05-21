package mangofusion.apps.shopassist

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView

class DeliveryDoneActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_done)

        findViewById<TextView>(R.id.txvw_congrats).text = getString(R.string.congratulations, CURR_USER!!.firstName)
        findViewById<TextView>(R.id.txvw_congrats_for).text = getString(R.string.you_have_successfully_delivered_the_shopping_to, "${THE_OTHER_USER!!.firstName} ${THE_OTHER_USER!!.lastName}")

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