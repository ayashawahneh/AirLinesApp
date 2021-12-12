package com.example.airlinesapp.ui.home.passengers.addPassenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.ActivityAddPassengerBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.ui.home.airlines.AirlinesViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.widget.AdapterView.OnItemClickListener
import com.example.airlinesapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_add_passenger.view.*

class AddPassengerActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityAddPassengerBinding
    private lateinit var arrayAdapter: ArrayAdapter<AirLine>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val airlinesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AirlinesViewModel::class.java)
    }
    private val addPassengerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AddPassengerViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_passenger)
        binding.model = addPassengerViewModel

        fillAirlineDropdownList()
        passengerNameObserving()
        airlineDropdownListObserving()
        buttonEnableObserving()
        buttonClickEvent()
    }

    private fun fillAirlineDropdownList() {
        airlinesViewModel.airlinesLiveData.observe(this,
            { airlinesList ->
                if (airlinesList != null) {
                    arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, airlinesList)
                    binding.airlineAutoCompleteTextView.setAdapter(arrayAdapter)
                    binding.progressBar.visibility = View.GONE

                    binding.airlineNameContainer.airlineAutoCompleteTextView.onItemClickListener =
                        OnItemClickListener { _, _, position, _ ->
                            val selectedValue: AirLine? = arrayAdapter.getItem(position)
                            addPassengerViewModel.airlineObject.value = selectedValue
                        }
                } else {
                    Toast.makeText(this, "Error getting Airlines", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        airlinesViewModel.getAirlinesList()
    }

    private fun buttonEnableObserving() {
        addPassengerViewModel.enableSubmitButton.observe(this) {
            binding.submitButton.isEnabled = it
        }
    }

    private fun airlineDropdownListObserving() {
        addPassengerViewModel.airlineName.observe(this) {
            binding.airlineNameContainer.helperText = addPassengerViewModel.validateAirlineName()
            addPassengerViewModel.setEnableSubmitButton()
        }
    }

    private fun passengerNameObserving() {
        addPassengerViewModel.passengerName.observe(this) {
            binding.passengerNameContainer.helperText =
                addPassengerViewModel.validatePassengerName()
            addPassengerViewModel.setEnableSubmitButton()
        }
    }

    private fun buttonClickEvent() {
        binding.submitButton.setOnClickListener {
            if (addPassengerViewModel.addPassenger()) {
                startActivity(newIntent(this, addPassengerViewModel.passengerName.value.toString()))
            } else {
                Toast.makeText(this, "Error sending data, try again later!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {

        private val EXTRA_Add_Passenger =
            AddPassengerActivity::class.java.name + "_Add_Passenger_EXTRA"

        fun newIntent(context: Context, passengerName: String) =
            Intent(context, HomeActivity::class.java)
                .putExtra(EXTRA_Add_Passenger, passengerName)
    }

}