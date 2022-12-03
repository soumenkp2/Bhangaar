package com.example.bhangaar.dataClass

import java.io.Serializable

data class Item_Info(var ItemName: String ?= null, var ItemRate: String ?= null, var ItemPic: String ?= null) : Serializable

