package com.example.bhangaar.fragmentClassVendor

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhangaar.Constants.Companion.mode
import com.example.bhangaar.R
import com.example.bhangaar.adapterClass.orderRequestAdapter
import com.example.bhangaar.dataClass.Order_Info
import com.google.firebase.firestore.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [orderFragmentVendor.newInstance] factory method to
 * create an instance of this fragment.
 */
class orderFragmentVendor : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var order_request_recycler : RecyclerView
    private lateinit var orderDetailList : ArrayList<Order_Info>
    private lateinit var db : FirebaseFirestore
    private lateinit var orderDetailsAdapter: orderRequestAdapter

    private var pincodes : ArrayList<String> = arrayListOf<String>("201204","201206","201201","201017")

    private lateinit var livebtn :TextView
    private lateinit var completebtn :TextView
    private lateinit var cancelbtn :TextView

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
        var view = inflater.inflate(R.layout.fragment_order_vendor, container, false)

        val bundle = arguments
        authVendorId = bundle!!.getString("userid").toString()
        lat = bundle.getString("lat").toString()
        long = bundle.getString("long").toString()
        state = bundle.getString("state").toString()
        postal = bundle.getString("postal").toString()
        name = bundle.getString("name").toString()
        address = bundle.getString("address").toString()
        role = bundle.getString("role").toString()

        //Initializing objects
        order_request_recycler = view.findViewById(R.id.orderlist_recycler)
        livebtn = view.findViewById(R.id.livebtn)
        completebtn = view.findViewById(R.id.completebtn)
        cancelbtn = view.findViewById(R.id.cancelbtn)

        order_request_recycler.layoutManager = LinearLayoutManager(context)
        order_request_recycler.hasFixedSize()
        orderDetailList = arrayListOf()

        fetchOrderDetailData_Live()
        orderDetailsAdapter = context?.let { orderRequestAdapter(orderDetailList, it, "order_history", "Accepted", authVendorId,state,postal,lat, long,name,address,role) }!!
        order_request_recycler.adapter = orderDetailsAdapter

        livebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline_dark)
        completebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
        cancelbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)

        livebtn.setOnClickListener {
            livebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline_dark)
            completebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            cancelbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            orderDetailList = arrayListOf()
            fetchOrderDetailData_Live()
            orderDetailsAdapter = context?.let { orderRequestAdapter(orderDetailList, it, "order_history","Accepted", authVendorId,state,postal,lat, long,name,address,role) }!!
            order_request_recycler.adapter = orderDetailsAdapter

        }

        completebtn.setOnClickListener {
            livebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            completebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline_dark)
            cancelbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            orderDetailList = arrayListOf()
            fetchOrderDetailData_Completed()
            orderDetailsAdapter = context?.let { orderRequestAdapter(orderDetailList, it, "order_history","Completed", authVendorId,state,postal,lat, long,name,address,role) }!!
            order_request_recycler.adapter = orderDetailsAdapter
        }

        cancelbtn.setOnClickListener {
            livebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            completebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            cancelbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline_dark)
            orderDetailList = arrayListOf()
            fetchOrderDetailData_Cancelled()
            orderDetailsAdapter = context?.let { orderRequestAdapter(orderDetailList, it, "order_history", "Cancelled", authVendorId,state,postal,lat, long,name,address,role) }!!
            order_request_recycler.adapter = orderDetailsAdapter
        }

        return view
    }

    private fun fetchOrderDetailData_Live() {

        var pincode_index : Int = 0
        while(pincode_index != pincodes.size)
        {
            db = FirebaseFirestore.getInstance()
            db.collection(mode).document(state).collection(pincodes[pincode_index])
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
                                val order_item : Order_Info = dc.document.toObject(Order_Info::class.java)

                                if(order_item.OrderStatus.equals("Accepted") && order_item.authvendorid.equals(authVendorId))
                                {
                                    orderDetailList.add(order_item)
                                }

                            }
                        }

                        orderDetailsAdapter.notifyDataSetChanged()

                    }


                })
            pincode_index++
        }


//        db = FirebaseFirestore.getInstance()
//        db.collection("BhangaarItems").document(state).collection(postal)
//            .document("Orders").collection("OrderDetailList").
//            addSnapshotListener(object : EventListener<QuerySnapshot>
//            {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
//                    if(p1!=null)
//                    {
//                        Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
//                        Log.e(p1.toString(),"Error Message")
//                    }
//
//                    for(dc : DocumentChange in p0?.documentChanges!!)
//                    {
//                        if(dc.type == DocumentChange.Type.ADDED)
//                        {
//                            val order_item : Order_Info = dc.document.toObject(Order_Info::class.java)
//
//                            if(order_item.OrderStatus.equals("Accepted") && order_item.authvendorid.equals(authVendorId))
//                            {
//                                orderDetailList.add(order_item)
//                            }
//
//                        }
//                    }
//
//                    orderDetailsAdapter.notifyDataSetChanged()
//
//                }
//
//
//            })

    }

    private fun fetchOrderDetailData_Completed() {

        var pincode_index : Int = 0
        while(pincode_index != pincodes.size)
        {
            db = FirebaseFirestore.getInstance()
            db.collection(mode).document(state).collection(pincodes[pincode_index])
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
                                val order_item : Order_Info = dc.document.toObject(Order_Info::class.java)

                                if(order_item.OrderStatus.equals("Completed") && order_item.authvendorid.equals(authVendorId))
                                {
                                    orderDetailList.add(order_item)
                                }

                            }
                        }

                        orderDetailsAdapter.notifyDataSetChanged()

                    }


                })
            pincode_index++
        }


    }

    private fun fetchOrderDetailData_Cancelled() {

        var pincode_index : Int = 0
        while(pincode_index != pincodes.size)
        {
            db = FirebaseFirestore.getInstance()
            db.collection(mode).document(state).collection(pincodes[pincode_index])
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
                                val order_item : Order_Info = dc.document.toObject(Order_Info::class.java)

                                if(order_item.OrderStatus.equals("Cancelled") && order_item.authvendorid.equals(authVendorId))
                                {
                                    orderDetailList.add(order_item)
                                }

                            }
                        }

                        orderDetailsAdapter.notifyDataSetChanged()

                    }


                })
            pincode_index++
        }


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment orderFragmentVendor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            orderFragmentVendor().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}