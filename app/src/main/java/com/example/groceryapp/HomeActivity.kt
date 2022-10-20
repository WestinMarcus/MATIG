package com.example.groceryapp


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView


class HomeActivity : AppCompatActivity() {

    lateinit var langPreference: LangPreference
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val testBtn = findViewById(R.id.distance_pin_IV) as ImageView

        val searchBtn = findViewById(R.id.btn_search) as ImageButton
        val shoppingListBtn = findViewById(R.id.btn_shoppingList) as ImageButton
        val favoritesBtn = findViewById(R.id.btn_favorites) as ImageButton
        val homeBtn = findViewById(R.id.btn_home) as ImageButton
        val settingsBtn = findViewById(R.id.btn_settings) as ImageButton
        val icaBtn = findViewById<View>(R.id.view1)
        val willysBtn = findViewById<View>(R.id.view2)
        val coopBtn = findViewById<View>(R.id.view4)
        val lidlBtn = findViewById<View>(R.id.view3)

        homeBtn.setBackgroundColor(getResources().getColor(R.color.button_row_highlight))

        homeBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        favoritesBtn.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            finish()

        }

        searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        shoppingListBtn.setOnClickListener {
            val intent = Intent(this, ShoppingListActivity::class.java)
            startActivity(intent)
            finish()
        }

        settingsBtn.setOnClickListener {
            val intent = Intent(this, settingsActivity::class.java)
            startActivity(intent)
            finish()
        }


        testBtn.setOnClickListener {
           val intent = Intent(this, StoresActivity::class.java)
            startActivity(intent)
        }

        icaBtn.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ica.se/butiker/maxi/karlstad/maxi-ica-stormarknad-karlstad-11010/start/?gclid=Cj0KCQjw48OaBhDWARIsAMd966Bki4B_IRxQuH0ySPLFwG0sjF8Rx1cyupXmaYnk0G6WDUiwZrLjhaEaAgTMEALw_wcB"))
            startActivity(i)
        }
        willysBtn.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.willys.se/butik/2117"))
            startActivity(i)
        }
        coopBtn.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coop.se/butiker-erbjudanden/stora-coop/stora-coop-valsviken/"))
            startActivity(i)
        }
        lidlBtn.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lidl.se/?gclid=Cj0KCQjw48OaBhDWARIsAMd966Dd-kY4PMh2ssbUdLv5mwZI4cR08Q1PlG41WPxokRlml-rWtdfenqEaAihfEALw_wcB"))
            startActivity(i)
        }
    }
    override fun attachBaseContext(newBase: Context?) {
        langPreference = LangPreference(newBase!!)
        val lang = langPreference.getLoginCount()
        super.attachBaseContext(lang?.let { GroceryContextWrapper.wrap(newBase, it) })
    }

}