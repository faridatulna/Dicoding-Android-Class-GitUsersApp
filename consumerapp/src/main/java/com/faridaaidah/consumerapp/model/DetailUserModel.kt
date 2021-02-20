package com.faridaaidah.consumerapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
@Parcelize
class DetailUserModel(
    @field:SerializedName("id") //id
    val id: String? = null,
    @field:SerializedName("login") //login
    val login: String? = null,
    @field:SerializedName("name") //name
    val name: String? = null,
    @field:SerializedName("public_repos") //repos_url
    val repository: String? = null,
    @field:SerializedName("company")
    val company: String? = null,
    @field:SerializedName("location") //location
    val location: String? = null
) : Parcelable