package com.example.bhangaar.adapterClass

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bhangaar.R
import com.example.bhangaar.dataClass.Item_Info
import com.example.bhangaar.dataClass.Order_Info
import com.google.firebase.firestore.*

class orderDetailsAdapter(private val userid : String, private val order_list : ArrayList<Order_Info>, private val context: Context) : RecyclerView.Adapter<orderDetailsAdapter.itemViewHolder>() {

    private lateinit var itemlist :ArrayList<Item_Info>
    private lateinit var orderAdapter: orderAdapter
    private lateinit var order_no : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_order_details,parent,false)
        return itemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int)
    {
        val order_item : Order_Info = order_list[position]

        holder.order_no.text = order_item.OrderNo.toString()
        holder.order_status.text = order_item.OrderStatus.toString()
        holder.username.text = order_item.UserName.toString()

        order_no = order_item.OrderNo.toString()

        holder.order_no.setOnClickListener {
            holder.order_pic.visibility = View.VISIBLE
            holder.order_status.visibility = View.VISIBLE
            holder.username.visibility = View.VISIBLE
            holder.order_list.visibility = View.VISIBLE
        }

        holder.order_list.layoutManager = LinearLayoutManager(context)
        holder.order_list.hasFixedSize()
        itemlist = arrayListOf()

        fetchOrderItemData()

        orderAdapter = context.let { orderAdapter(itemlist, it) }
        holder.order_list.adapter = orderAdapter



    }

    private fun fetchOrderItemData() {
        val db : FirebaseFirestore
        db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document("UttarPradesh").collection("201204").document("Users")
            .collection(userid).document("Orders").collection("OrderDetailList").document(order_no).collection("OrderItemList")
            .addSnapshotListener(object : EventListener<QuerySnapshot>
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
                        itemlist.add(dc.document.toObject(Item_Info::class.java))
                    }
                }

                orderAdapter.notifyDataSetChanged()

            }


        })
    }

    override fun getItemCount(): Int {
        return order_list.size
    }


    public class itemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val order_pic : ImageView = itemView.findViewById(R.id.order_pic)
        val order_no : TextView = itemView.findViewById(R.id.order_no)
        var order_status : TextView = itemView.findViewById(R.id.order_status)
        val username : TextView = itemView.findViewById(R.id.username)
        val order_list : RecyclerView = itemView.findViewById(R.id.recycler_order_items)

    }
}