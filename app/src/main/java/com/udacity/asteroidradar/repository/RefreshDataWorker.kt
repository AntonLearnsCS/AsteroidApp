package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import retrofit2.HttpException

//here we set up the background worker, we tell this worker when to run in the application class
class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshAsteroidList()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}