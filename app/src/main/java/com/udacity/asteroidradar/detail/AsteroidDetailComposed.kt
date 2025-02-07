package com.udacity.asteroidradar.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.copyovertest.R
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.asteroidEntity
import com.udacity.asteroidradar.testAsteroid

@Composable
fun AsteroidDetailComposed(asteroid: Asteroid){
    val image = if (asteroid.isPotentiallyHazardous) R.drawable.asteroid_hazardous else R.drawable.asteroid_safe
    val modifier = Modifier.padding(5.dp)
    Column(modifier = Modifier.fillMaxWidth()) {
    Image(painter = painterResource((image)), "" ,modifier = Modifier.fillMaxWidth())
    Column(modifier = Modifier.verticalScroll(state = rememberScrollState()).padding(5.dp).fillMaxWidth()) {

        Text(stringResource(R.string.close_approach_date), modifier = modifier)
        Text(asteroid.closeApproachDate, modifier = modifier)

        Row (modifier = Modifier.fillMaxWidth() ) {
            Column() {
                Text(stringResource(R.string.absolute_magnitude), modifier = modifier)
                Text(asteroid.absoluteMagnitude.toString() + " ${R.string.astronomical_unit_format}", modifier = modifier)
            }
            Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Icon(painterResource(R.drawable.ic_help_circle), "")
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
}

@Preview
@Composable
fun AsteroidDetailPreview(){
    AsteroidDetailComposed(testAsteroid)
}

