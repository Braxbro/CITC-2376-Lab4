package com.example.userdirectory.data.types

import com.google.gson.annotations.SerializedName
import java.io.Serial

// A data class representation of the partial JSON object I need to parse.

data class UserProfileResults(
    // It recommends I override comparison operators here.
    // I'm not going to, since this should never be compared.
    @SerializedName("results")
    val response: List<UserProfile>
)

data class UserProfile(
    @SerializedName("login")
    val login: ProfileLogin,
    @SerializedName("name")
    val name: ProfileName,
    @SerializedName("email")
    val email: String?,
    @SerializedName("picture")
    val picture: ProfileImage
)

data class ProfileLogin(
    @SerializedName("uuid")
    val id: String
)

data class ProfileName(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String
)

data class ProfileImage(
    @SerializedName("large")
    val large: String
)