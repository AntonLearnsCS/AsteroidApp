package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.pictureOfDayApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application)
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
            //Log.i("viewModel repo size: ",AsteroidRepository.domainAsteroidList.value?.size.toString())
        }
    }
    var list : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidList

    private val _detailClick = MutableLiveData<Asteroid>() //will set MutableLiveData to null
    val detailClick : LiveData<Asteroid>
    get() = _detailClick

    private val _pictureOfDay = MutableLiveData<PictureOfDay>() //will set MutableLiveData to null
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    fun detailClick(asteroid: Asteroid)
    {
        _detailClick.value = asteroid
    }
    fun completeClick()
    {
        _detailClick.value = null
    }
    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                    _pictureOfDay.value = pictureOfDayApi.retrofitService.getPicture(apiKey)
            } catch (e: Exception) {
                println("error: " + e)
                //_status.value = "Failure: ${e.message}"
            }
        }
    }


    fun getAsteroidDate()
    {
        viewModelScope.launch {
        }
    }

}
