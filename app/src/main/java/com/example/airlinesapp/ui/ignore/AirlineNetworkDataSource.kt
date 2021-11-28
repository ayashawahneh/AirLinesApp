//package com.example.airlinesapp.di
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.example.airlinesapp.models.AirLine
//import io.reactivex.schedulers.Schedulers
//import io.reactivex.disposables.CompositeDisposable
//import java.lang.Exception
//import javax.inject.Inject
//
//class AirlineNetworkDataSource (){//@Inject constructor(var retrofitInterface: RetrofitInterface, var compositeDisposable: CompositeDisposable) {
//
//    @Inject
//    lateinit var retrofitInterface: RetrofitInterface
//
//    @Inject
//    lateinit var compositeDisposable: CompositeDisposable
//    private val _networkState = MutableLiveData<NetworkState>()
//    val networkState: LiveData<NetworkState>
//        get() = _networkState
//
//    private val _downloadedAirlineResponse = MutableLiveData<List<AirLine>>()
//    val downloadedAirlineResponse: LiveData<List<AirLine>>
//        get() = _downloadedAirlineResponse
//
//    fun fetchAllAirlines() {
//
//        _networkState.postValue(NetworkState.LOADING)
//
//        try {
//            compositeDisposable.add(
//                retrofitInterface.getAirlines()
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(
//                        {
//                            _downloadedAirlineResponse.postValue(it)
//                            _networkState.postValue(NetworkState.LOADED)
//                        },
//                        {
//                            _networkState.postValue(NetworkState.ERROR)
//                        }
//                    )
//            )
//        } catch (e: Exception) {
//            Log.e("Airline", e.message.toString())
//        }
//
//
//    }
//
//}