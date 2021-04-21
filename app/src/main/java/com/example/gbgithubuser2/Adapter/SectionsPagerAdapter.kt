package com.example.gbgithubuser2.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gbgithubuser2.ui.FollowerFragment
import com.example.gbgithubuser2.ui.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String? = null
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FollowerFragment.newInstence(username.toString())
            1 -> fragment = FollowingFragment.newInstence(username.toString())
        }
        return fragment as Fragment
    }
}