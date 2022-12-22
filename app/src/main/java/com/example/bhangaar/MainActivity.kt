package com.example.bhangaar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.bhangaar.fragmentClass.homeFragment
import com.example.bhangaar.fragmentClass.orderFragment
import com.example.bhangaar.fragmentClass.profileFragment
import com.example.bhangaar.fragmentClassVendor.homeFragmentVendor
import com.example.bhangaar.fragmentClassVendor.orderFragmentVendor
import com.example.bhangaar.fragmentClassVendor.profileFragmentVendor
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottom : LinearLayout
    private lateinit var homebtn : ImageView
    private lateinit var orderbtn : ImageView
    private lateinit var profilebtn : ImageView

    var authUserId = "s"
    private lateinit var role : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        authUserId = intent.extras?.get("userid").toString()
        role = intent.extras?.get("role").toString()


        //Toast.makeText(applicationContext, authUserId, Toast.LENGTH_SHORT).show()

        if(role == "user")
        {
            val transaction = supportFragmentManager.beginTransaction()
            val homeFrag = homeFragment()
            val bundle = Bundle()
            bundle.putString("userid",authUserId)
            bundle.putString("role",role)
            homeFrag.arguments = bundle
            transaction.replace(R.id.frameLayout, homeFrag)
            transaction.commit()
        }
        else{
            val transaction = supportFragmentManager.beginTransaction()
            val homeFrag = homeFragmentVendor()
            val bundle = Bundle()
            bundle.putString("userid",authUserId)
            bundle.putString("role",role)
            homeFrag.arguments = bundle
            transaction.replace(R.id.frameLayout, homeFrag)
            transaction.commit()
        }


        homebtn.setOnClickListener {

            if(role == "user")
            {
                val transaction = supportFragmentManager.beginTransaction()
                val homeFrag = homeFragment()
                val bundle = Bundle()
                bundle.putString("userid",authUserId)
                bundle.putString("role",role)
                homeFrag.arguments = bundle
                transaction.replace(R.id.frameLayout, homeFrag)
                transaction.commit()
            }
            else{
                val transaction = supportFragmentManager.beginTransaction()
                val homeFrag = homeFragmentVendor()
                val bundle = Bundle()
                bundle.putString("userid",authUserId)
                bundle.putString("role",role)
                homeFrag.arguments = bundle
                transaction.replace(R.id.frameLayout, homeFrag)
                transaction.commit()
            }

        }

        orderbtn.setOnClickListener {

            if(role == "user")
            {
                val transaction = supportFragmentManager.beginTransaction()
                val orderFrag = orderFragment()
                val bundle = Bundle()
                bundle.putString("userid",authUserId)
                bundle.putString("role",role)
                orderFrag.arguments = bundle
                transaction.replace(R.id.frameLayout, orderFrag)
                transaction.commit()
            }
            else{
                val transaction = supportFragmentManager.beginTransaction()
                val orderFrag = orderFragmentVendor()
                val bundle = Bundle()
                bundle.putString("userid",authUserId)
                bundle.putString("role",role)
                orderFrag.arguments = bundle
                transaction.replace(R.id.frameLayout, orderFrag)
                transaction.commit()
            }

        }

        profilebtn.setOnClickListener {

            if(role == "user")
            {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frameLayout, profileFragment())
                transaction.commit()
            }
            else
            {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frameLayout, profileFragmentVendor())
                transaction.commit()
            }

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


