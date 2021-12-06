package com.example.airlinesapp.ui.ignore

//
//class AirlinesViewModel(private val airlinesRepository: API, application: Application) : AndroidViewModel(application){
//    private var liveDataList: MutableLiveData<List<AirLine>>
//    init {
//        (application as MyApplication).getRetroComponent().inject(this)
//        liveDataList = MutableLiveData()
//    }
//
//    @Inject
//    lateinit var compositeDisposable : CompositeDisposable
//
//    val airlines:LiveData<List<AirLine>> by lazy {
//        airlinesRepository.fetchAllAirlines()
//    }
//
//    val networkState : LiveData<NetworkState> by lazy {
//        airlinesRepository.getAirlinesNetworkState()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        compositeDisposable.dispose()
//    }
//}