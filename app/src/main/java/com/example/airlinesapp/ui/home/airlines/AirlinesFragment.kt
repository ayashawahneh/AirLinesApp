package com.example.airlinesapp.ui.home.airlines

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentAirlinesBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.ui.home.airlines.addAirline.AddAirlineActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAirlinesBinding.bind(view)

        setActionBar()
        setupView()
        observingAirlinesList()
        setupAddFloatingButton()
        networkStateObserving()
    }

    private fun setActionBar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.airlines)
    }

    private fun setupView() {
        binding.model = airlinesViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        airlinesListAdapter = AirlinesRecyclerViewListAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = airlinesListAdapter
        }
    }

    private fun networkStateObserving() {
        airlinesViewModel.networkState.observe(viewLifecycleOwner) {
            Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun observingAirlinesList() {
        airlinesViewModel.airlinesLiveData.observe(viewLifecycleOwner) {
            airlinesListAdapter.submitList(it)
        }
    }

    private fun setupAddFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            startActivity(AddAirlineActivity.newIntent(this.requireContext()))
        }
    }
}