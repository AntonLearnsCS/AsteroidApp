package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


    private val BASE_URL = "https://api.nasa.gov/neo/rest/v1/"
    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     Note: We will use Moshi for the Picture of the Day only
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
     * object.
     */
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    /**
     * A public interface that exposes the [getProperties] method
     */
    interface AsteroidsApiService {
        /**
         * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if in a Coroutine scope.
         * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
         * HTTP method
         */
        @GET("feed")
        suspend fun getProperties(@Query("start_date") start_date: String?, @Query("end_date") end_date: String?,
                                  @Query("api_key") api_key: String): NetworkAsteroidContainer

    }
    interface pictureOfDay
    {
        @GET("apod")
        suspend fun getPicture(@Query("api_key") type: String)
    }

    /**
     * A public Api object that exposes the lazy-initialized Retrofit service
     */
    object AsteroidsApi {
        val retrofitService : AsteroidsApiService by lazy { retrofit.create(AsteroidsApiService::class.java) }
    }


