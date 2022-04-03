package com.example.airlinesapp.ui.home.airlines

import android.util.Log
import androidx.lifecycle.*
import com.example.airlinesapp.R
import com.example.airlinesapp.di.repo.Repository
import com.example.airlinesapp.models.AirlineWithFavoriteFlagItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AirlinesViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var searchString: String? = null
    private var airlinesList: List<AirlineWithFavoriteFlagItem> = emptyList()
    var favoriteAirlinesList = emptyList<String>()
        private set

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    private val _isVisibleStateTextViewLiveData = MutableLiveData<Boolean>()
    val isVisibleStateTextViewLiveData: LiveData<Boolean> = _isVisibleStateTextViewLiveData

    private val _networkStateLiveData = MutableLiveData<Int>()
    val networkStateLiveData: LiveData<Int> = _networkStateLiveData

    private val _airlinesListLiveData = MutableLiveData<List<AirlineWithFavoriteFlagItem>>()
    val airlinesListLiveData: LiveData<List<AirlineWithFavoriteFlagItem>> = _airlinesListLiveData

    private val _isVisibleReloadLiveData = MutableLiveData<Boolean>()
    val isVisibleReloadLiveData: LiveData<Boolean> = _isVisibleReloadLiveData


    init {
        val dataStoreFavoriteList = repository.getFavoriteIdsFromDataStore()
        if (!dataStoreFavoriteList.isNullOrEmpty())
            favoriteAirlinesList = dataStoreFavoriteList
        getAirlinesList()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun saveIdInFavoriteAirlinesList(favoritesIdsList: MutableList<String>) {
        favoriteAirlinesList = favoritesIdsList
        repository.saveFavoriteIdsToDataStore(favoritesIdsList)
    }

    fun removeIdFromAirlinesFavoriteList(favoritesIdsList: MutableList<String>) {
        favoriteAirlinesList = favoritesIdsList
        repository.saveFavoriteIdsToDataStore(favoritesIdsList)

    }

    fun updateItemFavoriteFlagInTheCurrentList(position: Int, isFavoriteNewValue: Boolean) {
        _airlinesListLiveData.value?.get(position)?.isFavorite = isFavoriteNewValue
    }

    fun search(query: String) {
        searchString = query
        _airlinesListLiveData.value = airlinesList.applySearch()
        if (_airlinesListLiveData.value?.isEmpty()!!) {
            _networkStateLiveData.value = R.string.EMPTY_SEARCH
            _isVisibleStateTextViewLiveData.value = true
        } else {
            _isVisibleStateTextViewLiveData.value = false
        }
    }

    fun refreshList() {
        getAirlinesList()
    }

    private fun List<AirlineWithFavoriteFlagItem>.applySearch(): List<AirlineWithFavoriteFlagItem> {
        return if (!searchString.isNullOrEmpty()) {
            filter {
                it.name.contains(searchString.toString(), true)
            }
        } else {
            this
        }
    }

    private fun getAirlinesList() {
        _networkStateLiveData.value = R.string.LOADING
        _isLoadingLiveData.value = true
        _isVisibleStateTextViewLiveData.value = true
        _isVisibleReloadLiveData.value = false
        compositeDisposable.add(
            repository.getAirlines()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        it.let {
                            this.airlinesList = it
                            this._airlinesListLiveData.value = it
                        }
                        _isLoadingLiveData.value = false
                        _isVisibleStateTextViewLiveData.value = false
                        if (it.isEmpty()) {
                            _networkStateLiveData.value = R.string.EMPTY_LIST
                            _isVisibleStateTextViewLiveData.value = true
                        }
                    },
                    {
                        Log.e("AirlinesViewModel", it.message.toString())
                        _isLoadingLiveData.value = false
                        _isVisibleStateTextViewLiveData.value = true
                        _networkStateLiveData.value = R.string.CHECK_NETWORK_ERROR
                        _isVisibleReloadLiveData.value = true

                    }
                )
        )
    }
}