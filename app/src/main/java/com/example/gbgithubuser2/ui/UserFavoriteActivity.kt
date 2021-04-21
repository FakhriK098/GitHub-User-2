package com.example.gbgithubuser2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gbgithubuser2.Adapter.FavoriteAdapter
import com.example.gbgithubuser2.Database.MappingHelper
import com.example.gbgithubuser2.Database.UserHelper
import com.example.gbgithubuser2.R
import com.example.gbgithubuser2.databinding.ActivityUserFavoriteBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

@InternalCoroutinesApi
class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.title = "User Favorite"

        binding.rvFavorite.setHasFixedSize(true)
        binding.progressBar.visibility = View.INVISIBLE
        adapter = FavoriteAdapter(this)
        binding.rvFavorite.adapter = adapter

        loadNotesAsync()
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val deferredUser = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val users = deferredUser.await()
            if (users.size > 0){
                adapter.listUser = users
            }else{
                adapter.listUser = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            userHelper.close()
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }
}