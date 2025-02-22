package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.pictureOfDayApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel (application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application)
{
/*
    private val database = VideosDatabase.getDatabase(application)
    private val videosRepository = VideosRepository(database)
 */
    private val apiKey = "RGSQocYE7wIA2WbGRDSi4UnGJ6AgojgzFduwGOCJ"

    private val database = AsteroidDatabase.getInstance(application)

    private val AsteroidRepository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            AsteroidRepository.refreshAsteroidList()
            getPictureOfDay()
        }
    }
    /*fun refreshFun()
    {
        viewModelScope.launch {
            AsteroidRepository.refreshAsteroidList()
        }
    }*/
    var menuItemSelected = MutableLiveData("Weekly")

    //private val showPod = MutableLiveData<Boolean>(savedStateHandle.get<Boolean>())


     var showPod : MutableLiveData<Boolean>?
        set(value) {
            if (value == null){
                savedStateHandle["showPod"] = true
            }
            else {
                savedStateHandle["showPod"] = value.value
            }
        }
        get() {
            return savedStateHandle.getLiveData<Boolean>("showPod")
        }

    var detailClick : MutableLiveData<Asteroid>
        set(value) {
            if (value == null){
                savedStateHandle["detailClick"] = Asteroid()
            }
            else {
                savedStateHandle["detailClick"] = value.value?.id
            }
        }
    get() {
        return (savedStateHandle.getLiveData<Asteroid>("detailClick"))
    }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>() //will set MutableLiveData to null
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    val _masterList = MutableLiveData<List<Asteroid>>()
    val masterList : LiveData<List<Asteroid>>
    get() = _masterList


    var weekList : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidList

    var domainAsteroidTodayList : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidTodayList

    var domainAsteroidSavedList : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidSavedList

    fun changeShowPod(boolean: Boolean){
        showPod?.value = boolean
    }

    fun detailClick(asteroid: Asteroid)
    {
       detailClick.value = asteroid
    }
    fun completeClick()
    {
        detailClick.value = null
    }
    //if I treat this as another function with a viewModelScope.launch{} then the api will not load the data...
    suspend fun getPictureOfDay() {
        try {
            _pictureOfDay.value = pictureOfDayApi.retrofitService.getPicture(apiKey)
        }
        catch (e : Exception)
        {
            Log.i("Main Exception: ", e.toString())
        }
    }
}
