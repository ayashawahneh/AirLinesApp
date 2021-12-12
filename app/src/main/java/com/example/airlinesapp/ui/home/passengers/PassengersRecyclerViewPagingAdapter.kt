package com.example.airlinesapp.ui.home.passengers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.SinglePassengerViewBinding
import com.example.airlinesapp.models.Passenger
import com.chauthai.swipereveallayout.ViewBinderHelper
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.airlinesapp.ui.home.passengers.editPassenger.EditPassengerActivity


class PassengersRecyclerViewPagingAdapter(context: Context) :
    PagingDataAdapter<Passenger, PassengersRecyclerViewPagingAdapter.PassengersViewHolder>(
        PassengersComparator
    ) {
    private val viewBinderHelper = ViewBinderHelper()
    private lateinit var view : SinglePassengerViewBinding

    override fun onBindViewHolder(holder: PassengersViewHolder, position: Int) {
        viewBinderHelper.setOpenOnlyOne(true);
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
        return PassengersViewHolder(view)
    }

    class PassengersViewHolder(private val view: SinglePassengerViewBinding) :
        RecyclerView.ViewHolder(view.root) {
        private val EXTRA_Edit_Passenger =
            EditPassengerActivity::class.java.name + "_Edit_Passenger_EXTRA"

        fun bind(item: Passenger) = with(view) {
            passenger = item

            with(editButton) {
                setOnClickListener {
                    Log.d("Pas", "edit is clicked"+EXTRA_Edit_Passenger)
                    val intent = Intent(context, EditPassengerActivity::class.java)
                    intent.putExtra(EXTRA_Edit_Passenger,passenger)
                    context.startActivity(intent)
                }
            }

            with(deleteButton) {
                setOnClickListener {
                    Log.d("Pas", "delete is clicked")
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