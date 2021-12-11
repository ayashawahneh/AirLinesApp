package com.example.airlinesapp.ui.home.passengers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentPassengersBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.ui.home.passengers.addPassenger.AddPassengerActivity
import com.example.airlinesapp.util.Constants.LOADING
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
    val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPassengersBinding.bind(view)

        setupView()
        observingPassengersList()
        setupAddFloatingButton()

    }

    private fun setupAddFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            startActivity(newIntent(this.requireContext()))
        }
    }

    private fun observingPassengersList() {
        passengersViewModel.passengersList.observe(viewLifecycleOwner) {
            passengersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        passengersViewModel.progressBarStatus?.observe(viewLifecycleOwner) {
            if (it == LOADING)
                binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupView() {
        passengersAdapter = PassengersRecyclerViewPagingAdapter(this.requireContext())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = passengersAdapter.withLoadStateHeaderAndFooter(
                header = PassengersLoadStateAdapter(),
                footer = PassengersLoadStateAdapter()
            )
        }
    }

    companion object {

        fun newIntent(context: Context) =
            Intent(context, AddPassengerActivity::class.java)

    }
}