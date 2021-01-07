package com.faridaaidah.gitusersapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserModel(
    @field:SerializedName("login") //login
    var login: String? = null,
    @field:SerializedName("avatar_url") //avatar_url
    var avatar: String? = null,
    @field:SerializedName("html_url")
    var url: String? = null,
    @field:SerializedName("id")
    var id: Int? = null

) : Parcelable {
    @IgnoredOnParcel
    @SerializedName("items")
    private var items: List<UserModel>? = null

    fun getItems(): List<UserModel>? {
        return items
    }
}
