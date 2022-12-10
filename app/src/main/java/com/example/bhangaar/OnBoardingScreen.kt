package com.example.bhangaar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class OnBoardingScreen : AppCompatActivity() {

    private lateinit var register_btn : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_screen)

        init();

        register_btn.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this@OnBoardingScreen, Selection:: class.java);
                startActivity(intent);
            }
        )


    }

    private fun init(){
        register_btn = findViewById(R.id.signup_btn);
    }
}