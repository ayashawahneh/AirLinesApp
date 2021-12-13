package com.example.airlinesapp.ui.home.airlines.addAirline

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.di.network.ApiRepository
import com.example.airlinesapp.models.AirLine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AddAirlineViewModel @Inject constructor(private val apiRepository: ApiRepository) :
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

    fun validateSlogan(): String? {
        return if (slogan.value == null || slogan.value == "") {
            null
        } else {
            if (slogan.value.toString().length < 2) {
                "Too short"
            } else {
                null
            }
        }
    }

    fun validateWebsite(): String? {
        return if (website.value == null || website.value == "") {
            null
        } else {
            if (!(Patterns.WEB_URL.matcher(website.value!!).matches())) {
                "Invalid Website"
            } else {
                null
            }
        }
    }

    fun validateRequiredFields(str: String?): String? {
        return when {
            str == null || str == "" -> {
                "Required"
            }
            str.length < 2 -> {
                "Too short"
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
                apiRepository.addNewAirline(airlineData)
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