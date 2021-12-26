package com.example.airlinesapp.di.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.models.PassengerPost
import com.example.airlinesapp.ui.home.passengers.PassengersDataSource
import com.example.airlinesapp.util.Constants.PASSENGERS_PER_PAGE
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val apiService: ApiService) {

    fun getAirlines(): Single<List<AirLine>> {
        return apiService.getAirlines().subscribeOn(Schedulers.io())
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
        ).flowable
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