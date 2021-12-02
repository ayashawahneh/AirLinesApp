package com.example.airlinesapp.ui.home.passengers


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.airlinesapp.di.NetworkState
import com.example.airlinesapp.models.Passenger
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PassengersViewModel @Inject constructor(private val repository: PassengersRepository) :
    ViewModel() {
    var progressBarStatus: LiveData<NetworkState>? = null
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val passengersList = MutableLiveData<PagingData<Passenger>>()

    init {

        getPassengers()
    }

    private fun getPassengers() {

        _networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            repository
                .getPassengers()
                .subscribe(
                    {
                        passengersList.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                    }
                )
        )
        progressBarStatus = Transformations.switchMap(passengersList) { networkState };
    }


}