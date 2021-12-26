package com.example.airlinesapp.ui.home.airlines

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.rxjava2.rxPreferencesDataStore
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.rxjava2.RxDataStore
import io.reactivex.Single

@Singleton
class DataStoreManager @Inject constructor(val context: Context) {
    private val Context.dataStore: RxDataStore<Preferences> by rxPreferencesDataStore(name = FAVORITE_AIRLINE_DATASTORE)

    @ExperimentalCoroutinesApi
    fun saveFavoriteIds(favoriteAirlineIdsList: Set<String>) {
        context.dataStore
            .updateDataAsync { pref ->
                val mutablePref = pref.toMutablePreferences()
                mutablePref[AIRLINE_IDS] = favoriteAirlineIdsList
                Single.just(mutablePref)
            }
    }

    @ExperimentalCoroutinesApi
    fun getFavouriteIds(): Observable<Set<String>?> {
        val f = context.dataStore.data()
            .map {
                if (it[AIRLINE_IDS] == null) {
                    return@map setOf()
                } else
                    return@map it[AIRLINE_IDS]
            }
        return f.toObservable()
    }

    companion object {

        private const val FAVORITE_AIRLINE_DATASTORE = "datastore"
        private val AIRLINE_IDS = stringSetPreferencesKey("AIRLINE_IDS")
    }
}