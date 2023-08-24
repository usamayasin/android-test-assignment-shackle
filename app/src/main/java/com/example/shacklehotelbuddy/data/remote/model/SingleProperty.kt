package com.example.shacklehotelbuddy.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SingleProperty(
    val name:String,
    val id:String,
    val propertyImage:String,
    val rating:Double,
    val price:String
): Parcelable



