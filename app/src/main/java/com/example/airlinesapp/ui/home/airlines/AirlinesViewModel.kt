package com.example.airlinesapp.ui.home.airlines

import android.content.Context
import android.util.Log
import android.widget.Toast
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
   // var isLoading = MutableLiveData(true)
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

    fun getAirlinesList() {
        compositeDisposable.add(
            apiRepository.getAirlines()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _airlinesLiveData.postValue(it)
                       // isLoading.postValue(false)
                    },
                    {
                        Log.e("AirlinesViewModel", it.message.toString())
                      //  isLoading.postValue(false)
                    }
                )
        )
    }
}