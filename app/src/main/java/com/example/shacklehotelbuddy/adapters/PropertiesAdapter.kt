package com.example.shacklehotelbuddy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shacklehotelbuddy.data.remote.model.SingleProperty
import com.example.shacklehotelbuddy.databinding.HotelItemLayoutBinding

class PropertiesAdapter(val onPropertyClicked: (property: SingleProperty) -> Unit) :
    RecyclerView.Adapter<PropertiesAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = HotelItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class PropertyViewHolder(private val itemBinding: HotelItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(image: SingleProperty) {
            itemBinding.apply {
                data = image
                cardHotel.setOnClickListener {
                    onPropertyClicked(image)
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<SingleProperty>() {

        override fun areItemsTheSame(oldItem: SingleProperty, newItem: SingleProperty): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SingleProperty, newItem: SingleProperty): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
}
