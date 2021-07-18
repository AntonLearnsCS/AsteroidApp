package com.udacity.asteroidradar.repository

/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database: AsteroidDatabase) {

    /**
     * A list of Asteroids that can be shown on the screen.
     */

    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDateTime.now()
    /*
    @RequiresApi(Build.VERSION_CODES.O)
    fun adjustDate(current : LocalDateTime ): LocalDateTime
    {
        val constantWeek = current.plusDays(7)
        return constantWeek
    }
   @RequiresApi(Build.VERSION_CODES.O)
    fun convertStringToLocal(myString: String) : LocalDateTime
    {
        val localLocal = LocalDateTime.parse(myString)
        return localLocal
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val formatted = current.format(formatter)


    val domainAsteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.returnAll())
        {
            it.asDomainModel()
        }

    var domainAsteroidTodayList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getToday(formatted))
        {
            it.asDomainModel()
        }

    var domainAsteroidSavedList : LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.returnAll())
        {
            it.asDomainModel()
        }
    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [domainAsteroidList]
     */


    private val apiKey = "RGSQocYE7wIA2WbGRDSi4UnGJ6AgojgzFduwGOCJ"




    /*Calendar calendar=Calendar.getInstance();
//rollback 90 days
    calendar.add(Calendar.DAY_OF_YEAR, -90);*/
    suspend fun refreshAsteroidList() {
        withContext(Dispatchers.IO) {
            //returns a list of Asteroid objects from the network
            try {
                val refreshedAsteroid : List<Asteroid> = parseAsteroidsJsonResult(
                    //we set the interface return type to String that way Retrofit won't demand a converter just yet
                    //we can then convert the String result to a JSONObject using the syntax below
                    JSONObject(
                    AsteroidsApi.retrofitService.getProperties(
                        formatted,
                        apiKey
                    ))
                )
                database.asteroidDao.insertAll(*refreshedAsteroid.asDatabaseModel())
                //Log.i("repo size:", database.asteroidDao.returnAll().value?.size.toString())
                val refreshedPictureOfDay = pictureOfDayApi.retrofitService.getPicture(apiKey)
            }
            catch (e : Exception)
            {
                Log.i("repo","errorRepo")
                Log.e("repo","error",e)
                //e.printStackTrace()
            }
            //Q: Save Picture of day into local database
        }
    }
}
