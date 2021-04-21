package com.example.gbgithubuser2.Database

import android.database.Cursor
import com.example.gbgithubuser2.Data.dataUser

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<dataUser>{
        val userlist = ArrayList<dataUser>()

        notesCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                userlist.add(dataUser(id,"",username,"","",avatar))
            }
        }
        return userlist
    }
}