package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

private val _detailClick = MutableLiveData<Asteroid>() //will set MutableLiveData to null
    val detailClick : LiveData<Asteroid>
    get() = _detailClick


    fun detailClick(asteroid: Asteroid)
    {
        _detailClick.value = asteroid
    }
    fun completeClick()
    {
        _detailClick.value = null
    }



    fun getAsteroidDate()
    {
        viewModelScope.launch {
        }
    }

}
