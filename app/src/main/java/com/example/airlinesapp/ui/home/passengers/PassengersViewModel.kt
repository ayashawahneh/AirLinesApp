package com.example.airlinesapp.ui.home.passengers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.util.Constants.LOADED
import com.example.airlinesapp.util.Constants.LOADING
import com.example.airlinesapp.util.Constants.CHECK_NETWORK_ERROR
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations


class PassengersViewModel @Inject constructor(private val repository: PassengersRepository) :
    ViewModel() {

    private var networkState: MutableLiveData<String> = MutableLiveData(LOADING)
    var progressBarStatus: LiveData<String>? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val passengersList = MutableLiveData<PagingData<Passenger>>()

    init {
        getPassengers()
    }

    private fun getPassengers() {
        networkState.postValue(LOADING)

        compositeDisposable.add(
            repository
                .getPassengers()
                .subscribe(
                    {
                        passengersList.postValue(it)
                        networkState.postValue(LOADED)
                    },
                    {
                        networkState.postValue(CHECK_NETWORK_ERROR)

                    }
                )
        )
        progressBarStatus = Transformations.switchMap(passengersList) { networkState }
    }
}