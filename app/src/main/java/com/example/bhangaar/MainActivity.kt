package com.example.bhangaar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.bhangaar.fragmentClass.homeFragment
import com.example.bhangaar.fragmentClass.orderFragment
import com.example.bhangaar.fragmentClass.profileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottom : LinearLayout
    private lateinit var homebtn : ImageView
    private lateinit var orderbtn : ImageView
    private lateinit var profilebtn : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, homeFragment())
        transaction.commit()

        homebtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, homeFragment())
            transaction.commit()
        }

        orderbtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, orderFragment())
            transaction.commit()
        }

        profilebtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, profileFragment())
            transaction.commit()
        }





    }

    private fun init()
    {
        bottom = findViewById(R.id.bottom_navigation)
        homebtn = findViewById(R.id.homebtn)
        orderbtn = findViewById(R.id.ordersbtn)
        profilebtn = findViewById(R.id.profilebtn)
    }


}


