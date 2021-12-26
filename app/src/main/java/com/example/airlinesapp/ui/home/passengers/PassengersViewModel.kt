package com.example.airlinesapp.ui.home.passengers

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.example.airlinesapp.R
import com.example.airlinesapp.models.Passenger
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import com.example.airlinesapp.di.network.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PassengersViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    var networkState = MutableLiveData<Int>()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val passengersList = MutableLiveData<PagingData<Passenger>>()
    val isDeleted = MutableLiveData<Boolean>()
    val searchText = MutableLiveData("")
    val isVisibleStateTextView = MutableLiveData<Boolean>()

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
        networkState.value = R.string.LOADING
        isLoading.value = true
        isVisibleStateTextView.value = true
        compositeDisposable.add(
            repository.getPassengers(searchText.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        passengersList.value = it
                        isLoading.value = false
                        isVisibleStateTextView.value = false
                    },
                    {
                        networkState.value = R.string.CHECK_NETWORK_ERROR
                        isVisibleStateTextView.value = true
                        isLoading.value = false
                    }
                )
        )
    }
}