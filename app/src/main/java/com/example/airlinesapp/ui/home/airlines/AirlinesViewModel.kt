package com.example.airlinesapp.ui.home.airlines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.di.network.ApiRepository
import com.example.airlinesapp.models.AirLine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AirlinesViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    ViewModel() {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _airlinesLiveData = MutableLiveData<List<AirLine>>()
    val airlinesLiveData: LiveData<List<AirLine>>
        get() = _airlinesLiveData

    init {
        getAirlinesList()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun getAirlinesList() {
        compositeDisposable.add(
            apiRepository.getAirlines()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })
        )
    }

    private fun onFailure(t: Throwable) {
        t.message?.let { Log.e("AirlinesViewModel", it) }
    }

    private fun onResponse(response: List<AirLine>) {
        _airlinesLiveData.postValue(response)
    }
}