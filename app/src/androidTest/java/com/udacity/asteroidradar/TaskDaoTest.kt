package com.udacity.asteroidradar
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asteroidEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TaskDaoTest {

    //synchronizes tasks
    @get:Rule
    val instanceExecutor = InstantTaskExecutorRule()

    //Given - an Asteroid database
    private lateinit var mDatabase : AsteroidDatabase


    @ExperimentalCoroutinesApi
    @Test
    fun roomTest_insertion_true()
    {
        runBlockingTest {
            //Given - An Asteroid Database

            //When - insert a value into database
            val dataPoint = asteroidEntity(1L, "test", "2021-04-04", 20.0, 20.0, 20.0, 20.0, false)
            mDatabase.asteroidDao.insertAll(dataPoint)

            //Then the dataPoint should exists in the database
            assertThat(
                mDatabase.asteroidDao.getTaskById(dataPoint.id)?.closeApproachDate,
                `is`(dataPoint.closeApproachDate)
            )
        }
    }

    @Before
    fun roomDatabase_returnTrue()
    {
    mDatabase = Room.inMemoryDatabaseBuilder(
    getApplicationContext(),
    AsteroidDatabase::class.java
    ).build()

}
}