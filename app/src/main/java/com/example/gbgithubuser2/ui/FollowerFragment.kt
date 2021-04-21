package com.example.gbgithubuser2.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gbgithubuser2.Adapter.ListUserAdapter
import com.example.gbgithubuser2.Data.dataUser
import com.example.gbgithubuser2.databinding.FragmentFollowerBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class FollowerFragment : Fragment() {

    private var binding: FragmentFollowerBinding? = null
    private val listFollower = ArrayList<dataUser>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvFollower?.setHasFixedSize(true)
        val username = arguments?.getString(KEY_USER)
        getCountFollower(username.toString())
    }

    companion object {
        private val TAG = FollowerFragment::class.java.simpleName

        const val KEY_USER = "username"

        fun newInstence(username: String): FollowerFragment{
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(KEY_USER, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    private fun showRecyclerView(){
        binding?.rvFollower?.layoutManager = LinearLayoutManager(Activity())
        val listUserAdapter = ListUserAdapter(listFollower)
        binding?.rvFollower?.adapter = listUserAdapter
    }

    fun getCountFollower(username: String){
        binding?.progrees?.visibility = View.VISIBLE
        val list = ArrayList<dataUser>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                binding?.progrees?.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG,result)
                try {
                    val responseArray = JSONArray(result)
                    val itemsLength = responseArray.length()

                    for (i in 0 until itemsLength){
                        val jsonObject = responseArray.getJSONObject(i)
                        val datauser = dataUser()
                        datauser.username = jsonObject.getString("login")
                        datauser.avatar = jsonObject.getString("avatar_url")
                        list.add(datauser)
                    }
                    listFollower.addAll(list)
                    showRecyclerView()
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                binding?.progrees?.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}