package com.example.airlinesapp.ui.home.passengers

import android.view.MenuItem
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.airlinesapp.di.network.models.Passenger
import com.example.airlinesapp.di.repo.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PassengersViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _passengersListLiveData = MutableLiveData<PagingData<Passenger>>()
    val passengersListLiveData: LiveData<PagingData<Passenger>> = _passengersListLiveData

    private val _isDeletedLiveData = MutableLiveData<Boolean>()
    val isDeletedLiveData: LiveData<Boolean> = _isDeletedLiveData

    private val _searchTextLiveData = MutableLiveData("")
    val searchTextLiveData: LiveData<String> = _searchTextLiveData

    private var _searchItemLiveData = MutableLiveData<MenuItem>()
    val searchItemLiveData: LiveData<MenuItem> = _searchItemLiveData

    init {
        getPassengers()

//        val data = Transformations.switchMap(_passengersListLiveData) {
//            val d = it.filter { pas ->
//                Log.d("testy", pas.name.toString())
//                pas.name!!.contains("aya", true)
//            }
//            return@switchMap MutableLiveData(d)
//        }.cachedIn(viewModelScope)

    }

    fun setSearchItem(searchItem: MenuItem) {
        _searchItemLiveData.value = searchItem

    }

    fun search(query: String) {
        _searchTextLiveData.value = query
        getPassengers()
    }

    fun refresh() {
        getPassengers()
    }

    fun deletePassenger(id: String) {
        compositeDisposable
            .add(
                repository.deletePassenger(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            _isDeletedLiveData.value = true
                        },
                        {
                            _isDeletedLiveData.value = false
                        }
                    )
            )
    }

    private fun getPassengers() {
        compositeDisposable.add(
            repository.getPassengers(_searchTextLiveData.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .cachedIn(viewModelScope)
                .subscribe(
                    {
                        _passengersListLiveData.value = it
                    },
                    {
                    }
                )
        )
    }
}