package com.kabadiwaley.bhangaar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import com.kabadiwaley.bhangaar.R
import com.kabadiwaley.bhangaar.fragmentClass.profileFragment
import com.kabadiwaley.bhangaar.fragmentClassVendor.profileFragmentVendor

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Agreement.newInstance] factory method to
 * create an instance of this fragment.
 */
class Agreement : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var web : WebView
    private lateinit var back : ImageView
    private var link : String = ""

    private var authUserId = "Soumen"
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(R.layout.fragment_agreement, container, false)
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
        link = bundle.getString("link").toString()
        
        back = v.findViewById(R.id.back)
        back.setOnClickListener {

            if(role == "Users")
            {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                val profiles = profileFragment()
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
                //bundle.putString("link","https://www.freeprivacypolicy.com/live/621f5a9f-5796-4235-b5fe-277463504280")
                profiles.arguments = bundle
                transaction?.replace(R.id.frameLayout, profiles)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            }
            else{
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                val profiles = profileFragmentVendor()
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
                //bundle.putString("link","https://www.freeprivacypolicy.com/live/621f5a9f-5796-4235-b5fe-277463504280")
                profiles.arguments = bundle
                transaction?.replace(R.id.frameLayout, profiles)
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            }

        }

        web = v.findViewById(R.id.webview)
        web.loadUrl(link)

        return v;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Agreement.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Agreement().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}