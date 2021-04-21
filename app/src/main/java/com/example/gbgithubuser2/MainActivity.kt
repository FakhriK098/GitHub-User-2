package com.example.gbgithubuser2


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gbgithubuser2.Adapter.ListUserAdapter
import com.example.gbgithubuser2.Data.dataUser
import com.example.gbgithubuser2.databinding.ActivityMainBinding
import com.example.gbgithubuser2.ui.DetailUserActivity
import com.example.gbgithubuser2.ui.SettingActivity
import com.example.gbgithubuser2.ui.UserFavoriteActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.InternalCoroutinesApi
import org.json.JSONObject


@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listUser = ArrayList<dataUser>()

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)
        binding.progressBar.visibility = View.INVISIBLE

        search()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                val favorite = Intent(this, UserFavoriteActivity::class.java)
                startActivity(favorite)
                return true
            }
            R.id.setting -> {
                val setting = Intent(this, SettingActivity::class.java)
                startActivity(setting)
                return true
            }
        }
        return true
    }

    private fun showRecyclerView() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(listUser)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: dataUser) {
                showSelectedUser(data)
            }
        })
    }

    private fun getListUser(username : String): Collection<dataUser> {
        binding.progressBar.visibility = View.VISIBLE

        val list = ArrayList<dataUser>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG,result)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    Log.d(TAG, items.toString())
                    for (i in 0 until items.length()){
                        val item = items.getJSONObject(i)

                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")

                        val user = dataUser()
                        user.username = username
                        user.avatar = avatar
                        list.add(user)
                    }
                    listUser.addAll(list)
                    showRecyclerView()

                }catch (e: Exception){
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
        return list
    }

    private fun search(){
        binding.apply {
            svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    listUser.addAll(getListUser(query))
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    listUser.clear()
                    showRecyclerView()
                    return false
                }

            })
        }
    }

    private fun showSelectedUser(datauser: dataUser){
        val user = dataUser(
            datauser.id,
            datauser.nama,
            datauser.username,
            datauser.follower,
            datauser.following,
            datauser.avatar
        )

        val DetailUserIntent = Intent(this, DetailUserActivity::class.java)
        DetailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(DetailUserIntent)
    }
}