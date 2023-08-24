package com.example.shacklehotelbuddy.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PropertiesRequestBody(
    var currency: String = "USD",
    var eapid: Int = 1,
    var locale: String = "en_US",
    var siteId: Int = 300000001,
    var destination: Destination = Destination(),
    var checkInDate: Date? = null,
    var checkOutDate: Date? = null,
    var rooms: ArrayList<Rooms>? = null,
    var resultsStartingIndex: Int = 0,
    var resultsSize: Int = 50,
    var sort: String = "PRICE_LOW_TO_HIGH",
    var filters: Filters = Filters(),
) : Parcelable

@Parcelize
data class Destination(
    var regionId: String = "6054439",
) : Parcelable

@Parcelize
data class Date(
    var day: Int,
    var month: Int,
    var year: Int,
):Parcelable

@Parcelize
data class Rooms(
    var adults: Int = 1,
    var children: ArrayList<Children>,
):Parcelable

@Parcelize
data class Children(
    var age: Int = 1,
):Parcelable

@Parcelize
data class Filters(
    var price: PriceRange = PriceRange(),
):Parcelable

@Parcelize
data class PriceRange(
    var max: Int = 2000,
    var min: Int = 1,
):Parcelable