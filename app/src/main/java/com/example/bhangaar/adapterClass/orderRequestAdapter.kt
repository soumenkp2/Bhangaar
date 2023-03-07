package com.example.bhangaar.adapterClass

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhangaar.R
import com.example.bhangaar.dataClass.Item_Info
import com.example.bhangaar.dataClass.Order_Info
import com.example.bhangaar.orderTransactionDialog
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*


class orderRequestAdapter(private val order_list : ArrayList<Order_Info>, private val context: Context, private val screen : String, private val category : String, private val authVendorId : String, private val state : String, private val postal : String, private val lat : String, private val long : String, private val name : String, private val address : String, private val role : String) : RecyclerView.Adapter<orderRequestAdapter.itemViewHolder>() {

    private lateinit var itemlist :ArrayList<Item_Info>
    private lateinit var orderAdapter: orderAdapter
    private lateinit var order_no : String
    private lateinit var authuserid : String
    
    private var estimated_distance : Double = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_order_request,parent,false)
        return itemViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: itemViewHolder, position: Int)
    {
        val order_item : Order_Info = order_list[position]

        authuserid = order_item.authuserid.toString()

        holder.order_no.text = order_item.OrderNo.toString()
        holder.order_status.text = order_item.OrderStatus.toString()
        holder.username.text = order_item.UserName.toString()
        holder.user_address.text = order_item.userAddress.toString()
        holder.total_kg.text = "Total - " + order_item.totalKg.toString() + "/kg"

        order_no = order_item.OrderNo.toString()

        if(screen == "home_vendor")
        {
            holder.progress_linear.visibility = View.VISIBLE
            holder.accept_linear.visibility = View.GONE
        }
        else if(screen == "order_history")
        {
            holder.progress_linear.visibility = View.GONE
            holder.accept_linear.visibility = View.VISIBLE
        }

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
        else if(category == "Completed")
        {
            holder.showcase_pic.background = ContextCompat.getDrawable(context,R.drawable.ordercompleted)
            holder.order_pic.background = ContextCompat.getDrawable(context,R.drawable.ordercompleted)
            holder.progress_linear.visibility = View.GONE
            holder.accept_linear.visibility = View.GONE
        }
        else if(category == "Cancelled")
        {
            holder.showcase_pic.background = ContextCompat.getDrawable(context,R.drawable.ordercancelled)
            holder.order_pic.background = ContextCompat.getDrawable(context,R.drawable.ordercancelled)
            holder.progress_linear.visibility = View.GONE
            holder.accept_linear.visibility = View.GONE
        }

        holder.order_no.setOnClickListener {
            holder.up_arrow.rotation = 0F
            holder.order_pic.visibility = View.VISIBLE
            holder.order_status.visibility = View.VISIBLE
            holder.username.visibility = View.VISIBLE
            holder.order_list.visibility = View.VISIBLE
            holder.showcase_pic.visibility = View.GONE
            holder.actions.visibility = View.VISIBLE
            holder.user_address.visibility = View.VISIBLE
        }

        holder.up_arrow.setOnClickListener {
            holder.up_arrow.rotation = 180F
            holder.order_pic.visibility = View.GONE
            holder.order_status.visibility = View.GONE
            holder.username.visibility = View.GONE
            holder.order_list.visibility = View.GONE
            holder.showcase_pic.visibility = View.VISIBLE
            holder.actions.visibility = View.GONE
            holder.user_address.visibility = View.GONE
        }

        holder.order_list.layoutManager = LinearLayoutManager(context)
        holder.order_list.hasFixedSize()

        val itemlist :ArrayList<Item_Info> = arrayListOf()
        val db : FirebaseFirestore
        db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document(state).collection(order_item.UserLocation.toString())
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


        holder.user_location.setOnClickListener {
            val uri = Uri.parse("http://maps.google.com/maps?saddr=${lat},${long} &daddr=${order_item.latitude.toString()},${order_item.longitude.toString()} &dirflg=w")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        holder.user_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${order_item.UserPhone.toString()}"))
            context.startActivity(intent)
        }

        holder.accept_card.setOnClickListener {
            holder.order_status.text = "Accepted"

            val activity = context as FragmentActivity
            val fm: FragmentManager = activity.supportFragmentManager
            val alertDialog = orderTransactionDialog("Yay, you are finally accepting the order", holder.order_no.text.toString(), authuserid, authVendorId, order_item, itemlist, "accepted",state,order_item.UserLocation.toString(),lat, long,name,address,role)
            alertDialog.show(fm, "fragment_alert")

        }

        holder.complete_card.setOnClickListener {
            holder.order_status.text = "Completed"

            val activity = context as FragmentActivity
            val fm: FragmentManager = activity.supportFragmentManager
            val alertDialog = orderTransactionDialog("Are you sure to complete the order?", holder.order_no.text.toString(), authuserid, authVendorId,order_item, itemlist, "completed",state,order_item.UserLocation.toString(),lat, long,name,address,role)
            alertDialog.show(fm, "fragment_alert")

        }

        holder.cancel_card.setOnClickListener {
            holder.order_status.text = "Cancelled"

            val activity = context as FragmentActivity
            val fm: FragmentManager = activity.supportFragmentManager
            val alertDialog = orderTransactionDialog("Are you sure to cancel the order?", holder.order_no.text.toString(), authuserid, authVendorId, order_item, itemlist, "cancelled",state,order_item.UserLocation.toString(),lat, long,name,address,role)
            alertDialog.show(fm, "fragment_alert")

        }
        
        estimated_distance = calculate_distance(order_item.latitude!!.toDouble(), order_item.longitude!!.toDouble(), lat.toDouble(), long.toDouble())
        holder.distance.text = (estimated_distance/1000).toString().subSequence(0,3).toString() + " km"



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

    private fun fetchOrderItemData() {
        val db : FirebaseFirestore
        db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document("UttarPradesh").collection("201204")
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
        val actions : LinearLayout = itemView.findViewById(R.id.linearactions)
        val user_location : ImageView = itemView.findViewById(R.id.user_location)
        val user_phone : ImageView = itemView.findViewById(R.id.user_call)
        val user_address : TextView = itemView.findViewById(R.id.address)
        val total_kg : TextView = itemView.findViewById(R.id.total_kg)

    }
}