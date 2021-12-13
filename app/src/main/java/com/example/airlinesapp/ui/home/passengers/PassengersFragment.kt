package com.example.airlinesapp.ui.home.passengers

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentPassengersBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.ui.home.passengers.addPassenger.AddPassengerActivity
import com.example.airlinesapp.ui.home.passengers.editPassenger.EditPassengerActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    private val goToEditPassenger: (Passenger) -> Unit = {
        startActivity(
            EditPassengerActivity.newIntentWithPassengerExtra(
                this.requireContext(),
                it
            )
        )
    }
    private val showDialogConfirmation: (Passenger) -> Unit = {
        MaterialAlertDialogBuilder(this.requireContext())
            .setTitle(resources.getString(R.string.delete_dialog_title))
            .setMessage(resources.getString(R.string.delete_dialog_message))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(
                resources.getString(R.string.confirm)
            ) { _, _ ->
                passengersViewModel.deletePassenger(it.id)
                passengersAdapter.deletedItem.value = it
            }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPassengersBinding.bind(view)

        setupView()
        observingPassengersList()
        observingPassengerDeleted()
        setupAddFloatingButton()
    }

    private fun setupView() {
        binding.viewModel = passengersViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        passengersAdapter =
            PassengersRecyclerViewPagingAdapter(goToEditPassenger, showDialogConfirmation)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = passengersAdapter.withLoadStateHeaderAndFooter(
                header = PassengersLoadStateAdapter(),
                footer = PassengersLoadStateAdapter()
            )
        }
    }

    private fun observingPassengersList() {
        passengersViewModel.passengersList.observe(viewLifecycleOwner) {
            passengersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observingPassengerDeleted() {
        passengersViewModel.isDeleted.observe(viewLifecycleOwner) {

            if (it) {
                passengersAdapter.deleteItem()
                passengersAdapter.notifyDataSetChanged()
                Toast.makeText(
                    this.requireContext(),
                    "Passenger data deleted successfully.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else
                Toast.makeText(
                    this.requireContext(),
                    "Error deleting, try again later!",
                    Toast.LENGTH_SHORT
                )
                    .show()
        }
    }

    private fun setupAddFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            startActivity(AddPassengerActivity.newIntent(this.requireContext()))
        }
    }
}