package com.udacity.asteroidradar.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.asteroidComposed.R
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.testAsteroid
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidDetailComposed(asteroid: Asteroid, paddingValues: PaddingValues){

    DisposableEffect(Unit) {
        val timer = Timer()
        timer.scheduleAtFixedRate(0, 1000) {
            // Update UI every second
        }
        onDispose {
            timer.cancel()
            Log.v("dispose", "AsteroidDetailComposed disposed")

        }
    }


    val image = if (asteroid.isPotentiallyHazardous) R.drawable.asteroid_hazardous else R.drawable.asteroid_safe
    val modifier = Modifier.padding(5.dp)
    val openAlertDialog = rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        Spacer(Modifier.height(10.dp))
    Image(painter = painterResource((image)), "" ,modifier = Modifier.fillMaxWidth().padding(5.dp))
    Column(modifier = Modifier.verticalScroll(state = rememberScrollState()).padding(5.dp).fillMaxWidth()) {

        Text(stringResource(R.string.close_approach_date), modifier = modifier)
        Text(asteroid.closeApproachDate, modifier = modifier)

        Row (modifier = Modifier.fillMaxWidth() ) {
            Column() {
                Text(stringResource(R.string.absolute_magnitude), modifier = modifier)
                Text(asteroid.absoluteMagnitude.toString() + " ${R.string.astronomical_unit_format}", modifier = modifier)
            }
            Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Icon(painterResource(R.drawable.ic_help_circle), "", modifier = Modifier.clickable(enabled = true) {
                     openAlertDialog.value = true
                })
            }
        }
        Text(stringResource(R.string.estimated_diameter), modifier = modifier)
        Text(asteroid.estimatedDiameter.toString() + " ${R.string.km_unit_format}", modifier = modifier)

        Text(stringResource(R.string.relative_velocity_title), modifier = modifier)
        Text(asteroid.relativeVelocity.toString() + " ${R.string.km_s_unit_format}", modifier = modifier)

        Text(stringResource(R.string.distance_from_earth_title), modifier = modifier)
        Text(asteroid.distanceFromEarth.toString() + " ${R.string.astronomical_unit_format}", modifier = modifier)
    }
    }
    //observing a boolean state to determine if dialog is to be shown as suggested here:
    //https://stackoverflow.com/questions/69592115/use-a-composable-inside-of-a-modifier-clickable
    if (openAlertDialog.value){
        BasicAlertDialog( onDismissRequest = {openAlertDialog.value = false}) {
            Card() {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.absolute_magnitude_info))
                    Button(
                        onClick = { openAlertDialog.value = false },
                        modifier = Modifier.align(Alignment.End).padding(5.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AsteroidDetailPreview(){
    AsteroidDetailComposed(testAsteroid, PaddingValues(15.dp))
}

