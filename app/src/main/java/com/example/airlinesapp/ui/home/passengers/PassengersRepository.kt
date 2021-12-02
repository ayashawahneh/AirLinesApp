package com.example.airlinesapp.ui.home.passengers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.airlinesapp.di.network.ApiService
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.util.Constants.PASSENGERS_PER_PAGE
import io.reactivex.Flowable
import javax.inject.Inject


class PassengersRepository @Inject constructor(private val apiService: ApiService) {

    fun getPassengers(): Flowable<PagingData<Passenger>> {
        return Pager(
            config = PagingConfig(
                pageSize = PASSENGERS_PER_PAGE,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40),
            pagingSourceFactory = { PassengersDataSource(apiService) }
        ).flowable
    }

}
