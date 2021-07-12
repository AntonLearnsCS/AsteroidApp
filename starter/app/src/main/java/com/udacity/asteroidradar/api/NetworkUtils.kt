package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.asteroidEntity
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
//will use instead of Moshi
//private val retrofit = Retrofit.Builder()
//        .addConverterFactory(MoshiConverterFactory.create(parseAsteroidsJsonResult))
fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")

            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val asteroid = Asteroid(
                id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
            )
            asteroidList.add(asteroid)
        }
    }

    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}
//no need since parseAsteroidsJsonResult will convert a JSON object to a List<Asteroid>

/*@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val networkAsteroidContainer: List<networkAsteroid>)

@JsonClass(generateAdapter = true)
    data class networkAsteroid(val id: Long, val codename: String, val closeApproachDate: String,
val absoluteMagnitude: Double, val estimatedDiameter: Double,
val relativeVelocity: Double, val distanceFromEarth: Double,
val isPotentiallyHazardous: Boolean)*/

//extension function that converts from data transfer objects to database objects
//note that the database objects are just an array of entities/data classes
//here we have an extension function from the data class "NetworkVideoContainer" that takes the parameter of the data class
//which is of type "networkAsteroid" and maps it to a type of "asteroidEntity". Note that both data classes (networkAsteroid
// and asteroidEntity) are exactly the same except for their annotation. This is where the seperation of concerns comes in b/c
//we are annotating the "networkAsteroid" dataclass with "JsonClass", which lets Android know to store the network result in
//"networkAsteroid" instead of the database dataclass annotated with "@entity"

fun List<Asteroid>.asDatabaseModel(): Array<asteroidEntity> {
    return map {
        asteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

    //In this case, by simply restating the parameters we can convert asteroidEntity to Asteroid since they only differ by
    //annotation
    fun List<asteroidEntity>.asDomainModel(): List<Asteroid> {
        return map {
            Asteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
            )
        }
    }
