package com.example.airlinesapp.ui.home.airlines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.di.network.ApiRepository
import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.util.Constants.CHECK_NETWORK_ERROR
import com.example.airlinesapp.util.Constants.EMPTY_LIST
import com.example.airlinesapp.util.Constants.LOADING
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class AirlinesViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
        private set
    var networkState = MutableLiveData<String>()
        private set
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
        networkState.value = LOADING
        isLoading.value = true
        compositeDisposable.add(
            apiRepository.getAirlines()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _airlinesLiveData.value = it
                        isLoading.value = false

                        if (it.isEmpty()){
                            networkState.value = EMPTY_LIST
                        }

                    },
                    {
                        Log.e("AirlinesViewModel", it.message.toString())
                        isLoading.value = false
                        networkState.value = CHECK_NETWORK_ERROR
                    }
                )
        )
    }
}