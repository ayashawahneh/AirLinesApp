package com.example.airlinesapp.ui.home.airlines

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.SingleAirlineViewBinding
import com.example.airlinesapp.models.AirlineWithFavoriteFlagItem

class AirlinesRecyclerViewListAdapter(
    private val addToFavoriteListener: (String, Int) -> Unit,
    private val removeFromFavoriteListener: (String, Int) -> Unit
) :
    ListAdapter<AirlineWithFavoriteFlagItem, AirlinesRecyclerViewListAdapter.AirlineViewHolder>(
        UserDiffCallBack()
    ) {

    class AirlineViewHolder(
        private val viewBinding: SingleAirlineViewBinding,
        private val addToFavoriteListener: (String, Int) -> Unit,
        private val removeFromFavoriteListener: (String, Int) -> Unit
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun onBind(item: AirlineWithFavoriteFlagItem, position: Int) {
            with(viewBinding) {
                favoriteAirline = item
                with(iconFavorite) {
                    setOnClickListener {
                        if ((it as CheckBox).isChecked) {
                            //I filtered the list and remove items with id = null
                            addToFavoriteListener(item.id!!, position)
                        } else {
                            removeFromFavoriteListener(item.id!!, position)
                        }
                    }
                }
            }
        }
    }

    private class UserDiffCallBack : DiffUtil.ItemCallback<AirlineWithFavoriteFlagItem>() {
        override fun areItemsTheSame(
            oldItem: AirlineWithFavoriteFlagItem,
            newItem: AirlineWithFavoriteFlagItem
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: AirlineWithFavoriteFlagItem,
            newItem: AirlineWithFavoriteFlagItem
        ): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirlineViewHolder {
        val view = SingleAirlineViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AirlineViewHolder(view, addToFavoriteListener, removeFromFavoriteListener)
    }

    override fun onBindViewHolder(holder: AirlineViewHolder, position: Int) {
        getItem(position).let { holder.onBind(it, position) }
    }
}