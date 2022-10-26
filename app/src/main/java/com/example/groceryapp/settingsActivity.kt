package com.example.groceryapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class settingsActivity : AppCompatActivity() {

    lateinit var langPreference: LangPreference
    lateinit var context: Context

    val languageList = arrayOf("en","sv")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val searchBtn = findViewById(R.id.btn_search) as ImageButton
        val shoppingListBtn = findViewById(R.id.btn_shoppingList) as ImageButton
        val favoritesBtn = findViewById(R.id.btn_favorites) as ImageButton
        val homeBtn = findViewById(R.id.btn_home) as ImageButton
        val settingsBtn = findViewById(R.id.btn_settings) as ImageButton
        val langSpinner = findViewById(R.id.spinner) as Spinner
        val langBtn = findViewById(R.id.btn_language) as Button


        settingsBtn.setBackgroundColor(getResources().getColor(R.color.button_row_highlight))

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


        //BUTTONS
        val userBtn : TextView = findViewById(R.id.user_profile_TV)
        val notificationBtn  : TextView = findViewById(R.id.notification_tab_TV)
        val faqBtn : TextView = findViewById(R.id.faq_tab_TV)
        val signOutBtn: Button = findViewById(R.id.Signout_button_settings)

        val purchaseHistoryBtn : TextView = findViewById(R.id.tv_purchase_history)

        userBtn.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
        notificationBtn.setOnClickListener {
            val intent = Intent(this, notificationsSwitchActivity::class.java)
            startActivity(intent)
        }
        faqBtn.setOnClickListener {
            val intent = Intent(this, FAQActivity::class.java)
            startActivity(intent)
        }
        purchaseHistoryBtn.setOnClickListener {
            val intent = Intent(this, PurchaseHistoryActivity::class.java)
            startActivity(intent)
        }
        signOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        //Code for language change
        context = this
        langPreference = LangPreference(this)
        val languages = arrayOf("English","Svenska")
        val list: List<String> = languages.toList()
        langSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)

        val lang = langPreference.getLoginCount()
        val index = languageList.indexOf(lang)
        if(index >= 0){
            langSpinner.setSelection(index)
        }

        langBtn.setOnClickListener {
            langPreference.setLoginCount(languageList[langSpinner.selectedItemPosition])
            startActivity(Intent(this,settingsActivity::class.java))
            finish()
        }


    }

    override fun attachBaseContext(newBase: Context?) {
        langPreference = LangPreference(newBase!!)
        val lang = langPreference.getLoginCount()
        super.attachBaseContext(lang?.let { GroceryContextWrapper.wrap(newBase, it) })
    }
}