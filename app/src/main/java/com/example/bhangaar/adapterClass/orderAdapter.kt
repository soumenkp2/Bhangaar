package com.example.bhangaar.adapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bhangaar.R
import com.example.bhangaar.dataClass.Item_Info
import java.util.*

class orderAdapter (private val order_item_list : ArrayList<Item_Info>, private val context : Context) : RecyclerView.Adapter<orderAdapter.itemViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_order_item,parent,false)
        return orderAdapter.itemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val item : Item_Info = order_item_list[position]
        holder.item_name.text = item.ItemName
        holder.item_rate.text = item.ItemRate

        Glide.with(context).load(item.ItemPic).into(holder.item_pic)
    }

    override fun getItemCount(): Int {
        return order_item_list.size
    }

    public class itemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val item_pic : ImageView = itemView.findViewById(R.id.order_item_image)
        val item_name : TextView = itemView.findViewById(R.id.order_item_name)
        val item_rate : TextView = itemView.findViewById(R.id.order_item_rate)
    }
}