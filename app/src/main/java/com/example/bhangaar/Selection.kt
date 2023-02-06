package com.example.bhangaar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView

class Selection : AppCompatActivity() {

    private lateinit var usercard : CardView
    private lateinit var vendorcard : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        init()
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
    }
}