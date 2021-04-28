package mangofusion.apps.shopassist

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class PublishShoppingList : Activity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_shopping_list)

        /*val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(R.color.purple.toString()),
                Color.parseColor(R.color.magenta.toString()))
        );
        gradientDrawable.cornerRadius = 0f;

        findViewById<View>(R.id.btn_publish_list).background = gradientDrawable;*/
    }

    fun goBack(view: View) {
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

            }
        }
    }
}