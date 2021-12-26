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

        setupView()
        setActivityActionBar()
        initViewModel()
        submitButtonClickEvent()
        establishedClickEvent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_airline)
        with(binding) {
            this.model = viewModel
            this.lifecycleOwner = this@AddAirlineActivity
        }
    }

    private fun setActivityActionBar() {
        with(supportActionBar) {
            this!!.title = resources.getString(R.string.new_airline)
            this.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            this.name.observe(this@AddAirlineActivity) {
                val validationResult = this.validateRequiredFields(it)
                if (validationResult != null) {
                    binding.nameContainer.helperText = resources.getString(validationResult)
                } else {
                    binding.nameContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.country.observe(this@AddAirlineActivity) {
                val validationResult = this.validateRequiredFields(it)
                if (validationResult != null) {
                    binding.countryContainer.helperText = resources.getString(validationResult)
                } else {
                    binding.countryContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.headQuarter.observe(this@AddAirlineActivity) {
                val validationResult = this.validateRequiredFields(it)
                if (validationResult != null) {
                    binding.headQuarterContainer.helperText = resources.getString(validationResult)
                } else {
                    binding.headQuarterContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.website.observe(this@AddAirlineActivity) {
                val validationResult = this.validateWebsite()
                if (validationResult != null) {
                    binding.websiteContainer.helperText = resources.getString(validationResult)
                } else {
                    binding.websiteContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.slogan.observe(this@AddAirlineActivity) {
                val validationResult = this.validateSlogan()
                if (validationResult != null) {
                    binding.sloganContainer.helperText = resources.getString(validationResult)
                } else {
                    binding.sloganContainer.helperText = null
                }
                this.setEnableSubmitButton()
            }

            this.isSent.observe(this@AddAirlineActivity) {
                if (it) {
                    setResult(
                        HomeActivity.AIRLINE_RESULT_CODE,
                        HomeActivity.newIntentWithStringExtra(
                            this@AddAirlineActivity,
                            "${this.name.value} ${resources.getString(R.string.was_added_successfully)}"
                        )
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this@AddAirlineActivity,
                        resources.getString(R.string.error_sending_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    private fun showYearPickerDialog() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val dialog = AlertDialog.Builder(this).create()

        yearPickerBinding = YearPickerDialogBinding.inflate(LayoutInflater.from(this))
        dialog.setView(yearPickerBinding.root)
        dialog.setTitle(resources.getString(R.string.year_picker_title))

        with(yearPickerBinding) {
            this.yearPicker.minValue = FIRST_AIRLINE_YEAR
            this.yearPicker.maxValue = currentYear
            this.yearPicker.value = currentYear
            this.yearPicker.wrapSelectorWheel = false
            this.yearPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            this.buttonSet.setOnClickListener {
                binding.establishedEditText.setText(this.yearPicker.value.toString())
                dialog.dismiss()
            }
            this.buttonCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    companion object {

        private const val FIRST_AIRLINE_YEAR = 1919

        fun newIntent(context: Context) =
            Intent(context, AddAirlineActivity::class.java)
    }
}