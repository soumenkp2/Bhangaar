package com.example.bhangaar.fragmentClass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhangaar.R
import com.example.bhangaar.adapterClass.itemAdapter
import com.example.bhangaar.dataClass.Item_Info
import com.example.bhangaar.dataClass.Order_Info
import com.example.bhangaar.fragmentClassVendor.homeFragmentVendor
import com.example.bhangaar.orderDetails
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.lang.Double.parseDouble
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class homeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recycler_item : RecyclerView
    private lateinit var db : FirebaseFirestore
    private lateinit var setdb : FirebaseFirestore
    private lateinit var usersetdb : FirebaseFirestore
    private lateinit var vendorsetdb : FirebaseFirestore
    public lateinit var item_list : ArrayList<Item_Info>
    //public lateinit var expected_kg_item_list : ArrayList<Int>

    private lateinit var item_adapter : itemAdapter

    private lateinit var textname : TextView
    private lateinit var order_btn : TextView
    private lateinit var total_kg_txt : TextView
    var count : Int = 0
    lateinit var order_info : Order_Info
    lateinit var order_item_list : ArrayList<Item_Info>
    private lateinit var item_check_list : ArrayList<Boolean>

    var total_kg_value : String = ""

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)

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

        //Toast.makeText(context, authUserId, Toast.LENGTH_SHORT).show();

        textname = view.findViewById(R.id.textname)
        order_btn = view.findViewById(R.id.make_order_btn)
        total_kg_txt = view.findViewById(R.id.totalKg_txt)

        total_kg_txt.text = total_kg_value.toString()

        textname.text = "Hello " + name.toString()
        recycler_item = view.findViewById(R.id.item_recycler)
        recycler_item.layoutManager = LinearLayoutManager(context)
        recycler_item.hasFixedSize()

        item_list = arrayListOf()
        item_check_list = arrayListOf()
        order_item_list = arrayListOf()

        EventChangeListener()

        item_adapter = context?.let { itemAdapter(item_list, it, item_check_list, total_kg_value) }!!
        recycler_item.adapter = item_adapter

        textname.setOnClickListener {
            var item_s : Int = item_check_list.size
            var index : Int = 0
            var curr_order_string : String = ""

            while(item_s!=0)
            {
                if(item_check_list[index]==true)
                {
                    curr_order_string += item_list[index].expectedKg.toString()
                    curr_order_string += "\n"
                }
                index++
                item_s--
            }

            Toast.makeText(context, curr_order_string.toString(), Toast.LENGTH_SHORT).show()
        }


        order_info = Order_Info()
        order_info.OrderNo = (0..1000000).random()
        //order_info.OrderNo = 123456
        order_info.OrderStatus = "Confirmed"
        order_info.UserLocation = postal
        order_info.UserName = name
        order_info.UserPhone = phone
        order_info.OrderDate = LocalDate.now().toString()
        order_info.authuserid = authUserId.toString()
        order_info.authvendorid = "vendoruserid"
        order_info.latitude = lat
        order_info.longitude = long
        order_info.userAddress = address
        order_info.userState = state
        order_info.startTime = LocalTime.now().toString()
        order_info.endTime = "Will be completed soon"

        order_btn.setOnClickListener {
            order_info.OrderNo = (0..1000000).random()

            if(check_empty_checkBoxes())
            {
                SetDataEventListener()
            }
            else{
                Toast.makeText(context,"Fill all the empty boxes", Toast.LENGTH_SHORT).show()
            }


            //Toast.makeText(context,order_info.startTime + order_info.endTime, Toast.LENGTH_SHORT).show()

            val bundle = Bundle()
            bundle.putString("userid",authUserId)
            bundle.putString("role","user")
            bundle.putString("name",name)
            bundle.putString("state",state)
            bundle.putString("lat",lat)
            bundle.putString("long",long)
            bundle.putString("address",address)
            bundle.putString("postal",postal)
            bundle.putString("phone",phone)
            val homeFrag = homeFragment()
            homeFrag.arguments = bundle

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.frameLayout, homeFrag)
            }
            if (transaction != null) {
                transaction.disallowAddToBackStack()
            }
            if (transaction != null) {
                transaction.commit()

            }



        }



        return view
    }

    private fun check_empty_checkBoxes(): Boolean {
        var curr_s : Int = item_check_list.size
        var index_s : Int = 0;

        var tkg : Int = 0
        var numeric = true

        while(curr_s > 0)
        {
            if(item_check_list[index_s] == true)
            {
                try {
                    val num = parseDouble(item_list[index_s].expectedKg.toString())
                } catch (e: NumberFormatException) {
                    numeric = false
                }

            }
            curr_s--;
            index_s++;
        }

        return numeric
    }

    private fun SetDataEventListener()
    {
        order_info.OrderNo = (0..1000000).random()
        //order_info.OrderNo = 654321
        val order_no = order_info.OrderNo

        //Evaluating total kg value
        var curr_s : Int = item_check_list.size
        var index_s : Int = 0;

        var tkg : Int = 0

        while(curr_s > 0)
        {
            if(item_check_list[index_s] == true)
            {
                tkg += item_list[index_s].expectedKg?.toInt() ?: 0
            }
            curr_s--;
            index_s++;
        }

        total_kg_value = tkg.toString()
        total_kg_txt.text = total_kg_value

        order_info.totalKg = total_kg_value

        //Mapping the setdb object to insert data
        setdb = FirebaseFirestore.getInstance()
        setdb.collection("BhangaarItems").document(state).collection(postal)
            .document("Orders").collection("OrderDetailList").document(order_no.toString()).set(order_info)



        Toast.makeText(context, "Order made", Toast.LENGTH_SHORT).show()

        //Inserting Order List Items
        order_item_list = arrayListOf()
        var index = 0
        var test : String = ""

        while(index!=count)
        {
            if(item_check_list[index]==true)
            {
                test += item_list[index].expectedKg.toString() + ","
                order_item_list.add(item_list[index])

                setdb = FirebaseFirestore.getInstance()
                item_list[index].ItemName?.let { it1 ->
                    setdb.collection("BhangaarItems").document(state).collection(postal)
                        .document("Orders").collection("OrderDetailList").document(order_no.toString()).collection("OrderItemList")
                        .document(it1).set(item_list[index])
                }


            }

            ++index
        }

        Toast.makeText(context, test, Toast.LENGTH_SHORT).show()

        val intent = Intent(activity, orderDetails::class.java)
        intent.putExtra("OrderItemList", order_item_list)
        intent.putExtra("OrderNo", order_info.OrderNo)
        intent.putExtra("OrderStatus", order_info.OrderStatus)
        intent.putExtra("Username", order_info.UserName)
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun check_all_text_boxes()
    {
        for(i in 0 .. item_check_list.size-1)
        {
            if(item_check_list[i] == true)
            {

            }
        }
    }

    private fun EventChangeListener()
    {
        db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document(state).collection(postal).document("Items")
            .collection("ItemList").
        //db.collection("test").
                addSnapshotListener(object : EventListener<QuerySnapshot>
                {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        if(p1!=null)
                        {
                            //Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
                            Log.e(p1.toString(),"Error Message")
                        }

                        for(dc :DocumentChange in p0?.documentChanges!!)
                        {
                            if(dc.type == DocumentChange.Type.ADDED)
                            {
                                item_list.add(dc.document.toObject(Item_Info::class.java))
                                //expected_kg_item_list.add(0);
                            }
                        }

                        count = item_list.size
                        var temp = count
                        while(temp!=0)
                        {
                            item_check_list.add(false)
                            --temp
                        }

                        item_adapter.notifyDataSetChanged()

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
         * @return A new instance of fragment homeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            homeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}