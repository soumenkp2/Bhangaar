package com.kabadiwaley.bhangaar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class OnBoardingScreen : AppCompatActivity() {

    private lateinit var register_btn : TextView
    private lateinit var role : String
    private lateinit var signin_btn : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_screen)

        init();

        role = intent.extras?.get("role").toString()

        register_btn.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this@OnBoardingScreen, SignUp:: class.java);
                intent.putExtra("role",role)
                intent.putExtra("screen","register")
                startActivity(intent);
            }
        )

        signin_btn.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this@OnBoardingScreen, SignUp:: class.java);
                intent.putExtra("role",role)
                intent.putExtra("screen","signin")
                startActivity(intent);
            }
        )



    }

    private fun init(){
        register_btn = findViewById(R.id.signup_btn);
        signin_btn = findViewById(R.id.signin_btn)
    }
}