package com.example.airlinesapp.ui.home.passengers.deletePassenger

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.di.network.ApiRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DeletePassengerViewModel @Inject constructor(private val apiRepository: ApiRepository):ViewModel(){
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun deletePassenger(id:String): Boolean {
        var isDeleted = false

        compositeDisposable
            .add(
                apiRepository.deletePassenger(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.d("deletePas", it)
                            isDeleted  = true
                        },
                        {
                            Log.d("ErrorPas", it.toString())
                            isDeleted  = false
                        }
                    )
            )
        return isDeleted
    }
}