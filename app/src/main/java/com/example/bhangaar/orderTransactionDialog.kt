package com.example.bhangaar

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.bhangaar.fragmentClassVendor.homeFragmentVendor
import com.example.bhangaar.fragmentClassVendor.orderFragmentVendor
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class orderTransactionDialog(private val scr : String, private val order_no : String, private var action : String) : DialogFragment() {

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

        yesbtn.setOnClickListener {

            if(change == "accepted")
            {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {

                    val db = FirebaseFirestore.getInstance()
                    db.collection("test").document("UttarPradesh").collection("201204")
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("orderStatus" to "Accepted"))

                    transaction.replace(R.id.frameLayout, homeFragmentVendor())
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

                    val db = FirebaseFirestore.getInstance()
                    db.collection("test").document("UttarPradesh").collection("201204")
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("orderStatus" to "Completed"))

                    transaction.replace(R.id.frameLayout, orderFragmentVendor())
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
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {

                    val db = FirebaseFirestore.getInstance()
                    db.collection("test").document("UttarPradesh").collection("201204")
                        .document("Orders").collection("OrderDetailList").document(order_no.toString())
                        .update(mapOf("orderStatus" to "Cancelled"))

                    transaction.replace(R.id.frameLayout, orderFragmentVendor())
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

    //dialog view is ready
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


//        // create AgeItemSelectListener to listen to click event on items from the RecyclerView Adapter
//        val listenerAge:AgeItemSelectListener = object : AgeItemSelectListener {
//            override fun itemClicked(ageGroup: AgeGroupModel, position: Int) {
//                //when item in adapter is clicked, show selected age in an AlertDialog
//                showSelectedItemAlert(ageGroup,position)
//            }
//        };


    }

//    private fun showSelectedItemAlert(ageGroup: AgeGroupModel, position: Int){
//        val builder = AlertDialog.Builder(requireActivity())
//        builder.setMessage(ageGroup.label+" you are")
//            .setCancelable(false)
//            .setPositiveButton("ok") { dialog, id -> dismiss()}
//        val alert = builder.create()
//        alert.show()}

}

