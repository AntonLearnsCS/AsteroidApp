package com.udacity.asteroidradar.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.asteroidComposed.R
import com.udacity.asteroidradar.Asteroid


@Composable
fun AsteroidCardItem(asteroid: Asteroid, onClick: (Asteroid) -> Unit){
    Row(modifier = Modifier.clickable { onClick(asteroid)}.fillMaxWidth().fillMaxHeight(.5F)) {
        Column(modifier = Modifier.fillMaxWidth().weight(1F)) {
            Text(text = asteroid.codename, textAlign = TextAlign.Start, modifier = Modifier.padding(5.dp,5.dp,25.dp,5.dp))
            Text(text = asteroid.closeApproachDate, textAlign = TextAlign.Start, modifier = Modifier.padding(5.dp,5.dp,25.dp,5.dp))
        }
        Image(
            painter = painterResource(id =
            if (asteroid.isPotentiallyHazardous) R.drawable.ic_status_potentially_hazardous
            else R.drawable.ic_status_normal),
            contentDescription = "Asteroid hazard level image",
            alignment = AbsoluteAlignment. CenterRight,
            modifier = Modifier.weight(1F).fillMaxSize()
        )
    }
}

@Composable
@Preview
fun AsteroidCardItemPreview() {
    AsteroidCardItem(Asteroid(3, "testName","2/23/2028", 20.0,20.0,160.0,16888888.0, false), {})
}