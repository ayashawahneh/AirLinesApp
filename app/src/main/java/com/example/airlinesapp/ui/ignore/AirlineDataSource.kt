package com.example.airlinesapp.ui.ignore

//class AirlineDataSource (private val retrofitInterface: RetrofitInterface,private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, AirLine>() {
//private var page = FIRST_PAGE
//    val networkState : MutableLiveData<NetworkState> = MutableLiveData()
//    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AirLine>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AirLine>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun loadInitial(
//        params: LoadInitialParams<Int>,
//        callback: LoadInitialCallback<Int, AirLine>
//    ) {
//       networkState.postValue(NetworkState.LOADING)
//        compositeDisposable.add(retrofitInterface.getAirlines())
//    }
//}