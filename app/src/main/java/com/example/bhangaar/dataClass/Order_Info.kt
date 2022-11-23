package com.example.bhangaar.dataClass

data class Order_Info(var OrderNo : Int ?= null, var UserName : String ?= null, var UserPhone : String ?= null, var UserLocation : String ?= null, var OrderStatus : String ?= null, val order_item_list : ArrayList<Item_Info> ?= null )
