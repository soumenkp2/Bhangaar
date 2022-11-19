package com.example.bhangaar.adapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bhangaar.R
import com.example.bhangaar.dataClass.Item_Info

class itemAdapter(private val item_list : ArrayList<Item_Info>, private val context: Context) : RecyclerView.Adapter<itemAdapter.itemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_item,parent,false)
        return itemViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val item : Item_Info = item_list[position]
        holder.item_name.text = item.ItemName
        holder.item_rate.text = item.ItemRate

        Glide.with(context).load(item.ItemPic).into(holder.item_pic)

    }

    override fun getItemCount(): Int {
        return item_list.size
    }


    public class itemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val item_pic : ImageView = itemView.findViewById(R.id.item_image)
        val item_name : TextView = itemView.findViewById(R.id.item_name)
        val item_rate : TextView = itemView.findViewById(R.id.item_rate)
    }
}