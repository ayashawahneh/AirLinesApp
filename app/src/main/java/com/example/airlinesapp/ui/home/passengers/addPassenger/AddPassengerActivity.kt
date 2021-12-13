package com.example.airlinesapp.ui.home.passengers.addPassenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        binding.airlineViewModel = airlinesViewModel
        binding.lifecycleOwner = this

        setActivityActionBar()
        fillAirlineDropdownList()
        passengerNameObserving()
        airlineDropdownListObserving()
        buttonEnableObserving()
        isPassengerDataSentObserving()
        networkStateObserving()
        buttonClickEvent()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setActivityActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.new_passenger)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun fillAirlineDropdownList() {
        airlinesViewModel.airlinesLiveData.observe(this,
            { airlinesList ->
                arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, airlinesList)
                binding.airlineAutoCompleteTextView.setAdapter(arrayAdapter)
                binding.airlineNameContainer.airlineAutoCompleteTextView.onItemClickListener =
                    OnItemClickListener { _, _, position, _ ->
                        val selectedValue: AirLine? = arrayAdapter.getItem(position)
                        addPassengerViewModel.airlineObject.value = selectedValue
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
            addPassengerViewModel.addPassenger()
        }
    }

    private fun isPassengerDataSentObserving() {
        addPassengerViewModel.isSent.observe(this) {
            if (it) {
                startActivity(
                    HomeActivity.newIntentWithStringExtra(
                        this,
                        addPassengerViewModel.passengerName.value.toString()
                    )
                )
            } else {
                Toast.makeText(this, resources.getString(R.string.error_sending_data), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {

        fun newIntent(context: Context) =
            Intent(context, AddPassengerActivity::class.java)
    }
}