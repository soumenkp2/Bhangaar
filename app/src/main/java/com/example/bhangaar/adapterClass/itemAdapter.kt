package com.example.bhangaar.adapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bhangaar.R
import com.example.bhangaar.dataClass.Item_Info
import com.example.bhangaar.fragmentClass.homeFragment
import java.util.*

class itemAdapter(private val item_list : ArrayList<Item_Info>, private val context: Context, private val item_check_list : ArrayList<Boolean>) : RecyclerView.Adapter<itemAdapter.itemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_item,parent,false)
        return itemViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val item : Item_Info = item_list[position]
        holder.item_name.text = item.ItemName
        holder.item_rate.text = item.ItemRate

        Glide.with(context).load(item.ItemPic).into(holder.item_pic)

        item_check_list[position] = holder.item_btn.isChecked

        holder.item_btn.setOnClickListener {
//            if(holder.item_btn.isChecked == true)
//            {
//                holder.item_btn.isChecked = false
//            }


            if(item_check_list.size > 0)
            {
                if(item_check_list[position]==true)
                {
                    holder.item_btn.isChecked = false
                    item_check_list[position] = false
                }
                else
                {
                    item_check_list[position] = true
                }
            }



            //Toast.makeText(context, "State 1 : "+item_check_list[0] + " and " + "State 2 : "+item_check_list[1], Toast.LENGTH_SHORT).show();


        }






    }

    override fun getItemCount(): Int {
        return item_list.size
    }


    public class itemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val item_pic : ImageView = itemView.findViewById(R.id.item_image)
        val item_name : TextView = itemView.findViewById(R.id.item_name)
        val item_rate : TextView = itemView.findViewById(R.id.item_rate)
        val item_btn : RadioButton = itemView.findViewById(R.id.item_btn)
    }
}