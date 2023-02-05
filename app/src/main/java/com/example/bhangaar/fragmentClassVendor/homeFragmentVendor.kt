package com.example.bhangaar.fragmentClassVendor

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhangaar.R
import com.example.bhangaar.adapterClass.orderDetailsAdapter
import com.example.bhangaar.adapterClass.orderRequestAdapter
import com.example.bhangaar.dataClass.Order_Info
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragmentVendor.newInstance] factory method to
 * create an instance of this fragment.
 */
class homeFragmentVendor : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var order_request_recycler : RecyclerView
    private lateinit var orderDetailList : ArrayList<Order_Info>
    private lateinit var db : FirebaseFirestore
    private lateinit var orderDetailsAdapter: orderRequestAdapter
    private lateinit var authVendorId : String

    private var lat : String = "s"
    private var long : String= "s"
    private var address : String= "s"
    private var state : String = "s"
    private var postal : String = "s"
    private var name : String = "s"
    private var role : String = "s"

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
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home_vendor, container, false)

        val bundle = arguments
        authVendorId = bundle!!.getString("userid").toString()
        lat = bundle.getString("lat").toString()
        long = bundle.getString("long").toString()
        state = bundle.getString("state").toString()
        postal = bundle.getString("postal").toString()
        name = bundle.getString("name").toString()
        address = bundle.getString("address").toString()
        role = bundle.getString("role").toString()

        Toast.makeText(context, authVendorId + state + postal , Toast.LENGTH_SHORT).show()

        //Initializing objects
        order_request_recycler = view.findViewById(R.id.orderlist_recycler)
        order_request_recycler.layoutManager = LinearLayoutManager(context)
        order_request_recycler.hasFixedSize()
        orderDetailList = arrayListOf()

        fetchOrderDetailData()

        orderDetailsAdapter = context?.let { orderRequestAdapter(orderDetailList, it, "home_vendor", "Confirmed", authVendorId, state, postal, lat, long,name,address,role) }!!
        order_request_recycler.adapter = orderDetailsAdapter

        return view
    }

    private fun fetchOrderDetailData() {
        db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document(state).collection(postal)
            .document("Orders").collection("OrderDetailList").
            addSnapshotListener(object : EventListener<QuerySnapshot>
            {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                    if(p1!=null)
                    {
                        Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
                        Log.e(p1.toString(),"Error Message")
                    }

                    for(dc : DocumentChange in p0?.documentChanges!!)
                    {
                        if(dc.type == DocumentChange.Type.ADDED)
                        {
                            val item : Order_Info = dc.document.toObject(Order_Info::class.java)
                            if(item.OrderStatus.equals("Confirmed"))
                            {
                                orderDetailList.add(item)
                            }

                        }
                    }

                    orderDetailsAdapter.notifyDataSetChanged()

                }


            })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment homeFragmentVendor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            homeFragmentVendor().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}