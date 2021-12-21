package com.example.airlinesapp.di.network

import android.annotation.SuppressLint
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.models.AirlineWithFavoriteFlag
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.models.PassengerPost
import com.example.airlinesapp.ui.home.airlines.DataStoreManager
import com.example.airlinesapp.ui.home.passengers.PassengersDataSource
import com.example.airlinesapp.util.Constants.PASSENGERS_PER_PAGE
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton
import io.reactivex.internal.operators.single.SingleDelayWithObservable

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
    fun getAirlines(): Single<List<AirlineWithFavoriteFlag>> {
        val result = apiService.getAirlines().toObservable()
            .zipWith(dataStoreManager.getFavouriteIds()) { airlineList, favoriteIdsList ->

                val favorites = mutableListOf<AirlineWithFavoriteFlag>()
                if (favoriteIdsList.isEmpty()) {
                    for (item in airlineList)
                        favorites.add(AirlineWithFavoriteFlag(item, false))
                } else {
                    for (item in airlineList) {
                        if (item.id in favoriteIdsList) {
                            favorites.add(AirlineWithFavoriteFlag(item, true))
                        } else {
                            favorites.add(AirlineWithFavoriteFlag(item, false))
                        }
                    }
                }
                return@zipWith favorites.toList()
            }
        return SingleDelayWithObservable.fromObservable(result)
            .subscribeOn(Schedulers.io())
    }

    fun getPassengers(): Flowable<PagingData<Passenger>> {
        return Pager(
            config = PagingConfig(
                pageSize = PASSENGERS_PER_PAGE,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = { PassengersDataSource(apiService) }
        )
            .flowable
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
