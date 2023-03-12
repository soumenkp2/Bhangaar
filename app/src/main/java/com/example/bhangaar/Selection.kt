package com.example.bhangaar

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth


class Selection : AppCompatActivity() {

    private lateinit var usercard : CardView
    private lateinit var vendorcard : CardView
    private lateinit var auth : FirebaseAuth
    private lateinit var role : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        init()
        Log.v("selection", "ok")
        usercard.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this@Selection, OnBoardingScreen:: class.java);
                intent.putExtra("role","Users")
                startActivity(intent);
            }
        )

        vendorcard.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this@Selection, OnBoardingScreen:: class.java);
                intent.putExtra("role","Vendors")
                startActivity(intent);
            }
        )
    }

    private fun init()
    {
        usercard = findViewById(R.id.usercard)
        vendorcard = findViewById(R.id.vendorcard)
        auth = FirebaseAuth.getInstance()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        role = preferences.getString("role", "").toString()

    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null && !role.isEmpty())
        {
            Log.v("ss", "ssfekfe");
            val intent : Intent
            intent = Intent(this, personDetails::class.java)
            intent.putExtra("name","")
            intent.putExtra("role",role)
            intent.putExtra("userid", FirebaseAuth.getInstance().currentUser?.uid.toString())
            intent.putExtra("phone", FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
            intent.putExtra("screen","signin")

            startActivity(intent)
        }
    }
}