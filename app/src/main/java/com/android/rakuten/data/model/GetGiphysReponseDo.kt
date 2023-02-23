package com.android.rakuten.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetRecentImagesResponseDo(
    @SerializedName("photos")
    var photos: Photos? = Photos(),

    @SerializedName("stat")
    var stat: String? = null,
): Serializable

data class Photos(
    @SerializedName("page")
    var page: Int? = null,

    @SerializedName("pages")
    var pages: Int? = null,

    @SerializedName("perpage")
    var perpage: Int? = null,

    @SerializedName("total")
    var total: Int? = null,

    @SerializedName("photo")
    var photosList: ArrayList<Photo> = arrayListOf(),
):Serializable

data class Photo(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("owner")
    var owner: String? = null,

    @SerializedName("secret")
    var secret: String? = null,

    @SerializedName("server")
    var server: String? = null,

    @SerializedName("farm")
    var farm: Int? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("ispublic")
    var ispublic: Int? = null,

    @SerializedName("isfriend")
    var isfriend: Int? = null,

    @SerializedName("isfamily")
    var isfamily: Int? = null,
): Serializable