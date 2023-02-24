package com.android.rakuten.repo

import com.android.rakuten.data.model.GetRecentImagesResponseDo
import com.android.rakuten.data.model.Photo
import com.android.rakuten.data.model.Photos
import com.android.rakuten.data.remote.RakutenApi
import com.android.rakuten.data.repo.ImagesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ImagesRepositoryTest {

    private lateinit var rakutenApi: RakutenApi
    private lateinit var repo: ImagesRepository

    @Before
    fun setUp() {
        rakutenApi = mock(RakutenApi::class.java)
        repo = ImagesRepository(rakutenApi)
    }

    @Test
    fun `fetch recent images`() {
        runBlocking {
            whenever(rakutenApi.getRecentImages()).thenReturn(buildResponseDo)
            val gifs = repo.getRecentImages()
            gifs.collect {
                assertTrue(it.photos?.photosList?.isNotEmpty() == true)
            }
        }
    }

    companion object {
        val buildResponseDo: GetRecentImagesResponseDo
            get() {
                return GetRecentImagesResponseDo(
                    photos = Photos(
                        page = 1,
                        pages = 1,
                        perpage = 100,
                        total = 1000,
                        photosList = arrayListOf(
                            Photo(
                                id = "48837804368",
                                owner = "183727554@N08",
                                secret = "709b44acf8",
                                farm = 55,
                                server = "65535",
                                title = "Reflections Off a Backcountry Pond (Daylight Blues)",
                                isPublic = 1,
                                isFriend = 0,
                                isFamily = 0,
                            )
                        )
                    )
                )
            }
    }
}