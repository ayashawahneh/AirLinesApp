package com.example.airlinesapp.di.repo

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.example.airlinesapp.di.network.ApiService
import com.example.airlinesapp.di.network.models.AirLine
import com.example.airlinesapp.models.AirlineWithFavoriteFlagItem
import com.example.airlinesapp.di.network.models.Passenger
import com.example.airlinesapp.di.network.models.PassengerPost
import com.example.airlinesapp.di.dataStore.DataStoreManager
import com.example.airlinesapp.ui.home.passengers.PassengersDataSource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton
import io.reactivex.internal.operators.single.SingleDelayWithObservable
import java.util.*

@ExperimentalCoroutinesApi
@Singleton
class Repository @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

    fun saveFavoriteIdsToDataStore(favoriteAirlineIdsList: List<String>) {
        dataStoreManager.saveFavoriteIds(favoriteAirlineIdsList.toSet())
    }

    fun getFavoriteIdsFromDataStore(): MutableList<String>? {
        return dataStoreManager.getFavouriteIds().blockingFirst()?.toMutableList()
    }

    @SuppressLint("CheckResult")
    fun getAirlines(): Single<List<AirlineWithFavoriteFlagItem>> {
        val result = apiService.getAirlines().toObservable()
            .zipWith(dataStoreManager.getFavouriteIds()) { airlineList, favoriteIdsListFromDataStore ->
                Log.d("dddd", "in")
                val favoriteAirlinesList = mutableListOf<AirlineWithFavoriteFlagItem>()
                if (favoriteIdsListFromDataStore.isEmpty()) {
                    for (airlineItem in airlineList) {
                        if (!airlineItem.id.isNullOrEmpty())
                            favoriteAirlinesList.add(
                                AirlineWithFavoriteFlagItem.create(airlineItem, false)
                            )
                    }
                } else {
                    for (airlineItem in airlineList) {
                        if (!airlineItem.id.isNullOrEmpty()) {
                            if (airlineItem.id in favoriteIdsListFromDataStore) {
                                favoriteAirlinesList.add(
                                    AirlineWithFavoriteFlagItem.create(airlineItem, true)
                                )
                            } else {
                                favoriteAirlinesList.add(
                                    AirlineWithFavoriteFlagItem.create(airlineItem, false)
                                )
                            }
                        }
                    }
                }
                return@zipWith favoriteAirlinesList.toList()
            }
        return SingleDelayWithObservable.fromObservable(result)
            .subscribeOn(Schedulers.io())
    }

    fun getPassengers(searchedText: String): Observable<PagingData<Passenger>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = { PassengersDataSource(apiService, searchedText) }
        ).observable
    }

    fun addNewAirline(airlineData: AirLine): Single<AirLine> {
        return apiService.addNewAirline(airlineData).subscribeOn(Schedulers.io())
    }

    fun addNewPassenger(passengerData: PassengerPost): Single<Passenger> {
        return apiService.addNewPassenger(passengerData).subscribeOn(Schedulers.io())
    }

    fun editPassenger(passengerId: String, passengerData: PassengerPost): Single<Passenger> {
        return apiService.editPassenger(passengerId, passengerData).subscribeOn(Schedulers.io())
    }

    fun deletePassenger(passengerId: String): Single<Any> {
        return apiService.deletePassenger(passengerId).subscribeOn(Schedulers.io())
    }
}