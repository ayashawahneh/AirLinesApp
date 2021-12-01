package com.example.airlinesapp.ui.home.passengers


import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.airlinesapp.di.network.ApiService
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.models.PassengersResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PassengersDataSource(private val apiService: ApiService) :
    RxPagingSource<Int, Passenger>() {
    override fun getRefreshKey(state: PagingState<Int, Passenger>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Passenger>> {
        val position = params.key ?: 1
        return apiService.getPassengers(position)
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, position) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: PassengersResponse, position: Int): LoadResult<Int, Passenger> {
        return LoadResult.Page(
            data = data.data,
            prevKey = null,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }
}