package com.udacity.asteroidradar

object AsteroidOverviewScreen: AsteroidDestinations{
    override val route = "Overview"

}

object AsteroidDetailScreen: AsteroidDestinations{
    override val route: String = "Detail"
    const val asteroidDetailTypeArg = "asteroid_id"
    val routeWithArgs = "${route}/{${asteroidDetailTypeArg}}"
    //it is not recommended to pass complex data structures into NavGrapg args so we will save
    //the selected Asteroid in ViewModel instead and reference it through its "id"
   /* val arguments = listOf(
        navArgument(asteroidDetailTypeArg) { type = NavType.LongType}
    )*/
}

interface AsteroidDestinations{
    val route : String
}