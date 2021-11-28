package com.example.airlinesapp.ui.home.airlines

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentAirlinesBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AirlinesFragment : DaggerFragment(R.layout.fragment_airlines) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val airlinesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AirlinesViewModel::class.java)
    }
    private lateinit var airlinesListAdapter: AirlinesRecyclerViewListAdapter
    private lateinit var binding: FragmentAirlinesBinding
    private lateinit var favoritesIdsList: MutableList<String>
    private val saveIdInAirlinesFavoriteList: (String) -> Unit = {
        favoritesIdsList.add(it)
        airlinesViewModel.favoriteAirlinesList.value = favoritesIdsList
    }
    private val removeIdFromAirlinesFavoriteList: (String) -> Unit = {
        favoritesIdsList.remove(it)
        airlinesViewModel.favoriteAirlinesList.value = favoritesIdsList
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAirlinesBinding.bind(view)
        setHasOptionsMenu(true)

        setActionBar()
        setAdapter()
        setupView()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        configureSearchView(menu)
        isLoadingObserving(menu)
    }

    private fun configureSearchView(menu: Menu) {
        val searchView = menu.findItem(R.id.search_view)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty() && newText.length > 2) {
                    airlinesViewModel.searchText.value = newText
                } else {
                    if(!airlinesViewModel.searchText.value.isNullOrEmpty())
                        airlinesViewModel.searchText.value = ""
                }
                return true
            }
        })
    }

    private fun isLoadingObserving(menu: Menu) {
        airlinesViewModel.isLoading.observe(viewLifecycleOwner) {
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
        favoritesIdsList = airlinesViewModel.favoriteAirlinesList.value!!
        binding.model = airlinesViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = airlinesListAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel() {
        with(airlinesViewModel) {
            this.airlinesLiveData.observe(viewLifecycleOwner) {
                airlinesListAdapter.submitList(it)
            }

            this.mappedSearchedText.observe(viewLifecycleOwner) {
                airlinesListAdapter.submitList(it)
            }
            this.favoriteAirlinesList.observe(viewLifecycleOwner) {
                this.updateAirlineIdsInDataStore()
            }
        }
    }

}