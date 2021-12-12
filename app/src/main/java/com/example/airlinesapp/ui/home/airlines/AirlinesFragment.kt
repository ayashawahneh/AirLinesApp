package com.example.airlinesapp.ui.home.airlines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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

        setupView()
        observingAirlinesList()
        setupAddFloatingButton()
    }

    private fun setupView() {
        airlinesListAdapter = AirlinesRecyclerViewListAdapter()
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

    private fun setupAddFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            startActivity(newIntent(this.requireContext()))
        }
    }

    companion object {

        fun newIntent(context: Context) =
            Intent(context, AddAirlineActivity::class.java)
    }
}
