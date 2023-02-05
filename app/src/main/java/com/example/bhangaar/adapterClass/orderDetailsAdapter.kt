package com.example.bhangaar.adapterClass

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bhangaar.R
import com.example.bhangaar.dataClass.Item_Info
import com.example.bhangaar.dataClass.Order_Info
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class orderDetailsAdapter(private var userid : String, private val order_list : ArrayList<Order_Info>, private val context: Context, private val category : String, private val state : String, private val postal : String) : RecyclerView.Adapter<orderDetailsAdapter.itemViewHolder>() {

    private lateinit var itemlist : ArrayList<Item_Info>
    private lateinit var orderAdapter: orderAdapter
    private lateinit var order_no : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_order_request,parent,false)
        return itemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int)
    {
//        val order_item : Order_Info = order_list[position]
//
//        holder.order_no.text = order_item.OrderNo.toString()
//        holder.order_status.text = order_item.OrderStatus.toString()
//        holder.username.text = order_item.UserName.toString()
//
//        order_no = order_item.OrderNo.toString()
//
//        holder.order_no.setOnClickListener {
//            holder.order_pic.visibility = View.VISIBLE
//            holder.order_status.visibility = View.VISIBLE
//            holder.username.visibility = View.VISIBLE
//            holder.order_list.visibility = View.VISIBLE
//        }
//
//        holder.order_list.layoutManager = LinearLayoutManager(context)
//        holder.order_list.hasFixedSize()
//        val itemlist : ArrayList<Item_Info> = arrayListOf()
//
//        //fetchOrderItemData()
//
//        val db : FirebaseFirestore
//        db = FirebaseFirestore.getInstance()
//        db.collection("test").document("UttarPradesh").collection("201204").document("Users")
//            .collection(userid).document("Orders").collection("OrderDetailList").document(order_no).collection("OrderItemList")
//            .addSnapshotListener(object : EventListener<QuerySnapshot>
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
//                            itemlist.add(dc.document.toObject(Item_Info::class.java))
//                        }
//                    }
//
//                    orderAdapter.notifyDataSetChanged()
//
//                }
//
//
//            })
//
//        orderAdapter = context.let { orderAdapter(itemlist, it) }
//        holder.order_list.adapter = orderAdapter

        val order_item : Order_Info = order_list[position]

        userid = order_item.authuserid.toString()

        holder.order_no.text = order_item.OrderNo.toString()
        holder.order_status.text = order_item.OrderStatus.toString()
        holder.username.text = order_item.UserName.toString()

        order_no = order_item.OrderNo.toString()

        if(category == "Confirmed")
        {
            holder.showcase_pic.background = ContextCompat.getDrawable(context,R.drawable.received)
            holder.order_pic.background = ContextCompat.getDrawable(context,R.drawable.received)
        }
        else if(category == "Accepted")
        {
            holder.showcase_pic.background = ContextCompat.getDrawable(context,R.drawable.orderpending)
            holder.order_pic.background = ContextCompat.getDrawable(context,R.drawable.orderpending)
        }
        else if(order_item.OrderStatus == "Completed")
        {
            holder.showcase_pic.background = ContextCompat.getDrawable(context,R.drawable.ordercompleted)
            holder.order_pic.background = ContextCompat.getDrawable(context,R.drawable.ordercompleted)
            holder.progress_linear.visibility = View.GONE
            holder.accept_linear.visibility = View.GONE
        }
        else if(order_item.OrderStatus == "Cancelled")
        {
            holder.showcase_pic.background = ContextCompat.getDrawable(context,R.drawable.ordercancelled)
            holder.order_pic.background = ContextCompat.getDrawable(context,R.drawable.ordercancelled)
            holder.progress_linear.visibility = View.GONE
            holder.accept_linear.visibility = View.GONE
        }

        holder.order_list.layoutManager = LinearLayoutManager(context)
        holder.order_list.hasFixedSize()

        holder.accept_card.visibility = View.GONE

        holder.order_no.setOnClickListener {
            holder.up_arrow.rotation = 0F
            holder.order_pic.visibility = View.VISIBLE
            holder.order_status.visibility = View.VISIBLE
            holder.username.visibility = View.VISIBLE
            holder.order_list.visibility = View.VISIBLE
            holder.showcase_pic.visibility = View.GONE
        }

        holder.up_arrow.setOnClickListener {
            holder.up_arrow.rotation = 180F
            holder.order_pic.visibility = View.GONE
            holder.order_status.visibility = View.GONE
            holder.username.visibility = View.GONE
            holder.order_list.visibility = View.GONE
            holder.showcase_pic.visibility = View.VISIBLE
        }

        val itemlist :ArrayList<Item_Info> = arrayListOf()
        val db : FirebaseFirestore
        db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document(state).collection(postal)
            .document("Orders").collection("OrderDetailList").document(order_no).collection("OrderItemList")
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

        orderAdapter = context.let { orderAdapter(itemlist, it) }
        holder.order_list.adapter = orderAdapter





    }


    override fun getItemCount(): Int {
        return order_list.size
    }


    public class itemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val up_arrow : ImageView = itemView.findViewById(R.id.up_arrow)
        val order_pic : ImageView = itemView.findViewById(R.id.order_pic)
        val order_no : TextView = itemView.findViewById(R.id.order_no)
        var order_status : TextView = itemView.findViewById(R.id.order_status)
        val distance : TextView = itemView.findViewById(R.id.location)
        val username : TextView = itemView.findViewById(R.id.username)
        val order_list : RecyclerView = itemView.findViewById(R.id.recycler_order_items)
        val accept_card : CardView = itemView.findViewById(R.id.acceptcard)
        val cancel_card : CardView = itemView.findViewById(R.id.cancelcard)
        val complete_card : CardView = itemView.findViewById(R.id.completecard)
        val showcase_pic : ImageView = itemView.findViewById(R.id.showcase_pic)
        val accept_linear : LinearLayout = itemView.findViewById(R.id.accept_linear)
        val progress_linear : LinearLayout = itemView.findViewById(R.id.progress_linear)

    }
}