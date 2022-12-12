package com.example.bhangaar

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import java.util.*
import kotlin.collections.ArrayList

class orderTransactionDialog() : DialogFragment() {


    // dialog view is created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Objects.requireNonNull(dialog)?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.transaction_dialogbox,null,false)
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

