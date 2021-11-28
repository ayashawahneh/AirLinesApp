package com.example.airlinesapp.ui.home.airlines

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.FragmentAirlinesBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
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
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAirlinesBinding.bind(view)
        initRecyclerView()
        initViewModel()

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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
                    airlinesRecyclerViewAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this.context, "Error getting Data", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        airlinesViewModel.makeApiCall()
    }

}
