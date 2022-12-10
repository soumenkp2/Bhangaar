package com.example.bhangaar

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class SignUp : AppCompatActivity() {


    private lateinit var OTP: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private lateinit var sendOtpBtn : TextView
    private lateinit var editTxtPhone : EditText
    private lateinit var editTxtName : EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var number : String
    private lateinit var progressbar : ProgressBar
    private lateinit var otp_layout : LinearLayout
    private lateinit var resend_otp_txt : TextView
    private lateinit var verify_btn : TextView

    private lateinit var otp1 : EditText
    private lateinit var otp2 : EditText
    private lateinit var otp3 : EditText
    private lateinit var otp4 : EditText
    private lateinit var otp5 : EditText
    private lateinit var otp6 : EditText

    private lateinit var role : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        role = intent.extras?.get("role").toString()

        init()

        addTextChangeListener()


        //Responds when resend otp is invoked/clicked
        resend_otp_txt.setOnClickListener {
            resendVerificationCode()
            resendOTPTvVisibility()
        }

        //Verify if the written otp is right or wrong
        verify_btn.setOnClickListener {
            //collect otp from all the edit texts
            val typedOTP =
                (otp1.text.toString() + otp2.text.toString() + otp3.text.toString()
                        + otp4.text.toString() + otp5.text.toString() + otp6.text.toString())

            if (typedOTP.isNotEmpty()) {
                if (typedOTP.length == 6) {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP, typedOTP
                    )
                    progressbar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                } else {
                    Toast.makeText(this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }


        }

        //Send otp method
        sendOtpBtn.setOnClickListener {
            number = editTxtPhone.text.toString().trim()

            if(number.isNotEmpty() && number.length == 10)
            {
                number = "+91$number"

                otp1.setText("")
                otp2.setText("")
                otp3.setText("")
                otp4.setText("")
                otp5.setText("")
                otp6.setText("")

                progressbar.visibility = View.VISIBLE
                sendOtpBtn.visibility = View.GONE

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(number)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
            else
            {
                Toast.makeText(this, "Enter Valid Number", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //Sign in Auth Method
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this,"Verified", Toast.LENGTH_SHORT).show();


                    val intent : Intent
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("role",role)
                    intent.putExtra("userid", FirebaseAuth.getInstance().currentUser?.uid.toString())
                    startActivity(intent)

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    //Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    //Intiliazation method
    private fun init()
    {
        sendOtpBtn = findViewById(R.id.sendotp_btn)
        editTxtPhone = findViewById(R.id.edit_txt_phoneno)
        editTxtName = findViewById(R.id.edit_txt_name)
        auth = FirebaseAuth.getInstance()
        resend_otp_txt = findViewById(R.id.resendbtn)
        otp_layout = findViewById(R.id.otp_layout)
        progressbar = findViewById(R.id.progressbar)
        verify_btn = findViewById(R.id.verify_btn)

        otp1 = findViewById(R.id.otp1)
        otp2 = findViewById(R.id.otp2)
        otp3 = findViewById(R.id.otp3)
        otp4 = findViewById(R.id.otp4)
        otp5 = findViewById(R.id.otp5)
        otp6 = findViewById(R.id.otp6)

    }

    //3 callbacks generated after generating otp
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            //Log.d(TAG, "onVerificationCompleted:$credential")
            progressbar.visibility = View.GONE
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
            Toast.makeText(applicationContext,"Failed to send OTP", Toast.LENGTH_SHORT).show()

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            progressbar.visibility = View.GONE
            otp_layout.visibility = View.VISIBLE
            resend_otp_txt.visibility = View.VISIBLE
            verify_btn.visibility = View.VISIBLE

            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            //Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            OTP = verificationId
            resendToken = token

            Toast.makeText(applicationContext,"OTP : " + verificationId, Toast.LENGTH_SHORT).show();
        }
    }

    //Method to clear all otp edittext and visible resend after previous otp has been sent
    private fun resendOTPTvVisibility() {
        otp1.setText("")
        otp2.setText("")
        otp3.setText("")
        otp4.setText("")
        otp5.setText("")
        otp6.setText("")
        resend_otp_txt.visibility = View.INVISIBLE
        resend_otp_txt.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            resend_otp_txt.visibility = View.VISIBLE
            resend_otp_txt.isEnabled = true
        }, 60000)
    }

    //Method to resend code
    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    //Add on functionality to implement Edittext Watcher
    private fun addTextChangeListener() {
        otp1.addTextChangedListener(EditTextWatcher(otp1))
        otp2.addTextChangedListener(EditTextWatcher(otp2))
        otp3.addTextChangedListener(EditTextWatcher(otp3))
        otp4.addTextChangedListener(EditTextWatcher(otp4))
        otp5.addTextChangedListener(EditTextWatcher(otp5))
        otp6.addTextChangedListener(EditTextWatcher(otp6))
    }

    //Functionality to request the focuses between edittext of OTP
    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            otp1.requestFocus()
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {

            val text = p0.toString()
            when (view.id) {
                R.id.otp1 -> if (text.length == 1) otp2.requestFocus()
                R.id.otp2 -> if (text.length == 1) otp3.requestFocus() else if (text.isEmpty()) otp1.requestFocus()
                R.id.otp3-> if (text.length == 1) otp4.requestFocus() else if (text.isEmpty()) otp2.requestFocus()
                R.id.otp4 -> if (text.length == 1) otp5.requestFocus() else if (text.isEmpty()) otp3.requestFocus()
                R.id.otp5 -> if (text.length == 1) otp6.requestFocus() else if (text.isEmpty()) otp4.requestFocus()
                R.id.otp6 -> if (text.isEmpty()) otp5.requestFocus()

            }
        }

    }

    //Method to check if the user is already logged in or not
    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null)
        {
            val intent : Intent
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("role",role)
            intent.putExtra("userid", FirebaseAuth.getInstance().currentUser?.uid.toString())
            startActivity(intent)
        }
    }
}