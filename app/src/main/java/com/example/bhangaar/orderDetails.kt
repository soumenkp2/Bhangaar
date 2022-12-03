package com.example.bhangaar

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhangaar.adapterClass.itemAdapter
import com.example.bhangaar.adapterClass.orderAdapter
import com.example.bhangaar.dataClass.Item_Info
import com.example.bhangaar.dataClass.Order_Info

import java.io.Serializable

class orderDetails : AppCompatActivity() {

    private lateinit var order_no_txt : TextView
    private lateinit var order_status_txt : TextView
    private lateinit var username : TextView
    private lateinit var order_item_list : ArrayList<Item_Info>
    private lateinit var order_recycler_item : RecyclerView
    private lateinit var order_adapter : orderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        init()

        order_no_txt.text = intent.extras?.get("OrderNo").toString()
        order_status_txt.text = intent.extras?.get("OrderStatus").toString()
        username.text = intent.extras?.get("Username").toString()
        order_item_list =
            (intent.getSerializableExtra("OrderItemList") as ArrayList<Item_Info>)

        Toast.makeText(applicationContext, "s"+order_item_list[0].ItemName, Toast.LENGTH_SHORT).show()

        order_recycler_item.layoutManager = LinearLayoutManager(applicationContext)
        order_recycler_item.hasFixedSize()

        order_adapter = applicationContext.let { orderAdapter(order_item_list, it) }!!
        order_recycler_item.adapter = order_adapter
    }

    private fun init()
    {
        order_no_txt = findViewById(R.id.order_no)
        order_status_txt = findViewById(R.id.order_status)
        username = findViewById(R.id.username)
        order_recycler_item = findViewById(R.id.recycler_order_items)

    }
}