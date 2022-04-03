package com.example.airlinesapp.ui.home.passengers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.SinglePassengerViewBinding
import com.example.airlinesapp.di.network.models.Passenger
import com.chauthai.swipereveallayout.ViewBinderHelper

class PassengersRecyclerViewPagingAdapter(
    private val editListener: (Passenger) -> Unit,
    private val deleteListener: (Passenger) -> Unit
) :
    PagingDataAdapter<Passenger, PassengersRecyclerViewPagingAdapter.PassengersViewHolder>(
        PassengersComparator
    ) {
    private val viewBinderHelper = ViewBinderHelper()
    private lateinit var view: SinglePassengerViewBinding

    override fun onBindViewHolder(holder: PassengersViewHolder, position: Int) {
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(view.swipeLayout, getItem(position)?.id)
        viewBinderHelper.closeLayout(getItem(position)?.id)
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengersViewHolder {
        view = SinglePassengerViewBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        return PassengersViewHolder(view, editListener, deleteListener)
    }

    class PassengersViewHolder(
        private val view: SinglePassengerViewBinding,
        private val editListener: (Passenger) -> Unit,
        private val deleteListener: (Passenger) -> Unit
    ) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: Passenger) = with(view) {
            passenger = item

            with(editButton) {
                setOnClickListener {
                    editListener(item)
                }
            }

            with(deleteButton) {
                setOnClickListener {
                    deleteListener(item)
                }
            }
        }
    }

    object PassengersComparator : DiffUtil.ItemCallback<Passenger>() {
        override fun areItemsTheSame(oldItem: Passenger, newItem: Passenger) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Passenger, newItem: Passenger) =
            oldItem == newItem
    }
}