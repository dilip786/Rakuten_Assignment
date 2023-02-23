package com.android.rakuten.data.repo

import com.android.rakuten.data.model.GetRecentImagesResponseDo
import com.android.rakuten.data.remote.RakutenApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImagesRepository @Inject constructor(private val rakutenApi: RakutenApi) {
    suspend fun getRecentImages(): Flow<GetRecentImagesResponseDo> {
        return flow { emit(rakutenApi.getRecentImages()) }
    }
}