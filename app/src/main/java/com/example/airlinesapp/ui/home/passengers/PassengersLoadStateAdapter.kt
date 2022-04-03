package com.example.airlinesapp.ui.home.passengers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.NetworkStateItemBinding

class PassengersLoadStateAdapter :
    LoadStateAdapter<PassengersLoadStateAdapter.PassengerLoadStateViewHolder>() {

    class PassengerLoadStateViewHolder(
        private val binding: NetworkStateItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                binding.progressBar.visibility = View.VISIBLE
            }
            if (loadState is LoadState.Error) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = loadState.error.localizedMessage
            }
        }
    }

    override fun onBindViewHolder(holder: PassengerLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = PassengerLoadStateViewHolder(
        NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )
}