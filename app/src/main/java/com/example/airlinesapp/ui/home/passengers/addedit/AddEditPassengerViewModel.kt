package com.example.airlinesapp.ui.home.passengers.addedit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.R
import com.example.airlinesapp.di.repo.Repository
import com.example.airlinesapp.models.AirlineWithFavoriteFlagItem
import com.example.airlinesapp.di.network.models.PassengerPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddEditPassengerViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private lateinit var passengerId: String
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    var passengerName = String()
        private set

    var trips = String()
        private set

    var airlineItem: AirlineWithFavoriteFlagItem? = null
        private set

    private val _enableSubmitButtonLiveData = MutableLiveData(false)
    val enableSubmitButtonLiveData: LiveData<Boolean> = _enableSubmitButtonLiveData

    private val _isSentLiveData = MutableLiveData<Boolean>()
    val isSentLiveData: LiveData<Boolean> = _isSentLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    private val _isVisibleStateTextViewLiveData = MutableLiveData<Boolean>()
    val isVisibleStateTextViewLiveData: LiveData<Boolean> = _isVisibleStateTextViewLiveData

    private val _networkStateLiveData = MutableLiveData<Int>()
    val networkStateLiveData: LiveData<Int> = _networkStateLiveData

    private val _airlinesLiveData = MutableLiveData<List<AirlineWithFavoriteFlagItem>>()
    val airlinesLiveData: LiveData<List<AirlineWithFavoriteFlagItem>> = _airlinesLiveData


    init {
        getAirlinesList()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun setPassengerId(id: String) {
        passengerId = id
    }

    fun setPassengerName(name: String) {
        passengerName = name
    }

    fun setTrips(tripsNo: String) {
        trips = tripsNo
    }

    fun setAirlineItemLiveData(airlineItem: AirlineWithFavoriteFlagItem) {
        this.airlineItem = airlineItem
    }

    fun validatePassengerName(): Int? {
        return when {
            passengerName.isEmpty() -> {
                R.string.required
            }
            passengerName.length < 3 -> {
                R.string.too_short
            }
            else -> null
        }
    }

    fun validateAirlineName(): Int? {
        return if (airlineItem == null)
            R.string.required
        else
            null
    }

    fun setEnableSubmitButton() {
        _enableSubmitButtonLiveData.value = (
                validateAirlineName() == null
                        && validatePassengerName() == null
                )
    }

    fun editPassenger() {
        if (trips.isEmpty() || trips == "null")
            trips = "0"

        val passengerData = PassengerPost(
            name = passengerName,
            trips = trips.toInt(),
            airline = airlineItem?.id!!.toBigDecimal()
        )
        compositeDisposable
            .add(
                repository.editPassenger(passengerId, passengerData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            _isSentLiveData.value = true
                        },
                        {
                            Log.d("ErrorPas", it.toString())
                            _isSentLiveData.value = false
                        }
                    )
            )
    }

    fun addPassenger() {
        if (trips.isEmpty())
            trips = "0"

        val passengerData = PassengerPost(
            name = passengerName,
            trips = trips.toInt(),
            airline = airlineItem?.id!!.toBigDecimal()
        )

        compositeDisposable
            .add(
                repository.addNewPassenger(passengerData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.d("addNewPas", it.name.orEmpty())
                            _isSentLiveData.value = true
                        },
                        {
                            Log.d("ErrorPas", it.toString())
                            _isSentLiveData.value = false
                        }
                    )
            )
    }

    private fun getAirlinesList() {
        _networkStateLiveData.value = R.string.LOADING
        _isLoadingLiveData.value = true
        _isVisibleStateTextViewLiveData.value = true
        compositeDisposable.add(
            repository.getAirlines()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        this._airlinesLiveData.value = it
                        _isLoadingLiveData.value = false
                        _isVisibleStateTextViewLiveData.value = false
                        if (it.isEmpty()) {
                            _networkStateLiveData.value = R.string.EMPTY_LIST
                            _isVisibleStateTextViewLiveData.value = true
                        }
                    },
                    {
                        _isLoadingLiveData.value = false
                        _isVisibleStateTextViewLiveData.value = true
                        _networkStateLiveData.value = R.string.CHECK_NETWORK_ERROR
                    }
                )
        )
    }
}