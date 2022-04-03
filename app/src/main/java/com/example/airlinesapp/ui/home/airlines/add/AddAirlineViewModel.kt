package com.example.airlinesapp.ui.home.airlines.add

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airlinesapp.R
import com.example.airlinesapp.di.repo.Repository
import com.example.airlinesapp.di.network.models.AirLine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddAirlineViewModel
@Inject constructor(private val repository: Repository) :
    ViewModel() {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var name = String()
        private set
    private var country = String()
    private var headQuarter = String()
    private var slogan = String()
    private var website = String()
    private var established = String()

    private val _enableSubmitButtonLiveData = MutableLiveData(false)
    val enableSubmitButtonLiveData: LiveData<Boolean> = _enableSubmitButtonLiveData

    private val _isSentLiveData = MutableLiveData<Boolean>()
    val isSentLiveData: LiveData<Boolean> = _isSentLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setCountry(country: String) {
        this.country = country
    }

    fun setHeadQuarter(headQuarter: String) {
        this.headQuarter = headQuarter
    }

    fun setSlogan(slogan: String) {
        this.slogan = slogan
    }

    fun setEstablished(established: String) {
        this.established = established
    }

    fun setWebsite(website: String) {
        this.website = website
    }

    fun setEnableSubmitButton() {
        _enableSubmitButtonLiveData.value = (
                validateRequiredFields(name) == null
                        && validateRequiredFields(country) == null
                        && validateRequiredFields(headQuarter) == null
                        && validateSlogan() == null
                        && validateWebsite() == null
                )
    }

    fun validateSlogan(): Int? {
        return if (slogan.isEmpty()) {
            null
        } else {
            if (slogan.length < 3) {
                R.string.too_short
            } else {
                null
            }
        }
    }

    fun validateWebsite(): Int? {
        return if (website.isEmpty()) {
            null
        } else {
            if (!(Patterns.WEB_URL.matcher(website).matches())) {
                R.string.invalid_website
            } else {
                null
            }
        }
    }

    fun validateRequiredFields(str: String?): Int? {
        return when {
            str.isNullOrEmpty() -> {
                R.string.required
            }
            str.length < 3 -> {
                R.string.too_short
            }
            else -> null
        }
    }

    fun addAirline() {
        val airlineData = AirLine(
            country = country,
            established = established,
            slogan = slogan,
            name = name,
            headQuaters = headQuarter,
            website = website,
        )

        compositeDisposable
            .add(
                repository.addNewAirline(airlineData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.d("addNewAir", it.name)
                            _isSentLiveData.value = true
                        },
                        {
                            Log.d("addNewAir", it.message.toString())
                            _isSentLiveData.value = false
                        }
                    )
            )
    }
}