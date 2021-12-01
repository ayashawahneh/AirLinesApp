package com.example.airlinesapp.ui.home.passengers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.rxjava2.cachedIn
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.util.PASSENGERS_PER_PAGE
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class PassengersViewModel @Inject constructor(private val repository: PassengersRepository) :
    ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val passengersList = MutableLiveData<PagingData<Passenger>>()

    init {
        getPassengers()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun getPassengers() {
        compositeDisposable.add(
            repository
                .getPassengers()
                .subscribe({
                    Log.d("pass suc", it.toString())
                    passengersList.postValue(it)
                }, { Log.d("pass", it.toString()) })
        )
    }

}