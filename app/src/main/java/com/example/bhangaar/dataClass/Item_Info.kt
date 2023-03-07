package com.example.bhangaar.dataClass

import java.io.Serializable
import java.lang.IndexOutOfBoundsException

data class Item_Info(var ItemName: String ?= null, var ItemRate: String ?= null, var ItemPic: String ?= null, var expectedKg: String ?= null) : Serializable

