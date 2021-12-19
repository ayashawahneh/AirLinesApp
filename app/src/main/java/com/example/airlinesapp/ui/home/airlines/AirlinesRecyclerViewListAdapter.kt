package com.example.airlinesapp.ui.home.airlines

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.SingleAirlineViewBinding
import com.example.airlinesapp.models.AirlineWithFavoriteFlag

class AirlinesRecyclerViewListAdapter(
    private val addToFavoriteListener: (String) -> Unit,
    private val removeFromFavoriteListener: (String) -> Unit
) :
    ListAdapter<AirlineWithFavoriteFlag, AirlinesRecyclerViewListAdapter.AirlineViewHolder>(
        UserDiffCallBack()
    ) {

    class AirlineViewHolder(
        val view: SingleAirlineViewBinding,
        private val addToFavoriteListener: (String) -> Unit,
        private val removeFromFavoriteListener: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(view.root) {

        fun onBind(item: AirlineWithFavoriteFlag) {
            with(view) {  
                favoriteAirline = item

                with(iconFavorite) {
                    if (item.airline.id != null) {
                        setOnClickListener {
                            if ((it as CheckBox).isChecked) {
                                addToFavoriteListener(item.airline.id)
                            }
                            else run {
                                removeFromFavoriteListener(item.airline.id)
                            }
                        }
                    } else {
                        this.isEnabled = false
                    }
                }
            }
        }
    }

    private class UserDiffCallBack : DiffUtil.ItemCallback<AirlineWithFavoriteFlag>() {
        override fun areItemsTheSame(
            oldItem: AirlineWithFavoriteFlag,
            newItem: AirlineWithFavoriteFlag
        ): Boolean =
            oldItem.airline.id == newItem.airline.id

        override fun areContentsTheSame(
            oldItem: AirlineWithFavoriteFlag,
            newItem: AirlineWithFavoriteFlag
        ): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirlineViewHolder {
        val view = SingleAirlineViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AirlineViewHolder(view, addToFavoriteListener, removeFromFavoriteListener)
    }

    override fun onBindViewHolder(holder: AirlineViewHolder, position: Int) {
        getItem(position).let { holder.onBind(it) }
    }
}