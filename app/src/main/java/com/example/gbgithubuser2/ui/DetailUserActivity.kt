package com.example.gbgithubuser2.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gbgithubuser2.Adapter.SectionsPagerAdapter
import com.example.gbgithubuser2.Data.dataUser
import com.example.gbgithubuser2.Database.DatabaseContract
import com.example.gbgithubuser2.Database.UserHelper
import com.example.gbgithubuser2.R
import com.example.gbgithubuser2.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.InternalCoroutinesApi
import org.json.JSONArray

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userHelper: UserHelper
    private var datauser: dataUser? = null

    companion object{
        const val EXTRA_USER = "username"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
        private var TAG = DetailUserActivity::class.java.simpleName

        private val TAB_TITLES = intArrayOf(
            R.string.tab_text1,
            R.string.tab_text2
        )
    }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.title = "Profile"

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        val user = intent.getParcelableExtra<dataUser>(EXTRA_USER) as dataUser
        val username = user.username
        val avatar = user.avatar

        binding.tvUsername.text = username
        Glide.with(this)
            .load(avatar)
            .apply(RequestOptions.overrideOf(100,100))
            .into(binding.imgUser)

        getCountFollower(username.toString())
        getCountFollowing(username.toString())

        sectionPager()
        
        var statusFavorite = false
        setStatusFavorite(statusFavorite)
        binding.btnFavorite.setOnClickListener {
            insertDatabase(user)
        }
    }

    private fun insertDatabase(user: dataUser) {
        val username = user.username
        val avatar = user.avatar


        val values = ContentValues()
        values.put(DatabaseContract.UserColumns.USERNAME, username)
        values.put(DatabaseContract.UserColumns.AVATAR, avatar)

        val result = userHelper.insert(values)
        Log.d("coba", result.toString())
        if (result > 0){
            setStatusFavorite(true)
        }else{
            Toast.makeText(this,"Gagal menambahkan data", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite == true){
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun sectionPager(){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val user = intent.getParcelableExtra<dataUser>(EXTRA_USER) as dataUser
        sectionsPagerAdapter.username = user.username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    fun getCountFollower(username: String){

        val list = ArrayList<dataUser>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d(TAG,result)
                try {
                    val responseArray = JSONArray(result)
                    val itemsLength = responseArray.length()
                    binding.tvFollower.text = itemsLength.toString()

                    for (i in 0 until itemsLength){
                        val jsonObject = responseArray.getJSONObject(i)
                        val datauser = dataUser()
                        datauser.username = jsonObject.getString("login")
                        datauser.avatar = jsonObject.getString("avatar_url")
                        list.add(datauser)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailUserActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getCountFollowing(username: String){
        val list = ArrayList<dataUser>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d(TAG,result)
                try {
                    val responseArray = JSONArray(result)
                    val itemsLength = responseArray.length()
                    binding.tvFollowing.text = itemsLength.toString()

                    for (i in 0 until itemsLength){
                        val jsonObject = responseArray.getJSONObject(i)
                        val dataUser = dataUser()
                        dataUser.username = jsonObject.getString("login")
                        dataUser.avatar = jsonObject.getString("avatar_url")
                        list.add(dataUser)
                    }


                }catch (e: Exception){
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailUserActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}