package mangofusion.apps.shopassist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CreateShoppingList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shopping_list)
    }

    fun goBack(view: View) {
        finish()
    }
}