package com.example.airlinesapp.ui.home.passengers

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentPassengersBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.ui.home.HomeActivity
import com.example.airlinesapp.ui.home.passengers.editPassenger.EditPassengerActivity
import com.example.airlinesapp.util.Constants.EMPTY_LIST
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
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
        (activity as HomeActivity).launcher.launch(
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
            }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPassengersBinding.bind(view)
        setHasOptionsMenu(true)

        setActionBar()
        setupView()
        networkStateObserving()
        observingPassengersList()
        observingPassengerDeleted()
        searchTextObserving()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        configureSearchView(menu)
    }

    @SuppressLint("CheckResult")
    private fun configureSearchView(menu: Menu) {
        val searchView = menu.findItem(R.id.search_view)?.actionView as SearchView

        /* ----------- we can use rxjava too ----------- */
        /*  searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).textChanges()
            .skipInitialValue()
            .debounce(500, TimeUnit.MILLISECONDS)
            .doOnNext {
                if (it.trim().toString().length >= 3)
                    passengersViewModel.searchText.postValue(it.toString())
            }
            .filter {
                it.trim().toString().length >= 3
            }

            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({

            }, {
            })   */

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.length > 2) {
                    passengersViewModel.searchText.value = newText

                } else {
                    if (!passengersViewModel.searchText.value.isNullOrEmpty())
                        passengersViewModel.searchText.value = ""
                }

                return true
            }
        })
    }

    private fun searchTextObserving() {
        passengersViewModel.searchText.observe(viewLifecycleOwner) {
            passengersViewModel.search()
        }
    }

    private fun setActionBar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.passengers)
    }

    private fun setupView() {
        binding.viewModel = passengersViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        passengersAdapter =
            PassengersRecyclerViewPagingAdapter(goToEditPassenger, showDialogConfirmation)
        passengersAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && passengersAdapter.itemCount < 1)
                passengersViewModel.networkState.value = EMPTY_LIST
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = passengersAdapter.withLoadStateHeaderAndFooter(
                header = PassengersLoadStateAdapter(),
                footer = PassengersLoadStateAdapter()
            )
        }
    }

    private fun networkStateObserving() {
        passengersViewModel.networkState.observe(viewLifecycleOwner) {
            Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun observingPassengersList() {
        passengersViewModel.passengersList.observe(viewLifecycleOwner) {
            passengersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun observingPassengerDeleted() {
        passengersViewModel.isDeleted.observe(viewLifecycleOwner) {
            if (it) {
                passengersAdapter.refresh()
                Toast.makeText(
                    this.requireContext(),
                    resources.getString(R.string.deleted_successfully),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else
                Toast.makeText(
                    this.requireContext(),
                    resources.getString(R.string.error_deleting),
                    Toast.LENGTH_SHORT
                )
                    .show()
        }
    }
}