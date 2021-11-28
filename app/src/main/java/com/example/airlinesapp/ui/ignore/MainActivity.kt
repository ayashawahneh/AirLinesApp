package com.example.airlinesapp.ui.ignore

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airlinesapp.R
import com.example.airlinesapp.ui.home.airlines.AirlinesRecyclerViewAdapter
import com.example.airlinesapp.databinding.ActivityMainBinding
import com.example.airlinesapp.ui.home.airlines.AirlinesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var airlinesRecyclerViewAdapter: AirlinesRecyclerViewAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var airlinesViewModel: AirlinesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView() {

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        airlinesRecyclerViewAdapter = AirlinesRecyclerViewAdapter()
        binding.recyclerView.adapter = airlinesRecyclerViewAdapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel() {
        airlinesViewModel = ViewModelProvider(this).get(AirlinesViewModel::class.java)
        airlinesViewModel.liveDataList.observe(this,
            { t ->
                if (t != null) {
                    airlinesRecyclerViewAdapter.setUpdatedData(t)
                    binding.progressBar.visibility = View.GONE
                    airlinesRecyclerViewAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@MainActivity, "Error getting Data", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        airlinesViewModel.makeApiCall()
    }
}