package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.pictureOfDayApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application)
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
    val test = mutableStateOf(true)
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
