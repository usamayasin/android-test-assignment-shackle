package com.example.shacklehotelbuddy.data.mapper

import com.example.shacklehotelbuddy.data.local.entity.SearchQueryEntity
import com.example.shacklehotelbuddy.data.remote.model.*

object Mapper {

    fun SearchQueryEntity.mapToUiModel(): PropertiesRequestBody {
        return PropertiesRequestBody(
            checkInDate = parseDate(this.checkInDate),
            checkOutDate = parseDate(this.checkOutDate),
            rooms = arrayListOf<Rooms>().apply {
                add(Rooms(
                    adults = this@mapToUiModel.adults,
                    children = arrayListOf<Children>().apply {
                        add(Children(
                            age = this@mapToUiModel.children
                        ))
                    })
                )
            })
    }


    fun List<Properties>.mapToUiModelOfProperties(): List<SingleProperty> {
        val list: MutableList<SingleProperty> = ArrayList()
        this.map {
            SingleProperty(
                name = it.name ?: "No Name Found",
                id = it.id ?: "",
                propertyImage = it.propertyImage?.image?.url ?: "",
                rating = it.reviews?.score ?: 0.0,
                price = it.price?.lead?.formattedAmount ?: "$0"
            )
        }.run {
            list.addAll(this)
        }
        return list
    }

    private fun parseDate(date: String): Date {
        val splits = date.split("/")
        return Date(
            day = splits[0].toInt(),
            month = splits[1].toInt(),
            year = splits[2].toInt()
        )
    }
}