package com.udacity.asteroidradar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//note that the order of the listed variables do not have to coincide with the listed "variables" in the returned JSON array
@Parcelize
open class Asteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable

object testAsteroid : Asteroid (1L,"testName", "2/24/36", 253.0, 234234.0, 23423.0, 2234.0, true)

