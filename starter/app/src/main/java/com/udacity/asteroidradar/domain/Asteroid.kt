package com.udacity.asteroidradar.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//note that the order of the listed variables do not have to coincide with the listed "variables" in the returned JSON array
@Parcelize
data class Asteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable