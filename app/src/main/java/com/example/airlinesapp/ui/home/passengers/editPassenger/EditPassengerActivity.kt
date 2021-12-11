package com.example.airlinesapp.ui.home.passengers.editPassenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.ActivityEditPassengerBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.ui.home.HomeActivity
import com.example.airlinesapp.ui.home.airlines.AirlinesViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_passenger.view.*
import javax.inject.Inject

class EditPassengerActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityEditPassengerBinding
    private lateinit var arrayAdapter: ArrayAdapter<AirLine>
    val passenger by lazy {
        intent.getParcelableExtra<Passenger>(EXTRA_Edit_Passenger)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val airlinesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AirlinesViewModel::class.java)
    }
    private val editPassengerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(EditPassengerViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_passenger)
        binding.model = editPassengerViewModel

        fillFieldsWithPassengerData()
        fillAirlineDropdownList()
        passengerNameObserving()
        airlineDropdownListObserving()
        buttonEnableObserving()
        buttonClickEvent()
    }

    private fun fillFieldsWithPassengerData() {
        editPassengerViewModel.passengerId.value = passenger?.id
        editPassengerViewModel.passengerName.value = passenger?.name
        editPassengerViewModel.trips.value = passenger?.trips.toString()
        editPassengerViewModel.airlineName.value = passenger?.airline?.get(0)?.toString()
        editPassengerViewModel.airlineObject.value = passenger?.airline?.get(0)
    }

    private fun fillAirlineDropdownList() {
        airlinesViewModel.liveDataList.observe(this,
            { airlinesList ->
                if (airlinesList != null) {
                    arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, airlinesList)
                    binding.airlineAutoCompleteTextView.setAdapter(arrayAdapter)
                    binding.progressBar.visibility = View.GONE
                    binding.airlineNameContainer.airlineAutoCompleteTextView.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, position, _ ->
                            val selectedValue: AirLine? = arrayAdapter.getItem(position)
                            editPassengerViewModel.airlineObject.value = selectedValue
                        }
                } else {
                    Toast.makeText(this, "Error getting Airlines", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        airlinesViewModel.getAirlinesList()
    }

    private fun buttonEnableObserving() {
        editPassengerViewModel.enableSubmitButton.observe(this) {
            binding.submitButton.isEnabled = it
        }
    }

    private fun airlineDropdownListObserving() {
        editPassengerViewModel.airlineName.observe(this) {
            binding.airlineNameContainer.helperText = editPassengerViewModel.validateAirlineName()
            editPassengerViewModel.setEnableSubmitButton()
        }
    }

    private fun passengerNameObserving() {
        editPassengerViewModel.passengerName.observe(this) {
            binding.passengerNameContainer.helperText =
                editPassengerViewModel.validatePassengerName()
            editPassengerViewModel.setEnableSubmitButton()
        }
    }

    private fun buttonClickEvent() {
        binding.submitButton.setOnClickListener {
            if (editPassengerViewModel.editPassenger()) {
                startActivity(
                    newIntent(
                        this,
                        editPassengerViewModel.passengerName.value.toString()
                    )
                )
            } else {
                Toast.makeText(this, "Error sending data, try again later!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {

        private val EXTRA_Edit_Passenger =
            EditPassengerActivity::class.java.name + "_Edit_Passenger_EXTRA"

        fun newIntent(context: Context, passengerName: String) =
            Intent(context, HomeActivity::class.java)
                .putExtra(EXTRA_Edit_Passenger, passengerName)
    }
}