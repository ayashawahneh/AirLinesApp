package com.example.airlinesapp.ui.home.passengers.addedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.ActivityAddEditPassengerBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.models.AirlineWithFavoriteFlagItem
import com.example.airlinesapp.di.network.models.Passenger
import com.example.airlinesapp.ui.home.passengers.PassengersFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_edit_passenger.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddEditPassengerActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityAddEditPassengerBinding
    private lateinit var arrayAdapter: ArrayAdapter<AirlineWithFavoriteFlagItem>
    val passenger by lazy {
        intent.getParcelableExtra<Passenger?>(EXTRA_EDIT_PASSENGER)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AddEditPassengerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setActivityActionBar()
        initViewModel()
        setAirlineDropdownList()
        setListeners()
        buttonClickEvent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupView() {
        binding = setContentView(this, R.layout.activity_add_edit_passenger)
        with(binding) {
            model = viewModel
            lifecycleOwner = this@AddEditPassengerActivity
        }
    }

    private fun setActivityActionBar() {
        with(supportActionBar) {

            this!!.title =
                if (passenger != null)
                    resources.getString(R.string.edit_passenger)
                else
                    resources.getString(R.string.new_passenger)

            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            /*-------fill data on fields if it edit-------------*/
            if (passenger != null) {
                with(passenger as Passenger) {
                    setPassengerId(id.toString())
                    setPassengerName(name.toString())
                    setTrips(trips.toString())
                    setAirlineItemLiveData(
                        AirlineWithFavoriteFlagItem.create(airline?.get(0)!!, false)
                    )
                }
            }

            /*-----------observing--------------------*/
            isSentLiveData.observe(this@AddEditPassengerActivity) {
                if (it) {
                    setResult(
                        RESULT_OK,
                        PassengersFragment.newIntentWithStringExtra(
                            this@AddEditPassengerActivity,
                            if (passenger != null) {
                                "${this.passengerName}${resources.getString(R.string.updated_successfully)}"
                            } else {
                                "${this.passengerName} ${resources.getString(R.string.was_added_successfully)} ${this.airlineItem}"
                            }
                        )
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this@AddEditPassengerActivity,
                        resources.getString(R.string.error_sending_data),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.submitButton.isEnabled = true
                }
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            passengerNameEditText.addTextChangedListener {
                with(viewModel) {
                    setPassengerName(it.toString())
                    val validationResult = validatePassengerName()
                    if (validationResult != null) {
                        binding.passengerNameContainer.helperText =
                            resources.getString(validationResult)
                    } else {
                        binding.passengerNameContainer.helperText = null
                    }
                    setEnableSubmitButton()
                }
            }
            tripsEditText.addTextChangedListener {
                viewModel.setTrips(it.toString())
            }
        }
    }

    private fun setAirlineDropdownList() {
        viewModel.airlinesLiveData.observe(this,
            { airlinesList ->
                arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, airlinesList)
                binding.airlineAutoCompleteTextView.setAdapter(arrayAdapter)

                binding.airlineNameContainer.airlineAutoCompleteTextView.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val selectedAirlineItem: AirlineWithFavoriteFlagItem? =
                            arrayAdapter.getItem(position)
                        with(viewModel) {
                            setAirlineItemLiveData(selectedAirlineItem!!)
                            val validationResult = validateAirlineName()
                            if (validationResult != null) {
                                binding.airlineNameContainer.helperText =
                                    resources.getString(validationResult)
                            } else {
                                binding.airlineNameContainer.helperText = null
                            }
                            this.setEnableSubmitButton()
                        }
                    }
            })
    }

    private fun buttonClickEvent() {

        binding.submitButton.setOnClickListener {
            if (passenger != null) {
                viewModel.editPassenger()
            } else {
                viewModel.addPassenger()
            }
            it.isEnabled = false
        }
    }

    companion object {

        private val EXTRA_EDIT_PASSENGER =
            AddEditPassengerActivity::class.java.name + "_Edit_PASSENGER_EXTRA"

        fun newIntentWithPassengerExtra(context: Context, passenger: Passenger?) =
            Intent(context, AddEditPassengerActivity::class.java)
                .putExtra(EXTRA_EDIT_PASSENGER, passenger)
    }
}