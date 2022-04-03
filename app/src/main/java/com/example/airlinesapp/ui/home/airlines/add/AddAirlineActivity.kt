package com.example.airlinesapp.ui.home.airlines.add

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
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.core.widget.addTextChangedListener
import com.example.airlinesapp.databinding.YearPickerDialogBinding
import com.example.airlinesapp.ui.home.airlines.AirlinesFragment
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
        setListeners()
        checkSendingData()
        submitButtonClickEvent()
        establishedFieldClickEvent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_airline)
        with(binding) {
            model = viewModel
            lifecycleOwner = this@AddAirlineActivity
        }
    }

    private fun setActivityActionBar() {
        with(supportActionBar) {
            this!!.title = resources.getString(R.string.new_airline)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun checkSendingData() {
        with(viewModel) {
            isSentLiveData.observe(this@AddAirlineActivity) {
                if (it) {
                    setResult(
                        RESULT_OK,
                        AirlinesFragment.newIntentWithStringExtra(
                            this@AddAirlineActivity,
                            "$name ${resources.getString(R.string.was_added_successfully)}"
                        )
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this@AddAirlineActivity,
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
            nameEditText.addTextChangedListener {
                with(viewModel) {
                    setName(it.toString())
                    val validationResult = validateRequiredFields(it.toString())
                    if (validationResult != null) {
                        binding.nameContainer.helperText = resources.getString(validationResult)
                    } else {
                        binding.nameContainer.helperText = null
                    }
                    setEnableSubmitButton()
                }
            }

            countryEditText.addTextChangedListener {
                with(viewModel) {
                    setCountry(it.toString())
                    val validationResult = validateRequiredFields(it.toString())
                    if (validationResult != null) {
                        binding.countryContainer.helperText = resources.getString(validationResult)
                    } else {
                        binding.countryContainer.helperText = null
                    }
                    setEnableSubmitButton()
                }
            }

            websiteEditText.addTextChangedListener {
                with(viewModel) {
                    setWebsite(it.toString())
                    val validationResult = validateWebsite()
                    if (validationResult != null) {
                        binding.websiteContainer.helperText = resources.getString(validationResult)
                    } else {
                        binding.websiteContainer.helperText = null
                    }
                    setEnableSubmitButton()
                }
            }

            sloganEditText.addTextChangedListener {
                with(viewModel) {
                    setSlogan(it.toString())
                    val validationResult = validateSlogan()
                    if (validationResult != null) {
                        binding.sloganContainer.helperText = resources.getString(validationResult)
                    } else {
                        binding.sloganContainer.helperText = null
                    }
                    setEnableSubmitButton()
                }
            }

            establishedEditText.addTextChangedListener {
                viewModel.setEstablished(it.toString())
            }

            headQuarterEditText.addTextChangedListener {
                with(viewModel) {
                    viewModel.setHeadQuarter(it.toString())
                    val validationResult = validateRequiredFields(it.toString())
                    if (validationResult != null) {
                        binding.headQuarterContainer.helperText =
                            resources.getString(validationResult)
                    } else {
                        binding.headQuarterContainer.helperText = null
                    }
                    setEnableSubmitButton()
                }
            }
        }
    }

    private fun establishedFieldClickEvent() {
        binding.establishedEditText.setOnClickListener { showYearPickerDialog() }
    }

    private fun submitButtonClickEvent() {
        binding.submitButton.setOnClickListener {
            viewModel.addAirline()
            it.isEnabled = false
        }
    }

    private fun showYearPickerDialog() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val dialog = AlertDialog.Builder(this).create()

        yearPickerBinding = YearPickerDialogBinding.inflate(LayoutInflater.from(this))
        dialog.setView(yearPickerBinding.root)
        dialog.setTitle(resources.getString(R.string.year_picker_title))

        with(yearPickerBinding) {
            with(yearPicker) {
                minValue = FIRST_AIRLINE_YEAR
                maxValue = currentYear
                value = currentYear
                wrapSelectorWheel = false
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            }
            buttonSet.setOnClickListener {
                binding.establishedEditText.setText(this.yearPicker.value.toString())
                dialog.dismiss()
            }
            buttonCancel.setOnClickListener {
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