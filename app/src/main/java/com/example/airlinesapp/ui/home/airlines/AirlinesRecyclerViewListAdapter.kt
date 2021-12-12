package com.example.airlinesapp.ui.home.airlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.SingleAirlineViewBinding
import com.example.airlinesapp.models.AirLine

class AirlinesRecyclerViewListAdapter :
    ListAdapter<AirLine, AirlinesRecyclerViewListAdapter.AirlineViewHolder>(UserDiffCallBack()) {

    class AirlineViewHolder(val view: SingleAirlineViewBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun onBind(item: AirLine) {
            with(view) {
                airline = item
            }
        }
    }

    private class UserDiffCallBack : DiffUtil.ItemCallback<AirLine>() {
        override fun areItemsTheSame(oldItem: AirLine, newItem: AirLine): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AirLine, newItem: AirLine): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirlineViewHolder {
        val view = SingleAirlineViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AirlineViewHolder(view)
    }

    override fun onBindViewHolder(holder: AirlineViewHolder, position: Int) {
        getItem(position).let { holder.onBind(it) }
    }
}