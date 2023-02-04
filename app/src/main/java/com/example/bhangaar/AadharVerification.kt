package com.example.bhangaar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class AadharVerification : AppCompatActivity() {
    private lateinit var textskip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aadhar_verification)
        init()

        textskip.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        )
    }

    private fun init()
    {
        textskip = findViewById(R.id.textview_skip)
    }

}