package com.example.airlinesapp.ui.home.passengers

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentPassengersBinding
import com.example.airlinesapp.di.NetworkState
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PassengersFragment : DaggerFragment(R.layout.fragment_passengers) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val passengersViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(PassengersViewModel::class.java)
    }

    private lateinit var passengersAdapter: PassengersRecyclerViewPagingAdapter
    private var _binding: FragmentPassengersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPassengersBinding.bind(view)
        setupView()

        passengersViewModel.progressBarStatus?.observe(viewLifecycleOwner) {
            if (it == NetworkState.LOADING)
                binding.progressBar.visibility = View.VISIBLE
            if (it == NetworkState.LOADED) binding.progressBar.visibility = View.GONE
        }

        passengersViewModel.passengersList.observe(viewLifecycleOwner) {
            passengersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun setupView() {
        passengersAdapter = PassengersRecyclerViewPagingAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = passengersAdapter
        }
    }

}