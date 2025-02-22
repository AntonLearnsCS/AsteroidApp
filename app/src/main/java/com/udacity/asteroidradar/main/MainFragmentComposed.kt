package com.udacity.asteroidradar.main

// for a 'val' variable

// for a `var` variable also add

// or just
//import coil3.ImageLoader
//import coil.ImageLoader
//import coil.compose.AsyncImage
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import com.example.copyovertest.R
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidDestinations
import com.udacity.asteroidradar.AsteroidDetailScreen
import com.udacity.asteroidradar.AsteroidOverviewScreen
import com.udacity.asteroidradar.MenuItem
import com.udacity.asteroidradar.detail.AsteroidDetailComposed
import java.util.logging.Level
import java.util.logging.Logger

@Composable
fun AsteroidNavHost(navHostController: NavHostController, asteroidList: State<List<Asteroid>?>, padding: PaddingValues,
    mainViewModel: MainViewModel) {
    NavHost(
        navController = navHostController, startDestination = AsteroidOverviewScreen.route,
        modifier = Modifier.fillMaxWidth().padding(padding)
    ) {
        composable(route = AsteroidOverviewScreen.route) {

            ObserveMasterList(asteroidList, mainViewModel.pictureOfDay.observeAsState().value?.url) {
                //onClick Asteroid
                    asteroid: Asteroid ->

                // It is not recommended to pass complex data structures in the NavGraph:
                //https://developer.android.com/develop/ui/compose/navigation#nav-with-args
                //unfortunately we have to pass in the viewModel here if we are to supply the arg for the
                //AsteroidDetailScreen arguments parameter
                mainViewModel.detailClick.value = asteroid
                navHostController.navigate(AsteroidDetailScreen.route)
            }
        }
        composable(route = AsteroidDetailScreen.route) {
            //so technically we're supposed to used the arguments field but doing so would force
            //us to use the solution here: https://stackoverflow.com/questions/65610003/pass-parcelable-argument-with-compose-navigation
            //which is viable but not recommended.
            mainViewModel.detailClick.value?.let { it1 -> AsteroidDetailComposed(it1, padding) }
                ?: Toast.makeText(LocalContext.current, "Error", Toast.LENGTH_SHORT).show()
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
                            modifier = Modifier.padding(5.dp)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            IconButton(onClick = { expanded = !expanded }, modifier = Modifier.align(Alignment.TopEnd)) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More options")

                                DropdownMenu(
                                    modifier = Modifier.background(MaterialTheme.colorScheme.background), expanded = expanded, onDismissRequest = { expanded = false },
                                    containerColor = Color(color = Color.Blue.value.toInt()), tonalElevation = 3.dp, shadowElevation = 1.dp,
                                ) {
                                    DropdownMenuItem(text = { Text("Asteroid of Week") },
                                        onClick = {
                                            //have to do this because we can't reference states which are
                                            //composables in onClick, which is not a composable
                                            mainViewModel.menuItemSelected.value = "Weekly"
                                            navController.navigate(AsteroidOverviewScreen.route)
                                        })
                                    DropdownMenuItem(text = { Text("Todays Asteroid") }, onClick = {
                                        mainViewModel.menuItemSelected.value = "Today"
                                        navController.navigate(AsteroidOverviewScreen.route)
                                    })
                                    DropdownMenuItem(text = { Text("Saved") }, onClick = {
                                        mainViewModel.menuItemSelected.value = "Saved"
                                        navController.navigate(AsteroidOverviewScreen.route)
                                    })
                                }
                            }
                        }
                    }, modifier = Modifier.padding(5.dp))
                }
            ) { padding ->

                    val currentMenuItem = mainViewModel.menuItemSelected.observeAsState()
                    //TODO make sure that the when conditions are called when switching from menu items

                    when (currentMenuItem.value) {
                        MenuItem.WEEKLY.type -> mainViewModel._masterList.value = mainViewModel.weekList.observeAsState().value
                        MenuItem.TODAY.type -> mainViewModel._masterList.value = mainViewModel.domainAsteroidTodayList.observeAsState().value
                        MenuItem.SAVED.type -> mainViewModel._masterList.value = mainViewModel.domainAsteroidSavedList.observeAsState().value
                    }

                    Spacer(Modifier.height(5.dp))

                    AsteroidNavHost(navController, mainViewModel.masterList.observeAsState(), padding, mainViewModel)

                }
            }

        }

    }



    //unable to use Toast without context
    @Composable
    fun ShowToast(messageToDisplay: String) {
        Toast.makeText(LocalContext.current, messageToDisplay, Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun ObserveMasterList(asteroidList: State<List<Asteroid>?>, url: String?, onClick: (Asteroid) -> Unit): Unit {
        //we use this Column to save space for the PictureOfTheDay. Otherwise the lazyList will fill
        //the spot and then be pushed down when POD is rendered, which does not look clean
        //however this creates a rendering issue where composables beneath this composable will be shifted down
        //if we use a boolean flag to make this Column composable shrink to size 0.dp, there is still a rendering lapse
        //that looks janky
        Column {
            val imageLoader = ImageLoader.Builder(LocalContext.current).logger(DebugLogger())
                .build()
            val model = ImageRequest.Builder(LocalContext.current)
                .data(url ?: "")
                .build()
            AsyncImage(
                model = model,
                "Picture of the day",
                placeholder = painterResource(R.drawable.ic_help_circle),
                imageLoader = imageLoader,
                modifier = Modifier.fillMaxWidth().padding(5.dp).fillMaxHeight(.4F),
                onSuccess = {},
                onError = { Log.v("async", "error loading image") },
                onLoading = {})
            //state hoisting
            asteroidList.value?.let {
                DisplayAsteroidList(it, onClick)
            }
        }
    }

    @Composable
    fun DisplayAsteroidList(asteroid: List<Asteroid>, onClick: (Asteroid) -> Unit) {
        Surface(modifier = Modifier.fillMaxHeight(), color = MaterialTheme.colorScheme.background) {
            LazyColumn(modifier = Modifier) {
                items(asteroid) { item ->
                    AsteroidCardItem(item, onClick)
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val test = MainViewModel(application = Application(), SavedStateHandle())
            test._masterList.value = listOf()
            test.menuItemSelected.value = "Weekly"

            HomeScreen(mainViewModel = test)
        }
    }
}

/*
@Composable
fun VideoThumbnail(
    modifier: Modifier = Modifier,
    url: String = "",
) {
    AndroidView(
        modifier = modifier,
        factory = { ImageView(it).loadVideoUrl(url) },
        update = { it.loadVideoUrl(url) }
    )
}

fun ImageView.loadVideoUrl(url: String) = this.apply {
    load(url) {
        decoderFactory { result, options, _ ->
            VideoFrameDecoder(
                result.source,
                options
            )
        }
    }
    scaleType = ImageView.ScaleType.CENTER_CROP
}


 //TODO:
 //attempted to get video thumbnail using okhttp but kept running into error: "setDataSource failed: status = 0x80000000", followed recommendation
 //on coil documentation. Maybe try upgrading the version of coil-video dependency but that would need API 35, which would require an update of other
 //dependencies. Will have to try that in the future. So for now we are not able to get the picture of the day if the url points to a video and not
 //an image. The imports are not the issue as I was using only coil3 packages


AndroidView(
                        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
                        factory = { context ->
                            // Creates view
                            ImageView(context).apply {
                                // Sets up listeners for View -> Compose communication
                                setOnClickListener {
                                }
                            }
                        },
                        update = { view ->

                            /*val videoEnabledLoader = ImageLoader.Builder(view.context).logger(coil.util.DebugLogger())
                                .components {
                                    add(VideoFrameDecoder.Factory())
                                }.build()*/

                            val imageLoader = ImageLoader.Builder(view.context).logger(DebugLogger())
                                .components {
                                    add(OkHttpNetworkFetcherFactory(
                                        callFactory = {
                                            OkHttpClient()
                                        }
                                    )
                                    )
                                    add(VideoFrameDecoder.Factory())
                                }
                                .build()
                            view.findViewTreeLifecycleOwner()?.let { mainViewModel.pictureOfDay.observe(it){
                                    pictureOfDay ->
                                view.load(data = ("https://www.youtube.com/watch?v=ukCSRYcjSQw&ab_channel=APODVideos"), imageLoader = imageLoader){
                                    decoderFactory { result, options, _ -> VideoFrameDecoder(result.source, options) }
                                }
                            } }
                        }
                    )





 */

