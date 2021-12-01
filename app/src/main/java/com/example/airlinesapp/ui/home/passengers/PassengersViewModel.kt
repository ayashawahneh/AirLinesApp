package com.example.airlinesapp.ui.home.passengers

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.util.Constants.CHECK_NETWORK_ERROR
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import com.example.airlinesapp.di.network.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PassengersViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    var networkState = MutableLiveData<String>()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val passengersList = MutableLiveData<PagingData<Passenger>>()
    val isDeleted = MutableLiveData<Boolean>()
    val searchText = MutableLiveData("")

    init {
        getPassengers()
    }

    fun search() {
        getPassengers()
    }

    fun deletePassenger(id: String) {
        compositeDisposable
            .add(
                repository.deletePassenger(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.d("DeletePas", it.toString())
                            isDeleted.value = true
                        },
                        {
                            Log.d("ErrorPas", it.toString())
                            isDeleted.value = false
                        }
                    )
            )
    }

    private fun getPassengers() {
        isLoading.value = true
        compositeDisposable.add(
            repository.getPassengers(searchText.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        passengersList.value = it
                        isLoading.value = false
                    },
                    {
                        networkState.value = CHECK_NETWORK_ERROR
                        isLoading.value = false
                    }
                )
        )
    }
}