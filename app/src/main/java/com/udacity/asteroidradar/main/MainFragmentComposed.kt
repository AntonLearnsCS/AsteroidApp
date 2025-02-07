package com.udacity.asteroidradar.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.udacity.asteroidradar.AsteroidDetailScreen
import com.udacity.asteroidradar.AsteroidOverviewScreen


@Composable
fun AsteroidNavHost(navHostController: NavHostController, asteroidList: LiveData<List<Asteroid>>){
    NavHost(navController = navHostController, startDestination = AsteroidOverviewScreen.route,
        modifier = Modifier) {
        composable(route = AsteroidOverviewScreen.route) {
            ObserveMasterList(asteroidList){
                //onClick Asteroid
                asteroid: Asteroid ->
                navHostController.navigate(AsteroidDetailScreen.route)
            }
        }
        composable(route = AsteroidDetailScreen.route) {

        }
    }

}

@Composable
fun HomeScreen(mainViewModel: MainViewModel): Unit{

MaterialTheme{
    val navController = rememberNavController()

    AsteroidNavHost(navController, mainViewModel.masterList)
}

}

@Composable
fun ObserveMasterList(asteroidList: LiveData<List<Asteroid>>, onClick: (Asteroid) -> Unit): Unit{
    //state hoisting
    val list : State<List<Asteroid>?> = asteroidList.observeAsState()

    list.value?.let { DisplayAsteroidList(it, onClick)
    }
}

@Composable
fun DisplayAsteroidList(asteroid: List<Asteroid>, onClick: (Asteroid) -> Unit){
    Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background){
        LazyColumn (modifier = Modifier) {
                items(asteroid){ item ->
                    AsteroidCardItem(item, onClick)
                }
        }
    }
}