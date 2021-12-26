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
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.widget.AdapterView.OnItemClickListener
import com.example.airlinesapp.models.AirlineWithFavoriteFlag
import com.example.airlinesapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_add_passenger.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AddPassengerActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityAddPassengerBinding
    private lateinit var arrayAdapter: ArrayAdapter<AirlineWithFavoriteFlag>
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val addPassengerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(AddPassengerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setActivityActionBar()
        initAddPassengerViewModel()
        fillAirlineDropdownList()
        buttonClickEvent()
    }

    private fun setupView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_passenger)
        with(binding) {
            this.model = addPassengerViewModel
            this.lifecycleOwner = this@AddPassengerActivity
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setActivityActionBar() {
        with(supportActionBar) {
            this!!.title = resources.getString(R.string.new_passenger)
            this.setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun fillAirlineDropdownList() {
       addPassengerViewModel.airlinesLiveData.observe(this,
            { airlinesList ->
                arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, airlinesList)
                binding.airlineAutoCompleteTextView.setAdapter(arrayAdapter)
                binding.airlineNameContainer.airlineAutoCompleteTextView.onItemClickListener =
                    OnItemClickListener { _, _, position, _ ->
                        val selectedValue: AirLine? = arrayAdapter.getItem(position)?.airline
                        addPassengerViewModel.airlineObject.value = selectedValue
                    }
            })
    }

    private fun initAddPassengerViewModel() {
        with(addPassengerViewModel) {
            this.passengerName.observe(this@AddPassengerActivity) {
                val validationResult = this.validatePassengerName()
                if (validationResult != null) {
                    binding.passengerNameContainer.helperText =
                        resources.getString(validationResult)
                } else {
                    binding.passengerNameContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.airlineName.observe(this@AddPassengerActivity) {
                val validationResult = this.validateAirlineName()
                if (validationResult != null) {
                    binding.airlineNameContainer.helperText = resources.getString(validationResult)
                } else {
                    binding.airlineNameContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.isSent.observe(this@AddPassengerActivity) {
                if (it) {
                    setResult(
                        HomeActivity.PASSENGER_RESULT_CODE,
                        HomeActivity.newIntentWithStringExtra(
                            this@AddPassengerActivity,
                            "${this.passengerName.value} ${resources.getString(R.string.was_added_successfully)} ${this.airlineName.value}"
                        )
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this@AddPassengerActivity,
                        resources.getString(R.string.error_sending_data),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun buttonClickEvent() {
        binding.submitButton.setOnClickListener {
            addPassengerViewModel.addPassenger()
        }
    }

    companion object {

        fun newIntent(context: Context) =
            Intent(context, AddPassengerActivity::class.java)
    }
}