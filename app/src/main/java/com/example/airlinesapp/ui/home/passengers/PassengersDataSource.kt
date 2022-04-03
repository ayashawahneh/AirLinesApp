package com.example.airlinesapp.ui.home.passengers

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.airlinesapp.di.network.ApiService
import com.example.airlinesapp.di.network.models.Passenger
import com.example.airlinesapp.di.network.models.PassengersResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PassengersDataSource(
    private val apiService: ApiService, private val searchKey: String
) :
    RxPagingSource<Int, Passenger>() {
    override fun getRefreshKey(state: PagingState<Int, Passenger>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Passenger>> {
        val position = params.key ?: FIRST_PAGE
        return apiService.getPassengers(position)
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, position) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: PassengersResponse, position: Int): LoadResult<Int, Passenger> {
        var newData = data.data
        if (searchKey.isNotEmpty()) {
            newData = newData.filter {

                it.name?.contains(searchKey, true) ?: false
            }
        }
        return LoadResult.Page(
            data = newData,
            prevKey = null,
            nextKey = if (searchKey.isNotEmpty() && (position == data.totalPages || newData.isNotEmpty())) null else position + 1
        )
    }

    companion object {

        private const val FIRST_PAGE = 0
    }
}