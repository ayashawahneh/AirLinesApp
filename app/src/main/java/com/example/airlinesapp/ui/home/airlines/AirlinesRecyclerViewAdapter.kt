package com.example.airlinesapp.ui.home.airlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.airlinesapp.databinding.SingleAirlineViewBinding
import com.example.airlinesapp.models.AirLine

class AirlinesRecyclerViewAdapter :
    RecyclerView.Adapter<AirlinesRecyclerViewAdapter.AirlineViewHolder>() {

    private var airlinesList: List<AirLine>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirlineViewHolder {
        val view = SingleAirlineViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AirlineViewHolder(view)
    }

    override fun onBindViewHolder(holder: AirlineViewHolder, position: Int) {
        val airline = airlinesList?.get(position)!!
        holder.view.airline = airline
        holder.view.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (airlinesList == null) 0
        else airlinesList?.size!!
    }

    fun setUpdatedData(airlineListData: List<AirLine>) {
        this.airlinesList = airlineListData
    }

    class AirlineViewHolder(val view: SingleAirlineViewBinding) : RecyclerView.ViewHolder(view.root)
}