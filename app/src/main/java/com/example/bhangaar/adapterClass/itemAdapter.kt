package com.example.bhangaar.adapterClass

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bhangaar.R
import com.example.bhangaar.dataClass.Item_Info
import java.util.*


class itemAdapter(private val item_list : ArrayList<Item_Info>, private val context: Context, private val item_check_list : ArrayList<Boolean>, private var total_kg_value : String) : RecyclerView.Adapter<itemAdapter.itemViewHolder>() {

    var txt_kg : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_item,parent,false)
        return itemViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val item : Item_Info = item_list[position]
        holder.item_name.text = item.ItemName
        holder.item_rate.text = item.ItemRate

        holder.item_kg_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(!holder.item_kg_edit.text.isEmpty())
                {
                    txt_kg = holder.item_kg_edit.text.toString()
                    item.expectedKg = txt_kg.toString()

//                    var kg_int = total_kg_value.toInt()
//                    kg_int += txt_kg.toInt()
//
//                    total_kg_value += kg_int.toString()

                }
            }
        })

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

                    holder.item_kg_edit.text.clear()
                    holder.expected_linear.visibility = View.GONE
                }
                else
                {
                    item_check_list[position] = true
                    holder.expected_linear.visibility = View.VISIBLE
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
        val item_kg_edit : EditText = itemView.findViewById(R.id.item_kg)
        val expected_linear : LinearLayout = itemView.findViewById(R.id.expected_kg)
    }
}