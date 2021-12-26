package com.example.airlinesapp.ui.home.airlines.addAirline

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.R
import com.example.airlinesapp.di.network.Repository
import com.example.airlinesapp.models.AirLine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddAirlineViewModel
@Inject constructor(private val repository: Repository) :
    ViewModel() {
    val name = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val headQuarter = MutableLiveData<String>()
    val slogan = MutableLiveData<String>()
    val website = MutableLiveData<String>()
    val established = MutableLiveData<String>()
    val enableSubmitButton = MutableLiveData(false)
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    val isSent = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun setEnableSubmitButton() {
        enableSubmitButton.value = (
                validateRequiredFields(name.value) == null
                        && validateRequiredFields(country.value) == null
                        && validateRequiredFields(headQuarter.value) == null
                        && validateSlogan() == null
                        && validateWebsite() == null
                )
    }

    fun validateSlogan(): Int? {
        return if (slogan.value.isNullOrEmpty()) {
            null
        } else {
            if (slogan.value.toString().length < 3) {
                R.string.too_short
            } else {
                null
            }
        }
    }

    fun validateWebsite(): Int? {
        return if (website.value.isNullOrEmpty()) {
            null
        } else {
            if (!(Patterns.WEB_URL.matcher(website.value!!).matches())) {
                R.string.invalid_website
            } else {
                null
            }
        }
    }

    fun validateRequiredFields(str: String?): Int? {
        return when {
            str.isNullOrEmpty() -> {
                R.string.required
            }
            str.length < 3 -> {
                R.string.too_short
            }
            else -> null
        }
    }

    fun addAirline() {
        val airlineData = AirLine(
            country = country.value!!,
            established = established.value,
            slogan = slogan.value,
            name = name.value!!,
            headQuaters = headQuarter.value!!,
            website = website.value,
        )

        compositeDisposable
            .add(
                repository.addNewAirline(airlineData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.d("addNewAir", it.name)
                            isSent.value = true
                        },
                        {
                            Log.d("addNewAir", it.message.toString())
                            isSent.value = false
                        }
                    )
            )
    }
}