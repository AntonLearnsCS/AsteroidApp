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
            getPictureOfDay()
        }
       // pictureOfDay.value?.url?.let { Log.i("viewModel", it) }
    }
    var menuItemSelected = MutableLiveData("Week")

    var textDescription = "List of " + menuItemSelected.value + " Asteroid"

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

    var _masterList = MutableLiveData<List<Asteroid>>()
    var masterList : LiveData<List<Asteroid>> = _masterList


    var weekList : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidList

    //Self note: Should I encapsulate Room data retrieval in viewModelScope?

    var domainAsteroidTodayList : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidTodayList

    var domainAsteroidSavedList : LiveData<List<Asteroid>> = AsteroidRepository.domainAsteroidSavedList


   /* fun setMasterToSaved(saved : LiveData<List<Asteroid>>)
    {
        domainAsteroidTodayList.value?.get(0)?.closeApproachDate?.let { Log.i("MainViewToday", it) }
        _masterList.value = saved.value
    }*/

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
        try {
            _pictureOfDay.value = pictureOfDayApi.retrofitService.getPicture(apiKey)
        }
        catch (e : Exception)
        {
            Log.i("Main Exception: ", e.toString())
        }
    }
}
