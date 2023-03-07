package com.example.bhangaar.fragmentClassVendor

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhangaar.R
import com.example.bhangaar.adapterClass.orderDetailsAdapter
import com.example.bhangaar.adapterClass.orderRequestAdapter
import com.example.bhangaar.dataClass.Order_Info
import com.example.bhangaar.dataClass.Postal_Info
import com.google.firebase.firestore.*
import kotlin.math.roundToInt

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
    private lateinit var textname : TextView

    private var pincodes : ArrayList<String> = arrayListOf<String>("201204","201206","201201","201017")

    private var lat : String = "s"
    private var long : String= "s"
    private var address : String= "s"
    private var state : String = "s"
    private var postal : String = "s"
    private var name : String = "s"
    private var role : String = "s"

    private var test : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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

        //Toast.makeText(context, authVendorId + state + postal , Toast.LENGTH_LONG).show()

        //Initializing objects
        textname = view.findViewById(R.id.textname)
        textname.text = "Hello " + name.toString()
        order_request_recycler = view.findViewById(R.id.orderlist_recycler)
        order_request_recycler.layoutManager = LinearLayoutManager(context)
        order_request_recycler.hasFixedSize()
        orderDetailList = arrayListOf()

        //pincodes = arrayListOf()
        //fetchPincodes()

        fetchOrderDetailData()

        //orderDetailList.sortedWith(compareBy({it.OrderNo}))

        orderDetailsAdapter = context?.let { orderRequestAdapter(orderDetailList, it, "home_vendor", "Confirmed", authVendorId, state, postal, lat, long,name,address,role) }!!
        order_request_recycler.adapter = orderDetailsAdapter
//
//        Toast.makeText(context,"gg" + orderDetailList.size,Toast.LENGTH_SHORT).show()
//        orderDetailList.sortedWith(compareBy({calculate_distance(it.latitude!!.toDouble(), it.longitude!!.toDouble(), lat.toDouble(), long.toDouble())}))
//        orderDetailsAdapter.notifyDataSetChanged()

        return view
    }


    private fun fetchPincodes()
    {
        db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document("UttarPradesh").collection("pincodes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val users = document.toObject(Postal_Info::class.java)
                    //pincodes.add(users)
                    Toast.makeText(context,users.code,Toast.LENGTH_SHORT).show()
                    //Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents: ", exception)
            }
//            addSnapshotListener(object : EventListener<QuerySnapshot>
//            {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
//                    if(p1!=null)
//                    {
//                        //Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
//                        Log.e(p1.toString(),"Error Message")
//                    }
//
//                    for(dc : DocumentChange in p0?.documentChanges!!)
//                    {
//                        if(dc.type == DocumentChange.Type.ADDED)
//                        {
//                            val item : Postal_Info = dc.document.toObject(Postal_Info::class.java)
//                            pincodes.add(item)
//                            test += item.code.toString()
//                            test += "\n"
//
//                        }
//                    }
//
//                }
//
//
//            })
    }


    private fun calculate_distance(order_lat : Double, order_long : Double , vendor_lat : Double, vendor_long : Double): Double {
        val startPoint = Location("locationA")
        startPoint.setLatitude(vendor_lat)
        startPoint.setLongitude(vendor_long)

        val endPoint = Location("locationA")
        endPoint.setLatitude(order_lat)
        endPoint.setLongitude(order_long)

        val distance: Float = startPoint.distanceTo(endPoint)
        return distance.toDouble()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchOrderDetailData() {

        db = FirebaseFirestore.getInstance()

        if(pincodes.size == 0)
        {
            fetchPincodes()
            Toast.makeText(context, "Pincodes Size if : " + pincodes.size, Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Pincodes Size : " + pincodes.size, Toast.LENGTH_SHORT).show()
            var pincode_index : Int = 0
            while(pincode_index != pincodes.size)
            {
                db.collection("BhangaarItems").document(state).collection(pincodes[pincode_index])
                    .document("Orders").collection("OrderDetailList").
                    addSnapshotListener(object : EventListener<QuerySnapshot>
                    {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                            if(p1!=null)
                            {
                                //Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
                                Log.e(p1.toString(),"Error Message")
                            }

                            for(dc : DocumentChange in p0?.documentChanges!!)
                            {
                                if(dc.type == DocumentChange.Type.ADDED)
                                {
                                    val item : Order_Info = dc.document.toObject(Order_Info::class.java)
                                    val estimated_distance : Double = calculate_distance(item.latitude!!.toDouble(), item.longitude!!.toDouble(), lat.toDouble(), long.toDouble())/1000
                                    val dist : Int = estimated_distance.roundToInt()

                                    Toast.makeText(context, dist.toString(),Toast.LENGTH_SHORT).show()
                                    if(item.OrderStatus.equals("Confirmed") && dist<=12)
                                    {
                                        orderDetailList.add(item)
                                        //Toast.makeText(context, dist.toString(),Toast.LENGTH_SHORT).show()
                                        //orderDetailList.sortedWith(compareBy({dist}))
                                    }

                                }
                            }

                            //Toast.makeText(context,"ss"+orderDetailList.size,Toast.LENGTH_SHORT).show()
                              //orderDetailList.sortedBy { it.OrderNo }
                            orderDetailList.sortedBy { it.OrderNo.toString() }
                            //Toast.makeText(context,"ss"+orderDetailList.get(0).OrderNo,Toast.LENGTH_SHORT).show()
//                            orderDetailsAdapter = context?.let { orderRequestAdapter(orderDetailList, it, "home_vendor", "Confirmed", authVendorId, state, postal, lat, long,name,address,role) }!!
//                            order_request_recycler.adapter = orderDetailsAdapter
                            orderDetailsAdapter.notifyDataSetChanged()

                        }


                    })

                //orderDetailsAdapter.notifyDataSetChanged()
                pincode_index++
            }

            orderDetailList.sortedBy { it.OrderNo }
            Toast.makeText(context,"ss"+orderDetailList.size,Toast.LENGTH_SHORT).show()





        }
//        db.collection("BhangaarItems").document(state).collection(postal)
//            .document("Orders").collection("OrderDetailList").
//            addSnapshotListener(object : EventListener<QuerySnapshot>
//            {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
//                    if(p1!=null)
//                    {
//                        //Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
//                        Log.e(p1.toString(),"Error Message")
//                    }
//
//                    for(dc : DocumentChange in p0?.documentChanges!!)
//                    {
//                        if(dc.type == DocumentChange.Type.ADDED)
//                        {
//                            val item : Order_Info = dc.document.toObject(Order_Info::class.java)
//                            if(item.OrderStatus.equals("Confirmed"))
//                            {
//                                orderDetailList.add(item)
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