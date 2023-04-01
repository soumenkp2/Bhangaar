package com.kabadiwaley.bhangaar.fragmentClassVendor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kabadiwaley.bhangaar.R
import com.kabadiwaley.bhangaar.Selection
import com.kabadiwaley.bhangaar.personDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlin.math.log


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profileFragmentVendor.newInstance] factory method to
 * create an instance of this fragment.
 */
class profileFragmentVendor : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var signout : LinearLayout
    private lateinit var orders : LinearLayout
    private lateinit var offers : LinearLayout
    private lateinit var aadhar : LinearLayout
    private lateinit var edit : ImageView
    private lateinit var name_txt : TextView
    private lateinit var phone_txt : TextView
    private lateinit var location : TextView
    private lateinit var address_txt : TextView
    private lateinit var state_txt : TextView

    var authUserId = "Soumen"

    private var lat : String = "s"
    private var long : String= "s"
    private var address : String= "s"
    private var state : String = "s"
    private var postal : String = "s"
    private var name : String = "s"
    private var role : String = "s"
    private var phone : String = "s"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_profile_vendor, container, false)
        init(v)

        val bundle = arguments
        authUserId = bundle!!.getString("userid").toString()
        lat = bundle.getString("lat").toString()
        long = bundle.getString("long").toString()
        state = bundle.getString("state").toString()
        postal = bundle.getString("postal").toString()
        name = bundle.getString("name").toString()
        address = bundle.getString("address").toString()
        role = bundle.getString("role").toString()
        phone = bundle.getString("phone").toString()

        name_txt.text = name.toString()
        phone_txt.text = phone.toString()
        location.text = postal.toString()
        address_txt.text = address
        state_txt.text = state.toString()


        edit.setOnClickListener {
            val intent = Intent(activity, personDetails::class.java)
            intent.putExtra("role",role)
            intent.putExtra("userid", FirebaseAuth.getInstance().currentUser?.uid.toString())
            intent.putExtra("phone", FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
            intent.putExtra("screen","edit")
            intent.putExtra("name",name)
            startActivity(intent)
        }

        orders.setOnClickListener {

        }

        signout.setOnClickListener {

            Log.v("signout", "clicked")
            var firebaseAuth: FirebaseAuth
            val authStateListener =
                AuthStateListener { firebaseAuth ->
                    if (firebaseAuth.currentUser == null) {
                        //Do anything here which needs to be done after signout is complete
                        val intent = Intent(activity, Selection::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        Log.v("after signout", "done")
                        startActivity(intent)
                    } else {
                    }
                }

            firebaseAuth = FirebaseAuth.getInstance();
            //firebaseAuth.currentUser = null
            firebaseAuth.addAuthStateListener(authStateListener);
            firebaseAuth.signOut()
            Log.v("signingout", "ok")

        }

        return v
    }

    private fun init(v : View) {
        signout = v.findViewById(R.id.settings_signout)
        orders = v.findViewById(R.id.settings_orders)
        offers = v.findViewById(R.id.settings_offers)
        //aadhar = v.findViewById(R.id.settings_aadhar)
        edit = v.findViewById(R.id.settings_button)
        name_txt = v.findViewById(R.id.settings_name)
        location =  v.findViewById(R.id.settings_location)
        phone_txt =  v.findViewById(R.id.settings_phone)
        address_txt = v.findViewById(R.id.address_txt)
        state_txt = v.findViewById(R.id.state_vendor)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profileFragmentVendor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                profileFragmentVendor().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}