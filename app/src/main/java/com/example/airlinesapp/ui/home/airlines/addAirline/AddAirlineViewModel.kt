package com.example.airlinesapp.ui.home.airlines.addAirline

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.di.network.ApiRepository
import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.util.Constants.CURRENT_YEAR
import com.example.airlinesapp.util.Constants.FIRST_AIRLINE_YEAR
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun setEnableSubmitButton() {
        enableSubmitButton.value = (
                validateRequiredFields(name.value.toString()) == null
                        && validateRequiredFields(country.value.toString()) == null
                        && validateRequiredFields(headQuarter.value.toString()) == null
                        && validateSlogan() == null
                        && validateEstablished() == null
                        && validateWebsite() == null
                )
    }

    fun validateSlogan(): String? {
        return if (slogan.value == null) {
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
        return if (website.value == null) {
            null
        } else {
            if (!(Patterns.WEB_URL.matcher(website.toString()).matches())) {
                "Invalid Website"
            } else {
                null
            }
        }
    }

    fun validateEstablished(): String? {
        return if (established.value == null) {
            null
        } else {
            if (!(established.value?.length == 4 && established.value?.toInt() in FIRST_AIRLINE_YEAR..CURRENT_YEAR)) {
                "Invalid Year"
            } else {
                null
            }
        }
    }

    fun validateRequiredFields(str: String): String? {
        return when {
            str.isEmpty() -> {
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
            country = country.value.toString(),
            established = established.value.toString(),
            slogan = slogan.value.toString(),
            name = name.value.toString(),
            headQuaters = headQuarter.value.toString(),
            website = website.value.toString(),
            id = null,
            logo = null
        )

        compositeDisposable
            .add(
                apiRepository.addNewAirline(airlineData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.d("addNewAir", it.name)
                        },
                        {
                            Log.d("addNewAir", it.message.toString())
                        }
                    )
            )
    }
}