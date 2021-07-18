package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.asDomainModel
import com.udacity.asteroidradar.api.pictureOfDayApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application)
{
    enum class AsteroidApiFilter(val value: String) { SHOW_WEEK(""), SHOW_TODAY("buy"), SHOW_SAVED("all")
    }
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
            getPictureOfDay()
        }
       // pictureOfDay.value?.url?.let { Log.i("viewModel", it) }
    }
    var menuItemSelected = MutableLiveData("Today")


    var todayDate = AsteroidRepository.formatted

    private val _detailClick = MutableLiveData<Asteroid>() //will set MutableLiveData to null
    val detailClick : LiveData<Asteroid>
    get() = _detailClick

    private val _pictureOfDay = MutableLiveData<PictureOfDay>() //will set MutableLiveData to null
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    /*@RequiresApi(Build.VERSION_CODES.O)
    val todayAsteroidList : List<Asteroid> = database.asteroidDao.getToday(AsteroidRepository.adjustDate(AsteroidRepository.
    convertStringToLocal(AsteroidRepository.formatted)).toString())
        .value!!.asDomainModel()*/

    val masterList = MutableLiveData<List<Asteroid>>()
    var masterMasterList : LiveData<List<Asteroid>> = masterList

    var list : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidList

    //Q: Should I encapsulate Room data retrieval in viewModelScope?
    var domainAsteroidTodayList : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidTodayList

    var domainAsteroidSavedList : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidSavedList

    fun setMasterToSaved(saved : LiveData<List<Asteroid>>)
    {
        domainAsteroidTodayList.value?.get(0)?.closeApproachDate?.let { Log.i("MainViewToday", it) }
        masterMasterList = saved
    }

    fun detailClick(asteroid: Asteroid)
    {
        _detailClick.value = asteroid
    }
    fun completeClick()
    {
        _detailClick.value = null
    }
    //if I treat this as another function with a viewModelScope.launch{} then the api will not load the data...
    suspend fun getPictureOfDay() {
            _pictureOfDay.value = pictureOfDayApi.retrofitService.getPicture(apiKey)
    }
}
