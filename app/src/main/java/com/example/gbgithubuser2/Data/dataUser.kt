package com.example.gbgithubuser2.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class dataUser(
    var id : Int? = 0,
    var nama: String? = "",
    var username: String? = "",
    var follower: String? = "",
    var following: String? = "",
    var avatar: String? = ""
): Parcelable
