package com.example.bhangaar.fragmentClass

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhangaar.R
import com.example.bhangaar.adapterClass.itemAdapter
import com.example.bhangaar.dataClass.Item_Info
import com.google.firebase.firestore.*

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
    private lateinit var item_list : ArrayList<Item_Info>
    private lateinit var item_adapter : itemAdapter


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
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)

        recycler_item = view.findViewById(R.id.item_recycler)
        recycler_item.layoutManager = LinearLayoutManager(context)
        recycler_item.hasFixedSize()

        item_list = arrayListOf()
        item_adapter = context?.let { itemAdapter(item_list, it) }!!

        recycler_item.adapter = item_adapter

        EventChangeListener()

//        db = FirebaseFirestore.getInstance()
//        //db.collection("BhangaarItems").document("UttarPradesh").collection("201204").document("Items")
//        //.collection("ItemList").
//        db.collection("test").
//        addSnapshotListener(object : EventListener<QuerySnapshot>
//        {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
//                if(p1!=null)
//                {
//                    Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
//                    Log.e(p1.toString(),"Error Message")
//                }
//
//                for(dc :DocumentChange in p0?.documentChanges!!)
//                {
//                    if(dc.type == DocumentChange.Type.ADDED)
//                    {
//                        Log.v("ss","ss")
//                        item_list.add(dc.document.toObject(Item_Info::class.java))
//                    }
//                }
//
//                item_adapter.notifyDataSetChanged()
//
//            }
//
//
//        })

        //Log.v("s", item_list[0].ItemName.toString())

        return view
    }

    private fun EventChangeListener()
    {
        db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document("UttarPradesh").collection("201204").document("Items")
            .collection("ItemList").
        //db.collection("test").
                addSnapshotListener(object : EventListener<QuerySnapshot>
                {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        if(p1!=null)
                        {
                            Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
                            Log.e(p1.toString(),"Error Message")
                        }

                        for(dc :DocumentChange in p0?.documentChanges!!)
                        {
                            if(dc.type == DocumentChange.Type.ADDED)
                            {
                                item_list.add(dc.document.toObject(Item_Info::class.java))
                            }
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