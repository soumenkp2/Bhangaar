package com.kabadiwaley.bhangaar.adapterClass

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kabadiwaley.bhangaar.R
import com.kabadiwaley.bhangaar.dataClass.Item_Info
import java.util.*


class itemAdapter(private val item_list: ArrayList<Item_Info>, private val context: Context, private val item_check_list: ArrayList<Boolean>, private var total_kg_value: String, private var item_txt_list: ArrayList<String>) : RecyclerView.Adapter<itemAdapter.itemViewHolder>() {

    var txt_kg : String = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_item,parent,false)
        return itemViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: itemViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item : Item_Info = item_list[position]

        holder.setIsRecyclable(false)

        holder.item_name.text = item.ItemName
        holder.item_rate.text = item.ItemRate
        holder.item_btn.isChecked = item_check_list[position]
        holder.item_kg_edit.setText(item_txt_list[position])

        if(holder.item_btn.isChecked)
        {
            holder.expected_linear.visibility = View.VISIBLE
        }
        else{
            holder.expected_linear.visibility = View.GONE
        }


        if(holder.item_kg_edit.text.isEmpty())
        {
            holder.item_kg_edit.setError("Cant be empty!")
        }


        holder.item_kg_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(!holder.item_kg_edit.text.isEmpty())
                {
                    item_txt_list[position] = holder.item_kg_edit.text.toString()
                    txt_kg = holder.item_kg_edit.text.toString()
                    item.expectedKg = txt_kg.toString()

                    var cnt : Int = 0;
                    while(cnt != item_txt_list.size)
                    {
                        Log.v("txt : $cnt" , item_txt_list[cnt].toString() + "\n")
                        cnt++
                    }

                }
                else if(holder.item_kg_edit.text.isEmpty())
                {
                    holder.item_kg_edit.setError("Cant be empty!")
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



            if(item_check_list.size >= 0)
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
                    holder.item_btn.isChecked = true
                    item_check_list[position] = true
                    holder.expected_linear.visibility = View.VISIBLE
                    holder.item_kg_edit.setText(item_txt_list[position].toString())
                }
            }

//            Toast.makeText(context,item_check_list[0].toString() + item_check_list[1].toString() + item_check_list[2].toString()
//                    + item_check_list[3].toString() + item_check_list[4].toString() + item_check_list[5].toString()
//                    + item_check_list[6].toString() + item_check_list[7].toString() + item_check_list[8].toString()
//                    + item_check_list[9].toString() + item_check_list[10].toString() + item_check_list[11].toString()
//                    + item_check_list[12].toString() ,Toast.LENGTH_SHORT).show()


            Log.v("Item Check List", "Position : " + position.toString() + "\n" +
                    item_check_list[0].toString() + item_check_list[1].toString() + item_check_list[2].toString()
                    + item_check_list[3].toString() + item_check_list[4].toString() + item_check_list[5].toString()
                    + item_check_list[6].toString() + item_check_list[7].toString() + item_check_list[8].toString()
                    + item_check_list[9].toString() + item_check_list[10].toString())

            var cnt : Int = 0;
            while(cnt != item_txt_list.size)
            {
                Log.v("txt : $cnt" , item_txt_list[cnt].toString() + "\n")
                cnt++
            }


            //Toast.makeText(context, "State 1 : "+item_check_list[0] + " and " + "State 2 : "+item_check_list[1], Toast.LENGTH_SHORT).show();


        }






    }

    override fun getItemCount(): Int {
        return item_list.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
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