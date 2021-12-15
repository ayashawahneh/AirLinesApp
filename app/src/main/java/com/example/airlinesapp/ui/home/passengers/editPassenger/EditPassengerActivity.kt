package com.example.airlinesapp.ui.home.passengers.editPassenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.airlinesapp.util.Constants.PASSENGER_RESULT_CODE
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_passenger.view.*
import javax.inject.Inject

class EditPassengerActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityEditPassengerBinding
    private lateinit var arrayAdapter: ArrayAdapter<AirLine>
    val passenger by lazy {
        intent.getParcelableExtra<Passenger>(EXTRA_EDIT_PASSENGER)
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
        binding.airlineViewModel = airlinesViewModel
        binding.lifecycleOwner = this

        setActivityActionBar()
        fillFieldsWithPassengerData()
        networkStateObserving()
        fillAirlineDropdownList()
        passengerNameObserving()
        airlineDropdownListObserving()
        buttonEnableObserving()
        buttonClickEvent()
        isPassengerDataSentObserving()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setActivityActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.edit_passenger)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun fillFieldsWithPassengerData() {
        editPassengerViewModel.passengerId.value = passenger?.id
        editPassengerViewModel.passengerName.value = passenger?.name
        editPassengerViewModel.trips.value = passenger?.trips.toString()
        editPassengerViewModel.airlineName.value = passenger?.airline?.get(0)?.toString()
        editPassengerViewModel.airlineObject.value = passenger?.airline?.get(0)
    }

    private fun fillAirlineDropdownList() {
        airlinesViewModel.airlinesLiveData.observe(this,
            { airlinesList ->
                arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, airlinesList)
                binding.airlineAutoCompleteTextView.setAdapter(arrayAdapter)
                binding.airlineNameContainer.airlineAutoCompleteTextView.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val selectedValue: AirLine? = arrayAdapter.getItem(position)
                        editPassengerViewModel.airlineObject.value = selectedValue
                    }
            })
    }

    private fun networkStateObserving() {
        airlinesViewModel.networkState.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT)
                .show()
        }
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
            editPassengerViewModel.editPassenger()
        }
    }

    private fun isPassengerDataSentObserving() {
        editPassengerViewModel.isSent.observe(this) {
            if (it) {
                setResult(
                    PASSENGER_RESULT_CODE,
                    HomeActivity.newIntentWithStringExtra(
                        this,
                       "${editPassengerViewModel.passengerName.value.toString()} updated successfully"
                    )
                )
                finish()
            } else {
                Toast.makeText(this, resources.getString(R.string.error_sending_data), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {

        private val EXTRA_EDIT_PASSENGER =
            EditPassengerActivity::class.java.name + "_Edit_PASSENGER_EXTRA"

        fun newIntentWithPassengerExtra(context: Context, passenger: Passenger) =
            Intent(context, EditPassengerActivity::class.java)
                .putExtra(EXTRA_EDIT_PASSENGER, passenger)
    }
}