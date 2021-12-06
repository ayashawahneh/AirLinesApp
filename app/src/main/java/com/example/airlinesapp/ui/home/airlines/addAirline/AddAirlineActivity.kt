package com.example.airlinesapp.ui.home.airlines.addAirline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.ActivityAddAirlineBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.ui.home.HomeActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class AddAirlineActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityAddAirlineBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AddAirlineViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_airline)
        binding.model = viewModel


        nameObserving()
        countryObserving()
        headQuarterObserving()
        establishedObserving()
        websiteObserving()
        sloganObserving()

        buttonEnableObserving()
        buttonClickEvent()
    }

    private fun buttonClickEvent() {
        binding.submitButton.setOnClickListener {
            viewModel.addAirline()
            startActivity(newIntent(this, viewModel.name.value.toString()))
        }
    }

    private fun buttonEnableObserving() {
        viewModel.enableSubmitButton.observe(this) {
            binding.submitButton.isEnabled = it
        }
    }

    private fun nameObserving() {
        viewModel.name.observe(this) {
            binding.nameContainer.helperText = viewModel.validateRequiredFields(it)
            viewModel.setEnableSubmitButton()
        }
    }

    private fun countryObserving() {
        viewModel.country.observe(this) {
            binding.countryContainer.helperText = viewModel.validateRequiredFields(it)
            viewModel.setEnableSubmitButton()
        }
    }

    private fun headQuarterObserving() {
        viewModel.headQuarter.observe(this) {
            binding.headQuarterContainer.helperText = viewModel.validateRequiredFields(it)
            viewModel.setEnableSubmitButton()
        }
    }

    private fun establishedObserving() {
        viewModel.established.observe(this) {
            binding.establishedContainer.helperText = viewModel.validateEstablished()
            viewModel.setEnableSubmitButton()
        }
    }

    private fun websiteObserving() {
        viewModel.website.observe(this) {
            binding.websiteContainer.helperText = viewModel.validateWebsite()
            viewModel.setEnableSubmitButton()
        }
    }

    private fun sloganObserving() {
        viewModel.slogan.observe(this) {
            binding.sloganContainer.helperText = viewModel.validateSlogan()
            viewModel.setEnableSubmitButton()
        }
    }

    companion object {

        private val EXTRA_Add_Airline = AddAirlineActivity::class.java.name + "_Add_Airline_EXTRA"
        fun newIntent(context: Context, airlineName: String) =
            Intent(context, HomeActivity::class.java)
                .putExtra(EXTRA_Add_Airline, airlineName)
    }
}

