package com.example.bhangaar

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bhangaar.dataClass.Order_Info
import com.example.bhangaar.dataClass.Person_Info
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class personDetails : AppCompatActivity() {

    private lateinit var edit_name : EditText
    private lateinit var edit_address : EditText
    private lateinit var edit_postal : EditText
    private lateinit var edit_state : EditText

    private lateinit var fetch_btn : TextView
    private lateinit var next_btn : TextView

    private lateinit var name : String
    private lateinit var address : String
    private lateinit var postal : String
    private lateinit var state : String
    private lateinit var lat : String
    private lateinit var long : String

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private lateinit var role : String
    private lateinit var userid : String
    private var phone : String = "13939"
    private lateinit var screen : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_details)
        init()
        name = "s"
        state = "s"
        address = "s"
        postal = "s"
        lat = "s"
        long = "s"

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        userid = intent.extras?.get("userid").toString()
        role = intent.extras?.get("role").toString()
        phone = intent.extras?.get("phone").toString()
        screen = intent.extras?.get("screen").toString()

        Toast.makeText(applicationContext,userid,Toast.LENGTH_SHORT).show()


        //Signin fetch from Users/Vendors
        if(screen.equals("signin"))
        {

            var person_info : Person_Info = Person_Info()
            val db = FirebaseFirestore.getInstance()
            db.collection("BhangaarItems")
                .document(role).collection(userid).
                addSnapshotListener(object : EventListener<QuerySnapshot>
                {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        if(p1!=null)
                        {
                            Toast.makeText(applicationContext, p1.toString(), Toast.LENGTH_SHORT).show()
                            Log.e(p1.toString(),"Error Message")
                        }

                        for(dc : DocumentChange in p0?.documentChanges!!)
                        {
                            Toast.makeText(applicationContext,userid+"OYYYY",Toast.LENGTH_SHORT).show()
                            if(dc.type == DocumentChange.Type.ADDED)
                            {
                                val item : Person_Info = dc.document.toObject(Person_Info::class.java)
                                if(item.userid.equals(userid))
                                {
                                    Toast.makeText(applicationContext,"oyoy",Toast.LENGTH_SHORT).show()
                                    person_info = item

                                    name = person_info.name.toString()
                                    postal = person_info.postal.toString()
                                    state = person_info.state.toString()
                                    address = person_info.address.toString()
                                    lat = person_info.latitude.toString()
                                    long = person_info.longitude.toString()

                                    Toast.makeText(applicationContext, name+state+postal, Toast.LENGTH_SHORT).show()

                                    val intent : Intent
                                    intent = Intent(applicationContext, MainActivity::class.java)
                                    intent.putExtra("role",role)
                                    intent.putExtra("userid", userid)
                                    intent.putExtra("name",name);
                                    intent.putExtra("state",state);
                                    intent.putExtra("postal",postal);
                                    intent.putExtra("address",address);
                                    intent.putExtra("lat",lat);
                                    intent.putExtra("long",long);
                                    intent.putExtra("phone",phone);
                                    startActivity(intent)
                                }
                            }
                        }

                    }


                }) // Fetched data OF Person Information

        }


        //Sign Up
        next_btn.setOnClickListener {
            if(check_txt())
            {
                name = edit_name.text.toString()
                postal = edit_postal.text.toString()
                address = edit_address.text.toString()
                state = edit_state.text.toString()

                val person_info : Person_Info = Person_Info(edit_name.text.toString(),
                    edit_postal.text.toString(),
                    edit_state.text.toString(),
                    edit_address.text.toString(),
                    userid,phone,lat,long
                    )

                val setdb : FirebaseFirestore = FirebaseFirestore.getInstance()
                setdb.collection("BhangaarItems").document(role).collection(userid)
                    .document(userid).set(person_info)

                val intent : Intent
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("role",role)
                intent.putExtra("userid", userid)
                intent.putExtra("name",name);
                intent.putExtra("state",state);
                intent.putExtra("postal",postal);
                intent.putExtra("address",address);
                intent.putExtra("lat",lat);
                intent.putExtra("long",long);
                intent.putExtra("phone",phone);
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext,"Fill all the details", Toast.LENGTH_SHORT).show()
            }
        }

        fetch_btn.setOnClickListener {
            getLocation()
        }

    }

    fun check_txt(): Boolean {
        if(edit_name.length()!=0 && edit_state.length()!=0 && edit_address.length()!=0 && edit_postal.length()!=0)
        {
            return true
        }
        else
        {
            return false
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                Log.v("apun ka location found", lat+long)

                val location = mFusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,null)
                location.addOnSuccessListener {
                    if(it!=null)
                    {
                        lat = it.latitude.toString()
                        long = it.longitude.toString()

                        Log.v("apun ka location s", lat+long)

                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)
                        apply {
                            lat = list[0].latitude.toString()
                            long = list[0].longitude.toString()
                            state = list[0].adminArea.toString()
                            postal = list[0].postalCode.toString()
                            address = list[0].getAddressLine(0).toString()
                        }

                        edit_state.setText(state.toString())
                        edit_postal.setText(postal.toString())
                        edit_address.setText(address.toString())

                        fetch_btn.visibility = View.GONE
                        next_btn.visibility = View.VISIBLE
                    }
                    //getLocation()
                    Log.v("apun ka location found", lat+long+state+postal+address)
                }

            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED

        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    fun init()
    {
        edit_name = findViewById(R.id.edit_txt_name)
        edit_postal = findViewById(R.id.edit_txt_postal_code)
        edit_address = findViewById(R.id.edit_txt_address)
        edit_state = findViewById(R.id.edit_txt_state)

        fetch_btn = findViewById(R.id.fetchlocation_btn)
        next_btn = findViewById(R.id.next_btn)

    }
}