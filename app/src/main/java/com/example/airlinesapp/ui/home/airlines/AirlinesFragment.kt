package com.example.airlinesapp.ui.home.airlines

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
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
    private var _binding: FragmentAirlinesBinding? = null
    val binding get() = _binding!!
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
        _binding = FragmentAirlinesBinding.bind(view)
        setHasOptionsMenu(true)

        setActionBar()
        setupView()
        observingAirlinesList()
        networkStateObserving()
        favoriteAirlinesListObserving()
        searchTextObserving()
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
                if (newText != null && newText.length > 2) {
                    airlinesViewModel.searchText.value = newText
                }else{
                    airlinesViewModel.searchText.value = ""
                }
                return true
            }
        })
    }

    private fun isLoadingObserving(menu: Menu) {
        airlinesViewModel.isLoading.observe(viewLifecycleOwner){
            menu.findItem(R.id.search_view).isEnabled = !it
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchTextObserving() {
        airlinesViewModel.searchText.observe(viewLifecycleOwner) {
            //if text change -----> call search function on VM --> update searchList --> give the new list to the adapter
            if (!it.equals("")) {
                airlinesViewModel.search()
                airlinesListAdapter.submitList(airlinesViewModel.searchedAirlinesList.value)
                airlinesListAdapter.notifyDataSetChanged()
            } else {
                //binding.recyclerView.scrollToPosition(0)
                airlinesListAdapter.submitList(airlinesViewModel.airlinesLiveData.value)
                airlinesListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setActionBar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.airlines)
    }

    private fun setupView() {
        favoritesIdsList = airlinesViewModel.favoriteAirlinesList.value!!
        binding.model = airlinesViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        airlinesListAdapter = AirlinesRecyclerViewListAdapter(
            saveIdInAirlinesFavoriteList,
            removeIdFromAirlinesFavoriteList
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = airlinesListAdapter
        }
    }

    private fun observingAirlinesList() {
        airlinesViewModel.airlinesLiveData.observe(viewLifecycleOwner) {
            airlinesListAdapter.submitList(it)
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun favoriteAirlinesListObserving() {
        airlinesViewModel.favoriteAirlinesList.observe(viewLifecycleOwner) {
            airlinesViewModel.updateAirlineIdsInDataStore()
        }
    }

    private fun networkStateObserving() {
        airlinesViewModel.networkState.observe(viewLifecycleOwner) {
            Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT)
                .show()
        }
    }
}