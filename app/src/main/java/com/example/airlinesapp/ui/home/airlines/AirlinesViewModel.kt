package com.example.airlinesapp.ui.home.airlines

import android.util.Log
import androidx.lifecycle.*
import com.example.airlinesapp.R
import com.example.airlinesapp.di.network.Repository
import com.example.airlinesapp.models.AirlineWithFavoriteFlag
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AirlinesViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
        private set
    val isVisibleStateTextView = MutableLiveData<Boolean>()
    var networkState = MutableLiveData<Int>()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    val airlinesLiveData = MutableLiveData<List<AirlineWithFavoriteFlag>>()
    val favoriteAirlinesList = MutableLiveData<MutableList<String>>()
    val searchText = MutableLiveData<String>()
    private var searchedAirlinesList = MutableLiveData<List<AirlineWithFavoriteFlag>>()
    val mappedSearchedText = searchText.switchMap { query ->
        if (!query.isNullOrEmpty()) {
            searchedAirlinesList.value = airlinesLiveData.value?.filter {
                it.airline.name.contains(searchText.value.toString(), true)
            }
            if (searchedAirlinesList.value?.isEmpty()!!) {
                networkState.value = R.string.EMPTY_SEARCH
                isVisibleStateTextView.value = true
            } else {
                isVisibleStateTextView.value = false
            }
            searchedAirlinesList

        } else {
            isVisibleStateTextView.value = false
            airlinesLiveData
        }
    }

    init {
        favoriteAirlinesList.value = repository.getFavoriteIdsFromDataStore()
        getAirlinesList()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun updateAirlineIdsInDataStore() {
        favoriteAirlinesList.value?.let {
            repository.saveFavoriteIdsToDataStore(it)
        }
    }

    private fun getAirlinesList() {
        networkState.value = R.string.LOADING
        isLoading.value = true
        isVisibleStateTextView.value = true
        compositeDisposable.add(
            repository.getAirlines()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        this.airlinesLiveData.value = it
                        isLoading.value = false
                        isVisibleStateTextView.value = false
                        if (it.isEmpty()) {
                            networkState.value = R.string.EMPTY_LIST
                            isVisibleStateTextView.value = true
                        }
                    },
                    {
                        Log.e("AirlinesViewModel", it.message.toString())
                        isLoading.value = false
                        isVisibleStateTextView.value = true
                        networkState.value = R.string.CHECK_NETWORK_ERROR
                    }
                )
        )
    }
}