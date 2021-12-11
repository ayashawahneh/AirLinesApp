package com.example.airlinesapp.ui.home.airlines

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    private lateinit var airlinesRecyclerViewAdapter: AirlinesRecyclerViewAdapter
    private var _binding: FragmentAirlinesBinding? = null
    val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAirlinesBinding.bind(view)
        initRecyclerView()
        initViewModel()
        setupAddFloatingButton()

    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        airlinesRecyclerViewAdapter = AirlinesRecyclerViewAdapter()
        binding.recyclerView.adapter = airlinesRecyclerViewAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel() {
        airlinesViewModel.liveDataList.observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    airlinesRecyclerViewAdapter.setUpdatedData(t)
                    binding.progressBar.visibility = View.GONE
                    //  airlinesViewModel.isLoading.value = false
                    airlinesRecyclerViewAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this.context, "Error getting Airlines", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        airlinesViewModel.getAirlinesList()
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
