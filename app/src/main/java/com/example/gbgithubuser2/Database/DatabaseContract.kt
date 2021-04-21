package com.example.gbgithubuser2.Database

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class UserColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "user_favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
        }
    }
}