package com.example.bhangaar

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bhangaar.fragmentClass.homeFragment
import com.example.bhangaar.fragmentClass.orderFragment
import com.example.bhangaar.fragmentClass.profileFragment
import com.example.bhangaar.fragmentClassVendor.homeFragmentVendor
import com.example.bhangaar.fragmentClassVendor.orderFragmentVendor
import com.example.bhangaar.fragmentClassVendor.profileFragmentVendor
import com.google.android.gms.location.FusedLocationProviderClient


class MainActivity : AppCompatActivity() {

    private lateinit var bottom : LinearLayout
    private lateinit var homebtn : ImageView
    private lateinit var orderbtn : ImageView
    private lateinit var profilebtn : ImageView

    var authUserId = "s"
    private lateinit var role : String

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private var lat : String = "s"
    private var long : String= "s"
    private var address : String= "s"
    private var state : String = "s"
    private var postal : String = "s"
    private var name : String = "s"
    private var phone : String = "s"



    var location_found = false;

    private lateinit var dialog_box : ProgressBar;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        //dialog_box.visibility = View.VISIBLE
        //dialog_box = ProgressDialog(this);
        authUserId = intent.extras?.get("userid").toString()
        role = intent.extras?.get("role").toString()
        lat = intent.extras?.get("lat").toString()
        long = intent.extras?.get("long").toString()
        state = intent.extras?.get("state").toString()
        postal = intent.extras?.get("postal").toString()
        address = intent.extras?.get("address").toString()
        name = intent.extras?.get("name").toString()
        phone = intent.extras?.get("phone").toString()


        //Toast.makeText(applicationContext, authUserId + state + postal , Toast.LENGTH_SHORT).show()

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        getLocation()


        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putString("role", role)
        editor.apply()

        if(role == "Users")
        {
            val transaction = supportFragmentManager.beginTransaction()
            val homeFrag = homeFragment()
            val bundle = Bundle()
            bundle.putString("userid",authUserId)
            bundle.putString("role",role)
            bundle.putString("name",name)
            bundle.putString("state",state)
            bundle.putString("lat",lat)
            bundle.putString("long",long)
            bundle.putString("address",address)
            bundle.putString("postal",postal)
            bundle.putString("phone",phone)
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
            bundle.putString("name",name)
            bundle.putString("state",state)
            bundle.putString("lat",lat)
            bundle.putString("long",long)
            bundle.putString("address",address)
            bundle.putString("postal",postal)
            bundle.putString("phone",phone)
            homeFrag.arguments = bundle
            transaction.replace(R.id.frameLayout, homeFrag)
            transaction.commit()
        }


        homebtn.setOnClickListener {
            //Toast.makeText(applicationContext, lat+long+country+locality+address, Toast.LENGTH_SHORT).show()
            if(role == "Users")
            {
                val transaction = supportFragmentManager.beginTransaction()
                val homeFrag = homeFragment()
                val bundle = Bundle()
                bundle.putString("userid",authUserId)
                bundle.putString("role",role)
                bundle.putString("name",name)
                bundle.putString("state",state)
                bundle.putString("lat",lat)
                bundle.putString("long",long)
                bundle.putString("address",address)
                bundle.putString("postal",postal)
                bundle.putString("phone",phone)
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
                bundle.putString("name",name)
                bundle.putString("state",state)
                bundle.putString("lat",lat)
                bundle.putString("long",long)
                bundle.putString("address",address)
                bundle.putString("postal",postal)
                bundle.putString("phone",phone)
                homeFrag.arguments = bundle
                transaction.replace(R.id.frameLayout, homeFrag)
                transaction.commit()
            }

        }

        orderbtn.setOnClickListener {

            if(role == "Users")
            {
                val transaction = supportFragmentManager.beginTransaction()
                val orderFrag = orderFragment()
                val bundle = Bundle()
                bundle.putString("userid",authUserId)
                bundle.putString("role",role)
                bundle.putString("name",name)
                bundle.putString("state",state)
                bundle.putString("lat",lat)
                bundle.putString("long",long)
                bundle.putString("address",address)
                bundle.putString("postal",postal)
                bundle.putString("phone",phone)
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
                bundle.putString("name",name)
                bundle.putString("state",state)
                bundle.putString("lat",lat)
                bundle.putString("long",long)
                bundle.putString("address",address)
                bundle.putString("postal",postal)
                bundle.putString("phone",phone)
                orderFrag.arguments = bundle
                transaction.replace(R.id.frameLayout, orderFrag)
                transaction.commit()
            }

        }

        profilebtn.setOnClickListener {

            if(role == "Users")
            {
                val transaction = supportFragmentManager.beginTransaction()
                val orderFrag = profileFragment()
                val bundle = Bundle()
                bundle.putString("userid",authUserId)
                bundle.putString("role",role)
                bundle.putString("name",name)
                bundle.putString("state",state)
                bundle.putString("lat",lat)
                bundle.putString("long",long)
                bundle.putString("address",address)
                bundle.putString("postal",postal)
                bundle.putString("phone",phone)
                orderFrag.arguments = bundle
                transaction.replace(R.id.frameLayout, orderFrag)
                transaction.commit()
            }
            else
            {
                val transaction = supportFragmentManager.beginTransaction()
                val orderFrag = profileFragmentVendor()
                val bundle = Bundle()
                bundle.putString("userid",authUserId)
                bundle.putString("role",role)
                bundle.putString("name",name)
                bundle.putString("state",state)
                bundle.putString("lat",lat)
                bundle.putString("long",long)
                bundle.putString("address",address)
                bundle.putString("postal",postal)
                bundle.putString("phone",phone)
                orderFrag.arguments = bundle
                transaction.replace(R.id.frameLayout, orderFrag)
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
       // dialog_box = findViewById(R.id.pBar)
    }

    override fun onBackPressed() {
        finish()
        finishAffinity()
    }

}



