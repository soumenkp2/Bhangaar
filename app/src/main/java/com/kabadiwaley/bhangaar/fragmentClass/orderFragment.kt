package com.kabadiwaley.bhangaar.fragmentClass

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kabadiwaley.bhangaar.Constants.Companion.mode
import com.kabadiwaley.bhangaar.R
import com.kabadiwaley.bhangaar.adapterClass.itemAdapter
import com.kabadiwaley.bhangaar.adapterClass.orderDetailsAdapter
import com.kabadiwaley.bhangaar.adapterClass.orderRequestAdapter
import com.kabadiwaley.bhangaar.dataClass.Item_Info
import com.kabadiwaley.bhangaar.dataClass.Order_Info
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [orderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class orderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var order_detail_recycler : RecyclerView
    private lateinit var orderDetailList : ArrayList<Order_Info>
    private lateinit var db : FirebaseFirestore
    private lateinit var userid : String
    private lateinit var orderDetailsAdapter: orderDetailsAdapter

    private lateinit var livebtn : TextView
    private lateinit var acceptbtn : TextView
    private lateinit var finishbtn : TextView

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
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_order, container, false)

        //Fetching user id value via bundles from MainActivity
        val bundle = arguments
        userid = bundle!!.getString("userid").toString()
        lat = bundle.getString("lat").toString()
        long = bundle.getString("long").toString()
        state = bundle.getString("state").toString()
        postal = bundle.getString("postal").toString()
        name = bundle.getString("name").toString()
        address = bundle.getString("address").toString()
        role = bundle.getString("role").toString()
        phone = bundle.getString("phone").toString()

        //Initializing objects
        order_detail_recycler = view.findViewById(R.id.orderdetail_recycler)
        livebtn = view.findViewById(R.id.livebtn)
        acceptbtn = view.findViewById(R.id.acceptbtn)
        finishbtn = view.findViewById(R.id.finishbtn)

        order_detail_recycler.layoutManager = LinearLayoutManager(context)
        order_detail_recycler.hasFixedSize()
        orderDetailList = arrayListOf()

        fetchOrderDetailData_live()

        orderDetailsAdapter = context?.let { orderDetailsAdapter(userid, orderDetailList, it, "Confirmed", state, postal,address) }!!
        order_detail_recycler.adapter = orderDetailsAdapter

        livebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline_dark)
        acceptbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
        finishbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)


        livebtn.setOnClickListener {
            livebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline_dark)
            acceptbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            finishbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            orderDetailList = arrayListOf()
            fetchOrderDetailData_live()
            orderDetailsAdapter = context?.let { orderDetailsAdapter(userid, orderDetailList, it, "Confirmed", state, postal, address) }!!
            order_detail_recycler.adapter = orderDetailsAdapter

        }

        acceptbtn.setOnClickListener {
            livebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            acceptbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline_dark)
            finishbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            orderDetailList = arrayListOf()
            fetchOrderDetailData_accepted()
            orderDetailsAdapter = context?.let { orderDetailsAdapter(userid, orderDetailList, it, "Accepted", state, postal, address) }!!
            order_detail_recycler.adapter = orderDetailsAdapter
        }

        finishbtn.setOnClickListener {
            livebtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            acceptbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline)
            finishbtn.background = ContextCompat.getDrawable(requireContext(),R.drawable.primary_outline_dark)
            orderDetailList = arrayListOf()
            fetchOrderDetailData_Finished()
            orderDetailsAdapter = context?.let { orderDetailsAdapter(userid, orderDetailList, it, "Finished", state, postal, address) }!!
            order_detail_recycler.adapter = orderDetailsAdapter
        }

        return view
    }

    private fun fetchOrderDetailData_live() {
        db = FirebaseFirestore.getInstance()
        db.collection(mode).document(state).collection(postal)
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

                            if(order_item.OrderStatus.equals("Confirmed") && order_item.authuserid.equals(userid))
                            {
                                orderDetailList.add(order_item)
                            }
                        }
                    }

                    orderDetailsAdapter.notifyDataSetChanged()

                }


            })

    }

    private fun fetchOrderDetailData_accepted() {
        db = FirebaseFirestore.getInstance()
        db.collection(mode).document(state).collection(postal)
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

                            if(order_item.OrderStatus.equals("Accepted") && order_item.authuserid.equals(userid))
                            {
                                orderDetailList.add(order_item)
                            }
                        }
                    }

                    orderDetailsAdapter.notifyDataSetChanged()

                }


            })

    }

    private fun fetchOrderDetailData_Finished() {
        db = FirebaseFirestore.getInstance()
        db.collection(mode).document(state).collection(postal)
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

                            if((order_item.OrderStatus.equals("Completed") || order_item.OrderStatus.equals("Cancelled"))&& order_item.authuserid.equals(userid))
                            {
                                orderDetailList.add(order_item)
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
         * @return A new instance of fragment orderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            orderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}