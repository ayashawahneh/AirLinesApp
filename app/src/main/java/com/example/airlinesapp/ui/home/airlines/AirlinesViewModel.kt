package com.example.airlinesapp.ui.home.airlines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.models.AirLine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class AirlinesViewModel @Inject constructor(private val airlinesRepository: AirlinesRepository) :
    ViewModel() {
    //val  airlinesRecyclerViewAdapter: AirlinesRecyclerViewAdapter = AirlinesRecyclerViewAdapter()
    // private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _liveDataList = MutableLiveData<List<AirLine>>()
    val liveDataList: LiveData<List<AirLine>>
        get() = _liveDataList

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun makeApiCall() {
        compositeDisposable.add(
            airlinesRepository.getAirlines()
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { onRetrievePostListStart() }
//                .doAfterTerminate { onRetrievePostListFinish() }
                .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })
        )
    }

    //    private fun onRetrievePostListStart(){
//        loadingVisibility.value = View.VISIBLE
//    }
//
//    private fun onRetrievePostListFinish(){
//        loadingVisibility.value = View.GONE
//    }
    private fun onFailure(t: Throwable) {
        t.message?.let { Log.e("AirlinesViewModel", it) }
    }

    private fun onResponse(response: List<AirLine>) {
        _liveDataList.postValue(response)
        //airlinesRecyclerViewAdapter.updatePostList(response)
    }

}