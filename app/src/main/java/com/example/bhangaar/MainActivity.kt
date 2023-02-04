package com.example.bhangaar

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity.apply
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bhangaar.fragmentClass.homeFragment
import com.example.bhangaar.fragmentClass.orderFragment
import com.example.bhangaar.fragmentClass.profileFragment
import com.example.bhangaar.fragmentClassVendor.homeFragmentVendor
import com.example.bhangaar.fragmentClassVendor.orderFragmentVendor
import com.example.bhangaar.fragmentClassVendor.profileFragmentVendor
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.coroutines.cancellation.CancellationException

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


        Toast.makeText(applicationContext, authUserId + state + postal , Toast.LENGTH_SHORT).show()

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        getLocation()


        if(role == "user")
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
            homeFrag.arguments = bundle
            transaction.replace(R.id.frameLayout, homeFrag)
            transaction.commit()
        }


        homebtn.setOnClickListener {
            //Toast.makeText(applicationContext, lat+long+country+locality+address, Toast.LENGTH_SHORT).show()
            if(role == "user")
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
                bundle.putString("name",name)
                bundle.putString("state",state)
                bundle.putString("lat",lat)
                bundle.putString("long",long)
                bundle.putString("address",address)
                bundle.putString("postal",postal)
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
       // dialog_box = findViewById(R.id.pBar)
    }

}



