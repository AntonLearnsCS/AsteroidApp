package com.udacity.asteroidradar.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import androidx.test.ext.junit.runners.AndroidJUnit4


@RunWith(AndroidJUnit4::class)
class MainViewModelTest
{
    // Executes each task synchronously using Architecture Components.
    //Execute this function whenever you are calling viewModels
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    @Test
    fun testLiveData_changed_nonNull()
    {
        //GIVEN - a Live Data
        //AndroidTestX/Roboelectric allows you to get application context in a Unit test
            val mviewModel = MainViewModel(ApplicationProvider.getApplicationContext())
        //When - Menu item is clicked
            mviewModel.menuItemSelected.value = "Week"
        //Then - "MenuItemSelected" is equal to "Week"
        assertThat(mviewModel.menuItemSelected.value,`is`("Week"))
    }
}