package com.android.rakuten.data.remote

import com.android.rakuten.BuildConfig
import com.android.rakuten.data.model.GetRecentImagesResponseDo
import retrofit2.http.GET
import retrofit2.http.Query

interface RakutenApi {
    @GET("photos.json?")
    suspend fun getRecentImages(
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("api_key") api_key: String = BuildConfig.PROJECT_TOKEN,
        @Query("page") page: Int = 1,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Boolean = true,
        @Query("safe_search") safeSearch: Boolean = true,
    ): GetRecentImagesResponseDo
}
