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
import com.example.airlinesapp.models.AirlineWithFavoriteFlag
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.ui.home.HomeActivity
import com.example.airlinesapp.ui.home.airlines.AirlinesViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_passenger.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EditPassengerActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityEditPassengerBinding
    private lateinit var arrayAdapter: ArrayAdapter<AirlineWithFavoriteFlag>
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

        setupView()
        setActivityActionBar()
        initEditPassengerViewModel()
        fillAirlineDropdownList()
        buttonClickEvent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_passenger)
        with(binding) {
            this.model = editPassengerViewModel
            this.airlineViewModel = airlinesViewModel
            this.lifecycleOwner = this@EditPassengerActivity
        }
    }

    private fun setActivityActionBar() {
        with(supportActionBar) {
            this!!.title = resources.getString(R.string.edit_passenger)
            this.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initEditPassengerViewModel() {
        with(editPassengerViewModel) {
            /*-------fill data on fields-------------*/
            this.passengerId.value = passenger?.id
            this.passengerName.value = passenger?.name
            this.trips.value = passenger?.trips.toString()
            this.airlineName.value = passenger?.airline?.get(0)?.toString()
            this.airlineObject.value = passenger?.airline?.get(0)

            /*-----------observing--------------------*/
            this.airlineName.observe(this@EditPassengerActivity) {
                val validationResult = this.validateAirlineName()
                if (validationResult != null) {
                    binding.airlineNameContainer.helperText = resources.getString(validationResult)
                } else {
                    binding.airlineNameContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.passengerName.observe(this@EditPassengerActivity) {
                val validationResult = this.validatePassengerName()
                if (validationResult != null) {
                    binding.passengerNameContainer.helperText =
                        resources.getString(validationResult)
                } else {
                    binding.passengerNameContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.isSent.observe(this@EditPassengerActivity) {
                if (it) {
                    setResult(
                        HomeActivity.PASSENGER_RESULT_CODE,
                        HomeActivity.newIntentWithStringExtra(
                            this@EditPassengerActivity,
                            "${this.passengerName.value.toString()} ${resources.getString(R.string.updated_successfully)}"
                        )
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this@EditPassengerActivity,
                        resources.getString(R.string.error_sending_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun fillAirlineDropdownList() {
        airlinesViewModel.airlinesLiveData.observe(this,
            { airlinesList ->
                arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, airlinesList)
                binding.airlineAutoCompleteTextView.setAdapter(arrayAdapter)
                binding.airlineNameContainer.airlineAutoCompleteTextView.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val selectedValue: AirLine? = arrayAdapter.getItem(position)?.airline
                        editPassengerViewModel.airlineObject.value = selectedValue
                    }
            })
    }

    private fun buttonClickEvent() {
        binding.submitButton.setOnClickListener {
            editPassengerViewModel.editPassenger()
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