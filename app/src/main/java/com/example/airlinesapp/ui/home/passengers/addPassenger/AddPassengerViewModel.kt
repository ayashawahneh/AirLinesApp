package com.example.airlinesapp.ui.home.passengers.addPassenger

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.di.network.ApiRepository
import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.models.PassengerPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AddPassengerViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    ViewModel() {

    val passengerName = MutableLiveData<String>()
    val trips = MutableLiveData<String>()
    val airlineName = MutableLiveData<String>()
    val airlineObject = MutableLiveData<AirLine>()
    val enableSubmitButton = MutableLiveData(false)
    val isSent = MutableLiveData<Boolean>()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun validatePassengerName(): String? {
        return when {
            passengerName.value == null || passengerName.value == "" -> {
                "Required"
            }
            passengerName.value.toString().length < 3 -> {
                "Too short"
            }
            else -> null
        }
    }

    fun validateAirlineName(): String? {
        return if (airlineName.value == null || airlineName.value == "")
            "Required"
        else
            null
    }

    fun setEnableSubmitButton() {
        enableSubmitButton.value = (
                validateAirlineName() == null
                        && validatePassengerName() == null
                )
    }

    fun addPassenger() {
        if (trips.value == null || trips.value == "")
            trips.value = "0"

        val passengerData = PassengerPost(
            name = passengerName.value!!,
            trips = trips.value!!.toInt(),
            airline = airlineObject.value!!.id!!.toBigDecimal()
        )

        compositeDisposable
            .add(
                apiRepository.addNewPassenger(passengerData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.d("addNewPas", it.name)
                            isSent.value = true
                        },
                        {
                            Log.d("ErrorPas", it.toString())
                            isSent.value = false
                        }
                    )
            )
    }
}