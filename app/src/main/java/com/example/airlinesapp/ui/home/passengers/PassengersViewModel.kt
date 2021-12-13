package com.example.airlinesapp.ui.home.passengers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.util.Constants.LOADED
import com.example.airlinesapp.util.Constants.LOADING
import com.example.airlinesapp.util.Constants.CHECK_NETWORK_ERROR
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import com.example.airlinesapp.di.network.ApiRepository


class PassengersViewModel @Inject constructor(private val repository: ApiRepository) :
    ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    private var networkState: MutableLiveData<String> = MutableLiveData(LOADING)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val passengersList = MutableLiveData<PagingData<Passenger>>()
    val isDeleted = MutableLiveData<Boolean>()

    init {
        getPassengers()
    }

    private fun getPassengers() {
        networkState.postValue(LOADING)
        isLoading.value = true
        compositeDisposable.add(
            repository
                .getPassengers()
                .subscribe(
                    {
                        passengersList.value = it
                        networkState.value = LOADED
                        isLoading.value = false
                    },
                    {
                        networkState.value = CHECK_NETWORK_ERROR
                        isLoading.value = false
                    }
                )
        )
    }

    fun deletePassenger(id: String) {
        compositeDisposable
            .add(
                repository.deletePassenger(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            isDeleted.value = true
                        },
                        {
                            Log.d("ErrorPas", it.toString())
                            isDeleted.value = false
                        }
                    )
            )
    }
}