package com.example.shacklehotelbuddy.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PropertiesResponse(
    @Expose(serialize = true)
    @SerializedName("data") var data: Data,
)

data class Data(
    @SerializedName("propertySearch") var propertySearch: PropertySearch? = PropertySearch(),
)

data class PropertySearch(
    @SerializedName("__typename") var _typename: String? = null,
    @SerializedName("properties") var properties: List<Properties> = arrayListOf(),
)

data class Properties(
    @SerializedName("__typename") var _typename: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("availability") var availability: Availability? = Availability(),
    @SerializedName("propertyImage") var propertyImage: PropertyImage? = PropertyImage(),
    @SerializedName("price") var price: Price? = Price(),
    @SerializedName("propertyFees") var propertyFees: ArrayList<String> = arrayListOf(),
    @SerializedName("reviews") var reviews: Reviews? = Reviews(),
)

data class Price(
    @SerializedName("__typename") var _typename: String? = null,
    @SerializedName("lead") var lead: Lead? = Lead(),
)

data class Availability(
    @SerializedName("__typename") var _typename: String? = null,
    @SerializedName("available") var available: Boolean? = null,
    @SerializedName("minRoomsLeft") var minRoomsLeft: Int? = null,
)

data class PropertyImage(
    @SerializedName("__typename") var _typename: String? = null,
    @SerializedName("fallbackImage") var fallbackImage: String? = null,
    @SerializedName("image") var image: Image? = Image(),
    @SerializedName("subjectId") var subjectId: Int? = null,
)

data class Image(
    @SerializedName("__typename") var _typename: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("url") var url: String? = null,
)

data class Reviews(
    @SerializedName("__typename") var _typename: String? = null,
    @SerializedName("score") var score: Double? = null,
    @SerializedName("total") var total: Int? = null,
)

data class Lead(
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("formatted") var formattedAmount: String? = null,
)


