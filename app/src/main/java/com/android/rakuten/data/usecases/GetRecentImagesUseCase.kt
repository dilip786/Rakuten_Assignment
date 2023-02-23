package com.android.rakuten.data.usecases

import com.android.rakuten.data.model.GetRecentImagesResponseDo
import com.android.rakuten.data.repo.ImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentImagesUseCase @Inject constructor(private val imagesRepository: ImagesRepository) :
    BaseUseCase.NoParamUseCase<Flow<GetRecentImagesResponseDo>> {
    override suspend fun getAction(): Flow<GetRecentImagesResponseDo> {
        return imagesRepository.getRecentImages()
    }
}