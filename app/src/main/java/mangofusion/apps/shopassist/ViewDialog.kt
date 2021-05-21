package mangofusion.apps.shopassist

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView

class ViewDialog {

    fun showEraseDialog(context: Context, msg: String, shList: ShoppingList) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(true)

        dialog.setContentView(R.layout.alert_box)
        (dialog.findViewById(R.id.txvw_alert_msg) as TextView).text = msg

        (dialog.findViewById<Button>(R.id.btn_cancel)).setOnClickListener { dialog.dismiss() }
        (dialog.findViewById<Button>(R.id.btn_proceed)).setOnClickListener { shList.eraseList(); dialog.dismiss() }

        dialog.show()
    }

}