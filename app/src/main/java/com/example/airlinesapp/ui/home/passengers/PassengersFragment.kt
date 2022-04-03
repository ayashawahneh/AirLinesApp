package com.example.airlinesapp.ui.home.passengers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentPassengersBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.di.network.models.Passenger
import com.example.airlinesapp.ui.home.passengers.addedit.AddEditPassengerActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_airlines.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PassengersFragment : DaggerFragment(R.layout.fragment_passengers) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(PassengersViewModel::class.java)
    }
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var passengersAdapter: PassengersRecyclerViewPagingAdapter
    private lateinit var binding: FragmentPassengersBinding

    private val goToEditPassenger: (Passenger) -> Unit = {
        launcher.launch(
            AddEditPassengerActivity.newIntentWithPassengerExtra(
                this.requireContext(),
                it
            )
        )
    }
    private val showDialogConfirmation: (Passenger) -> Unit = {
        if (it.id != null) {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle(resources.getString(R.string.delete_dialog_title))
                .setMessage(resources.getString(R.string.delete_dialog_message))
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(
                    resources.getString(R.string.confirm)
                ) { _, _ ->
                    viewModel.deletePassenger(it.id)
                }
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPassengersBinding.bind(view)
        setHasOptionsMenu(true)

        setActionBar()
        setupView()
        initViewModel()
        launchActivity()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        viewModel.setSearchItem(menu.findItem(R.id.search_view))
        val searchView = menu.findItem(R.id.search_view).actionView as SearchView
        val query = viewModel.searchTextLiveData.value.toString()
        (viewModel.searchItemLiveData.value as MenuItem)
            .setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    searchView.onActionViewExpanded()
                    searchView.setQuery(query, false)
                    searchView.clearFocus()
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?) = true
            })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        configureSearchView(menu)
    }

    @SuppressLint("CheckResult")
    private fun configureSearchView(menu: Menu) {
        val searchView = menu.findItem(R.id.search_view)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty() && newText.length >= 2) {
                    viewModel.search(newText)
                } else {
                    if (!viewModel.searchTextLiveData.value.isNullOrEmpty())
                        viewModel.search("")
                }
                return true
            }
        })

        if (!viewModel.searchTextLiveData.value.isNullOrEmpty()) {
            viewModel.searchItemLiveData.value?.expandActionView()
        }
    }

    private fun setActionBar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.passengers)
    }

    private fun setupView() {
        passengersAdapter =
            PassengersRecyclerViewPagingAdapter(goToEditPassenger, showDialogConfirmation)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                prepareAdapterSatesListeners()
                adapter = passengersAdapter.withLoadStateFooter(
                    footer = PassengersLoadStateAdapter()
                )
            }
            floatingActionButton.setOnClickListener {
                launcher.launch(
                    AddEditPassengerActivity.newIntentWithPassengerExtra(
                        this@PassengersFragment.requireContext(),
                        null
                    )
                )
            }
            reloadButton.setOnClickListener {
                viewModel.refresh()
            }

        }
    }

    private fun prepareAdapterSatesListeners() {
        passengersAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    viewModel.searchItemLiveData.value?.isEnabled = true
                    binding.isEmpty =
                        it.append.endOfPaginationReached && passengersAdapter.itemCount < 1
                    binding.isError = false
                    binding.isLoading = false
                }
                is LoadState.Loading -> {
                    viewModel.searchItemLiveData.value?.isEnabled = false
                    binding.isLoading = true
                    it.append.endOfPaginationReached && passengersAdapter.itemCount > 1
                    binding.isError = false
                    binding.isEmpty = false
                }
                is LoadState.Error -> {
                    if (passengersAdapter.itemCount < 1) {
                        binding.isError = true
                        binding.isEmpty = false
                        binding.isLoading = false
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            passengersListLiveData.observe(viewLifecycleOwner) {
                passengersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            isDeletedLiveData.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.deleted_successfully),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    passengersAdapter.refresh()
                    viewModel.refresh()
                } else
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.error_deleting),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    private fun launchActivity() {
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == DaggerAppCompatActivity.RESULT_OK) {
                Snackbar.make(
                    binding.frameLayout,
                    result.data?.getStringExtra(EXTRA_PASSENGERS).toString(),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(
                    ContextCompat.getColor(
                        this.requireContext(),
                        R.color.successGreen
                    )
                )
                    .show()
                passengersAdapter.refresh()
                viewModel.refresh()
            }
        }
    }

    companion object {

        private val EXTRA_PASSENGERS = PassengersFragment::class.java.name + "_EXTRA_PASSENGERS"

        fun newIntentWithStringExtra(context: Context, stringExtra: String) =
            Intent(context, PassengersFragment::class.java)
                .putExtra(EXTRA_PASSENGERS, stringExtra)
    }
}