package com.udacity.asteroidradar.Database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao
{
    @Query("select * from asteroidEntity")
    fun returnAll() : LiveData<List<asteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: asteroidEntity)
}
//recall that the order of the variables do not have to match the order by which the properties appear on the JSON array
//but they do need to match the name of the properties listed on the JSON array
@Entity
data class asteroidEntity(val id: Long, val codename: String, val closeApproachDate: String,
                          val absoluteMagnitude: Double, val estimatedDiameter: Double,
                          val relativeVelocity: Double, val distanceFromEarth: Double,
                          val isPotentiallyHazardous: Boolean)

//create an instance of a Room database

@Database(entities = [asteroidEntity::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    //declare DAO here so we can use its queries for the database
    abstract val asteroidDao: AsteroidDao

    companion object
    {
        //volatile ensures that "instance" will never be cached and instead written to main memory
        @Volatile
        private var instance : AsteroidDatabase? = null
        //ensures there is only one instance of a Room database
        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var localInstance = instance
                // If instance is `null` make a new database instance.
                if (localInstance == null) {
                    localInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "room_asteroid_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    instance = localInstance
                }
                // Return instance; smart cast to be non-null.
                return localInstance
            }
        }
    }
}