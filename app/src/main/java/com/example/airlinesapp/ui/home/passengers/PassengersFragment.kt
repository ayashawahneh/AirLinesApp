package com.example.airlinesapp.ui.home.passengers

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentPassengersBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.ui.home.passengers.addPassenger.AddPassengerActivity
import com.example.airlinesapp.ui.home.passengers.editPassenger.EditPassengerActivity
import com.example.airlinesapp.util.Constants.LOADING
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPassengersBinding.bind(view)

        setupView()
        observingPassengersList()
        setupAddFloatingButton()

    }

    private fun setupAddFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            startActivity(newIntentToAddPassengerActivity(this.requireContext()))
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
        passengersAdapter = PassengersRecyclerViewPagingAdapter(showDialogConfirmation)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = passengersAdapter.withLoadStateHeaderAndFooter(
                header = PassengersLoadStateAdapter(),
                footer = PassengersLoadStateAdapter()
            )
        }
    }

  private val showDialogConfirmation : (Passenger)-> Unit = {
        MaterialAlertDialogBuilder(this.requireContext())
            .setTitle(resources.getString(R.string.delete_dialog_title))
            .setMessage(resources.getString(R.string.delete_dialog_message))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss();
            }
            .setPositiveButton(
                resources.getString(R.string.confirm)
            ) { dialog, which ->
                // apply delete


                // return to pass fragment with response message
            }
            .show()
    }

    companion object {

        fun newIntentToAddPassengerActivity(context: Context) =
            Intent(context, AddPassengerActivity::class.java)
    }
}