package com.example.airlinesapp.ui.home.passengers

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.SinglePassengerViewBinding
import com.example.airlinesapp.models.Passenger

class PassengersRecyclerViewPagingAdapter :
    PagingDataAdapter<Passenger, PassengersRecyclerViewPagingAdapter.PassengersViewHolder>(
        PassengersComparator
    ) {

    override fun onBindViewHolder(holder: PassengersViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): PassengersViewHolder {
        val view = SinglePassengerViewBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        return PassengersViewHolder(view)
    }

    class PassengersViewHolder(val view: SinglePassengerViewBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: Passenger) = with(view) {
            passenger = item
        }
    }

    object PassengersComparator : DiffUtil.ItemCallback<Passenger>() {
        override fun areItemsTheSame(oldItem: Passenger, newItem: Passenger) =
            oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: Passenger, newItem: Passenger) =
            oldItem == newItem
    }
}