package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


    private val BASE_URL = "https://api.nasa.gov/neo/rest/v1/"
    private val BASE_URL_Pic_Day = "https://api.nasa.gov/planetary/"
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
    //Note: Ordering of added converters matters b/c Retrofit checks each converter if it's able of handling
    // the specified data type
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory()) //allows us to replace the callback methods for Retrofit with coroutine
        .baseUrl(BASE_URL)
        .build()
    //we need a seperate Retrofit object for a separate URL
    private val retrofitPictureOfDay = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL_Pic_Day)
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
        suspend fun getProperties(@Query("start_date") start_date: String?,
                                  @Query("api_key") api_key: String): JSONObject
        //note, when requesting a JSON response, we can specify the JSON response to be of type: JsonArray, JsonObject,
        // or JsonStringer.
    }

    interface pictureOfDay
    {
        @GET("apod")
        suspend fun getPicture(@Query("api_key") type: String) : PictureOfDay
    }

    /**
     * A public Api object that exposes the lazy-initialized Retrofit service
     */
    object AsteroidsApi {
        val retrofitService : AsteroidsApiService by lazy { retrofit.create(AsteroidsApiService::class.java) }
    }
    object pictureOfDayApi{
        val retrofitService : pictureOfDay by lazy { retrofitPictureOfDay.create(pictureOfDay::class.java) }
    }



