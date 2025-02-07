package com.udacity.asteroidradar

object AsteroidOverviewScreen: AsteroidDestinations{
    override val route = "Overview"

}

object AsteroidDetailScreen: AsteroidDestinations{
    override val route: String = "Detail"
}

interface AsteroidDestinations{
    val route : String
}