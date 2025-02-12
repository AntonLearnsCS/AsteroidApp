package com.udacity.asteroidradar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.copyovertest.R
import com.udacity.asteroidradar.main.HomeScreen
import com.udacity.asteroidradar.main.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel = MainViewModel(application)
                    //will return the local viewModel that we have created. Not sure how it is known what viewModel is to be selected
                    HomeScreen( mainViewModel = viewModel)
                }
            }
        }
      /*  setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)*/
    }

    //Q: Up button is not working while back button is exiting application
   /* override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }*/
}
@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen(mainViewModel = viewModel())
}
