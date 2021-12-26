package com.example.airlinesapp.di.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.ui.home.passengers.PassengersDataSource
import com.example.airlinesapp.util.Constants
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
                pageSize = Constants.PASSENGERS_PER_PAGE,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40),
            pagingSourceFactory = { PassengersDataSource(apiService) }
        ).flowable
    }

}