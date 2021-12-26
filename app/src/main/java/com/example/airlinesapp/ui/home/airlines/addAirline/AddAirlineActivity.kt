package com.example.airlinesapp.ui.home.airlines.addAirline

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.ActivityAddAirlineBinding
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.ui.home.HomeActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.view.LayoutInflater
import android.widget.NumberPicker
import com.example.airlinesapp.databinding.YearPickerDialogBinding
import com.example.airlinesapp.util.Constants.AIRLINE_RESULT_CODE
import com.example.airlinesapp.util.Constants.FIRST_AIRLINE_YEAR
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class AddAirlineActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityAddAirlineBinding
    private lateinit var yearPickerBinding: YearPickerDialogBinding

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
        binding.lifecycleOwner = this

        setActivityActionBar()
        nameObserving()
        countryObserving()
        headQuarterObserving()
        websiteObserving()
        sloganObserving()
        isAirlineDataSentObserving()
        buttonEnableObserving()
        submitButtonClickEvent()
        establishedClickEvent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setActivityActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.new_airline)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun isAirlineDataSentObserving() {
        viewModel.isSent.observe(this) {
            if (it) {
                setResult( AIRLINE_RESULT_CODE,
                    HomeActivity.newIntentWithStringExtra(
                        this,
                        "${viewModel.name.value} was added successfully"
                    )
                )
                finish()
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.error_sending_data),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun establishedClickEvent() {
        binding.establishedEditText.setOnClickListener { showYearPickerDialog() }
    }

    private fun submitButtonClickEvent() {
        binding.submitButton.setOnClickListener {
            viewModel.addAirline()
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

    private fun showYearPickerDialog() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val dialog = AlertDialog.Builder(this).create()
        yearPickerBinding = YearPickerDialogBinding.inflate(LayoutInflater.from(this))

        dialog.setView(yearPickerBinding.root)

        dialog.setTitle("Year Picker")
        yearPickerBinding.yearPicker.minValue = FIRST_AIRLINE_YEAR
        yearPickerBinding.yearPicker.maxValue = currentYear
        yearPickerBinding.yearPicker.value = currentYear

        yearPickerBinding.yearPicker.wrapSelectorWheel = false
        yearPickerBinding.yearPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        yearPickerBinding.buttonSet.setOnClickListener {
            binding.establishedEditText.setText(yearPickerBinding.yearPicker.value.toString())
            dialog.dismiss()
        }

        yearPickerBinding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {

        fun newIntent(context: Context) =
            Intent(context, AddAirlineActivity::class.java)
    }
}
