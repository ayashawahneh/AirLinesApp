package com.example.airlinesapp.ui.home.airlines

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentAirlinesBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.ui.home.airlines.add.AddAirlineActivity
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AirlinesFragment : DaggerFragment(R.layout.fragment_airlines) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AirlinesViewModel::class.java)
    }
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var airlinesListAdapter: AirlinesRecyclerViewListAdapter
    private lateinit var binding: FragmentAirlinesBinding
    private lateinit var favoritesIdsList: MutableList<String>
    private val saveIdInAirlinesFavoriteList: (String, Int) -> Unit =
        { id, position ->
            viewModel.airlinesListLiveData.value?.get(position)?.isFavorite = true
            favoritesIdsList.add(id)
            viewModel.saveIdInFavoriteAirlinesList(favoritesIdsList)
            viewModel.updateItemFavoriteFlagInTheCurrentList(position, true)
        }
    private val removeIdFromAirlinesFavoriteList: (String, Int) -> Unit =
        { id, position ->
            favoritesIdsList.remove(id)
            viewModel.removeIdFromAirlinesFavoriteList(favoritesIdsList)
            viewModel.updateItemFavoriteFlagInTheCurrentList(position, false)
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAirlinesBinding.bind(view)
        setHasOptionsMenu(true)

        setActionBar()
        setAdapter()
        setupView()
        setViewModel()
        launchActivity()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        configureSearchView(menu)
        loadingObserving(menu)
    }

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
                    viewModel.search("")
                }
                return true
            }
        })
    }

    private fun loadingObserving(menu: Menu) {
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) {
            menu.findItem(R.id.search_view).isEnabled = !it
        }
    }

    private fun setActionBar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.airlines)
    }

    private fun setAdapter() {
        airlinesListAdapter = AirlinesRecyclerViewListAdapter(
            saveIdInAirlinesFavoriteList,
            removeIdFromAirlinesFavoriteList
        )
    }

    private fun setupView() {
        favoritesIdsList = viewModel.favoriteAirlinesList.toMutableList()
        with(binding) {
            model = viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = airlinesListAdapter
            }
            floatingActionButton.setOnClickListener {
                launcher.launch(AddAirlineActivity.newIntent(this@AirlinesFragment.requireContext()))
            }
            reloadButton.setOnClickListener { viewModel.refreshList() }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel() {
        with(viewModel) {
            airlinesListLiveData.observe(viewLifecycleOwner) {
                airlinesListAdapter.submitList(it) {
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        }
    }

    private fun launchActivity() {
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == DaggerAppCompatActivity.RESULT_OK) {
                val intent = result.data
                Snackbar.make(
                    binding.frameLayout,
                    intent?.getStringExtra(EXTRA_AIRLINES).toString(),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(
                    ContextCompat.getColor(
                        this.requireContext(),
                        R.color.successGreen
                    )
                )
                    .show()
            }
            //viewModel.refreshList()
        }
    }

    companion object {

        private val EXTRA_AIRLINES = AirlinesFragment::class.java.name + "_EXTRA_AIRLINES"

        fun newIntentWithStringExtra(context: Context, stringExtra: String) =
            Intent(context, AirlinesFragment::class.java)
                .putExtra(EXTRA_AIRLINES, stringExtra)
    }
}