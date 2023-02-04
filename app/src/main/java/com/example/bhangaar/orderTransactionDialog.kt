package com.example.bhangaar

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.bhangaar.dataClass.Item_Info
import com.example.bhangaar.dataClass.Order_Info
import com.example.bhangaar.fragmentClassVendor.homeFragmentVendor
import com.example.bhangaar.fragmentClassVendor.orderFragmentVendor
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class orderTransactionDialog(private val scr : String, private val order_no : String, private var authUserId : String, private var authVendorId : String, private var order_item : Order_Info, private var itemlist : ArrayList<Item_Info>, private var action : String, private val state : String, private val postal : String) : DialogFragment() {

    private lateinit var yesbtn : ImageView
    private lateinit var nobtn : ImageView
    private lateinit var content : TextView
    private var screen : String = scr.toString()
    private var change : String = action.toString()
   // private var dialog : orderTransactionDialog = alertDialog


    // dialog view is created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Objects.requireNonNull(dialog)?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        var v : View =  inflater.inflate(R.layout.transaction_dialogbox,null,false)

        yesbtn = v.findViewById(R.id.yesbtn)
        nobtn = v.findViewById(R.id.nobtn)
        content = v.findViewById(R.id.dialog_content)

        content.text = screen.toString()


        Toast.makeText(context, order_item.OrderNo.toString(), Toast.LENGTH_SHORT).show()

        yesbtn.setOnClickListener {

            if(change == "accepted")
            {
                var homefrag = homeFragmentVendor()
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {

                    var db = FirebaseFirestore.getInstance()
                    db = FirebaseFirestore.getInstance()
                    db.collection("BhangaarItems").document(state).collection(postal)
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("orderStatus" to "Accepted"))

                    var dbb = FirebaseFirestore.getInstance()
                    dbb = FirebaseFirestore.getInstance()
                    dbb.collection("BhangaarItems").document(state).collection(postal)
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("authvendorid" to authVendorId.toString()))

                    val bundle = Bundle()
                    bundle.putString("userid",authVendorId)
                    homefrag.arguments = bundle

                    transaction.replace(R.id.frameLayout, homefrag)
                }
                if (transaction != null) {
                    transaction.disallowAddToBackStack()
                }
                if (transaction != null) {
                    dismiss()
                    transaction.commit()

                }
            }
            else if(change == "completed")
            {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {

                    var db = FirebaseFirestore.getInstance()
                    db = FirebaseFirestore.getInstance()
                    db.collection("BhangaarItems").document(state).collection(postal)
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("orderStatus" to "Completed"))

                    var dbb = FirebaseFirestore.getInstance()

                    dbb = FirebaseFirestore.getInstance()
                    dbb.collection("BhangaarItems").document(state).collection(postal)
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("authvendorid" to authVendorId.toString()))

                    val orderfrag = orderFragmentVendor()
                    val bundle = Bundle()
                    bundle.putString("userid",authVendorId)
                    orderfrag.arguments = bundle

                    transaction.replace(R.id.frameLayout, orderfrag)
                }
                if (transaction != null) {
                    transaction.disallowAddToBackStack()
                }
                if (transaction != null) {
                    dismiss()
                    transaction.commit()

                }
            }
            else if(change == "cancelled")
            {
                var transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {

                    var db = FirebaseFirestore.getInstance()
                    db = FirebaseFirestore.getInstance()
                    db.collection("BhangaarItems").document(state).collection(postal)
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("orderStatus" to "Cancelled"))

                    var dbb = FirebaseFirestore.getInstance()
                    dbb = FirebaseFirestore.getInstance()
                    dbb.collection("BhangaarItems").document(state).collection(postal)
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("authvendorid" to authVendorId.toString()))

                    val orderfrag = orderFragmentVendor()
                    val bundle = Bundle()
                    bundle.putString("userid",authVendorId)
//                    bundle.putString("lat",lat)
//                    bundle.putString("long",long)
//                    bundle.putString("state",state)
//                    bundle.putString("postal",postal)
//                    bundle.putString("name",name)
//                    bundle.putString("address",address)
//                    bundle.putString("role",role)

                    orderfrag.arguments = bundle

                    transaction.replace(R.id.frameLayout, orderfrag)
                }
                if (transaction != null) {
                    transaction.disallowAddToBackStack()
                }
                if (transaction != null) {
                    dismiss()
                    transaction.commit()

                }
            }

        }

        nobtn.setOnClickListener {
            dismiss()
        }


        return v
    }

    fun assign_vendor()
    {
        Log.e("entered", "yayay")
        var db = FirebaseFirestore.getInstance()
        db.collection("BhangaarItems").document("UttarPradesh").collection("201204")
            .document("Vendors").collection(authVendorId)
            .document("Orders").collection("OrderDetailList").document(order_no.toString()).set(order_item)

        var index = 0
        while(index!=itemlist.size)
        {
            db = FirebaseFirestore.getInstance()
            itemlist[index].ItemName?.let { it1 ->
                db.collection("BhangaarItems").document("UttarPradesh").collection("201204")
                    .document("Vendors").collection(authVendorId)
                    .document("Orders").collection("OrderDetailList").document(order_no.toString()).collection("OrderItemList")
                    .document(it1).set(itemlist[index])

            }
            ++index
        }
    }

    //dialog view is ready
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }



}

