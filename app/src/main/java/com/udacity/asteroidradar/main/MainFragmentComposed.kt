package com.udacity.asteroidradar.main

import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.udacity.asteroidradar.AsteroidDetailScreen
import com.udacity.asteroidradar.AsteroidOverviewScreen
// for a 'val' variable
import androidx.compose.runtime.getValue

// for a `var` variable also add
import androidx.compose.runtime.setValue

// or just
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.udacity.asteroidradar.MenuItem

@Composable
fun AsteroidNavHost(navHostController: NavHostController, asteroidList: State<List<Asteroid>?>) {
    NavHost(
        navController = navHostController, startDestination = AsteroidOverviewScreen.route,
        modifier = Modifier
    ) {
        composable(route = AsteroidOverviewScreen.route) {
            ObserveMasterList(asteroidList) {
                //onClick Asteroid
                    asteroid: Asteroid ->
                navHostController.navigate(AsteroidDetailScreen.route)
            }
        }
        composable(route = AsteroidDetailScreen.route) {

        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        HomeScreen(mainViewModel = viewModel())
    }
}

@Composable
fun MinimalDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Option 1") },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Option 2") },
                onClick = { /* Do something... */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    MaterialTheme {
        Scaffold { padding ->
            var expanded by rememberSaveable { mutableStateOf(false) }
            val navController = rememberNavController()
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Asteroid App") }, actions = {
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxSize()
                        ) {
                            IconButton(onClick = { expanded = !expanded }, modifier = Modifier.align(Alignment.TopEnd)) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More options")

                                DropdownMenu(
                                    modifier = Modifier.padding(16.dp), expanded = expanded, onDismissRequest = { expanded = false },
                                    containerColor = Color(color = Color.Blue.value.toInt())
                                ) {
                                    DropdownMenuItem(text = { Text("Asteroid of Week") },
                                        onClick = {
                                            //have to do this because we can't reference states which are
                                            //composables in onClick, which is not a composable
                                            mainViewModel.menuItemSelected.value = "Weekly"
                                        })
                                    DropdownMenuItem(text = { Text("Todays Asteroid") }, onClick = {
                                        mainViewModel.menuItemSelected.value = "Today"
                                    })
                                    DropdownMenuItem(text = { Text("Saved") }, onClick = {
                                        mainViewModel.menuItemSelected.value = "Saved"
                                    })
                                }
                            }
                        }
                    })
                }
            ) { padding ->

                val currentMenuItem = mainViewModel.menuItemSelected.observeAsState()
                //TODO make sure that the when conditions are called when switching from menu items

                when (currentMenuItem.value) {
                    MenuItem.WEEKLY.type -> mainViewModel._masterList.value = mainViewModel.weekList.observeAsState().value
                    MenuItem.TODAY.type -> mainViewModel._masterList.value = mainViewModel.domainAsteroidTodayList.observeAsState().value
                    MenuItem.SAVED.type -> mainViewModel._masterList.value = mainViewModel.domainAsteroidSavedList.observeAsState().value
                }
                AsteroidNavHost(navController, mainViewModel.masterList.observeAsState())
            }

        }

    }
}

@Composable
fun ObserveMasterList(asteroidList: State<List<Asteroid>?>, onClick: (Asteroid) -> Unit): Unit {
    //state hoisting
    asteroidList.value?.let {
        DisplayAsteroidList(it, onClick)
    }
}

@Composable
fun DisplayAsteroidList(asteroid: List<Asteroid>, onClick: (Asteroid) -> Unit) {
    Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
        LazyColumn(modifier = Modifier) {
            items(asteroid) { item ->
                AsteroidCardItem(item, onClick)
            }
        }
    }
}
