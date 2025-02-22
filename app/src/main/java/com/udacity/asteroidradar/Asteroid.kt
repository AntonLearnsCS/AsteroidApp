package com.udacity.asteroidradar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serial
import kotlinx.serialization.*
import kotlinx.serialization.KSerializer

//note that the order of the listed variables do not have to coincide with the listed "variables" in the returned JSON array
/*
We will use Serializable instead of Parcelize because we need to use SavedStateHandle
A SerializationException related to SavedStateHandle in Kotlin typically arises when attempting
 to store or retrieve data that isn't serializable by default. SavedStateHandle relies on the
 kotlinx.serialization library for persisting data across configuration changes or process restarts.
 To resolve this, ensure that the data types you're using with SavedStateHandle are properly serializable.
 */


@Serializable
open class Asteroid(val id: Long = 0L, val codename: String = "", val closeApproachDate: String = "",
                    val absoluteMagnitude: Double = 0.0, val estimatedDiameter: Double = 0.0,
                    val relativeVelocity: Double = 0.0, val distanceFromEarth: Double = 0.0,
                    val isPotentiallyHazardous: Boolean = false)

object testAsteroid : Asteroid (1L,"testName", "2/24/36", 253.0, 234234.0, 23423.0, 2234.0, true)

